package org.boyoot.app.utilities;

public class PhoneUtility {

    public static String getValidPhoneNumber(String s){


        if (s.length() == 9){
            return s;
        }else if(s.length() > 9){
            return s.substring(s.length() - 9);
        }else{
            return "invalid";
        }



    }
}
