package com.nxdmn.demoproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nxdmn.demoproduct.databinding.FragmentProductListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment() {
    private val viewmodel: ProductListViewModel by viewModels<ProductListViewModel> { ProductListViewModel.Factory }

    private var _binding: FragmentProductListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.refreshList()

        val nameObserver = Observer<List<Product>> { products ->
            binding.gridView.adapter = ProductGridAdapter(view.context, products, onProductClicked = { id ->
                val bundle = bundleOf("productId" to id)
                findNavController().navigate(R.id.action_ProductListFragment_to_ProductDetailFragment, bundle)
            })
        }

        viewmodel.currentProducts.observe(viewLifecycleOwner, nameObserver)

//        binding.gridView.adapter = ProductGridAdapter(view.context, viewmodel.products, onProductClicked = { id ->
//            val bundle = bundleOf("productId" to id)
//            findNavController().navigate(R.id.action_ProductListFragment_to_ProductDetailFragment, bundle)
//        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ProductListFragment_to_ProductDetailFragment)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}