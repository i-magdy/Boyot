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

    public static int getDurationValueOfJob(CurrentWork work,int workers){

        int i=0;
        i = i + (work.getConcealed()*60);
        i = i + (work.getWindow()*80);
        i = i + (work.getStand()*80);
        i = i + (work.getCover()*60);
        i = i + (work.getSplit()*60);

        return (int) i / workers;
    }

    public static String getDurationTextOfJob(CurrentWork work,int workers){

        int i=0;
        i = i + (work.getConcealed()*60);
        i = i + (work.getWindow()*80);
        i = i + (work.getStand()*80);
        i = i + (work.getCover()*60);
        i = i + (work.getSplit()*60);

        int timeInMin = i / workers;

        int hours = timeInMin / 60;

        int mins = timeInMin - (hours*60);

        return hours+" : "+mins;
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

    public static int getDiscount(String s){
        if (s.isEmpty()) {
            return 0;
        }
        double d = Double.parseDouble(s);
        return  (int) d;

    }
    public static  String getTextTotalNumberOfWork(CurrentWork p){
        return String.valueOf(p.getCover()+p.getConcealed()+p.getSplit()+p.getWindow()+p.getStand());
    }

    public static  int getIntTotalNumberOfWork(CurrentWork p){
        return (p.getCover()+p.getConcealed()+p.getSplit()+p.getWindow()+p.getStand());
    }

    public static String getDurationText(CurrentWork p){
        return "NULL";
    }

    public static  int getTotalPrice(CurrentWork w,Price p){
        int concealed = w.getConcealed() *p.getConcealed();
        int cover = w.getCover() * p.getCover();
        int split = w.getSplit() * p.getSplit();
        int window =w.getWindow() * p.getWindow();
        int stand = w.getStand() * p.getStand();
        int discount = w.getDiscount();
        return  concealed+cover+split+stand+window-discount;
    }

    public static  String getTotalPriceText(CurrentWork w ,Price p){
        int concealed = w.getConcealed() *p.getConcealed();
        int cover = w.getCover() * p.getCover();
        int split = w.getSplit() * p.getSplit();
        int window =w.getWindow() * p.getWindow();
        int stand = w.getStand() * p.getStand();
        int discount = w.getDiscount();
        int cost = concealed+cover+split+stand+window-discount;
        return String.valueOf(cost);
    }


}
