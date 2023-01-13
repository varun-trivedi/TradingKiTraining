package com.example.commoditytrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;



public class NewsActivity extends AppCompatActivity {

    String nTicker;
    ListView lv;
    ArrayList<String> title = new ArrayList<String>();
    HashMap<String,String> urilink=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        lv = (ListView) findViewById(R.id.listView1);

        Intent intent = getIntent();
        String ticker = intent.getStringExtra("ticker");
        nTicker = ticker;

        lv.setAdapter(null);
        RequestQueue rq;
        rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://imad-stocks-app.herokuapp.com/news/"+ticker, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    for(int pos=0;pos<arr.length();pos++)
                    {
                        if(pos > 5)
                            break;
                        JSONObject obj = arr.getJSONObject(pos);
                        title.add(obj.getString("title"));
                        urilink.put(obj.getString("title"),obj.getString("url"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, title);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String t_name = (String) adapterView.getItemAtPosition(i);
                            String ticker_link = urilink.get(t_name);

                            Uri webpage = Uri.parse(ticker_link);
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            startActivity(webIntent);



                        }
                    });


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
    }


}