package com.nxdmn.demoproduct

class ProductRepository(private val dataSource: ProductDataSource) {
    private var productList: List<Product> = emptyList()

    suspend fun getAllProducts(refresh: Boolean = false): List<Product> {
        if (refresh || productList.isEmpty()) {
            productList = dataSource.findAll()
        }
        return productList
    }

    suspend fun getProduct(id: Int): Product? {
        return if (productList.isNotEmpty()) productList.find { it.id == id }
        else dataSource.find(id)
    }

    suspend fun updateProduct(product: Product) = dataSource.update(product)

    suspend fun createProduct(product: Product) = dataSource.create(product)

    suspend fun deleteProduct(id: Int) = dataSource.delete(id)
}