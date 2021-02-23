package com.app.dtk.redsocialturistico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txt_nameUser;
    private AppCompatButton btn_confirm;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_complete);

        getViewId();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    private void getViewId() {

        txt_nameUser = findViewById(R.id.id_txt_nameUser);

        btn_confirm = findViewById(R.id.id_btn_confirm);
        btn_confirm.setOnClickListener(this);
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

        if(!nameUser.isEmpty()) {
            updateUser(nameUser);
        } else {
            Toast.makeText(this, "Ingresa todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser(final String nameUser) {
        String idDoc = firebaseAuth.getCurrentUser().getUid();

        Map<String, Object> map = new HashMap<>();
        map.put("userName", nameUser);

        firebaseFirestore.collection("Users").document(idDoc).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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