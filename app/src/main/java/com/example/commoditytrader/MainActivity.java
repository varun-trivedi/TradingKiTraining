package com.example.commoditytrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.commoditytrader.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=3000;

    //variables
    Animation topAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);


        //Hooks
        image=findViewById(R.id.imageView);
        image.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent= new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }

        },SPLASH_SCREEN);




    }
}
