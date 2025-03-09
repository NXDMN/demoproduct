package com.nxdmn.demoproduct

import android.app.Application
import com.nxdmn.demoproduct.data.ProductRepository
import com.nxdmn.demoproduct.data.ProductWebAPIDataSource

class MainApplication : Application() {
    val productRepository: ProductRepository = ProductRepository(ProductWebAPIDataSource())
}