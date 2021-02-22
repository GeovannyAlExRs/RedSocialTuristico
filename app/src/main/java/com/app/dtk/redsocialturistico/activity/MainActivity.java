package com.app.dtk.redsocialturistico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.dtk.redsocialturistico.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewId();
    }

    private void getViewId() {
        textRegister = findViewById(R.id.id_txt_register);
        textRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_enter:
                goToView(RegisterActivity.class);
                break;
        }
    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(MainActivity.this, activiyClass);
        startActivity(intent);
    }
}