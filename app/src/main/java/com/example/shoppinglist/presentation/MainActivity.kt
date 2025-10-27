package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var buttonAdd: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        buttonAdd = findViewById(R.id.buttonAddItem)
        buttonAdd.setOnClickListener{
            val intent = ShopItemActivity.newIntentAddMode(this)
            startActivity(intent)
        }
        shopListAdapter = ShopListAdapter()

        shopListAdapter.onShopItemClick = {
            Log.d("MainActivityEditShopItem", it.toString())
            val intent = ShopItemActivity.newIntentEditMode(this, it.id)
            startActivity(intent)
        }

        shopListAdapter.onShopItemLongClick = {
            mainViewModel.changeActiveShopItem(it)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = shopListAdapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            R.layout.item_shop_active,
            ShopListAdapter.MAX_POOL_SIZE
        )
        recyclerView.recycledViewPool.setMaxRecycledViews(
            R.layout.item_shop_inactive,
            ShopListAdapter.MAX_POOL_SIZE
        )

        val callback = object: ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel.deleteShopItem(
                    shopListAdapter.currentList[viewHolder.adapterPosition]
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.shopList.observe(this){
            shopListAdapter.submitList(it)
        }
    }
}