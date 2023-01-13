package com.example.commoditytrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SellActivity extends AppCompatActivity {
    String ticker;
    int quant;
    double price,bal,amt;
    TextView tv;
    EditText et;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Intent i = getIntent();
        tv = (TextView) findViewById(R.id.textView7);
        ticker = i.getStringExtra("ticker");
        price = i.getDoubleExtra("price",0.0);
        tv.setText(ticker);
        et = findViewById(R.id.editTextNumber2);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
    }
    public void sell(View view)
    {
        quant = Integer.parseInt(String.valueOf(et.getText()));
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        DocumentReference docRef = db.collection("sto_cks").document(ticker+"#"+email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = user.getEmail();
                        Map<String, Object> stock = new HashMap<>();
                        stock = document.getData();
                        int count = Integer.parseInt(String.valueOf(stock.get("count")));
                        if(count < quant)
                        {
                            Toast.makeText(SellActivity.this, "You don't have these many shares", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            db = FirebaseFirestore.getInstance();
                            amt = price*quant;
                            DocumentReference stockRef = db.collection("sto_cks").document(ticker+"#"+email);

                            ((DocumentReference) stockRef)
                                    .update("count", FieldValue.increment(-1*quant) )
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("imad", "DocumentSnapshot successfully updated!");
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            String email = user.getEmail();
                                            db.collection("users")
                                                    .whereEqualTo("email", email)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {

                                                                amt = quant * price;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    Log.d("imad", document.getId() + " => " + document.getData());
                                                                    Map<String, Object> u = new HashMap<>();
                                                                    u = document.getData();
                                                                    bal = Double.parseDouble(String.valueOf(u.get("amount")));

                                                                }
                                                                double finalAmt = bal + amt;

                                                                DocumentReference userRef = db.collection("users").document(email);

                                                                ((DocumentReference) userRef)
                                                                        .update("amount", finalAmt )
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d("imad", "DocumentSnapshot successfully updated!");
                                                                                Toast.makeText(SellActivity.this, "Stock Sold Successfully", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w("imad", "Error updating document", e);
                                                                            }
                                                                        });

                                                            } else {
                                                                Log.d("imad", "Error getting documents: ", task.getException());
                                                            }
                                                        }
                                                    });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("imad", "Error updating document", e);
                                        }
                                    });


                        }

                        Log.d("imad", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("imad", "No such document");
                        Toast.makeText(SellActivity.this, "You don't have a single share of this Stock", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d("imad", "get failed with ", task.getException());
                }
            }
        });
    }
}