package com.avssolutiion.newspinwheelappSpinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.avssolutiion.newspinwheelappSpinapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MaxAdListener {

    ActivityMainBinding binding;

    private static final int[]  sectors = {10,6,2,0,4,8};
    private static final int[] sectorsDegrees = new int[sectors.length];
    private static final Random random = new Random();

    private int degree = 0;
    private boolean isSpinning = false;

    int wonAmount;
    int amount;
    int addCoins;

    int maxLimit;
    long todaySpin ;

    String limit;
    String todayDate,currentDate;

    String deviceId;
    FirebaseFirestore database;
    FirebaseDatabase firebaseDatabase;
    Dialog dialog;

    private MaxInterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dialog = new Dialog(MainActivity.this);

        getMaxLimit();
        getDegreesForSectors();
        Drawer();

        interstitialAd = new MaxInterstitialAd(getString(R.string.inter),this);
        interstitialAd.setListener(this);
        interstitialAd.loadAd();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        todayDate = df.format(Calendar.getInstance().getTime());
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        binding.wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyWalletActivity.class));
            }
        });


        database.collection("users")
                .document(deviceId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if(snapshot!=null){

                                if(snapshot.get("MyTodaySpin")!=null && snapshot.get("todayDate")!=null){
                                    todaySpin = (long) snapshot.get("MyTodaySpin");
                                    Log.e("kkk", String.valueOf(todaySpin));
                                    currentDate = (String) snapshot.get("todayDate");
                                  //  Log.e("kkk", String.valueOf(currentDate));
                                }else {
                                    // MyTodaySpin is null
                                 //   Log.e("kkk", String.valueOf(todaySpin));
                                    Map<String, Object> updateUserData1 = new HashMap<>();
                                    updateUserData1.put("MyTodaySpin", 0);
                                    updateUserData1.put("todayDate", todayDate);
                                    database.collection("users")
                                            .document(deviceId)
                                            .set(updateUserData1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        database.collection("users")
                                                                .document(deviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()){
                                                                            DocumentSnapshot snapshot1 = task.getResult();
                                                                            todaySpin = (long) snapshot1.get("MyTodaySpin");
                                                                            Log.e("kkk", String.valueOf(todaySpin));
                                                                            currentDate = (String) snapshot1.get("todayDate");
                                                                            Log.e("kkk", String.valueOf(currentDate));

                                                                        }else {
                                                                            Toast.makeText(MainActivity.this,""+ task.getException(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                        Toast.makeText(MainActivity.this, "Today Spin is added", Toast.LENGTH_SHORT).show();
                                                    }else {

                                                    }
                                                }
                                            });
                                }
                            }



                        }else {
                            Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        //int finalSpin = (int) (maxLimit);
//        binding.totalSpin.setText(String.valueOf("You Have Left : " + maxLimit));
//        Log.e("max", String.valueOf(maxLimit));


        // fetch coins
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyWallet",MODE_PRIVATE);
        amount = sharedPreferences1.getInt("coins",0);
      //  binding.totalSpin.setText(String.valueOf(amount));
        // fetch coins




        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maxLimit>todaySpin && currentDate.equals(todayDate)){
                    if (!isSpinning){
                        spin();
                        isSpinning = true;
//                        todaySpinPlus = (int) liveSpin++;
//                        finalSpin = (int) (todaySpin+liveSpin);
                        database.collection("users")
                                .document(deviceId)
                                .update("MyTodaySpin", FieldValue.increment(1))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){


                                        }else {
                                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                }else {
                    Toast.makeText(MainActivity.this, "You Don't Have Any Chance", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }

    private void Drawer(){
        this.binding.navLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    case R.id.share:

                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            String shareMessage= "";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="+getPackageName();
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }

                        break;

                    case R.id.rates:

                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }

                        break;


                    case R.id.PrivacyPolicy:

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy)));
                        startActivity(browserIntent);
                        break;

                    case R.id.contact:

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.supported_email)});
                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }

                        break;


                }

                return true;
            }
        });
        this.binding.imgDrawerController.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.binding.dl.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void spin(){
        degree = random.nextInt(sectors.length-1);
        RotateAnimation rotateAnimation = new RotateAnimation(0,(360*sectors.length)+sectorsDegrees[degree],
                RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);

        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                wonAmount= sectors[sectors.length-(degree+1)];

                dialog.setContentView(R.layout.scratch_diloag);
                dialog.setCancelable(false);
                Objects.requireNonNull(dialog
                        .getWindow()).setBackgroundDrawable
                        (new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button button = dialog.findViewById(R.id.addButton);
                TextView textView = dialog.findViewById(R.id.amount1);
                textView.setText(String.valueOf(wonAmount));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (interstitialAd.isReady()){
                            interstitialAd.showAd();
                            SharedPreferences addAMountSp = getSharedPreferences("MyWallet",MODE_PRIVATE);
                            SharedPreferences.Editor addCoin = addAMountSp.edit();
                            addCoins = amount+wonAmount;
                            addCoin.putInt("coins",addCoins);
                            addCoin.commit();

                            startActivity(getIntent());
                            overridePendingTransition(0,0);
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, "Ads is not Ready   \n   Please check Your Internet !\n And Restart App", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                isSpinning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.spinWheel.startAnimation(rotateAnimation);



    }

    public void getDegreesForSectors(){
        int sectorsDegree = 360/sectors.length;

        for (int i = 0;i<sectors.length;i++){
            sectorsDegrees[i] =(i+1)*sectorsDegree;
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

                            database.collection("users")
                                    .document(deviceId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot!=null){
                                                    if (documentSnapshot.get("MyTodaySpin")!=null){
                                                        long finalSpinLeft = (long) documentSnapshot.get("MyTodaySpin");

                                                        int finalSpin = (int) (maxLimit-finalSpinLeft);
                                                        Log.e("max", String.valueOf("max Limit "+maxLimit));
                                                        Log.e("max", String.valueOf("Today spin "+todaySpin));
                                                        Log.e("max", String.valueOf("Final Spin "+finalSpin));
                                                        binding.totalSpin.setText(String.valueOf("You've Left : "+finalSpin));
                                                    }
                                                }
                                            }

                                        }
                                    });

                        }else {
                            Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
       finishAffinity();
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



