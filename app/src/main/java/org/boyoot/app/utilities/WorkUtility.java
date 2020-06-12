package org.boyoot.app.utilities;

import android.util.Log;

import org.boyoot.app.model.Price;
import org.boyoot.app.model.Work;
import org.boyoot.app.model.job.CurrentWork;

public class WorkUtility {


    public static int getRequiredTime(int window,int split,int stand,int cover , int concealed){
        Log.i("testTime",window+" "+split+" "+cover+" "+concealed+"");
        return (int)((window*80)+(stand*80)+(split*60)+(concealed*60)+(cover*60))/2;
    }

    public static int getDurationOfJob(CurrentWork work,int workers){
        int i = (work.getConcealed()*60)+
                (work.getWindow()*80)+
                (work.getStand()*80)+
                (work.getCover()*60)+
                (work.getSplit()*60);

        return (int) i / workers;
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

       return new CurrentWork(w.getInterval(),Integer.parseInt(w.getSplit()),Integer.parseInt(w.getWindow()),Integer.parseInt(w.getCover()),
                Integer.parseInt(w.getStand()),Integer.parseInt(w.getConcealed()),w.isOffer(),0);

    }

    public static String getStringSplit(String s){
        if (s.isEmpty()) {
            return "0";
        }
        double d = Double.parseDouble(s);
        int i = (int) d;
        return String.valueOf(i);
    }
    public static String getStringWindow(String s){
        if (s.isEmpty()) {
            return "0";
        }
        double d = Double.parseDouble(s);
        int i = (int) d;
        return String.valueOf(i);
    }
    public static String getStringCover(String s){
        if (s.isEmpty()) {
        return "0";
        }
        double d = Double.parseDouble(s);
        int i = (int) d;
        return String.valueOf(i);

    }
    public static String getStringStand(String s){
        if (s.isEmpty()) {
            return "0";
        }
        double d = Double.parseDouble(s);
        int i = (int) d;
        return String.valueOf(i);
    }
    public static String getStringConcealed(String s){
        if (s.isEmpty()) {
            return "0";
        }
        double d = Double.parseDouble(s);
        int i = (int) d;
        return String.valueOf(i);
    }

    public static int getIntSplit(String s){
        if (s.isEmpty()) {
            return 0;
        }
        double d = Double.parseDouble(s);
        return (int) d;

    }
    public static int getIntWindow(String s){
        if (s.isEmpty()) {
            return 0;
        }
        double d = Double.parseDouble(s);
        return (int) d;

    }
    public static int getIntCover(String s){
        if (s.isEmpty()) {
            return 0;
        }
        double d = Double.parseDouble(s);
         return  (int) d;


    }
    public static int getIntStand(String s){
        if (s.isEmpty()) {
            return 0;
        }
        double d = Double.parseDouble(s);
        return  (int) d;

    }
    public static int getIntConcealed(String s){
        if (s.isEmpty()) {
            return 0;

        }
        double d = Double.parseDouble(s);
        return (int) d;

    }

    public static  String totalNumberOfWork(CurrentWork p){
        return String.valueOf(p.getCover()+p.getConcealed()+p.getSplit()+p.getWindow()+p.getStand());
    }

    public static String getDurationText(CurrentWork p){
        return "NULL";
    }
    public static  int getTotalPrice(Price p,int discount){
        return p.getCover()+p.getConcealed()+p.getSplit()+p.getWindow()+p.getStand()-discount;
    }

    public static  String getTotalPriceText(Price p,int discount){
        return String.valueOf(p.getCover()+p.getConcealed()+p.getSplit()+p.getWindow()+p.getStand()-discount);
    }


}
