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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private CircleImageView imageViewBack;
    private TextInputEditText txt_nameUser, txt_email, txt_password, confirmPassword;
    private AppCompatButton btn_register;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getViewId();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
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
                if (password.equals(confirmPassword)) {
                    Toast.makeText(this, "Bienvenido " + nameUser,Toast.LENGTH_LONG).show();
                    if (password.length() >= 8) {
                        saveUser(nameUser, email, password);
                    } else {
                        Toast.makeText(this, "La clave debe ser mayor a 8 caracteres",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Las claves no coinciden",Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Email Incorrecto" + email,Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Ingresa todos los campos",Toast.LENGTH_LONG).show();
        }

    }

    private void saveUser(final String nameUser, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idDoc = firebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("userName", nameUser);
                    map.put("email", email);
                    map.put("password", password);
                    firebaseFirestore.collection("Users").document(idDoc).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente en la BD", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "El usuario NO se registro correctamente en la BD", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "El usuario NO se registro correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
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