package org.boyoot.app.utilities;

import android.text.TextUtils;

public class CityUtility {

    private static String[][] areas ={
            {"R","رياض"},
            {"R","الرياض"},
            {"K","الخرج"},
            {"K","خرج"},
            {"K","الدلم"},
            {"M","شقراء"},
            {"M","أشقير"},
            {"M","اشقير"},
            {"M","شقراء"},
            {"M","القصب"},
            {"M","قصب"},
            {"M","ساجر"},
            {"M","الدوامي"},
            {"M","الدوامى"},
            {"M","المجمعه"},
            {"M","المجمعة"},
            {"M","الزلفى"},
            {"M","الزلفي"},
            {"M","سدير"},
            {"M","حوطة سدير"},
            {"M","حوطه سدير"},
            {"M","حوطة تميم"},
            {"M","حوطه تميم"},
            {"M","حوطةسدير"},
            {"M","حوطةتميم"},
            {"M","تمير"},
            {"M","ثرمداء"},
            {"Q","القصيم"},
            {"Qَ","بدائع"},
            {"Q","البدائع"},
            {"Q","عنيزه"},
            {"Q","عنيزة"},
            {"Q","بريده"},
            {"Q","بريدة"},
            {"Q","خبراء"},
            {"Q","الخبراء"},
            {"Q","الرس"},
            {"Q","المذنب"},
            {"Q","رياض الخبراء"},
            {"Q","البكيريه"},
            {"Q","بكيريه"},
            {"Q","البكيرية"},
            {"Q","بكيرية"},
            {"Q","الشحية"},
            {"Q","الشحيه"},
            {"Q","شحيه"},
            {"Q","شحية"},
            {"D","الدمام"},
            {"D","خبر"},
            {"D","الخبر"},
            {"D","سيهات"},
            {"D","جبيل"},
            {"D","الجبيل"},
            {"D","بقيق"},
            {"D","البقيق"},
            {"D","قطيف"},
            {"D","القطيف"},
            {"D","عنك"},
            {"D","ام الساهك"},
            {"D","أم الساهك"},
            {"D","ام ساهك"},
            {"D","أم ساهك"},
            {"W","إحساء"},
            {"W","الإحساء"},
            {"W","أحساء"},
            {"W","الاحساء"},
            {"W","الأحساء"},
            {"W","هفوف"},
            {"W","الهفوف"},
            {"W","المبرز"},
            {"W","مبرز"},
            {"H","مكه المكرمه"},
            {"H","مكةالمكرمة"},
            {"H","مكه المكرمة"},
            {"H","مكةالمكرمه"},
            {"H","طائف"},
            {"H","الطائف"},
            {"J","جده"},
            {"J","جدة"},
            {"J","عسفان"},
            {"J","ابحر"},
            {"J","أبحر"},
            {"J","بحره"},
            {"J","بحرة"},
            {"D","أجيال"},
            {"D","اجيال"},
            {"D","ظهران"},
            {"D","الظهران"},
            {"D","الاوجام"},
            {"D","الأوجام"},
            {"M","الدوادمي"},
            {"Z","جازان"},
            {"L","المدينة المنورة"},
            {"H","مكة المكرمة"},
            {"D","راس تنوره"},
            {"D","رأس تنوره"},
            {"D","راس تنورة"}
    };


    private static String[][] branchesCode = {
            {"D", "الدمام"},
            {"H", "مكه"},
            {"J", "جده"},
            {"K", "الخرج"},
            {"L", "المدينه"},
            {"M", "المجمعه"},
            {"N", "منطقه 1"},
            {"Q", "القصيم"},
            {"R", "الرياض"},
            {"W", "الإحساء"},
            {"Y", "منطقه 2"},
            {"Z", "جيزان"},
            {"D", "Dammam"},
            {"H", "Mecca"},
            {"J", "Jeddah"},
            {"K", "Al-Kharj"},
            {"L", "Medina"},
            {"M", "Al Majma\\'ah"},
            {"N", "Area 1"},
            {"Q", "Al Qassim"},
            {"R", "Riyadh"},
            {"W", "Alahsaa"},
            {"Y", "Area 2"},
            {"Z", "Jazan"}
    };


    public static String  getCityCode(String city){

        for (int i=0;i<97;++i){
            String s = areas[i][1];
            if (TextUtils.equals(city,s)){
                return areas[i][0];
            }
        }

        return "N";
    }


    public static String getInterval(String s){
        if (TextUtils.equals(s, "الفترة الأولى")){
            return "Morning";
        }else{
            return "Evening";
        }
    }

    public static String getBranchCode(String city){
        String s= null;
        for (int i=0;i<24;++i){
           s = branchesCode[i][1];
            if (TextUtils.equals(city,s)){
                s= branchesCode[i][0];
                break;
            }
        }
        return s;
    }


}
