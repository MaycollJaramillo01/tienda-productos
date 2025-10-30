package com.example.census.Data

import com.example.census.Entity.Product

interface IDataManager {
    fun add(product: Product)
    fun update(product: Product)
    fun delete(id: String)
    fun getAll(): List<Product>
    fun getById(id: String): Product?
}
