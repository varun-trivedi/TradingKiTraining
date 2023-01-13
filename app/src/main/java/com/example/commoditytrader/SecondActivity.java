package com.example.commoditytrader;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {

    String gTicker;
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        String ticker = intent.getStringExtra("ticker");
        gTicker = ticker;

        TextView tv = (TextView)findViewById(R.id.textView);
        TextView tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        RequestQueue rq;
        RequestQueue rq2;

        rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://imad-stocks-app.herokuapp.com/detail/"+ticker, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    JSONObject obj = arr.getJSONObject(0);
                    String x = obj.getString("name");
                    String y = obj.getString("description");
                    tv.setText(x);
                    tv2.setText(y);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });

        rq2 = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET,
                "https://imad-stocks-app.herokuapp.com/price/"+ticker, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    JSONObject obj = arr.getJSONObject(0);
                    double x = obj.getDouble("last");
                    tv3.setText(valueOf(x));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });

        rq.add(jsonObjectRequest);
        rq2.add(jsonObjectRequest2);

    }

    public void showGraph(View view){
        Intent i = new Intent(this,GraphActivity.class);
        i.putExtra("ticker",gTicker);
        startActivity(i);

    }


    public void getNews(View view) {
        Intent i = new Intent(this,NewsActivity.class);
        i.putExtra("ticker",gTicker);
        startActivity(i);
    }
    public void buy(View view)
    {
        Intent i = new Intent(this,BuyActivity.class);
        i.putExtra("ticker",gTicker);
        i.putExtra("price",Double.parseDouble(String.valueOf(tv3.getText())));
        startActivity(i);
    }
    public void sell(View view)
    {
        Intent i = new Intent(this,SellActivity.class);
        i.putExtra("ticker",gTicker);
        i.putExtra("price",Double.parseDouble(String.valueOf(tv3.getText())));
        startActivity(i);
    }
}