package com.finalproject.seahawkyardsale;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    Button logOut;
    private FirebaseAuth auth;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private static final String LISTINGS = "listings";
    private static final String TAG = "HomeActivity";
    private ListingsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOut = (Button) findViewById(R.id.buttonLogout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        auth = FirebaseAuth.getInstance();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CurrentyLoggedOut:success");
                auth.signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });

        Query query = mDb.collection(LISTINGS).orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listings> options = new FirestoreRecyclerOptions.Builder<Listings>()
                .setQuery(query, Listings.class).build();

        adapter = new ListingsRecyclerAdapter(options, new ListingsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "ItemView:success");
                String id = adapter.getSnapshots().getSnapshot(position).getId();
                Intent i = new Intent(HomeActivity.this, ItemViewActivity.class);
                i.putExtra("doc id", id);
                startActivity(i);

            }
        });
        RecyclerView recyclerView = findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, s.toString());
                if (s.toString().isEmpty()) {
                    Query query2 = mDb.collection(LISTINGS)
                            .orderBy("date", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Listings> options2 = new FirestoreRecyclerOptions.Builder<Listings>()
                            .setQuery(query2, Listings.class)
                            .build();
                    adapter.updateOptions(options2);
                }
                else {
                    Query query2 = mDb.collection(LISTINGS)
                            .whereEqualTo("product", s.toString())
                            .orderBy("date", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Listings> options2 = new FirestoreRecyclerOptions.Builder<Listings>()
                            .setQuery(query2, Listings.class)
                            .build();
                    adapter.updateOptions(options2);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public void onClickCreate(View view) {
        Intent intent = new Intent(HomeActivity.this, NewListingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}