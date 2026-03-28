package com.example.studymateriallocator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button uploadBtn, viewBtn, logoutBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadBtn = findViewById(R.id.uploadBtn);
        viewBtn = findViewById(R.id.viewBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        auth = FirebaseAuth.getInstance();

        // Open Upload Screen
        uploadBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UploadActivity.class));
        });

        viewBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewMaterialsActivity.class));
        });

        // Logout
        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

    }
}