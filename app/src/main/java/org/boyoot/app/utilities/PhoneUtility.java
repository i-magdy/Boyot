package org.boyoot.app.utilities;

public class PhoneUtility {

    public static String getValidPhoneNumber(String s){
        s = s.replaceAll(" ","");
        if (s.length() == 9){
            return s;
        }else if(s.length() > 9){
            return s.substring(s.length() - 9);
        }else{
            return "invalid";
        }


    }

    public static String getValidTimeStamp(String timeStamp){

        String s= clear(timeStamp);
        if (timeStamp.contains("20")){
            return s.substring(0,timeStamp.length()-5);

        }

        return s;
    }

    private static String clear(String s){
        return s.replace("T"," ");
    }
}
