package com.nxdmn.demoproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel(private val repo: ProductRepository) : ViewModel() {
    val currentProducts: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    fun refreshList(){
        viewModelScope.launch {
            var data: List<Product>
            withContext(Dispatchers.IO){
                data = repo.getAllProducts(true)
            }
            currentProducts.value = data
        }
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