package com.nxdmn.demoproduct

import android.app.Application

class MainApplication : Application() {
    val productRepository: ProductRepository = ProductRepository(ProductWebAPIDataSource())
}