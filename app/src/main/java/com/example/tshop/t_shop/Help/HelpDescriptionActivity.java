package com.example.tshop.t_shop.Help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tshop.t_shop.R;

public class HelpDescriptionActivity extends Activity {

    String textProblem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_description);
        initialize();
        textProblem = getIntent().getStringExtra("text_problem");
        editTextProblem.setText(textProblem);
        buttonBackListener();
        buttonCheckListener();
    }

    ImageView buttonBack;
    ImageView buttonCheck;
    EditText editTextProblem;

    void initialize() {
        buttonBack = findViewById(R.id.button_help_desk_back);
        buttonCheck = findViewById(R.id.button_help_desk_check);
        editTextProblem = findViewById(R.id.edit_text_help_problem);
    }

    private void buttonBackListener() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void buttonCheckListener() {
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("text_problem", editTextProblem.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!textProblem.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("text_problem", textProblem);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}