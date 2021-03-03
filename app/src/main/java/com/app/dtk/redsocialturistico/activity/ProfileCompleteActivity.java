package com.app.dtk.redsocialturistico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.model.Users;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.UsersFirestoreProvider;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class ProfileCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txt_nameUser, txt_phone;
    private AppCompatButton btn_confirm;
    private ProgressBar progressBar;
    private LinearLayoutCompat linearLayoutCompat;

    AuthFirebaseProvider authFirebaseProvider;
    UsersFirestoreProvider usersFirestoreProvider;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_complete);

        getViewId();
        authFirebaseProvider = new AuthFirebaseProvider();
        usersFirestoreProvider = new UsersFirestoreProvider();
    }
    private void getViewId() {

        txt_nameUser = findViewById(R.id.id_txt_nameUser);
        txt_phone = findViewById(R.id.id_txt_phone);

        btn_confirm = findViewById(R.id.id_btn_confirm);
        btn_confirm.setOnClickListener(this);

        progressBar = findViewById(R.id.id_spinkit_progress);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SCREEN);

        linearLayoutCompat = findViewById(R.id.id_linearLayout_transparent);
        linearLayoutCompat.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_confirm:
                goRegister();
                break;
        }
    }

    private void goRegister() {
        final String nameUser = txt_nameUser.getText().toString();
        final String phone = txt_phone.getText().toString();

        if(!nameUser.isEmpty() && !phone.isEmpty()) {
            updateUser(nameUser, phone);
        } else {
            Toast.makeText(this, "Ingresa todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser(String nameUser, final String phone) {

        linearLayoutCompat.setVisibility(View.VISIBLE);

        Users u = new Users();

        u.setId_users(authFirebaseProvider.getFirebaseUid());
        u.setUsername(nameUser);
        u.setPhone(phone);
        u.setTimestamp(new Date().getTime());

        usersFirestoreProvider.updateUsers(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                linearLayoutCompat.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Toast.makeText(ProfileCompleteActivity.this, "El usuario se registro correctamente en la BD", Toast.LENGTH_SHORT).show();
                    goToView(MainActivity.class);
                } else {
                    Toast.makeText(ProfileCompleteActivity.this, "El usuario NO se registro correctamente en la BD", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(this, activiyClass);
        startActivity(intent);
    }
}