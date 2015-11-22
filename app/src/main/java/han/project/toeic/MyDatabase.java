package han.project.toeic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Han on 22/11/2015.
 */
public class MyDatabase extends SQLiteOpenHelper{
    public static final String DB_NAME = "StudentManager";
    public static final int DB_VERSION = 1;
    public static final String ID = "_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TABLE = "login";
    SQLiteDatabase db = this.getWritableDatabase();
    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+TABLE+"(" +
                ID + " integer primary key autoincrement, "+
                USERNAME+" text not null, " +
                PASSWORD+" text not null " +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE);
        onCreate(db);
    }
    public long insert(register re) {
        ContentValues values = new ContentValues();
        values.put("username", re.username);
        values.put("password", re.password);
        return db.insert("login", null, values);
    }

    public int update(register re) {
        ContentValues values = new ContentValues();
        values.put("username", re.username);
        values.put("password", re.password);
        return db.update("login", values, " _id = ? ", new String[] { re.id + "" });
    }

    public int delete(register re) {
        return db.delete("login", " _id = ? ", new String[] { re.id + "" });
    }

    public ArrayList<register> select() {
        ArrayList<register> list = new ArrayList<register>();
        Cursor c = db.rawQuery("select * from login ", null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String username = c.getString(1);
                String password = c.getString(2);
                register re = new register(id, username, password);
                list.add(re);
            } while (c.moveToNext());
        }
        return list;
    }

    public String selectusername(String userName ) {
        Cursor cursor = db.query("login", null, " username=?", new String[] { userName }, null, null, null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

}
