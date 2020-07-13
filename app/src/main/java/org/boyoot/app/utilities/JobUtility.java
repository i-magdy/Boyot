package org.boyoot.app.utilities;

public class JobUtility {


    public static String getSortedId(String branch,int path,int year,int month,int day){

        return branch+path+year+month+day;
    }
}
