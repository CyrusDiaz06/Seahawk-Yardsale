package com.finalproject.seahawkyardsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewListingActivity extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private String email;
    private static final String LISTINGS = "listings";
    private static final String TAG = "NewListingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        email = user.getEmail();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Seahawk Yardsale");
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickCreateListing(View view) {
        if (!validateForm()) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        EditText nameEditText = findViewById(R.id.productName);
        EditText descEditText = findViewById(R.id.productDescription);
        EditText priceEditText = findViewById(R.id.productPrice);
        String prodName = nameEditText.getText().toString();
        String prodDescript = descEditText.getText().toString();
        String priceString = priceEditText.getText().toString();
        int price = Integer.parseInt(priceString);

        Listings l = new Listings(email, prodName, prodDescript, formatter.format(date), price);

        mDb.collection(LISTINGS).add(l).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "ItemCreation:success");
                Toast.makeText(NewListingActivity.this,
                        "Listing entry added!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewListingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                        Toast.makeText(NewListingActivity.this,
                                "Error: Entry failed to upload.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        TextView product = findViewById(R.id.productName);
        TextView description = findViewById(R.id.productDescription);
        TextView price = findViewById(R.id.productPrice);

        String stringProduct = product.getText().toString();
        if (TextUtils.isEmpty(stringProduct)) {
            product.setError("Required.");
            valid = false;
        } else {
            product.setError(null);
        }

        String stringDescript = description.getText().toString();
        if (TextUtils.isEmpty(stringDescript)) {
            description.setError("Required.");
            valid = false;
        } else {
            description.setError(null);
        }

        String stringPrice = price.getText().toString();
        if (TextUtils.isEmpty(stringPrice)) {
            price.setError("Required");
            valid = false;
        }

        int intPrice = Integer.parseInt(stringPrice);
        if (intPrice < 0) {
            price.setError("Price must be greater than 0.");
            valid = false;
        } else {
            price.setError(null);
        }
        return valid;
    }

}