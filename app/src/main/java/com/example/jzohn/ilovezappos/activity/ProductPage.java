package com.example.jzohn.ilovezappos.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.jzohn.ilovezappos.R;
import com.example.jzohn.ilovezappos.databinding.ActivityProductPageBinding;
import com.example.jzohn.ilovezappos.model.Product;
import com.example.jzohn.ilovezappos.model.ProductImage;
import com.example.jzohn.ilovezappos.rest.ApiClient;
import com.example.jzohn.ilovezappos.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPage extends AppCompatActivity {
    private static final String LOG = "ProductPage";
    private static final String KEY = "b743e26728e16b81da139182bb2094357c31d331";
    private ApiInterface mApiService = null;
    private Product mProduct;
    private FloatingActionButton mAddCartFab;
    private FloatingActionButton mRemoveCartFab;
    private Animation mCartAnimStart;
    private Animation mCartAnimEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // passes the query to the getProductSearch method
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mApiService = ApiClient.getClient().create(ApiInterface.class);
            getProductSearch(query);
        }
    }

    // GET product data from API
    private void getProductSearch(String productQuery) {
        Call<Product> call = mApiService.getProductSearch(productQuery, 1, KEY);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Log.e(LOG, " >>> query success");
                    mProduct = response.body().getProductList().get(0);
                    // pass id to method to fetch an image large enough to represent the product
                    getProductImage(mProduct.getProductId());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // GET product image from API
    private void getProductImage(int productId) {
        Call<ProductImage> call = mApiService.getProductWithImage(productId, KEY);
        call.enqueue(new Callback<ProductImage>() {
            @Override
            public void onResponse(Call<ProductImage> call, Response<ProductImage> response) {
                if (response.isSuccessful()) {
                    Log.e(LOG, " >>> get image success");
                    mProduct.setDefaultImageUrl(response.body()
                            .getProductList().get(0).getDefaultImageUrl());
                    bindProductToPage(mProduct);
                }
            }

            @Override
            public void onFailure(Call<ProductImage> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void bindProductToPage(Product product) {
        ActivityProductPageBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_product_page);
        binding.setProduct(product);
        binding.executePendingBindings();

        // using picasso to set the image
        final ImageView productImage = (ImageView) findViewById(R.id.product_imageView);
        Picasso.with(getApplicationContext()).load(product.getDefaultImageUrl()).into(productImage);

        // sets the up (back) button to return to main activity
        final ImageView upButtonImageView = (ImageView) findViewById(R.id.back_image_view);
        upButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // fab anim dec
        mRemoveCartFab = (FloatingActionButton) findViewById(R.id.remove_cart_fab);
        mAddCartFab = (FloatingActionButton) findViewById(R.id.add_cart_fab);
        mCartAnimStart = AnimationUtils.loadAnimation(this, R.anim.cart_fab_animation_start);
        mCartAnimEnd = AnimationUtils.loadAnimation(this, R.anim.cart_fab_animation_end);
    }

    // anim switch fab to remove from cart
    public void clickAddToCart(View v) {
        Log.e(LOG, " >>> ADD to cart");
        mCartAnimStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddCartFab.setVisibility(View.GONE);
                mRemoveCartFab.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAddCartFab.startAnimation(mCartAnimStart);
        mRemoveCartFab.startAnimation(mCartAnimEnd);

    }

    // anim switch fab to add to cart
    public void clickRemoveFromCart(View v) {
        Log.e(LOG, " >>> REMOVE from cart");
        mCartAnimStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddCartFab.setVisibility(View.VISIBLE);
                mRemoveCartFab.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAddCartFab.startAnimation(mCartAnimEnd);
        mRemoveCartFab.startAnimation(mCartAnimStart);
    }
}
