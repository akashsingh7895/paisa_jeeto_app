package com.avssolutiion.newspinwheelappSpinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.avssolutiion.newspinwheelappSpinapp.databinding.ActivitySplashBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class splashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    private Animation topAnim,bottomAmin;

    int maxLimit;
    String limit;
    String todayDate,currentDate;
    FirebaseFirestore database;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
       // binding.progressbar.setVisibility(View.VISIBLE);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        todayDate = df.format(Calendar.getInstance().getTime());

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();



        if (networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable()){

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

            Button button= dialog.findViewById(R.id.btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });

            dialog.show();


        }else {


            topAnim = AnimationUtils.loadAnimation(this,R.anim.splash_top_anim);
            bottomAmin = AnimationUtils.loadAnimation(this,R.anim.splash_bottom_anim);

            binding.logoIv.setAnimation(topAnim);
            binding.titleTv.setAnimation(bottomAmin);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //todo: check if user is already is login

                    database.collection("users")
                            .document(deviceId).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot snapshot = task.getResult();
                                        if (snapshot!=null){
                                            if (snapshot.get("todayDate")!=null){
                                                currentDate = (String) snapshot.get("todayDate");
                                                Log.e("cudate",currentDate + " Date is available");

                                                if (!todayDate.equals(currentDate)){

                                                    Map<String, Object> updateUserData = new HashMap<>();
                                                    updateUserData.put("MyTodaySpin", 0);
                                                    updateUserData.put("todayDate", todayDate);

                                                    database.collection("users")
                                                            .document(deviceId)
                                                            .update(updateUserData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        Intent intent = new Intent(splashActivity.this, MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }

                                                            });
                                                }else {
                                                    Intent intent = new Intent(splashActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

//
                                            }else {
                                                Log.e("cudate"," Date is not available");
                                                Intent intent = new Intent(splashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
//                            else {
//                                Intent intent = new Intent(splashActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
                                    }else {
                                        Toast.makeText(splashActivity.this, "loaded", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                }
            },5000);

        }







    }
    public void getMaxLimit(){
        database.collection("maxLimit")
                .document("maxlimitdaily").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            limit = (String) snapshot.get("maxLimit");
                            maxLimit = Integer.parseInt(limit);
                            Log.e("ssss",""+maxLimit);

                        }else {
                            Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}


//                                    if (!currentDate.equals(todayDate)){
//                                        Map<String, Object> updateUserData = new HashMap<>();
//                                        updateUserData.put("MyTodaySpin", 0);
//                                        database.collection("users")
//                                                .document(deviceId)
//                                                .set(updateUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()){
//
//                                                            Intent intent = new Intent(splashActivity.this, MainActivity.class);
//                                                            startActivity(intent);
//                                                            finish();
//                                                            binding.progressbar.setVisibility(View.INVISIBLE);
//                                                            Toast.makeText(splashActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                                                        }else {
//                                                            Toast.makeText(splashActivity.this, "faild", Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    }
//                                                });
