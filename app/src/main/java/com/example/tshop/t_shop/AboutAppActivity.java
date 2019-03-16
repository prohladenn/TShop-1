package com.example.tshop.t_shop;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tshop.t_shop.BuildConfig;
import com.example.tshop.t_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AboutAppActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private FirebaseUser currntUser;
    String versionName = BuildConfig.VERSION_NAME;
    String url;
    ImageView buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        TextView version = (TextView) findViewById(R.id.version_name);
        version.setText("Версия приложения - " + versionName);
        TextView description = (TextView) findViewById(R.id.description);
        FrameLayout linkButton = (FrameLayout) findViewById(R.id.button_url);

        buttonBack = findViewById(R.id.button_back);
        buttonBackListener();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("service").document("about").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.getString("description")!=null)
                description.setText(doc.getString("description"));
                url =doc.getString("developerUrl");

            }
        });
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
    }
        private void buttonBackListener() {
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
        }
}
