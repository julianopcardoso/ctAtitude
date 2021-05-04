package br.com.ctatitude.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseConnection extends SQLiteOpenHelper {

    private static final String name = "ctatitude.db";
    private static final int version = 1;

    public DatabaseConnection(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EXERCICIO ( " +
                "ID integer NOT NULL CONSTRAINT EXERCICIO_PK PRIMARY KEY AUTOINCREMENT, " +
                "NOME varchar(100) NOT NULL, " +
                "DESCRICAO varchar(500) NOT NULL, " +
                "PESO varchar(1) NOT NULL, " +
                "REPETICAO varchar(1) NOT NULL, " +
                "DISTANCIA varchar(1) NOT NULL, " +
                "TEMPO varchar(1) NOT NULL)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
