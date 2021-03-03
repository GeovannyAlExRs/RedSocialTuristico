package com.app.dtk.redsocialturistico.include;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.dtk.redsocialturistico.R;

public class MyToolbar {

    public static void showToolbar(AppCompatActivity activity, String title, boolean activeBotton) {

        Toolbar toolbar= activity.findViewById(R.id.id_toolbar);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(activeBotton);

    }
}
