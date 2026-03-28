package com.example.studymateriallocator;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password;
    Button registerBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {

            String e = email.getText().toString();
            String p = password.getText().toString();

            auth.createUserWithEmailAndPassword(e,p)
                    .addOnSuccessListener(authResult ->
                            Toast.makeText(this,"Account Created",Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e1 ->
                            Toast.makeText(this,e1.getMessage(),Toast.LENGTH_SHORT).show()
                    );

        });

    }
}