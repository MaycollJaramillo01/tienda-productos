package com.example.census.Data

import com.example.census.Entity.Product

object MemoryDataManager : IDataManager {
    private val productList = mutableListOf<Product>()

    override fun add(product: Product) {
        productList.add(product)
    }

    override fun update(product: Product) {
        delete(product.ID)
        add(product)
    }

    override fun delete(id: String) {
        productList.removeIf { it.ID.trim() == id.trim() }
    }

    override fun getAll(): List<Product> = productList

    override fun getById(id: String): Product? {
        return productList.firstOrNull { it.ID.trim() == id.trim() }
    }
}
