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
            MutableCreationExtras(this.defaultViewModelCreationExtras).apply {
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

        viewmodel.isEdit.observe(viewLifecycleOwner){
            binding.toolbar.menu.findItem(R.id.action_delete).isVisible = it
        }

        binding.toolbar.apply {
            setNavigationOnClickListener{
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_save -> {
                        save()
                        true
                    }
                    R.id.action_delete -> {
                        viewmodel.delete()
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }

        viewmodel.name.observe(viewLifecycleOwner){
            binding.nameView.setText(it)
        }
        viewmodel.description.observe(viewLifecycleOwner){
            binding.descriptionView.setText(it)
        }
        viewmodel.type.observe(viewLifecycleOwner){
            binding.typeView.setText(it)
        }
        viewmodel.image.observe(viewLifecycleOwner){
            if(it != null){
                binding.imageView.setImageBitmap(readImageFromPath(requireContext(), it))
            }else{
                binding.imageView.visibility = View.INVISIBLE
            }
        }
        viewmodel.price.observe(viewLifecycleOwner){
            if(it != 0.0){
                binding.priceView.setText(String.format("%.2f".format(it)))
            }
        }

        binding.addImgButton.setOnClickListener {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun save(){
        val name = binding.nameView.text.toString()
        val description = binding.descriptionView.text.toString()
        val type = binding.typeView.text.toString()
        val price = binding.priceView.text.toString()
        viewmodel.save(name, description, type, price)
        findNavController().navigateUp()
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