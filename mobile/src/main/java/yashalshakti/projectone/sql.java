package yashalshakti.projectone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Yashal on 1/6/2015.
 */
public class sql {

    public static final String KEY_INDEX = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_LOC = "location";
    public static final String KEY_COM = "comment";

    private static final String DATABASE_NAME = "Testdb";
    private static final String DATABASE_Table = "dataTable";
    private static final int DATABASE_Ver = 1;

    private Db helper;
    private final Context ourContext;
    private SQLiteDatabase database;



    private static class Db extends SQLiteOpenHelper{

        public Db(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Ver);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL( "CREATE TABLE "+DATABASE_Table + " (" +
                    KEY_INDEX+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            KEY_NAME+ " TEXT NOT NULL, " +
                            KEY_TYPE+ " TEXT NOT NULL, " +
                            KEY_LOC+ " TEXT NOT NULL, " +
                            KEY_COM+ " TEXT NOT NULL);"
    );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_Table);
            onCreate(db);
        }
    }




    public sql(Context c)
    {
        ourContext=c;
    }

    public sql open()throws SQLException
    {
        helper = new Db(ourContext);
        database = helper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        helper.close();
    }

    public long createEntry(String[] data) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,data[0]);
        cv.put(KEY_TYPE,data[1]);
        cv.put(KEY_LOC,data[2]);
        cv.put(KEY_COM,data[3]);
       return database.insert(DATABASE_Table,null,cv);

    }

    public String[] getData(int i)throws Exception{
        String[] colums = new String[]{KEY_INDEX,KEY_NAME,KEY_TYPE,KEY_LOC,KEY_COM};
        Cursor c = database.query(DATABASE_Table,colums,null,null,null,null,null);

        int index=c.getColumnIndex(KEY_INDEX);
        int iName=c.getColumnIndex(KEY_NAME);
        int iType=c.getColumnIndex(KEY_TYPE);
        int iLoc=c.getColumnIndex(KEY_LOC);
        int iCom=c.getColumnIndex(KEY_COM);
        c.moveToPosition(i);
       String result[] = {c.getString(iName),c.getString(iType),c.getString(iLoc),c.getString(iCom)};
        return result;



    }

}
