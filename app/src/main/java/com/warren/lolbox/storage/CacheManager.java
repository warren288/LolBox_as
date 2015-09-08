package com.warren.lolbox.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author:yangsheng
 * @date:2015/8/31
 */
public class CacheManager {

    private static CacheManager cm;

    public static CacheManager getInstance(){
        if(cm == null){
            synchronized (CacheManager.class){
                if(cm == null){
                    cm = new CacheManager();
                }
            }
        }
        return cm;
    }

    private CacheManager(){

    }


    private static class CacheDbHelper extends SQLiteOpenHelper {

        public CacheDbHelper(Context context, String name, int version) {

            super(context, name, null, version);
        }

        public CacheDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String strCreate = "create table " + UserDbConfig.TBL_SEARCH_HISTORY + "("
                    + UserDbConfig.FLD_SEARCH_HISTORY_ID + "INTEGER PRIMARY KEY, "
                    + UserDbConfig.FLD_SEARCH_HISTORY_SERVER + " varchar(20), "
                    + UserDbConfig.FLD_SEARCH_HISTORY_SUMMONER + " varchar(20),"
                    + UserDbConfig.FLD_SEARCH_HISTORY_TIME + " varchar(20))";
            String strCreateFavHero = "create table " + UserDbConfig.TBL_FAVORATE_HERO + "("
                    + UserDbConfig.FLD_FAVORATE_HERO_ID + "INTEGER PRIMARY KEY, "
                    + UserDbConfig.FLD_FAVORATE_HERO_HEROID + " varchar(20), "
                    + UserDbConfig.FLD_FAVORATE_HERO_HERONAME + " varchar(20),"
                    + UserDbConfig.FLD_FAVORATE_HERO_TIME + " varchar(20))";
            db.execSQL(strCreate);
            db.execSQL(strCreateFavHero);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            if (oldVersion == VERSION && newVersion == VERSION_ADDFAVHERO){
//                String strCreateFavHero = "create table " + UserDbConfig.TBL_FAVORATE_HERO + "("
//                        + UserDbConfig.FLD_FAVORATE_HERO_ID + "INTEGER PRIMARY KEY, "
//                        + UserDbConfig.FLD_FAVORATE_HERO_HEROID + " varchar(20), "
//                        + UserDbConfig.FLD_FAVORATE_HERO_HERONAME + " varchar(20),"
//                        + UserDbConfig.FLD_FAVORATE_HERO_TIME + " varchar(20))";
//                db.execSQL(strCreateFavHero);
//            }
        }

    }

    private static final class CacheDbConfig{
        public static final String TBL_HERODETAIL = "HERODETAIL";
//        public static final String FLD_
    }

}
