package cn.ucai.fulicenter.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    DBOpenHelper mHelper;
    static DBManager dbManager = new DBManager();
    public synchronized void initDB(Context context){
        mHelper = DBOpenHelper.getInstance(context);
    }
    public static DBManager getInstance(){
        return dbManager;
    }
    public boolean saveUserInfo(User user){
        SQLiteDatabase database = mHelper.getWritableDatabase();
        if (database.isOpen()){
            ContentValues values = new ContentValues();
            values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
            values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
            values.put(UserDao.USER_COLUMN_AVATAR,user.getMavatarId());
            values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMavatarLastUpdateTime());
            if (user != null) {
                return database.insert(UserDao.USER_TABLE_NAME, null,values) != -1;
            }
            Log.e(TAG,"insert,user = " + user);
        }
        return false;
    }

    public User getUserInfo(String userName){
        SQLiteDatabase database = mHelper.getReadableDatabase();
        if (database.isOpen()){
            String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                    UserDao.USER_COLUMN_NAME +"= '" + userName +"'";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()){
                User user = new User();
                user.setMuserName(userName);
                user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
                user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR)));
                user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
                user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
                user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
                user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME)));
                return user;
            }
        }
        return null;
    }
}
