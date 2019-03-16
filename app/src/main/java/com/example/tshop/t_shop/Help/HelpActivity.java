package com.example.tshop.t_shop.Help;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tshop.t_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class HelpActivity extends Activity {

    String textProblem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initialize();
        buttonBackListener();
        frameProblemListener();
        frameSendListener();
    }
    private ProgressDialog progressDialog;
    ImageView buttonBack;
    EditText textName;
    EditText textNumber;
    EditText textTheme;
    TextView textViewProblem;
    FrameLayout frameProblem;
    FrameLayout frameSend;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currntUser;
    AlertDialog.Builder ad;

    void initialize() {
        buttonBack = findViewById(R.id.button_help_back);
        textName = findViewById(R.id.edit_text_help_name);
        textNumber = findViewById(R.id.edit_text_help_number);
        textTheme = findViewById(R.id.edit_text_help_theme);
        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(textNumber);
        textViewProblem = findViewById(R.id.edit_text_help_description);
        frameProblem = findViewById(R.id.button_help_description);
        switch (getIntent().getIntExtra("problem",0)){
            case 1: textTheme.setText("Водитель не выдал товар");
            break;
            case 2:  textTheme.setText("Товар ненадлежащего качества");
            break;
            default: break;
        }
        frameSend = findViewById(R.id.button_help_send);
        mAuth = FirebaseAuth.getInstance();
        currntUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void buttonBackListener() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void frameProblemListener() {
        frameProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, HelpDescriptionActivity.class);
                if (!textProblem.isEmpty()) {
                    intent.putExtra("text_problem", textProblem);
                    textProblem = "";
                } else intent.putExtra("text_problem", "");
                startActivityForResult(intent, 1234);
            }
        });
    }

    private void frameSendListener() {
        frameSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textName.getText().toString().trim().isEmpty()) {
                    textName.setError("Введите имя");
                    textName.requestFocus();
                    return;
                }
                String number = textNumber.getText().toString().trim();
                if (number.isEmpty() || number.length() < 18) {
                    textNumber.setError("Некорректный номер телефона");
                    textNumber.requestFocus();
                    return;
                }
                if (textTheme.getText().toString().trim().isEmpty()) {
                    textTheme.setError("Введите тему");
                    textTheme.requestFocus();
                    return;
                }
                if (textProblem.isEmpty()){
                    Toast.makeText(HelpActivity.this, "Заполните отзыв",
                            Toast.LENGTH_LONG).show();
                    return;}
                progressDialog = new ProgressDialog(HelpActivity.this);
                progressDialog.setMessage("Загрузка");
                progressDialog.show();
                progressDialog.setCancelable(false);
                Map<String, Object> appeals = new HashMap<>();
                appeals.put("date", FieldValue.serverTimestamp());
                appeals.put("description",textProblem);
                appeals.put("status","not_answered");
                appeals.put("subject",textTheme.getText().toString().trim());
                Map<String, Object> user = new HashMap<>();
                user.put("contactPhone","+" + textNumber.getText().toString().trim().replaceAll("[^0-9]+", ""));
                user.put("name",textName.getText().toString().trim());
                user.put("ref", mFirestore.collection("users").document(currntUser.getUid()));
                appeals.put("user",user);
                appeals.put("userId",currntUser.getUid());
                mFirestore.collection("service").document("help").collection("appeals").add(appeals).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        progressDialog.dismiss();
                        ad = new AlertDialog.Builder(HelpActivity.this);
                        ad.setTitle("Заявка оставлена!");
                        ad.setMessage("Тех поддержка связжется с вами");
                        ad.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                finish();
                            }
                        });
                        ad.setCancelable(true);
                        ad.show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {

            textProblem = data.getStringExtra("text_problem");
            if (textProblem.isEmpty()){
                textViewProblem.setText(R.string.help_description);
                textViewProblem.setTextColor(getResources().getColor(R.color.colorText));}
            else {
                int pos = textProblem.indexOf('\n');
                String firstLine = textProblem;
                if(pos > 0)
                    firstLine = textProblem.substring(0, pos);
                else {
                    if(textProblem.length() > 25)
                        firstLine = textProblem.substring(0, 28);}
                textViewProblem.setText(firstLine);
                textViewProblem.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}