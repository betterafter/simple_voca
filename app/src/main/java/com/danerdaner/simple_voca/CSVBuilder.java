package com.danerdaner.simple_voca;

import com.danerdaner.Items.ListItem;
import com.danerdaner.activity.LoadingActivity;

import java.util.ArrayList;


public class CSVBuilder {

    public String getCSVString(ArrayList<ListItem> list){
        String ret = "";
        for (ListItem line:list) {
            ret += packLineToCSVString(line);
        }
        return ret;
    }

    private String packLineToCSVString(ListItem line){
        String ret = "";
        for(int i = 0 ; i < line.data.length; i ++){
            ret += line.data[i];
            ret += ',';
        }
        ret += '\n';
        return ret;
    }

    public void StringToDatabase(String file){
        String valid;
        ArrayList<ListItem> dataBase = new ArrayList<ListItem>();
        valid = getWordsFromCSVString(file, dataBase);
        if(valid == null){
            return;
        }
        if(!LoadingActivity.categoryDatabase.contains(valid)){
            LoadingActivity.categoryDatabase.insert(valid , "");
        }
        LoadingActivity.vocaDatabase.listToDatabase(dataBase);
    }

    public String getWordsFromCSVString(String data, ArrayList<ListItem> ret){
        String[] line = data.split("\n");
        String category = "";
        for(int i = 0 ; i < line.length; i ++){
            ret.add(getLineFromString(line[i]));
        }
        for(int i = 0 ; i < ret.size(); i ++){
            if(ret.get(i).data.length != 9){
                ret.remove(i);
                i --;
            }
        }
        if(ret.size() <= 0){
            return null;
        }
        category = ret.get(0).data[7];
        return category;
    }

    public ListItem getLineFromString(String line){
        String[] elements = line.split(",");
        ListItem item = new ListItem(elements, 0);
        return item;
    }
}
