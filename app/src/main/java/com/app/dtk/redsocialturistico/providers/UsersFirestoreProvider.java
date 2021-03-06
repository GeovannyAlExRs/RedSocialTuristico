package com.app.dtk.redsocialturistico.providers;

import com.app.dtk.redsocialturistico.model.Users;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UsersFirestoreProvider {

    FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    public UsersFirestoreProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<Void> createUsers(Users users) {
        return collectionReference.document(users.getId_users()).set(users);
    }

    public Task<DocumentSnapshot> readUsers(String idDoc) {
        return collectionReference.document(idDoc).get();
    }

    public Task<Void> updateUsers(Users users) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", users.getUsername());
        map.put("phone", users.getPhone());
        map.put("timestamp", new Date().getTime());
        map.put("img_perfil", users.getImg_perfil());
        map.put("img_cover", users.getImg_cover());

        return collectionReference.document(users.getId_users()).update(map);
    }


}
