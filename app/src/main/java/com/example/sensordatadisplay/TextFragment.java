package com.example.sensordatadisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TextFragment extends Fragment {
    private View view;
    public static TextView mConnectionState;
    public static TextView mDataField;
    public static String mDeviceAddress;
    public static String Connection;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.text_layout, container, false);
        //display data in numbers
        mConnectionState = (TextView) view.findViewById(R.id.connection_state);
        mDataField = (TextView) view.findViewById(R.id.data_value);
        ((TextView) view.findViewById(R.id.device_address)).setText(mDeviceAddress);
        ((TextView) view.findViewById(R.id.connection_state)).setText(Connection);
        return view;
    }

}
