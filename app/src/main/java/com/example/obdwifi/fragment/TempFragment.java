package com.example.obdwifi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obdwifi.R;

public class TempFragment extends Fragment {
    //这个fragment可以用来做更多服务，比如违章查询，天气查询
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp, null);
    }
}