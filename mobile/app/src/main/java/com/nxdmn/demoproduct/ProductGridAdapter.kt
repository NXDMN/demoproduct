package com.nxdmn.demoproduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.Locale

class ProductGridAdapter(context: Context,
                         list: List<Product>,
                         val onProductClicked: (Int) -> Unit,
) : ArrayAdapter<Product?>(context, 0, list as List<Product?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        }

        val product: Product? = getItem(position)
        val nameView = itemView!!.findViewById<TextView>(R.id.product_name)
        val imageView = itemView.findViewById<ImageView>(R.id.product_img)
        val priceView = itemView.findViewById<TextView>(R.id.product_price)

        nameView.text = product!!.name
        imageView.setImageBitmap(readImageFromPath(context, product.picture))
        priceView.text = String.format(Locale.getDefault(), "%.2f", product.price)
        itemView.setOnClickListener { onProductClicked(product.id!!) }
        return itemView
    }
}