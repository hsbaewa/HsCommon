package kr.co.hs.common.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kr.co.hs.common.database.HsSqlConstant;

/**
 * Created by Bae on 2016-11-25.
 */
public abstract class HsSQLiteOpenHelper extends SQLiteOpenHelper implements HsSqlConstant {
    public HsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
