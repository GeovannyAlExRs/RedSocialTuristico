package com.app.dtk.redsocialturistico.providers;

import com.app.dtk.redsocialturistico.model.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostFirebaseProvider {

    CollectionReference collectionReference;

    public PostFirebaseProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> createPost(Post post) {
        return collectionReference.document().set(post);
    }

    public String getFirebaseId() {
        return collectionReference.getId().toString();
    }

    public Query readPost() {
        return collectionReference.orderBy("title", Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String id) {
        return collectionReference.whereEqualTo("idUsers", id);
    }
}
