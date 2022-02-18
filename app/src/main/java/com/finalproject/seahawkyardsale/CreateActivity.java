package com.finalproject.seahawkyardsale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirm;
    private FirebaseAuth auth;
    private static final String TAG = "CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Seahawk Yardsale");
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void createAccount(View view) {
        if (!validateForm()) {
            return;
        }

        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Your account has successfully been created! Please Log in with your credentials.", Toast.LENGTH_LONG).show();
                } else {
                    Exception e = task.getException();
                    Log.w(TAG, "createUserWithEmail:failure", e);
                    Toast.makeText(CreateActivity.this, "Registration failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        passwordConfirm = findViewById(R.id.password2Login);

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

        String password2 = passwordConfirm.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            passwordConfirm.setError("Required");
            valid = false;
        }
        else if (!password.equals(password2)) {
            passwordConfirm.setError("Not identical");
            valid = false;
        }
        else {
            passwordConfirm.setError(null);
        }

        return valid;
    }


}
