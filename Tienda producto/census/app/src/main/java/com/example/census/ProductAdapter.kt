package com.example.census

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.census.Entity.Product

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.productImage)
        private val nameView: TextView = itemView.findViewById(R.id.productName)
        private val priceView: TextView = itemView.findViewById(R.id.productPrice)
        private val descriptionView: TextView = itemView.findViewById(R.id.productDescription)

        fun bind(product: Product) {
            nameView.text = product.Name
            priceView.text = itemView.context.getString(R.string.price_with_currency, product.Price)
            descriptionView.text = product.Description

            val imageBitmap = product.Image
            if (imageBitmap != null) {
                imageView.setImageBitmap(imageBitmap)
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher)
            }
        }
    }
}
