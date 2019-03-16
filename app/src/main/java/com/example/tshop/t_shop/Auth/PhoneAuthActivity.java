package com.example.tshop.t_shop.Auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tshop.t_shop.R;
import com.google.firebase.auth.FirebaseAuth;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class PhoneAuthActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText phoneEditText;
    static final int DELETE_REQUEST = 3475;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        phoneEditText = findViewById(R.id.edit_text_phone_number);
        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(phoneEditText);


        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneEditText.getText().toString().trim();
                if (number.isEmpty() || number.length() < 18) {
                    phoneEditText.setError("Некорректный номер телефона");
                    phoneEditText.requestFocus();
                    return;
                }
                Intent intent = new Intent(PhoneAuthActivity.this, ConfirmationActivity.class);
                intent.putExtra("phonenumber", number);
                startActivity(intent);
                finish();
            }
        });

    }


}
