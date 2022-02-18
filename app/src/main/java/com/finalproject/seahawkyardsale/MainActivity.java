package com.finalproject.seahawkyardsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailField;
    private EditText passwordField;
    private static final String TAG = "MainActivity";
    private ConstraintLayout logged_in;
    private ConstraintLayout logged_out;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "AppStartUp:success");
        logged_in = findViewById(R.id.logged_in);
        logged_out = findViewById(R.id.logged_out);
        title = findViewById(R.id.welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.createAccount);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Seahawk Yardsale");

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            logged_in.setVisibility(View.VISIBLE);
            logged_out.setVisibility(View.GONE);
            title.setText("You are already logged in.");
        } else {
            logged_in.setVisibility(View.GONE);
            logged_out.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        }
        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        auth = FirebaseAuth.getInstance();
        FirebaseUser current = auth.getCurrentUser();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }
        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }
        return valid;
    }

    public void onClickLogin(View view) {
        if (!validateForm()) {
            return;
        }

        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Exception e = task.getException();
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Login failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void OnClickHome(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        Toast.makeText(getApplicationContext(),"Welcome back!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "CurrentyLoggedIn:success");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}