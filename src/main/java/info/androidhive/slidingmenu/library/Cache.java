package info.androidhive.slidingmenu.library;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rer on 20.05.2015.
 */
public class Cache {

    private static ArrayList<HashMap<String, String>> terminList;
    private static ArrayList<HashMap<String, String>> ListeCat1;
    private static ArrayList<HashMap<String, String>> ListeCat2;
    private static ArrayList<HashMap<String, String>> ListeCat3;
    private static ArrayList<HashMap<String, String>> ListeCat4;


    public static ArrayList<HashMap<String, String>> getTerminList() {
        return terminList;
    }

    public static ArrayList<HashMap<String, String>> getListeCat1() {
        return ListeCat1;
    }


    public static ArrayList<HashMap<String, String>> getListeCat2() {
        return ListeCat2;
    }

    public static ArrayList<HashMap<String, String>> getListeCat3() {
        return ListeCat3;
    }

    public static ArrayList<HashMap<String, String>> getListeCat4() {
        return ListeCat4;
    }



    public static void setTerminList(ArrayList<HashMap<String, String>> terminList) {
        Cache.terminList = terminList;
    }

    public static void setListeCat1(ArrayList<HashMap<String, String>> ListeCat1) {
        Cache.ListeCat1 = ListeCat1;
    }

    public static void setListeCat2(ArrayList<HashMap<String, String>> ListeCat2) {
        Cache.ListeCat2 = ListeCat2;
    }

    public static void setListeCat3(ArrayList<HashMap<String, String>> ListeCat3) {
        Cache.ListeCat3 = ListeCat3;
    }

    public static void setListeCat4(ArrayList<HashMap<String, String>> ListeCat4) {
        Cache.ListeCat4 = ListeCat4;
    }

}
