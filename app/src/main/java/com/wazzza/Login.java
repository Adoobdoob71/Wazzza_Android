package com.wazzza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    MaterialToolbar toolbar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_login);
        InitializeVariables();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_from_top);
            }
        });
    }

    public void InitializeVariables(){
        email = findViewById(R.id.login_activity_email);
        password = findViewById(R.id.login_activity_password);
        toolbar = findViewById(R.id.login_activity_toolbar);
    }

    public void SubmitLogin(View view) {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_from_top);
                    }
                    else {
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
    }
}