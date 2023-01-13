package com.example.commoditytrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class GraphActivity extends AppCompatActivity {


    int n = 503;
    double[] x= new double[n];
    double[] y= new double[n];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent i = getIntent();
        String gTicker = i.getStringExtra("ticker");
        for(int j=0;j<n;j++)
        {
            x[j] = j+1;
        }
        RequestQueue rq3;
        rq3 = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET,
                "https://imad-stocks-app.herokuapp.com/chart/historical/"+gTicker, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int pos = 0;
                    JSONArray arr = response.getJSONArray("results");
                    while(pos < arr.length()) {
                        JSONObject obj = arr.getJSONObject(pos);
                        y[pos] = obj.getDouble("close");
                        pos++;
                    }
                    GraphView graph;
                    LineGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs
                    graph = (GraphView) findViewById(R.id.graph);
                    series= new LineGraphSeries<>(data());
                    graph.setTitle("My Graph View");

                    // on below line we are setting
                    // text color to our graph view.
                    graph.setTitleColor(R.color.purple_200);

                    // on below line we are setting
                    // our title text size.
                    graph.setTitleTextSize(18);
                    //initializing/defining series to get the data from the method 'data()'
                    graph.addSeries(series);                   //adding the series to the GraphView


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
        rq3.add(jsonObjectRequest3);
    }
    public DataPoint[] data(){
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(x[i],y[i]);
            values[i] = v;
        }
        return values;
    }
}