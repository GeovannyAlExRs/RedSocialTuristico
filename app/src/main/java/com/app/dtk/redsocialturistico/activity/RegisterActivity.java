package com.app.dtk.redsocialturistico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private CircleImageView imageViewBack;
    private TextInputEditText txt_nameUser, txt_email, txt_password, confirmPassword;
    private AppCompatButton btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getViewId();
    }

    private void getViewId() {
        imageViewBack = findViewById(R.id.id_circleBack);
        imageViewBack.setOnClickListener(this);

        txt_nameUser = findViewById(R.id.id_txt_nameUser);
        txt_email = findViewById(R.id.id_txt_email);
        txt_password = findViewById(R.id.id_txt_password);
        confirmPassword = findViewById(R.id.id_txt_confirmPassword);

        btn_register = findViewById(R.id.id_btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_circleBack:
                //goToView(LoginActivity.class); goRegister();
                finish();
                break;
            case R.id.id_btn_register:
                goRegister();
                break;
        }
    }

    private void goRegister() {
        final String nameUser = txt_nameUser.getText().toString();
        final String email = txt_email.getText().toString();
        final String password = txt_password.getText().toString();
        final String confirmPassword = this.confirmPassword.getText().toString();

        if(!nameUser.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (isEmailValid(email)) {
                Toast.makeText(this, "Bienvenido " + nameUser,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email Incorrecto" + email,Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Ingresa todos los campos",Toast.LENGTH_LONG).show();
        }

    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void goToView(Class activiyClass) {
        Intent intent = new Intent(this, activiyClass);
        startActivity(intent);
    }
}