package com.geora.ui.walkthrough;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geora.R;


/**
 * Created by app-server on 22/3/17.
 */

public class WalkThroughFirstFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkthrough_first, container, false);
        return view;
    }

}
