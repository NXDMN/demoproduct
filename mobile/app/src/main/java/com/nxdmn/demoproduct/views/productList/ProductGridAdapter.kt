package com.nxdmn.demoproduct.views.productList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nxdmn.demoproduct.R
import com.nxdmn.demoproduct.data.Product
import com.nxdmn.demoproduct.readImageFromPath
import java.util.Locale

class ProductGridAdapter(
    private val context: Context,
    val onProductClicked: (Int) -> Unit,
) : BaseAdapter() {

    private var items: List<Product> = emptyList()

    fun setItems(data: List<Product>) {
        items = data
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Product {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        }

        val product: Product = getItem(position)
        val nameView = itemView!!.findViewById<TextView>(R.id.product_name)
        val imageView = itemView.findViewById<ImageView>(R.id.product_img)
        val priceView = itemView.findViewById<TextView>(R.id.product_price)

        nameView.text = product.name
        imageView.setImageBitmap(readImageFromPath(context, product.picture))
        priceView.text = String.format(Locale.getDefault(), "%.2f", product.price)
        itemView.setOnClickListener { onProductClicked(product.id!!) }
        return itemView
    }
}