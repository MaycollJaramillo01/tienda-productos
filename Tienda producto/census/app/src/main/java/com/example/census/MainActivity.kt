package com.example.census

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.census.Controller.ProductController
import com.example.census.Entity.Product
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var productController: ProductController
    private lateinit var idInput: TextInputEditText
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var productsList: ListView
    private lateinit var productsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productController = ProductController(this)

        idInput = findViewById(R.id.inputId)
        nameInput = findViewById(R.id.inputName)
        descriptionInput = findViewById(R.id.inputDescription)
        priceInput = findViewById(R.id.inputPrice)
        productsList = findViewById(R.id.productsListView)

        productsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        productsList.adapter = productsAdapter

        findViewById<Button>(R.id.buttonAdd).setOnClickListener { addProduct() }
        findViewById<Button>(R.id.buttonUpdate).setOnClickListener { confirmUpdate() }
        findViewById<Button>(R.id.buttonDelete).setOnClickListener { confirmDelete() }
        findViewById<Button>(R.id.buttonSearch).setOnClickListener { searchProduct() }
        findViewById<Button>(R.id.buttonClear).setOnClickListener { clearFields() }

        refreshProductList()
    }

    private fun addProduct() {
        val product = buildProductFromInputs() ?: return

        try {
            productController.add(product)
            showToast(getString(R.string.toast_add_success))
            refreshProductList()
            clearFields()
        } catch (e: Exception) {
            showToast(e.message ?: getString(R.string.ErrorMsgAdd))
        }
    }

    private fun confirmUpdate() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_update_title))
            .setMessage(getString(R.string.dialog_update_message))
            .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ -> updateProduct() }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }

    private fun updateProduct() {
        val product = buildProductFromInputs() ?: return

        try {
            productController.update(product)
            showToast(getString(R.string.toast_update_success))
            refreshProductList()
            clearFields()
        } catch (e: Exception) {
            showToast(e.message ?: getString(R.string.ErrorMsgUpdate))
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_delete_title))
            .setMessage(getString(R.string.dialog_delete_message))
            .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ -> deleteProduct() }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }

    private fun deleteProduct() {
        val id = idInput.text?.toString()?.trim().orEmpty()
        if (id.isEmpty()) {
            showToast(getString(R.string.toast_fill_fields))
            return
        }

        try {
            productController.delete(id)
            showToast(getString(R.string.toast_delete_success))
            refreshProductList()
            clearFields()
        } catch (e: Exception) {
            showToast(e.message ?: getString(R.string.ErrorMsgDelete))
        }
    }

    private fun searchProduct() {
        val id = idInput.text?.toString()?.trim().orEmpty()
        if (id.isEmpty()) {
            showToast(getString(R.string.toast_fill_fields))
            return
        }

        try {
            val product = productController.getById(id)
            idInput.setText(product.ID)
            nameInput.setText(product.Name)
            descriptionInput.setText(product.Description)
            priceInput.setText(product.Price.toString())
            showToast(getString(R.string.toast_loaded_for_editing))
        } catch (e: Exception) {
            showToast(getString(R.string.toast_not_found))
        }
    }

    private fun buildProductFromInputs(): Product? {
        val id = idInput.text?.toString()?.trim().orEmpty()
        val name = nameInput.text?.toString()?.trim().orEmpty()
        val description = descriptionInput.text?.toString()?.trim().orEmpty()
        val priceText = priceInput.text?.toString()?.trim().orEmpty()

        if (id.isEmpty() || name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            showToast(getString(R.string.toast_fill_fields))
            return null
        }

        val price = priceText.toDoubleOrNull()
        if (price == null) {
            showToast(getString(R.string.toast_invalid_price))
            return null
        }

        return Product(id, name, description, price, null)
    }

    private fun refreshProductList() {
        val items = productController.getAll().map { product ->
            "${product.ID} - ${product.Name} (${String.format("$%.2f", product.Price)})"
        }
        productsAdapter.clear()
        productsAdapter.addAll(items)
        productsAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        idInput.text?.clear()
        nameInput.text?.clear()
        descriptionInput.text?.clear()
        priceInput.text?.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
