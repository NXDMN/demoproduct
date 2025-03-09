package com.nxdmn.demoproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(private val repo: ProductRepository, private val productId: Int?) : ViewModel() {
    val isEdit: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val description: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val type: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val image: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val price: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>(0.0)
    }

    init {
        if(productId != null){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    val product = repo.getProduct(productId)
                    if(product != null){
                        name.postValue(product.name)
                        description.postValue(product.description)
                        type.postValue(product.type)
                        image.postValue(product.picture)
                        price.postValue(product.price)
                        isEdit.postValue(true)
                    }
                }
            }
        }
    }

    fun updateImage(value: String){
        image.postValue(value)
    }

    fun save(name: String, description: String, type: String, price: String){
        val product = Product(
            id = productId,
            name = name,
            description = description,
            type = type,
            picture = image.value ?: "",
            price = price.toDoubleOrNull() ?: 0.0
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(isEdit.value == true) {
                    repo.updateProduct(product)
                }else{
                    repo.createProduct(product)
                }
            }
        }
    }

    fun delete(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.deleteProduct(productId!!)
            }
        }
    }

    companion object {
        val PRODUCT_ID_KEY = object : CreationExtras.Key<Int?> {}
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repo = (this[APPLICATION_KEY] as MainApplication).productRepository
                val productId = this[PRODUCT_ID_KEY]

                ProductDetailViewModel(repo, productId)
            }
        }
    }
}