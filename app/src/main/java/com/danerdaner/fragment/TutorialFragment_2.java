package com.danerdaner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danerdaner.simple_voca.R;

import androidx.fragment.app.Fragment;

public class TutorialFragment_2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tutorial_fragment_2, container, false);

        return rootView;
    }
}
