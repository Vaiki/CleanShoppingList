package com.vaiki.cleanshoppinglist.domain

class GetShopListItemUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): List<ShopItem> {
        return shopListRepository.getShopList()
    }
}