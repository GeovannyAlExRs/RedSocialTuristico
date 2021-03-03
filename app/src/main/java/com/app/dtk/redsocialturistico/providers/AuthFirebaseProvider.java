package com.app.dtk.redsocialturistico.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthFirebaseProvider {

    private FirebaseAuth firebaseAuth;

    public AuthFirebaseProvider(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> register (String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
    public Task<AuthResult> login(String email, String clave) {
        return firebaseAuth.signInWithEmailAndPassword(email, clave);
    }

    public Task<AuthResult> loginGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return firebaseAuth.signInWithCredential(credential);
    }

    public String getFirebaseUid() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public String getFirebaseEmail() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getEmail();
        } else {
            return null;
        }
    }

    public FirebaseUser getUserSession() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser();
        } else {
            return null;
        }
    }

    public void logout() {
        if (firebaseAuth != null){
            firebaseAuth.signOut();
        }
    }
}
