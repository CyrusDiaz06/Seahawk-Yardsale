package com.finalproject.seahawkyardsale;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.ActionBar;


public class ItemViewActivity extends AppCompatActivity {
    private String docId;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private FirebaseAuth auth;
    private ConstraintLayout own;
    private ConstraintLayout other;
    private static final String TAG = "ItemViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final String email = user.getEmail();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Seahawk Yardsale");
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        own = findViewById(R.id.userItem);
        other = findViewById(R.id.notUserItem);
        Bundle intentDoc = getIntent().getExtras();
        docId = intentDoc.getString("doc id");
        docRef = database.collection("listings").document(docId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Listings L = documentSnapshot.toObject(Listings.class);

                if (email.equals(L.getUsername())) {
                    own.setVisibility(View.VISIBLE);
                    EditText productName = findViewById(R.id.editProductName);
                    EditText productDesc = findViewById(R.id.editProductDescription);
                    EditText productPrice = findViewById(R.id.editProductPrice);

                    productName.setText(L.getProduct());
                    productDesc.setText(L.getDescription());
                    productPrice.setText(String.valueOf(L.getPrice()));

                } else {
                    other.setVisibility(View.VISIBLE);
                    TextView product = findViewById(R.id.productName);
                    TextView description = findViewById(R.id.productDescription);
                    TextView price = findViewById(R.id.productPrice);
                    TextView name = findViewById(R.id.productUser);
                    TextView date = findViewById(R.id.productDate);
                    description.setMovementMethod(new ScrollingMovementMethod());

                    product.setText("Product: \n" + L.getProduct());
                    description.setText("Description: \n" + L.getDescription());
                    price.setText("Price: \n$" + String.valueOf(L.getPrice()));
                    name.setText("User: \n" + L.getUsername());
                    date.setText("Posted: \n" + L.getDate());
                }
            }
        });
    }

    public void onClickSave(View view) {
        if (!validateForm()) {
            return;
        }

        EditText productName = findViewById(R.id.editProductName);
        EditText productDesc = findViewById(R.id.editProductDescription);
        EditText productPrice = findViewById(R.id.editProductPrice);
        String newName = productName.getText().toString();
        String newDesc = productDesc.getText().toString();
        String newPrice = productPrice.getText().toString();
        int price = Integer.parseInt(newPrice);

        docRef.update("product", newName);
        docRef.update("description", newDesc);
        docRef.update("price", price);
        Log.d(TAG, "ItemChangeSave:success");
        Toast.makeText(getApplicationContext(),"Changes have been saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ItemViewActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onClickDelete(View view) {
        docRef.delete();
        Log.d(TAG, "ItemDeletion:success");
        Toast.makeText(getApplicationContext(),"Listing has been deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ItemViewActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;
        TextView product = findViewById(R.id.editProductName);
        TextView description = findViewById(R.id.editProductDescription);
        TextView price = findViewById(R.id.editProductPrice);

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

        if (TextUtils.isDigitsOnly(stringPrice)) {
            return valid;
        } else {
            price.setError("Only numbers are allowed in the Price field.");
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

    public void onClickContact(View view) {
        Log.d(TAG, "ItemSellerContact:success");
        TextView name = findViewById(R.id.productUser);
        String emailSend = name.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, emailSend);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
        intent.putExtra(Intent.EXTRA_TEXT, "I am interested in your item for sale!");
        intent.setType("text/plain");
        startActivity(intent);
    }
}