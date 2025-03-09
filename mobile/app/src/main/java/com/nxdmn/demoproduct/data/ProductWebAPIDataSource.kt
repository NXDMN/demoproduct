package com.nxdmn.demoproduct.data

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ProductWebAPIDataSource : ProductDataSource {
    private val client: OkHttpClient = OkHttpClient()

    @Throws(IOException::class)
    override suspend fun findAll(page: Int, limit: Int, searchText: String): List<Product> {
        var products: List<Product> = emptyList()

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:3000/products?page=$page&limit=$limit${if(searchText.isNotEmpty()) "&searchText=$searchText" else ""}")
            .build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            if(!json.isNullOrEmpty()) {
                products = Json.decodeFromString<List<Product>>(json)
            }
        }

        return products
    }

    @Throws(IOException::class)
    override suspend fun find(id: Int): Product? {
        var product: Product? = null
        val request: Request = Request.Builder()
            .url("http://10.0.2.2:3000/product/$id")
            .build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            if(!json.isNullOrEmpty()) {
                product = Json.decodeFromString<Product>(json)
            }
        }

        return product
    }

    @Throws(IOException::class)
    override suspend fun create(product: Product): Int {
        var id = -1
        val body = Json.encodeToString(product).toRequestBody()

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:3000/product")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            if(!json.isNullOrEmpty()) {
                id = Json.decodeFromString<Int>(json)
            }
        }

        return id
    }

    @Throws(IOException::class)
    override suspend fun update(product: Product): Product? {
        var updatedProduct: Product? = null

        val body = Json.encodeToString(product).toRequestBody()

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:3000/product")
            .put(body)
            .build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            if(!json.isNullOrEmpty()) {
                updatedProduct = Json.decodeFromString<Product>(json)
            }
        }

        return updatedProduct
    }

    @Throws(IOException::class)
    override suspend fun delete(id: Int): Int {
        var returnId = -1
        val request: Request = Request.Builder()
            .url("http://10.0.2.2:3000/product/$id")
            .delete()
            .build()

        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            if(!json.isNullOrEmpty()) {
                returnId = Json.decodeFromString<Int>(json)
            }
        }

        return returnId
    }
}