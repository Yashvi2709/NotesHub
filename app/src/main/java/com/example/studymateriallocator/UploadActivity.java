package com.example.studymateriallocator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    EditText title, subject, link;
    Button uploadPdf;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        title = findViewById(R.id.title);
        subject = findViewById(R.id.subject);
        link = findViewById(R.id.link);
        uploadPdf = findViewById(R.id.uploadPdf);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        uploadPdf.setOnClickListener(v -> {
            String mTitle = title.getText().toString();
            String mSubject = subject.getText().toString();
            String mLink = link.getText().toString();

            if (mTitle.isEmpty() || mSubject.isEmpty() || mLink.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadMaterial(mTitle, mSubject, mLink);
        });
    }

    private void uploadMaterial(String title, String subject, String url) {
        String userEmail = auth.getCurrentUser().getEmail();
        Map<String, Object> material = new HashMap<>();
        material.put("title", title);
        material.put("subject", subject);
        material.put("url", url);
        material.put("uploadedBy", userEmail);
        material.put("downloads", 0);

        db.collection("materials")
                .add(material)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UploadActivity.this, "Material Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UploadActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}