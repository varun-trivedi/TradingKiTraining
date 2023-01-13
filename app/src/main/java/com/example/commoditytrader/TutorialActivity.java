package com.example.commoditytrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }

    Uri webpage;
    Intent webIntent;

    public void opentutorial(View v)
    {
        switch (v.getId())
        {
            case R.id.imageView2:
                webpage = Uri.parse("https://www.youtube.com/playlist?list=PL8uhW8cclMiNv8UT1NUawB-XpXVeJ8mN8");
                webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
                break;
            case R.id.imageView3:
                webpage = Uri.parse("https://www.youtube.com/watch?v=HyOdHAFqsfU&ab_channel=warikoo");
                webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
                break;
            case R.id.imageView4:
                webpage = Uri.parse("https://www.youtube.com/watch?v=GpiM_qi5mAc&ab_channel=Groww");
                webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
                break;
        }
    }
}