package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopViewHolder>(
    ShopItemDiffCallback()
) {

    var onShopItemLongClick: ((ShopItem) -> Unit)? = null
    var onShopItemClick: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val resId = if (viewType == ACTIVE_VIEW_TYPE){
            R.layout.item_shop_active
        }else{
            R.layout.item_shop_inactive
        }
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.textViewName.text = shopItem.name
        holder.textViewCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClick?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClick?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.isActive){
            ACTIVE_VIEW_TYPE
        }else{
            INACTIVE_VIEW_TYPE
        }
    }

    companion object {
        const val ACTIVE_VIEW_TYPE = 100
        const val INACTIVE_VIEW_TYPE = 101

        const val MAX_POOL_SIZE = 20
    }
}