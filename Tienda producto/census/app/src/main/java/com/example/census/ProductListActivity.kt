package com.example.census

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.census.Controller.ProductController

class ProductListActivity : AppCompatActivity() {

    private lateinit var productController: ProductController
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        productController = ProductController(this)
        recyclerView = findViewById(R.id.productsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductAdapter(productController.getAll())
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = ProductAdapter(productController.getAll())
    }
}
