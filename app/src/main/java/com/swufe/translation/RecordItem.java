package com.swufe.translation;

public class RecordItem {
    private int id;
    private String Name;

    public RecordItem() {
        this.Name = "";
    }

    public RecordItem(String Name) {
        this.Name = Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}


