package com.finalproject.seahawkyardsale;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

public class Listings {

    private String username;
    private String product;
    private String description;
    private String date;
    private int price;
    private int id;
    @DocumentId
    private String documentId;

    public Listings() {
    }

    public Listings(String username, String product, String description, String date, int price) {
        this.username = username;
        this.product = product;
        this.description = description;
        this.date = date;
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @NonNull
    @Override
    public String toString(){
        return this.price+"";
    }

}
