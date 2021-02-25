package com.app.dtk.redsocialturistico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.model.Users;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.UsersFirestoreProvider;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textRegister;
    private TextInputEditText txt_email, txt_password;
    private AppCompatButton btn_login;
    private SignInButton btn_signInGoogle;
    private ProgressBar progressBar;
    private  LinearLayoutCompat linearLayoutCompat;

    AuthFirebaseProvider authFirebaseProvider;
    UsersFirestoreProvider usersFirestoreProvider;

    private GoogleSignInClient googleSignInClient;

    private final int RC_SIGN_IN_GOOGLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViewId();

        authFirebaseProvider = new AuthFirebaseProvider();
        usersFirestoreProvider = new UsersFirestoreProvider();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_txt_register:
                goToView(RegisterActivity.class);
                break;
            case R.id.id_btn_login:
                //goToView(MainActivity.class);
                goLogin();
                break;
            case R.id.id_btn_signInGoogle:
                signInGoogle();
                break;
        }
    }

    private void goLogin() {
        String email = txt_email.getText().toString();
        String clave = txt_password.getText().toString();

        linearLayoutCompat.setVisibility(View.VISIBLE);

        if(!email.isEmpty() && !clave.isEmpty()) {
            authFirebaseProvider.login(email, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    linearLayoutCompat.setVisibility(View.INVISIBLE);
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "INGRESO CORRECTO", Toast.LENGTH_SHORT).show();
                        goToView(MainActivity.class);
                    } else {
                        Toast.makeText(LoginActivity.this, "Email o clave son incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Log.d("ENTRADA", "email: " + email + ", password: " + clave);
        } else {
            Toast.makeText(LoginActivity.this, "Ingresa los datos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("AUTHENTICAION", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        linearLayoutCompat.setVisibility(View.VISIBLE);

        authFirebaseProvider.loginGoogle(idToken).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNIN", "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "INICIO DE SESION CORRECTO", Toast.LENGTH_SHORT).show();
                            checkUserExist(authFirebaseProvider.getFirebaseUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            linearLayoutCompat.setVisibility(View.INVISIBLE);
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "ERROR AL INICIAR SESION", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Consulta el doc para verificar si existe el usuario para posterior guardarlo en la BD
    private void checkUserExist(final String idDoc) {
        usersFirestoreProvider.readUsers(idDoc).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Si el usuario existe en la BD inicia session
                    linearLayoutCompat.setVisibility(View.INVISIBLE);
                    goToView(MainActivity.class);
                } else {
                    // Caso contrario, el usuario nuevo sera almacenado en la BD e inicia session
                    Users u = new Users();

                    u.setId_users(idDoc);
                    u.setEmail(authFirebaseProvider.getFirebaseEmail());

                    usersFirestoreProvider.createUsers(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            linearLayoutCompat.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                goToView(ProfileCompleteActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "ERROR AL INICIAR SESION GOOGLE", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getViewId() {
        textRegister = findViewById(R.id.id_txt_register);
        textRegister.setOnClickListener(this);

        txt_email = findViewById(R.id.id_txt_email);
        txt_password = findViewById(R.id.id_txt_password);

        btn_login = findViewById(R.id.id_btn_login);
        btn_login.setOnClickListener(this);

        btn_signInGoogle = findViewById(R.id.id_btn_signInGoogle);
        btn_signInGoogle.setOnClickListener(this);

        progressBar = findViewById(R.id.id_spinkit_progress);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SCREEN);

        linearLayoutCompat = findViewById(R.id.id_linearLayout_transparent);
        linearLayoutCompat.setVisibility(View.INVISIBLE);
    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(LoginActivity.this, activiyClass);
        startActivity(intent);
    }
}