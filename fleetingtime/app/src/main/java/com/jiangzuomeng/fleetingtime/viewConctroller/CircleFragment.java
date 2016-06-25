package com.jiangzuomeng.fleetingtime.viewConctroller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiangzuomeng.fleetingtime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends Fragment {

    private View view;

    private static final String TAG = "CIRCLE";


    public CircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getResources().getString(R.string.circle));
        System.out.println(getActivity().getTitle());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_circle, container, false);
        return view;
    }



    @Override
    public void onHiddenChanged(boolean hidd) {
        if(!hidd) {
            System.out.println(TAG);
            getActivity().setTitle(getResources().getString(R.string.circle));

        }
    }
}
