package com.nxdmn.demoproduct

import kotlinx.serialization.Serializable

@Serializable
data class Product (
    val id: Int? = 0,
    val name: String,
    val description: String,
    val type: String,
    val picture: String,
    val price : Double,
)