package com.example.commoditytrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.commoditytrader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BrowseActivity extends AppCompatActivity {
    EditText et;
    ListView lv;
    ArrayList<String> stock_name = new ArrayList<String>();
    HashMap<String,String> tickers=new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        et = (EditText) findViewById(R.id.editText2);
        lv = (ListView) findViewById(R.id.listView);



    }
    public void getDescription(View view)
    {
        lv.setAdapter(null);
        stock_name.clear();
        tickers.clear();
        String stock = String.valueOf(et.getText());
        RequestQueue rq;
        rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://imad-stocks-app.herokuapp.com/search/"+stock, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    for(int pos=0;pos<arr.length();pos++)
                    {
                        if(pos > 5)
                            break;
                        JSONObject obj = arr.getJSONObject(pos);
                        stock_name.add(obj.getString("name"));
                        tickers.put(obj.getString("name"),obj.getString("ticker"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stock_name);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String s_name = (String) adapterView.getItemAtPosition(i);
                            String ticker = tickers.get(s_name);
                            Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                            intent.putExtra("ticker",ticker);
                            startActivity(intent);
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