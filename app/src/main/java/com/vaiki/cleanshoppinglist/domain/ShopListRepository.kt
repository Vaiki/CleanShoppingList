package com.vaiki.cleanshoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItemId(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
}