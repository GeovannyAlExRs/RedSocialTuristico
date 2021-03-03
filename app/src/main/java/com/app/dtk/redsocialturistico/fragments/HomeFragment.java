package com.app.dtk.redsocialturistico.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.activity.LoginActivity;
import com.app.dtk.redsocialturistico.activity.PostActivity;
import com.app.dtk.redsocialturistico.adapters.AdapterPost;
import com.app.dtk.redsocialturistico.model.Post;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.PostFirebaseProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private View view;
    private FloatingActionButton floatingActionButton;

    private Toolbar toolbar;
    RecyclerView recyclerView;

    AdapterPost adapterPost;

    AuthFirebaseProvider authFirebaseProvider;
    PostFirebaseProvider postFirebaseProvider;

    private static final int OPTIONVIEW1 = 1;
    private static final int OPTIONVIEW2 = 2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        //MyToolbar.showToolbar((AppCompatActivity) getActivity(), "Maps Here", true);

        authFirebaseProvider = new AuthFirebaseProvider();
        postFirebaseProvider = new PostFirebaseProvider();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        return view;
    }

    private void init(View v) {
        toolbar = v.findViewById(R.id.id_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Publicaciones");
        setHasOptionsMenu(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        recyclerView = v.findViewById(R.id.id_post_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);

        floatingActionButton = v.findViewById(R.id.id_floating_new_post);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postFirebaseProvider.readPost();
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        adapterPost = new AdapterPost(options, getContext());
        recyclerView.setAdapter(adapterPost);
        adapterPost.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterPost.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_action_logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        authFirebaseProvider.logout();
        goToView(LoginActivity.class, OPTIONVIEW2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_floating_new_post:
                goToView(PostActivity.class, OPTIONVIEW1);
                break;
        }
    }

    private void goToView(Class activiyClass, int option) {
        Intent intent = new Intent(getContext(), activiyClass);
        if (option == 1) {
            startActivity(intent);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}