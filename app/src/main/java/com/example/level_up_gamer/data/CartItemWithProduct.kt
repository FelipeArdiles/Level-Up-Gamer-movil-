package com.example.level_up_gamer.data

import com.example.level_up_gamer.model.CartItem
import com.example.level_up_gamer.model.Product

data class CartItemWithProduct(
    val cartItem: CartItem,
    val product: Product
)

