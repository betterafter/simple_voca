package com.danerdaner.Items;

public class ListItem implements Comparable<ListItem> {
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

    @Override
    public int compareTo(ListItem listItem) {
        return data[0].compareTo(listItem.getData()[0]);
    }
}
