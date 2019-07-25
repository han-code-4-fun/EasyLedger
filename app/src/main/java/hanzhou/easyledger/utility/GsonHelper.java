package hanzhou.easyledger.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class GsonHelper {

    private static final String TAG =GsonHelper.class.getSimpleName();

    private Gson mGson;
    private SharedPreferences mSharedPreferences;



    public GsonHelper(Context context) {
        mGson = new Gson();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public ArrayList<String> getLedgers(String name){

        return getStringArrayFromSharedPreference(name);
    }


    public ArrayList<String> getDataFromSharedPreference(String categoryType){

        return getStringArrayFromSharedPreference(categoryType);

    }

    public void saveDataToSharedPreference(ArrayList<String> categoriesArray, String categoryType){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String json = mGson.toJson(categoriesArray);

        editor.putString(categoryType, json);

        editor.apply();
    }

    public ArrayList<String> convertJsonToArrayListString(String json){
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return mGson.fromJson(json,type);
    }

    private ArrayList<String> getStringArrayFromSharedPreference(String input){

        String json = mSharedPreferences.getString(input,null);

        return convertJsonToArrayListString(json);
    }

    public void saveHashMapToSharedPreference(HashMap<String, String> hashMap, String nameInSharedPref){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String json = mGson.toJson(hashMap);

        editor.putString(nameInSharedPref, json);

        editor.apply();
    }


    public HashMap<String, String> getHashMapFromSharedPreference(String preferenceKeyRemark) {

        String json = mSharedPreferences.getString(preferenceKeyRemark,null);

        return convertJsonToHashMapStringString(json);
    }

    private HashMap<String, String> convertJsonToHashMapStringString(String json){
        if(json==null){
            return Constant.getDefaultRemarks();
        }
        Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        return mGson.fromJson(json,type);
    }
}
