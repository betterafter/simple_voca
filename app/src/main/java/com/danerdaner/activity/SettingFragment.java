package com.danerdaner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.VocaForegroundService;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SettingFragment extends PreferenceFragment {

    SharedPreferences sharedPreferences;

    ListPreference font_size;
    ListPreference word_order;
    ListPreference category_select;

    CheckBoxPreference lock_screen;
    CheckBoxPreference service;

    Preference developer_info;
    Preference question;

    private static final int PICKFILE_REQUEST_CODE = 0;




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
        @RequiresApi(api = Build.VERSION_CODES.O)
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

            if(s.equals("service")){
                Intent intent = new Intent(getActivity(), VocaForegroundService.class);
                if(service.isChecked()) getActivity().startForegroundService(intent);
                else getActivity().stopService(intent);
            }
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();
        if(key.equals("get_wordfile")){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        }

        if(key.equals("developer_info")){
            Intent intent = new Intent(getActivity(), Setting_Developer_Info_Activity.class);
            startActivity(intent);
        }

        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
