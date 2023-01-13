package com.example.commoditytrader;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ListView lv;
    TextView tv;
    ArrayList<String> stock_name = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db.collection("sto_cks")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stock_name.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("imad", document.getId() + " => " + document.getData());
                                Map<String, Object> stock = new HashMap<>();
                                stock = document.getData();
                                stock_name.add(stock.get("name") + "   " + stock.get("count"));
                            }
                            lv = findViewById(R.id.dash_stock);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stock_name);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String s_name = (String) adapterView.getItemAtPosition(i);
                                    String ticker = "";
                                    int pos = 0;
                                    while(s_name.charAt(pos) != ' ')
                                    {
                                        ticker += s_name.charAt(pos);
                                        pos = pos + 1;
                                    }
                                    Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                                    intent.putExtra("ticker",ticker);
                                    startActivity(intent);
                                }
                            });


                        } else {
                            Log.d("imad", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("imad", document.getId() + " => " + document.getData());
                                Map<String, Object> u = new HashMap<>();
                                tv = (TextView) findViewById(R.id.textView6);
                                u = document.getData();
                                tv.setText(String.valueOf(u.get("amount")));

                            }


                        } else {
                            Log.d("imad", "Error getting documents: ", task.getException());
                        }
                    }
                });







    }


    public void search(View view)
    {
        Intent intent = new Intent(this,BrowseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db.collection("sto_cks")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stock_name.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("imad", document.getId() + " => " + document.getData());
                                Map<String, Object> stock = new HashMap<>();
                                stock = document.getData();
                                stock_name.add(stock.get("name") + "   " + stock.get("count"));
                            }
                            lv = findViewById(R.id.dash_stock);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stock_name);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String s_name = (String) adapterView.getItemAtPosition(i);
                                    String ticker = "";
                                    int pos = 0;
                                    while(s_name.charAt(pos) != ' ')
                                    {
                                        ticker += s_name.charAt(pos);
                                        pos = pos + 1;
                                    }
                                    Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                                    intent.putExtra("ticker",ticker);
                                    startActivity(intent);
                                }
                            });


                        } else {
                            Log.d("imad", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("imad", document.getId() + " => " + document.getData());
                                Map<String, Object> u = new HashMap<>();
                                tv = (TextView) findViewById(R.id.textView6);
                                u = document.getData();
                                tv.setText(String.valueOf(u.get("amount")));

                            }


                        } else {
                            Log.d("imad", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void tutorial(View view)
    {
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
    }
}