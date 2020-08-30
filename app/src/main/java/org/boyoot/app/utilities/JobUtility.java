package org.boyoot.app.utilities;

public class JobUtility {


    public static String getSortedId(String branch,int path,int year,int month,int day){

        return branch+path+year+month+day;
    }

    public static boolean isWorkersDivided(int original,int worker){
        int i = original - worker;
        return i > 1;
    }
}
