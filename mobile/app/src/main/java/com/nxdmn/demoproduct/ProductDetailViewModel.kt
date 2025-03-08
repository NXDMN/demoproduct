package com.nxdmn.demoproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class ProductDetailViewModel(productId: Int?) : ViewModel() {
    var product: Product? = null

    init {
        product = Product(
            id = productId ?: 0,
            name = "",
            description = "",
            productType = "",
            picture = "",
            price = 10.0
        )
    }

    fun updateImage(value: String){
        product?.picture = value
    }

    companion object {
        val PRODUCT_ID_KEY = object : CreationExtras.Key<Int?> {}
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //val repo = (this[APPLICATION_KEY] as MainApplication).productRepository
                val productId = this[PRODUCT_ID_KEY]

                ProductDetailViewModel(productId)
            }
        }
    }
}