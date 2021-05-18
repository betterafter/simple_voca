package com.danerdaner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.danerdaner.simple_voca.R;

import androidx.annotation.Nullable;

public class SettingFragment extends PreferenceFragment {

    SharedPreferences sharedPreferences;

    ListPreference font_size;
    ListPreference word_order;
    ListPreference category_select;

    CheckBoxPreference lock_screen;
    CheckBoxPreference service;

    Preference developer_info;
    Preference question;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        font_size = (ListPreference) findPreference("font_size");
        word_order = (ListPreference) findPreference("word_order");
        category_select = (ListPreference) findPreference("category");

        lock_screen = (CheckBoxPreference) findPreference("lock_screen");
        service = (CheckBoxPreference) findPreference("service");

        developer_info = findPreference("developer_info");
        question = findPreference("question");



        String[] category_names = new String[LoadingActivity.categoryList.size()];
        String[] category_values = new String[LoadingActivity.categoryList.size()];
        for(int i = 0; i < LoadingActivity.categoryList.size(); i++){
            category_names[i] = LoadingActivity.categoryList.get(i).getData()[0];
            category_values[i] = LoadingActivity.categoryList.get(i).getData()[0];
        }

        category_select.setEntries(category_names);
        category_select.setEntryValues(category_values);
        category_select.setSummary(category_names[0]);

        font_size.setSummary(sharedPreferences.getString("font_size", "24"));
        word_order.setSummary(sharedPreferences.getString("word_order", "알파벳 순서"));
        category_select.setSummary(sharedPreferences.getString("category", "전체"));

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            font_size.setSummary(sharedPreferences.getString("font_size", "24"));
            word_order.setSummary(sharedPreferences.getString("word_order", "알파벳 순서"));
            category_select.setSummary(sharedPreferences.getString("category", "전체"));

            String lock_category_name = sharedPreferences.getString("category", "전체");
            LoadingActivity.vocaDatabase.makeList(LoadingActivity.lockVocaList, lock_category_name);
            LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);

            MainActivity.vocaRecyclerViewAdapter.notifyDataSetChanged();
            MainActivity.vocaRecyclerViewAdapter.notifyItemRangeChanged(0, LoadingActivity.vocaList.size());

            if(s.equals("lock_screen") || s.equals("service")){
                if((s.equals("lock_screen") && lock_screen.isChecked())
                || (s.equals("service") && service.isChecked())){
                    String category_name = sharedPreferences.getString("category", "전체");
                    System.out.println(category_name);
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.lockVocaList, category_name);
                    if(LoadingActivity.lockVocaList.size() <= 0){
                        Toast.makeText(getActivity(), "단어를 먼저 추가해주세요.", Toast.LENGTH_SHORT).show();
                        lock_screen.setChecked(false);
                        service.setChecked(false);
                    }
                }
            }
        }
    };
}
