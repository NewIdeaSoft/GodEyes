package com.nisoft.inspectortools.utils;

/**
 * Created by Administrator on 2017/6/11.
 */

public class CheckUserInfoUtil {
    public static boolean checkPhoneFormat(String phone){
        if(phone.length()!=11){
            return false;
        }
        char phoneChar[] =  phone.toCharArray();
        for (char c:phoneChar){
            if (!Character.isDigit(c)){
                return false;
            }
        }
        if (phone.startsWith("13")||phone.startsWith("18")
                ||phone.startsWith("14")||phone.startsWith("15")||phone.startsWith("17")){
            return true;
        }
        return false;
    }
    public static boolean checkPasswordFormat(String password){
        if (password.length()>=6&&password.length()<=20){
            return true;
        }
        return false;
    }
}
