package com.example.sensordatadisplay;

import java.util.HashMap;

public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    //Service uid
    public static String SENSONR = "0000FFE1-0000-1000-8000-00805F9B34FB";

    static {
        //Characteristics uid
        attributes.put(SENSONR, "Sensor");
        attributes.put("0000FFE0-0000-1000-8000-00805F9B34FB", "Sense service");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
