package com.example.jzohn.ilovezappos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jzohn on 2/8/2017.
 */

public class ProductImage {
    @SerializedName("product")
    List<Product> productList;
    String defaultProductUrl;

    public String getDefaultProductUrl() {
        return defaultProductUrl;
    }

    public void setDefaultProductUrl(String defaultProductUrl) {
        this.defaultProductUrl = defaultProductUrl;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
