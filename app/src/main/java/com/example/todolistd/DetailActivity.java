package com.example.todolistd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView d_uid, d_id, d_title, d_completed;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        d_uid = findViewById(R.id.duid);
        d_id = findViewById(R.id.did);
        d_title = findViewById(R.id.dtitle);
        d_completed = findViewById(R.id.dcompleted);

        Bundle extras = getIntent().getExtras();
        String duid = (String) extras.get("UID");
        String did = (String) extras.get("ID");
        String dtitle = (String) extras.get("TITLE");
        String dcompleted = (String) extras.get("COMPLETED");

        d_uid.setText("User ID: "+duid);
        d_id.setText("ID: "+did);
        d_title.setText("Title: "+dtitle);
        d_completed.setText("Status Complete: "+dcompleted);
    }
}