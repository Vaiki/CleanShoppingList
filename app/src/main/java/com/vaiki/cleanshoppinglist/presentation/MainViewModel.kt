package com.vaiki.cleanshoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vaiki.cleanshoppinglist.data.ShopListRepositoryImpl
import com.vaiki.cleanshoppinglist.domain.DeleteShopItemUseCase
import com.vaiki.cleanshoppinglist.domain.EditShopItemUseCase
import com.vaiki.cleanshoppinglist.domain.GetShopListUseCase
import com.vaiki.cleanshoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enable = !shopItem.enable)
        editShopItemUseCase.editShopItem(newItem)
    }

}