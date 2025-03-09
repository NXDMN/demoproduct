package com.nxdmn.demoproduct.views.productList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nxdmn.demoproduct.MainApplication
import com.nxdmn.demoproduct.data.Product
import com.nxdmn.demoproduct.data.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel(private val repo: ProductRepository) : ViewModel() {
    private var allProducts: List<Product> = emptyList()
    private var hasMoreProducts: Boolean = true
    private var isLoading: Boolean = false

    val currentProducts: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    fun refreshList(){
        hasMoreProducts = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                allProducts = repo.getAllProducts(true)
            }
            currentProducts.value = allProducts
        }
    }

    fun loadMore(){
        if(isLoading) return
        isLoading = true
        if(hasMoreProducts){
            viewModelScope.launch {
                val newAllProducts = withContext(Dispatchers.IO){
                     repo.getAllProducts(loadMore = true)
                }
                if(newAllProducts.count() == allProducts.count()){
                    hasMoreProducts = false
                }
                allProducts = newAllProducts
                currentProducts.value = allProducts
            }
        }
        isLoading = false
    }

    fun search(searchText: String?){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                allProducts = repo.getAllProducts(true, searchText = searchText ?: "")
            }
            currentProducts.value = allProducts
        }
    }

    fun sort(){
        currentProducts.value = allProducts.sortedBy { it.price }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repo = (this[APPLICATION_KEY] as MainApplication).productRepository
                ProductListViewModel(repo)
            }
        }
    }
}