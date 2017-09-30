package com.example.heojuyeong.foodandroid.model;


import com.example.heojuyeong.foodandroid.model.cart.CartItem;
import com.example.heojuyeong.foodandroid.model.user.UserItem;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */


public class OrderItem  {

    UserItem user;
    ArrayList<CartItem> cartItems=new ArrayList<>();
    int status;

    public OrderItem(){

    }

    public OrderItem(UserItem user, ArrayList<CartItem> cartItems, int status) {
        this.user = user;
        this.cartItems = cartItems;
        this.status = status;
    }

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


