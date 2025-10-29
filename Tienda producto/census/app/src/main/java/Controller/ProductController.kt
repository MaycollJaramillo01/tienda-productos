package com.example.census.Controller

import android.content.Context
import com.example.census.Data.IDataManager
import com.example.census.Data.MemoryDataManager
import com.example.census.Entity.Product
import com.example.census.R

class ProductController(private val context: Context) {
    private val dataManager: IDataManager = MemoryDataManager

    fun add(product: Product) {
        try { dataManager.add(product) }
        catch (e: Exception) { throw Exception(context.getString(R.string.ErrorMsgAdd)) }
    }

    fun update(product: Product) {
        try { dataManager.update(product) }
        catch (e: Exception) { throw Exception(context.getString(R.string.ErrorMsgUpdate)) }
    }

    fun delete(id: String) {
        try { dataManager.delete(id) }
        catch (e: Exception) { throw Exception(context.getString(R.string.ErrorMsgDelete)) }
    }

    fun getAll(): List<Product> {
        try { return dataManager.getAll() }
        catch (e: Exception) { throw Exception(context.getString(R.string.ErrorMsgGetAll)) }
    }

    fun getById(id: String): Product {
        try {
            val result = dataManager.getById(id)
            if (result == null) throw Exception(context.getString(R.string.ErrorMsgGetById))
            return result
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }
}
