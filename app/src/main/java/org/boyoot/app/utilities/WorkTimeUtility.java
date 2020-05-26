package org.boyoot.app.utilities;

import android.util.Log;

import org.boyoot.app.model.Work;
import org.boyoot.app.model.job.CurrentWork;

public class WorkTimeUtility {


    public static int getRequiredTime(int window,int split,int stand,int cover , int concealed){
        Log.i("testTime",window+" "+split+" "+cover+" "+concealed+"");
        return (window*40)+(stand*40)+(split*30)+(concealed*30)+(cover*30);
    }

    public static String calculateTime(Work work){
        String window = work.getWindow();
        String split = work.getSplit();
        String stand = work.getStand();
        String cover = work.getCover();
        String concealed = work.getConcealed();
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
                Integer.parseInt(split.replace(".","")),
                Integer.parseInt(stand.replace(".","")),
                Integer.parseInt(cover.replace(".","")),
                Integer.parseInt(concealed.replace(".","")));

        int hours = timeInMin / 60;

        int mins = timeInMin - (hours*60);

        return hours+" : "+mins;
    }
    public static CurrentWork parseWork(Work w){

        CurrentWork currentWork = new CurrentWork(w.getInterval(),Integer.parseInt(w.getSplit()),Integer.parseInt(w.getWindow()),Integer.parseInt(w.getCover()),
                Integer.parseInt(w.getStand()),Integer.parseInt(w.getConcealed()),w.isOffer(),0);

        return currentWork;

    }
}
