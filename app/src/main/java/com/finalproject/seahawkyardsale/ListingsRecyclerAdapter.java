package com.finalproject.seahawkyardsale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ListingsRecyclerAdapter extends FirestoreRecyclerAdapter<Listings, ListingsRecyclerAdapter.ListingsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private final OnItemClickListener listener;

    ListingsRecyclerAdapter(FirestoreRecyclerOptions<Listings> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    ListingsRecyclerAdapter(FirestoreRecyclerOptions<Listings> options) {
        super(options);
        this.listener = null;
    }

    class ListingsViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView user;
        final TextView product;
        final TextView price;
        final TextView postDate;

        ListingsViewHolder(CardView v) {
            super(v);
            view = v;
            user = v.findViewById(R.id.username);
            product = v.findViewById(R.id.itemName);
            price = v.findViewById(R.id.price);
            postDate = v.findViewById(R.id.date);
        }

    }

    @Override
    public void onBindViewHolder(final ListingsViewHolder holder, @NonNull int position, @NonNull final Listings listings) {

        holder.user.setText(listings.getUsername());
        holder.product.setText(listings.getProduct());
        holder.price.setText(String.valueOf(listings.getPrice()));
        holder.postDate.setText(listings.getDate());

        if (listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAbsoluteAdapterPosition());
                }
            });
        }
    }

    @Override
    public ListingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list, parent, false);

        return new ListingsViewHolder(v);
    }

}