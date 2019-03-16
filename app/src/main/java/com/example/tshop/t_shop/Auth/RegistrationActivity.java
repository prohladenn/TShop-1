package com.example.tshop.t_shop.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.tshop.t_shop.R;
import com.example.tshop.t_shop.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseUser currntUser;
    private FirebaseFirestore mFirestore;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private EditText nameEditText;
    private FrameLayout nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        currntUser = mAuth.getCurrentUser();
        nextButton = (FrameLayout) findViewById(R.id.button_next);
        nameEditText = (EditText) findViewById(R.id.edit_text_name);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().isEmpty()) {
                    nameEditText.setError("Введите имя");
                    nameEditText.requestFocus();
                    return;
                }
                addData();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void addData() {
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setMessage("Загрузка");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Map<String, Object> user = new HashMap<>();
        user.put("name", nameEditText.getText().toString().trim());
        user.put("phoneNumber", getIntent().getStringExtra("phone_number"));
        user.put("registerDate", FieldValue.serverTimestamp());
        user.put("status", "online");
        mFirestore.collection("users").document(currntUser.getUid()).set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> role = new HashMap<>();
                role.put("code", "client");
                role.put("name", "Клиент");
                mFirestore.collection("users").document(currntUser.getUid()).collection("roles").add(role).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Intent intent = new Intent(RegistrationActivity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
