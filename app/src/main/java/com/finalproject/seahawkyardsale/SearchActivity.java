package com.finalproject.seahawkyardsale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "com.finalproject.seahawkyardsale.SearchActivity";
    private static final String PEOPLE = "people";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private ListingsRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestoresearch);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Query query = mDb.collection(PEOPLE)
                .orderBy("createdTime", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Listings> options = new FirestoreRecyclerOptions.Builder<Listings>()
                .setQuery(query, Listings.class)
                .build();

        mAdapter = new ListingsRecyclerAdapter(options);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}