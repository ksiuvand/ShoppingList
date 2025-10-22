package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopViewHolder>() {

     var shopList = listOf<ShopItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val resId = if (viewType == ACTIVE_VIEW_TYPE){
            R.layout.item_shop_active
        }else{
            R.layout.item_shop_inactive
        }
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return ShopViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopItem = shopList.get(position)
        holder.textViewName.text = shopItem.name
        holder.textViewCount.text = shopItem.count.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList.get(position)
        return if (shopItem.isActive){
            ACTIVE_VIEW_TYPE
        }else{
            INACTIVE_VIEW_TYPE
        }
    }



    class ShopViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById<TextView>(R.id.textViewItemName)
        val textViewCount = itemView.findViewById<TextView>(R.id.textViewItemCount)
    }

    companion object {
        const val ACTIVE_VIEW_TYPE = 100
        const val INACTIVE_VIEW_TYPE = 101

        const val MAX_POOL_SIZE = 20
    }
}