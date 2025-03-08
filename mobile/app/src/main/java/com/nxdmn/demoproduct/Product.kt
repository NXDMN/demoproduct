package com.nxdmn.demoproduct

data class Product (
    val id: Int,
    val name: String,
    val description: String,
    val productType: String,
    var picture: String,
    val price : Double,
)