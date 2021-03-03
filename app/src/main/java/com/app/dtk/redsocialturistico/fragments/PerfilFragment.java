package com.app.dtk.redsocialturistico.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.activity.EditPerfilActivity;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.PostFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.UsersFirestoreProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircleImageView circleBackUser;
    private ImageView img_Portada;
    private TextView txt_nameUser, txt_phone, txt_num_post, txt_email;

    LinearLayoutCompat linear_btn_edit_user;

    PostFirebaseProvider postFirebaseProvider;
    UsersFirestoreProvider usersFirestoreProvider;
    AuthFirebaseProvider authFirebaseProvider;

    String nameUser = "";
    String phone = "";
    String email = "";
    String imgUrlPerfil = "";
    String imgUrlCover = "";

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        init(view);

        postFirebaseProvider = new PostFirebaseProvider();
        usersFirestoreProvider = new UsersFirestoreProvider();
        authFirebaseProvider = new AuthFirebaseProvider();

        getUsers();
        getNumberPost();

        return view;
    }

    private void getUsers() {
        usersFirestoreProvider.readUsers(authFirebaseProvider.getFirebaseUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        nameUser = documentSnapshot.getString("userName");
                        txt_nameUser.setText(nameUser);
                    }
                    if (documentSnapshot.contains("phone")) {
                        phone = documentSnapshot.getString("phone");
                        txt_phone.setText(phone);
                    }
                    if (documentSnapshot.contains("email")) {
                        email = documentSnapshot.getString("email");
                        txt_email.setText(email);
                    }
                    if (documentSnapshot.contains("img_perfil")) {
                        imgUrlPerfil = documentSnapshot.getString("img_perfil");
                        if (imgUrlPerfil != null) {
                            if (!imgUrlPerfil.isEmpty()) {
                                Picasso.with(getContext()).load(imgUrlPerfil).into(circleBackUser);
                            }
                        }
                    }
                    if (documentSnapshot.contains("img_cover")) {
                        imgUrlCover = documentSnapshot.getString("img_cover");
                        if (imgUrlCover != null) {
                            if (!imgUrlCover.isEmpty()) {
                                Picasso.with(getContext()).load(imgUrlCover).into(img_Portada);
                            }
                        }
                    }
                    // video 9 - min 5
                } else {

                }
            }
        });
    }

    private void getNumberPost() {
        postFirebaseProvider.getPostByUser(authFirebaseProvider.getFirebaseUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                txt_num_post.setText(String.valueOf(numberPost));
            }
        });
    }

    private void init(View v) {
        linear_btn_edit_user = v.findViewById(R.id.id_linear_btn_edit_user);
        linear_btn_edit_user.setOnClickListener(this);

        img_Portada = v.findViewById(R.id.id_img_Portada);
        circleBackUser= v.findViewById(R.id.id_circleBackUser);

        txt_nameUser = v.findViewById(R.id.id_txt_nameUser);
        txt_email = v.findViewById(R.id.id_txt_email);
        txt_num_post = v.findViewById(R.id.id_txt_num_post);
        txt_phone = v.findViewById(R.id.id_txt_phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_linear_btn_edit_user:
                goToView(EditPerfilActivity.class);
                break;
        }
    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(getContext(), activiyClass);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}