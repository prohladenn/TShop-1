package com.example.tshop.t_shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tshop.t_shop.Auth.PhoneAuthActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currntUser;

    private Boolean isProfileExist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currntUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        if (currntUser != null) {
            mFirestore.collection("users").document(currntUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    isProfileExist = !task.getResult().exists();

                }
            });
        }
        if (currntUser == null || isProfileExist) {
            Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
