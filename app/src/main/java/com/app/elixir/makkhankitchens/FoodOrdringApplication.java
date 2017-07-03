package com.app.elixir.makkhankitchens;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.app.elixir.makkhankitchens.database.DbHelper;
import com.app.elixir.makkhankitchens.utils.CM;
import com.app.elixir.makkhankitchens.utils.CV;
import com.app.elixir.makkhankitchens.volly.VolleySingleton;

import java.io.IOException;


public class FoodOrdringApplication extends Application {

    //initialize font typeface
    public Typeface mTypeFaceRobotoBlack;
    public Typeface mTypeFaceRobotoLight;

    public Context mContext;
    public VolleySingleton volley;

    public static SQLiteDatabase sqLiteDatabase;

    private RequestQueue mRequestQueue;
    private static FoodOrdringApplication mInstance;

    public static synchronized FoodOrdringApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        // MultiDex.install(this);
        //Initialize font typeface and accessing instance font wise
        initializeTypeFace();

        //initialize Database
        mContext = getApplicationContext();
        volley = new VolleySingleton(mContext);
        CV.UDID = CM.getUDID(mContext);

        CV.ACCESS_TOKEN = (String) CM.getSp(mContext, CV.USER_ACCESS_TOKEN, "");

        //Log.e("FxNewsStand app", "isDayMode: " + (Boolean) CM.getSp(mContext, CV.IS_DAY_MODE, true));
//
        DbHelper dbHelper = new DbHelper(mContext);
        try {
            dbHelper.createDataBase();
            sqLiteDatabase = dbHelper.openDataBase();
            CM.copyDataBase(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Access direct font-face instance  in settypeface method
     */
    private void initializeTypeFace() {


        mTypeFaceRobotoBlack = Typeface.createFromAsset(getAssets(), getResources().getString(
                R.string.fontface_robotoblack));
        mTypeFaceRobotoLight = Typeface.createFromAsset(getAssets(), getResources().getString(
                R.string.fontface_robotolight_0));


    }


}
