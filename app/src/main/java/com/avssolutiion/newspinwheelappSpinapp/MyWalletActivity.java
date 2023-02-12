package com.avssolutiion.newspinwheelappSpinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avssolutiion.newspinwheelappSpinapp.databinding.ActivityMyWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyWalletActivity extends AppCompatActivity {

    ActivityMyWalletBinding binding;
    Dialog dialog;
    int amount;
    int finalCoins;


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    String deviceId;
    String todayDate;

    Random random;
    long randomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new Dialog(MyWalletActivity.this);
        database = FirebaseDatabase.getInstance();



        random = new Random();
        randomId = random.nextInt(100000000);
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyWallet",MODE_PRIVATE);
        amount = sharedPreferences1.getInt("coins",0);
        Log.e("coins", String.valueOf(amount));
        binding.yourCoins.setText(String.valueOf("Your Coins : "+amount));

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        todayDate = df.format(Calendar.getInstance().getTime());
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);



        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",599);
                intent.putExtra("Value",0);
                startActivity(intent);
            }
        });


        binding.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",899);
                intent.putExtra("Value",1);
                startActivity(intent);
            }
        });


        binding.card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",999);
                intent.putExtra("Value",2);
                startActivity(intent);
            }
        });


        binding.card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",699);
                intent.putExtra("Value",3);
                startActivity(intent);
            }
        });

        binding.card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",799);
                intent.putExtra("Value",4);
                startActivity(intent);
            }
        });

        binding.card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",1099);
                intent.putExtra("Value",5);
                startActivity(intent);
            }
        });

        binding.card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",2999);
                intent.putExtra("Value",6);
                startActivity(intent);
            }
        });

        binding.card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,ProductActivity.class);
                intent.putExtra("product_coins",5999);
                intent.putExtra("Value",7);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(MyWalletActivity.this,MainActivity.class));
    }
}