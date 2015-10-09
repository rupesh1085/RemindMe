package com.bluepi.remindme.Utils;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rupesh on 21/9/15.
 */
public class ApplicationUtil {

    private   Matcher m ;


    public String parseIntsAndFloats(String raw) {

        String listBuffer = null;

        String[] arr = raw.split(" ");

        Pattern p = Pattern.compile("[0-9]*\\.?[0-9]+");
       // Pattern p = Pattern.compile("^([+-]?\\d*\\.?\\d*)$");


        /*for (int i = 0; i < arr.length; i++) {
            if(arr[i].contains(",")){
                arr[i].replace(",", "");
                m = p.matcher(arr[i]);
            }else{*/

                m = p.matcher(raw);
           // }

            while (m.find()) {

                Log.e("=====price" , m.group());
                if(isDouble(m.group())){

                    double a = Double.parseDouble(m.group());
                    if(a == Math.round(a)){

                    }else{

                        listBuffer = m.group();
                    }
                }else{

                    Log.e("=============Integer", m.group());
                }
            }
      //  }

        return listBuffer;
    }

    public boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
