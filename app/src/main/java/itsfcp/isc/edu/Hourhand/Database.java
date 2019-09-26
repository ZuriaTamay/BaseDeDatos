package itsfcp.isc.edu.Hourhand;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import itsfcp.isc.edu.Hourhand.adapters.AgendaAdapter;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static final String DATABASE_NAMES = "BDHorario";

        private static final int DATABASE_VERSION = 1;

        public Database(Context context) {
            super(context, DATABASE_NAMES, null, DATABASE_VERSION);
        }


    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS uni_academica ");
        db.execSQL("DROP TABLE IF EXISTS perfil ");
        db.execSQL("DROP TABLE IF EXISTS carreras ");
        db.execSQL("DROP TABLE IF EXISTS semestre ");
        db.execSQL("DROP TABLE IF EXISTS grupo ");
        db.execSQL("DROP TABLE IF EXISTS agenda ");
        onCreate(db);
    }
}
