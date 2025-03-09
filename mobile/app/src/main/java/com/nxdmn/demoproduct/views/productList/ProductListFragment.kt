package com.nxdmn.demoproduct.views.productList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nxdmn.demoproduct.R
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

        binding.toolbar.inflateMenu(R.menu.menu_product_list)
        binding.toolbar.apply {
            setNavigationOnClickListener{
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_sort -> {
                        viewmodel.sort()
                        true
                    }
                    else -> false
                }
            }
        }

        viewmodel.refreshList()

        val adapter = ProductGridAdapter(view.context, onProductClicked = { id ->
            findNavController().navigate(R.id.action_ProductListFragment_to_ProductDetailFragment, bundleOf("productId" to id))
        })
        binding.gridView.adapter = adapter
        viewmodel.currentProducts.observe(viewLifecycleOwner){ products ->
            adapter.setItems(products)
        }

        binding.gridView.setOnScrollListener(object: OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) { }

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if(totalItemCount > 0){
                    val lastVisibleItem = firstVisibleItem + visibleItemCount
                    if(lastVisibleItem == totalItemCount){
                        viewmodel.loadMore()
                    }
                }
            }
        })

        binding.searchBar.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrEmpty()){
                    viewmodel.search(query)
                }
                binding.searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    viewmodel.search(newText)
                }
                return false
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ProductListFragment_to_ProductDetailFragment)
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