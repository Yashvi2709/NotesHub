package com.example.studymateriallocator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button loginBtn,goRegister;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goRegister = findViewById(R.id.goRegister);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {

            String e = email.getText().toString();
            String p = password.getText().toString();

            auth.signInWithEmailAndPassword(e,p)
                    .addOnSuccessListener(authResult -> {

                        Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this,MainActivity.class));

                    })
                    .addOnFailureListener(e1 ->
                            Toast.makeText(this,e1.getMessage(),Toast.LENGTH_SHORT).show()
                    );

        });

        goRegister.setOnClickListener(v ->
                startActivity(new Intent(this,RegisterActivity.class))
        );
    }
}