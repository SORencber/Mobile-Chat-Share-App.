package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("resim_no")
    public String pid;
    @SerializedName("resim_baslik")
    public String name;
    @SerializedName("asagi")
    public String qty;
    @SerializedName("yukari")
    public String price;
    @SerializedName("resim_adi")
    public String image_url;
}
