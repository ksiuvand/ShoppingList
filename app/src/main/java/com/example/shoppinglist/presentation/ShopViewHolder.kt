package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R


class ShopViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewName = itemView.findViewById<TextView>(R.id.textViewItemName)
    val textViewCount = itemView.findViewById<TextView>(R.id.textViewItemCount)
}