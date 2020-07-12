package com.example.sensordatadisplay;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;

public class Read_Write {
    //write data into txt files
    public static void writeData(String data) {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String date = year + "-" + month + "-" + day;
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()  + "/Sensor Data/";
        String fileName = date + ".txt";
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        String time = hour + ":" + minute + ":" + second;
        String strcontent = time + "  " + data;
        writeTxtToFile(strcontent, filePath, fileName);
    }

    //write Strings to txt files
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //create folder first
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // change line
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    //create files
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    //create folder
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
    //get content of file
    public static ArrayList<String> getFileContent(File file) {
        ArrayList dataList = new ArrayList<String>();
        if (!file.isDirectory()) {
            if (file.getName().endsWith("txt")) {//txt file
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //read by line
                        while ((line = buffreader.readLine()) != null) {
                            dataList.add(line);
                        }
                        instream.close();
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return dataList;
    }
    //get name of the file
    public static ArrayList<String> getFileName(){
        ArrayList names = new ArrayList<String>();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()  + "/Sensor Data/";
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            return names;
        }
        for (File name : files) {
            if (name.getName().endsWith(".txt")){
                String filename = name.getName();
                filename = filename.replace(".txt", "");
                names.add(filename);
            }
        }
        return names;
    }
}
