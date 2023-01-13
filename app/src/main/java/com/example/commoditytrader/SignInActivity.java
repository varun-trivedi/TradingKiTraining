package com.example.commoditytrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;

    Button btnSignIn;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ProgressDialog progressDialog;

    Boolean isPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword2);

        btnSignIn = findViewById(R.id.btnSignIn);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        isPresent = false;

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView goSignUp = findViewById(R.id.goSignUp);
        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SignIn(View view) {

        progressDialog.setTitle("Sign In");
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        String emailID = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());
        if (TextUtils.isEmpty(emailID)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailID, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot doc : task.getResult()){
//                                String email = doc.getReference().getPath().substring(6);
//                                if(email.equals(emailID)){
//                                    HashMap<String, Object> pwd = (HashMap<String, Object>) doc.getData();
//                                    if (password.equals(pwd.get("password"))) {
//                                        isPresent = true;
//                                        if (isPresent) {
//                                            progressDialog.dismiss();
//                                        }
//                                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
//                                        startActivity(intent);
//                                    }
//                                    else {
//                                        if (isPresent) {
//                                            progressDialog.dismiss();
//                                        }
//                                    }
//                                }
//                            }
//                            if (!isPresent) {
//                                Toast.makeText(SignInActivity.this, "Invalid Credentials 1", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Log.w("TAG", "Error fetching document", e);
//                        Toast.makeText(SignInActivity.this, "Fetch Error", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

}