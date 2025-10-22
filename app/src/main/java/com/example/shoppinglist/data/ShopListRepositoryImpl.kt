package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository{

    private val shopListLD = MutableLiveData<List<ShopItem>>()

    private val shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})

    private var newId = 0

    init {
        for (i in 0..5){
            addShopItem(ShopItem("Name $i", i, true))
        }
        for (i in 0..5){
            addShopItem(ShopItem("Name $i", i, false))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = newId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        deleteShopItem(oldShopItem)
        addShopItem(shopItem)
        updateList()
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id } ?: throw Exception("ShopItem with this id cant be find")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateList(){
        shopListLD.postValue(shopList.toList())
    }
}