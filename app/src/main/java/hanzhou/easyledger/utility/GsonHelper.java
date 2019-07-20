package hanzhou.easyledger.utility;

import android.content.Context;
import android.content.SharedPreferences;

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
        mSharedPreferences =mContext.getSharedPreferences(Constant.APP_PREFERENCE,Context.MODE_PRIVATE);

    }

    public ArrayList<String> getLedgers(){

        return getStringArrayFromSharedPreference(Constant.LEDGERS);
    }

    public void saveLedgers(ArrayList<String> inputList){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String json = mGson.toJson(inputList);

        editor.putString(Constant.LEDGERS, json);

        editor.apply();
    }

    public ArrayList<String> getCategories(String categoryType){

        return getStringArrayFromSharedPreference(categoryType);

    }

    public void saveCategories(ArrayList<String> categoriesArray, String categoryType){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String json = mGson.toJson(categoriesArray);

        editor.putString(categoryType, json);

        editor.apply();
    }

    private ArrayList<String> getStringArrayFromSharedPreference(String input){
        String json = mSharedPreferences.getString(input,null);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        return mGson.fromJson(json,type);
    }
}
