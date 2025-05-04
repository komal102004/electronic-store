package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemCardRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {
    // add itmes to cart
    // case1: cart for user is not available: we will create the cart and then add the item
    //case 2: cart is available and the items to cart
    CartDto addItemToCart(String userId, AddItemCardRequest request);
    //remove item from cart
    void removeItemFromCart(String userId,int cartItem);
    //remove all items from cart
    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
