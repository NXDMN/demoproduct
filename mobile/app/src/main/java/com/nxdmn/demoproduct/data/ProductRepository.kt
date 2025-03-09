package com.nxdmn.demoproduct.data

class ProductRepository(private val dataSource: ProductDataSource) {
    private var productList: List<Product> = emptyList()

    private var page: Int = 1
    private val limit: Int = 10

    suspend fun getAllProducts(refresh: Boolean = false, loadMore: Boolean = false, searchText: String = ""): List<Product> {
        if (refresh || productList.isEmpty()) {
            page = 1
            productList = dataSource.findAll(
                page = page++,
                limit = limit,
                searchText = searchText,
            )
        }
        else if(loadMore){
            productList += dataSource.findAll(
                page = page++,
                limit = limit,
                searchText = searchText,
            )
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