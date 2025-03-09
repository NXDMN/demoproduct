package com.nxdmn.demoproduct

interface ProductDataSource {
    suspend fun findAll(): List<Product>
    suspend fun find(id: Int): Product?
    suspend fun create(product: Product): Int
    suspend fun update(product: Product): Product?
    suspend fun delete(id: Int): Int
}