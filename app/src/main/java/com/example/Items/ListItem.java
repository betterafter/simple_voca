package com.example.Items;

public class ListItem {
    public String[] data;
    public int type;

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

    public ListItem(String[] data, int type){
        this.data = data;
        this.type =type;
    }
}
