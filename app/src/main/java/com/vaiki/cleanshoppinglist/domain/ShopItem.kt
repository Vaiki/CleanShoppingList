package com.vaiki.cleanshoppinglist.domain

data class ShopItem(var id: Int = UNDEFINED_ID, val name: String, val count: Int, val enable: Boolean){
    companion object{
        const val UNDEFINED_ID = -1
    }
}