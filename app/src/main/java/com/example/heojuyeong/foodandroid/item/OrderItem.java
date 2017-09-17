package com.example.heojuyeong.foodandroid.item;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */

public class OrderItem {
    int menu_id;
    int rest_id;
    int user_id;
    int quantity;
    ArrayList<Integer> optionNecessary;
    ArrayList<Integer> optionNotNecessary;
    int status;
    String arrived_time;
    String message;
    String payment;
    String restaurantName;
    String restaurantAddress;


    public OrderItem(int menu_id, int rest_id, int user_id, String arrived_time, String restaurantName, String restaurantAddress,  ArrayList<Integer> optionNecessary, ArrayList<Integer> optionNotNecessary) {
        this.menu_id = menu_id;
        this.rest_id = rest_id;
        this.user_id = user_id;
        this.quantity = 1;
        this.status = 0;
        this.arrived_time = arrived_time;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.message = "";
        this.payment = "";
        this.optionNecessary = optionNecessary;
        this.optionNotNecessary = optionNotNecessary;
    }

    public OrderItem(int menu_id, int rest_id, int user_id, String arrived_time, String restaurantName, String restaurantAddress) {
        this.menu_id = menu_id;
        this.rest_id = rest_id;
        this.user_id = user_id;
        this.quantity = 1;
        this.status = 0;
        this.arrived_time = arrived_time;
        this.message = "";
        this.payment = "";
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;

    }


    public int getMenu_id() {
        return menu_id;
    }

    public int getRest_id() {
        return rest_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStatus() {
        return status;
    }

    public String getArrived_time() {
        return arrived_time;
    }

    public String getMessage() {
        return message;
    }

    public String getPayment() {
        return payment;
    }

    public ArrayList<Integer> getOptionNecessary() {
        return optionNecessary;
    }

    public ArrayList<Integer> getOptionNotNecessary() {
        return optionNotNecessary;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "menu_id=" + menu_id +
                ", rest_id=" + rest_id +
                ", user_id=" + user_id +
                ", quantity=" + quantity +
                ", optionNecessary=" + optionNecessary +
                ", optionNotNecessary=" + optionNotNecessary +
                ", status=" + status +
                ", arrived_time='" + arrived_time + '\'' +
                ", message='" + message + '\'' +
                ", payment='" + payment + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                '}';
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOptionNecessary(ArrayList<Integer> optionNecessary) {
        this.optionNecessary = optionNecessary;
    }

    public void setOptionNotNecessary(ArrayList<Integer> optionNotNecessary) {
        this.optionNotNecessary = optionNotNecessary;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setArrived_time(String arrived_time) {
        this.arrived_time = arrived_time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }
}


