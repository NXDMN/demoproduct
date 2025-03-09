package com.nxdmn.demoproduct.data

interface ProductDataSource {
    suspend fun findAll(page: Int, limit: Int, searchText: String): List<Product>
    suspend fun find(id: Int): Product?
    suspend fun create(product: Product): Int
    suspend fun update(product: Product): Product?
    suspend fun delete(id: Int): Int
}