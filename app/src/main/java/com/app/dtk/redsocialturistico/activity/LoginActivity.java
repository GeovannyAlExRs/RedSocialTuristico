package com.app.dtk.redsocialturistico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.dtk.redsocialturistico.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textRegister;
    private TextInputEditText txt_email, txt_password;
    private AppCompatButton btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViewId();
    }

    private void getViewId() {
        textRegister = findViewById(R.id.id_txt_register);
        textRegister.setOnClickListener(this);

        txt_email = findViewById(R.id.id_txt_email);
        txt_password = findViewById(R.id.id_txt_password);

        btn_enter = findViewById(R.id.id_btn_enter);
        btn_enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_txt_register:
                goToView(RegisterActivity.class);
                break;
            case R.id.id_btn_enter:
                //goToView(MainActivity.class);
                goLogin();
                break;
        }
    }

    private void goLogin() {
        String email = txt_email.getText().toString();
        String clave = txt_password.getText().toString();

        Log.d("ENTRADA", "email: " + email);
        Log.d("ENTRADA", "clave: " + clave);

        /*if(!email.isEmpty() && !clave.isEmpty()) {
            if (clave.length() >= 8) {

            }
        }*/

    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(LoginActivity.this, activiyClass);
        startActivity(intent);
    }
}