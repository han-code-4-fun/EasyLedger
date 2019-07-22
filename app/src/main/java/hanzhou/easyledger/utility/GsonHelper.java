package hanzhou.easyledger.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonHelper {

    private static final String TAG =GsonHelper.class.getSimpleName();

    private Context mContext;
    private Gson mGson;
    private SharedPreferences mSharedPreferences;



    public GsonHelper(Context context) {
        mContext = context;
        mGson = new Gson();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public ArrayList<String> getLedgers(String name){

        return getStringArrayFromSharedPreference(name);
    }

    public void saveLedgers(ArrayList<String> inputList, String type){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String json = mGson.toJson(inputList);

        editor.putString(type, json);

        editor.apply();
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

        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        return mGson.fromJson(json,type);
    }


}
