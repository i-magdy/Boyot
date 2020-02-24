package org.boyoot.app.utilities;

import android.text.TextUtils;

public class CityUtility {

    static String areas[][] ={
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
            {"H","مكة المكرمة"}
    };


    public static String  getCityCode(String city){

        for (int i=0;i<93;++i){
            String s = areas[i][1];
            if (TextUtils.equals(city,s)){
                return areas[i][0];
            }

        }

        return "N";
    }


    public static String getInterval(String s){
        if (TextUtils.equals(s,"الفترة الأولى من :10 صباحاً إلى: 2 ظهراً")){
            return "Morning";
        }else{
            return "Evening";
        }
    }


}
