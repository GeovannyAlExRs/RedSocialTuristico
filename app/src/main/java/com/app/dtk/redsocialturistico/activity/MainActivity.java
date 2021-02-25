package com.app.dtk.redsocialturistico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.fragments.ChatFragment;
import com.app.dtk.redsocialturistico.fragments.FilterFragment;
import com.app.dtk.redsocialturistico.fragments.HomeFragment;
import com.app.dtk.redsocialturistico.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewId();
        openFragment(new HomeFragment());
    }

    private void getViewId() {
        bottomNavigationView = findViewById(R.id.id_bottom_navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.id_item_navigation_home:
                            openFragment(new HomeFragment());
                            return true;
                        case R.id.id_item_navigation_filtro:
                            openFragment(new FilterFragment());
                            return true;
                        case R.id.id_item_navigation_chat:
                            openFragment(new ChatFragment());
                            return true;
                        case R.id.id_item_navigation_perfil:
                            openFragment(new PerfilFragment());
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_register:
                goToView(RegisterActivity.class);
                break;
        }
    }

    private void goToView(Class activiyClass) {
        Intent intent = new Intent(MainActivity.this, activiyClass);
        startActivity(intent);
    }*/
}