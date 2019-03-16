package com.example.tshop.t_shop.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.tshop.t_shop.R;
import com.example.tshop.t_shop.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static com.example.tshop.t_shop.Auth.PhoneAuthActivity.DELETE_REQUEST;

public class ConfirmationActivity extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currentUser;
    private EditText editText;
    private Boolean flag = false;
    private String phonenumber;
    private FrameLayout signIn;
    private FrameLayout resendCodeButton;
    private Timer timer;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack;
    private Map<String, Object> user = new HashMap<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editText = findViewById(R.id.edit_text_code);
        resendCodeButton  = findViewById(R.id.button_again);
        resendCodeButton.setVisibility(View.GONE);
        resendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phonenumber);
                resendCodeButton.setVisibility(View.GONE);
            }
        });
        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);
        signIn = (FrameLayout) findViewById(R.id.button_next);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Неверный код");
                    editText.requestFocus();
                    return;
                }
                signIn.setEnabled(false);
                verifyCode(code);
            }
        });



    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }
        catch (Exception e){
            Log.i("exception",e.toString());
            editText.setError("Неверный код");
            editText.requestFocus();
            return;
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            resendCodeButton.setVisibility(View.GONE);
                            isUserCreated();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,               // Activity (for callback binding)
                mCallBack
        );

    }
    public void resendCode(View view) {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack,
                resendToken);
    }

    private void isUserCreated(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean flag = false;
                        for( DocumentSnapshot doc:task.getResult()) {
                            if (doc.getId().equals(mAuth.getCurrentUser().getUid())) {
                                flag = true;
                            }
                        }
                        if (flag){Intent intent = new Intent(ConfirmationActivity.this, StartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(ConfirmationActivity.this,  RegistrationActivity.class);
                            String numberClear = "+" + phonenumber.replaceAll("[^0-9]+", "");
                            intent.putExtra("phone_number", numberClear);
                            startActivityForResult(intent,DELETE_REQUEST);
                            finish();
                        }
                        }
                });
                }
    private void setUpVerificationCallbacks() {
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationId = s;
                resendToken = forceResendingToken;
                resendCodeButton.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editText.setText(code);
                    verifyCode(code);
                    resendCodeButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
        };
    }

}
