package itsfcp.isc.edu.Hourhand;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbObject {

 private static Database dbHelper;
    private SQLiteDatabase db;

    public DbObject(Context context) {
        dbHelper = new Database(context);
        this.db = dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getDbConnection(){
        return this.db;
    }

    public void closeDbConnection() {
        if (this.db != null) {
            this.db.close();
        }
    }
}
