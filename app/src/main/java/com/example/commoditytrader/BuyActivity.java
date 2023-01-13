package com.example.commoditytrader;

import static android.content.ContentValues.TAG;

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

public class BuyActivity extends AppCompatActivity {
    String ticker;
    double price, bal, amt;
    int quant;
    TextView tv;
    EditText et;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Intent i = getIntent();
        tv = (TextView) findViewById(R.id.textView5);
        ticker = i.getStringExtra("ticker");
        price = i.getDoubleExtra("price",0.0);
        tv.setText(ticker);
        et = findViewById(R.id.editTextNumber);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
//        db.collection("users")
//                .whereEqualTo("email", email)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("imad", document.getId() + " => " + document.getData());
//                                Map<String, Object> u = new HashMap<>();
//                                u = document.getData();
//                                et.setText(String.valueOf(u.get("amount")));
//
//                            }
//
//
//                        } else {
//                            Log.d("imad", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



    }
    public void buy(View view)
    {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            quant = Integer.parseInt(String.valueOf(et.getText()));
                            amt = quant * price;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("imad", document.getId() + " => " + document.getData());
                                Map<String, Object> u = new HashMap<>();
                                u = document.getData();
                                bal = Double.parseDouble(String.valueOf(u.get("amount")));
                            }
                            if(amt > bal)
                            {
                                Toast.makeText(BuyActivity.this, "Not enough funds", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
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
                                                DocumentReference stockRef = db.collection("sto_cks").document(ticker+"#"+email);

                                                ((DocumentReference) stockRef)
                                                        .update("count", FieldValue.increment(quant) )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("imad", "DocumentSnapshot successfully updated!");
                                                                Toast.makeText(BuyActivity.this, "Stock Bought Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("imad", "Error updating document", e);
                                                            }
                                                        });
                                                Log.d("imad", "DocumentSnapshot data: " + document.getData());
                                            } else {
                                                Log.d("imad", "No such document");
                                                db = FirebaseFirestore.getInstance();
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String email = user.getEmail();
                                                Map<String, Object> newStock = new HashMap<>();
                                                newStock.put("name", ticker);
                                                newStock.put("email", email);
                                                newStock.put("count", quant);
                                                db.collection("sto_cks")
                                                        .document(ticker+"#"+email)
                                                        .set(newStock)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(BuyActivity.this, "Stock Bought Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("imad", "Error adding document", e);
                                                            }
                                                        });

                                            }
                                        } else {
                                            Log.d("imad", "get failed with ", task.getException());
                                        }
                                    }
                                });
                                double finalAmt = bal - amt;

                                DocumentReference userRef = db.collection("users").document(email);

                                ((DocumentReference) userRef)
                                        .update("amount", finalAmt )
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("imad", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("imad", "Error updating document", e);
                                            }
                                        });


                            }

                        } else {
                            Log.d("imad", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}