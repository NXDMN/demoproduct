package com.nxdmn.demoproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import com.nxdmn.demoproduct.databinding.FragmentProductDetailBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProductDetailFragment : Fragment() {
    private val viewmodel: ProductDetailViewModel by viewModels<ProductDetailViewModel> (
        extrasProducer = {
            MutableCreationExtras().apply {
                set(ProductDetailViewModel.PRODUCT_ID_KEY, arguments?.getInt("productId"))
            }
        },
        factoryProducer = { ProductDetailViewModel.Factory }
    )

    private var _binding: FragmentProductDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            viewmodel.updateImage(uri.toString())
            invalidate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_product_detail)
        binding.toolbar.apply {
            setNavigationOnClickListener{
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_save -> {
                        true
                    }
                    else -> false
                }
            }
        }
        invalidate()

        binding.addImgButton.setOnClickListener {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun invalidate(){
        val product = viewmodel.product
        binding.nameView.setText(product?.name)
        binding.descriptionView.setText(product?.description)
        binding.typeView.setText(product?.productType)
        binding.priceView.setText(String.format("%.2f".format(product?.price)))
        binding.nameView.setText(product?.name)

        if(product?.picture != null){
            binding.imageView.setImageBitmap(readImageFromPath(requireContext(), product.picture))
        }else{
            binding.imageView.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}