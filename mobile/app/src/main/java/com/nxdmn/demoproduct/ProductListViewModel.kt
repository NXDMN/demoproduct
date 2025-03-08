package com.nxdmn.demoproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class ProductListViewModel : ViewModel() {
    var products: List<Product> = emptyList()

    init {
        products = listOf<Product>(Product(
            id = 1,
            name = "Product 1",
            description = "This is product 1",
            productType = "Type A",
            picture = "",
            price = 10.0,
        ))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //val repo = (this[APPLICATION_KEY] as MainApplication).productRepository
                ProductListViewModel()
            }
        }
    }
}