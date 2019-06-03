package com.example.ra.finalproject;

public class Item {
    //existence justified by simplifying the delete of an item - ListActivity
    private String id;

    public Item() {
    }

    public Item(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
