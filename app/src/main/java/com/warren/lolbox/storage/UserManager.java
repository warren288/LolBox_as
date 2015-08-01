package com.warren.lolbox.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.warren.lolbox.AppContext;
import com.warren.lolbox.util.LogTool;

/**
 * 用户信息管理器
 * @author yangsheng
 * @date 2015年5月2日
 */
public class UserManager {

	private static UserManager um;
	private UserDbHelper mUdl;

	private static final String DBPATH = Environment.getExternalStorageDirectory().getPath()
				+ "/LolBox_warren/user.db";
	private static final int VERSION = 1;
	private static final int VERSION_ADDFAVHERO = 2;

	public static UserManager getInstance() {
		if (um == null) {
			synchronized (UserManager.class) {
				if (um == null) {
					um = new UserManager();
				}
			}
		}
		return um;
	}

	private UserManager() {
		File file = new File(DBPATH);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				LogTool.exception(e);
			}
		}
		mUdl = new UserDbHelper(AppContext.getApp(), DBPATH, VERSION_ADDFAVHERO);
	}

	/**
	 * 取查询记录
	 * @return 查询记录，查询结果中每一条格式为： "服务器名,召唤师名"
	 */
	public List<String> getSearchHistory(){
		
		List<String> lstHistory = new ArrayList<String>();
		SQLiteDatabase db = mUdl.getReadableDatabase();
		String[] columns = {UserDbConfig.FLD_SEARCH_HISTORY_SERVER, UserDbConfig.FLD_SEARCH_HISTORY_SUMMONER};
		
		Cursor cursor = db.query(UserDbConfig.TBL_SEARCH_HISTORY, columns, null, null, null, null, UserDbConfig.FLD_SEARCH_HISTORY_TIME);
		
		if (cursor == null || cursor.getCount() <= 0){
			return lstHistory;
		}
		cursor.moveToFirst();
		while(! cursor.isAfterLast()){
			lstHistory.add(cursor.getString(0) + "," + cursor.getString(1));
			cursor.moveToNext();
		}
		db.close();
		return lstHistory;
	}
	
	/**
	 * 添加一条查询记录
	 * @param strServer
	 * @param strSummoner
	 * @return
	 */
	public long addSearchHistory(String strServer, String strSummoner){
		
		SQLiteDatabase db = mUdl.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(UserDbConfig.FLD_SEARCH_HISTORY_SERVER, strServer);
		values.put(UserDbConfig.FLD_SEARCH_HISTORY_SUMMONER, strSummoner);
		long i = db.insert(UserDbConfig.TBL_SEARCH_HISTORY, null, values);
		db.close();
		return i;
	}

	/**
	 * 指定英雄是否收藏英雄
	 * @param strHeroId
	 * @return
	 */
	public boolean isFavorateHero(String strHeroId){
		SQLiteDatabase db = mUdl.getReadableDatabase();
		String strWhere = UserDbConfig.FLD_FAVORATE_HERO_HEROID + " = '" + strHeroId + "'";
		Cursor cursor = db.query(UserDbConfig.TBL_FAVORATE_HERO, null, strWhere, null, null, null, null);
		boolean isFav = false;
		if(cursor.getCount()== 0){
			isFav = false;
		}else{
			isFav = true;
		}
		cursor.close();
		db.close();
		return isFav;
	}

	/**
	 * 添加一个收藏英雄
	 * @param strHeroId
	 * @param strHeroName
	 * @return
	 */
	public long addFavorateHero(String strHeroId, String strHeroName){
		SQLiteDatabase db = mUdl.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(UserDbConfig.FLD_FAVORATE_HERO_HEROID, strHeroId);
		values.put(UserDbConfig.FLD_FAVORATE_HERO_HERONAME, strHeroName);
		long i = db.insert(UserDbConfig.TBL_FAVORATE_HERO, null, values);
		db.close();
		return i;
	}

	/**
	 * 删除一个收藏英雄
	 * @param strHeroId
	 * @return
	 */
	public long delFavorateHero(String strHeroId){
		SQLiteDatabase db = mUdl.getReadableDatabase();
		String strWhere = UserDbConfig.FLD_FAVORATE_HERO_HEROID + " = '" + strHeroId + "'";
		int i = db.delete(UserDbConfig.TBL_FAVORATE_HERO, strWhere, null);
		db.close();
		return i;
	}

	/**
	 * 取收藏英雄列表
	 * @return 返回列表每一项的格式为："英雄ID,英雄名称"
	 */
	public List<String> getFavorateHero(){

		List<String> lstFavHero = new ArrayList<String>();
		SQLiteDatabase db = mUdl.getReadableDatabase();
		String[] columns = {UserDbConfig.FLD_FAVORATE_HERO_HEROID, UserDbConfig.FLD_FAVORATE_HERO_HERONAME};

		Cursor cursor = db.query(UserDbConfig.TBL_FAVORATE_HERO, columns, null, null, null, null, UserDbConfig.FLD_FAVORATE_HERO_TIME);
		if (cursor == null || cursor.getCount() <= 0){
			return lstFavHero;
		}
		cursor.moveToFirst();
		while(! cursor.isAfterLast()){
			lstFavHero.add(cursor.getString(0) + "," + cursor.getString(1));
			cursor.moveToNext();
		}
		return lstFavHero;

	}
	
	
	/**
	 * 用户管理DB的帮助工具
	 * @author yangsheng
	 * @date 2015年5月2日
	 */
	private static class UserDbHelper extends SQLiteOpenHelper {

		public UserDbHelper(Context context, String name, int version) {
			
			super(context, name, null, version);
		}

		public UserDbHelper(Context context, String name, CursorFactory factory, int version) {
			
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
			if (oldVersion == VERSION && newVersion == VERSION_ADDFAVHERO){
				String strCreateFavHero = "create table " + UserDbConfig.TBL_FAVORATE_HERO + "("
						+ UserDbConfig.FLD_FAVORATE_HERO_ID + "INTEGER PRIMARY KEY, "
						+ UserDbConfig.FLD_FAVORATE_HERO_HEROID + " varchar(20), "
						+ UserDbConfig.FLD_FAVORATE_HERO_HERONAME + " varchar(20),"
						+ UserDbConfig.FLD_FAVORATE_HERO_TIME + " varchar(20))";
				db.execSQL(strCreateFavHero);
			}
		}

	}

}
