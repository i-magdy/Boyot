package org.boyoot.app.utilities;

import android.util.Log;

public class WorkTimeUtility {


    public static int getRequiredTime(int window,int split,int stand,int cover , int concealed){
        Log.i("testTime",window+" "+split+" "+cover+" "+concealed+"");
        return (window*40)+(stand*40)+(split*30)+(concealed*30)+(cover*30);
    }

    public static String calculateTime(String window,String split,String stand,String cover , String concealed){
        if (concealed == null|| concealed.equals("")){
            concealed = "0";
        }
        if (cover == null|| cover.equals("")){
            cover = "0";
        }
        if (stand == null|| stand.equals("")){
            stand = "0";
        }
        if (split == null|| split.equals("")){
            split = "0";
        }
        if (window == null || window.equals("")){
            window = "0";
        }
        int timeInMin = getRequiredTime(Integer.parseInt(window),
                Integer.parseInt(split),
                Integer.parseInt(stand),
                Integer.parseInt(cover),
                Integer.parseInt(concealed));

        int hours = timeInMin / 60;

        int mins = timeInMin - (hours*60);

        return hours+" h - "+mins+" m";
    }
}
