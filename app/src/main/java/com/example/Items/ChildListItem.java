package com.example.Items;

public class ChildListItem {

    private String[] data;
    private int type;

    public ChildListItem(String[] data, int type) {
        this.data = data;
        this.type = type;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
