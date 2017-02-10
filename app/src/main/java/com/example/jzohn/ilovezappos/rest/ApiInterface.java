package com.example.jzohn.ilovezappos.rest;

import com.example.jzohn.ilovezappos.model.Product;
import com.example.jzohn.ilovezappos.model.ProductImage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jzohn on 2/3/2017.
 */

public interface ApiInterface {
    @GET("Search")
    Call<Product> getProductSearch(@Query("term") String productName,
                                   @Query("limit") int numOfItems,
                                   @Query("key") String key);

    @GET("Product")
    Call<ProductImage> getProductWithImage(@Query("id") int productId,
                                           @Query("key") String key);
}
