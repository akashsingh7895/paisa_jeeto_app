package com.avssolutiion.newspinwheelappSpinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.avssolutiion.newspinwheelappSpinapp.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity implements MaxAdListener {

    ActivityProductBinding binding;
    int amount;
    int product_coins,value;


    public ProgressDialog dialog;

    //applovin ads
    private MaxInterstitialAd interstitialAd;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences1 = getSharedPreferences("MyWallet",MODE_PRIVATE);
        amount = sharedPreferences1.getInt("coins",0);
        Log.e("coins", String.valueOf(amount));
        binding.yourCoins.setText(String.valueOf("Your Coins : "+amount));


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        interstitialAd = new MaxInterstitialAd(getString(R.string.inter),this);
        interstitialAd.setListener(this);
        interstitialAd.loadAd();



        product_coins = getIntent().getIntExtra("product_coins",0);
        value = getIntent().getIntExtra("Value",0);

        if (value==0){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==1){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_1));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==2){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_2));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==3){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_3));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==4){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_4));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==5){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_5));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==6){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_6));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }else  if (value==7){
            binding.productImage.setImageDrawable(getDrawable(R.drawable.img_8));
            binding.textCom.setText(String.valueOf("Unlock This Product = " + product_coins+" Points"));
        }

        binding.claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (interstitialAd.isReady()){
                    interstitialAd.showAd();

                    if (amount>product_coins){
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dialog.dismiss();
                                Toast.makeText(ProductActivity.this, "Wait", Toast.LENGTH_SHORT).show();

                            }
                        },4000);
                    }else {
                        Toast.makeText(ProductActivity.this, "You Need More Points", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    if (amount>product_coins){
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dialog.dismiss();
                                Toast.makeText(ProductActivity.this, "Wait", Toast.LENGTH_SHORT).show();

                            }
                        },4000);
                    }else {
                        Toast.makeText(ProductActivity.this, "You Need More Points", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }
}