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
        createTableEXERCICIO(db);
        createTableETAPA(db);
        createTableEXERCICIO_ETAPA(db);
        createTableTREINO(db);
        createTableETAPA_TREINO(db);
    }

    private void createTableETAPA_TREINO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ETAPA_TREINO (" +
                "ID_TREINO integer NOT NULL, " +
                "ID_ETAPA integer NOT NULL, " +
                "ORDEM integer NOT NULL, " +
                "CONSTRAINT ETAPA_TREINO_PK PRIMARY KEY (ID_TREINO,ID_ETAPA, ORDEM))" );
    }

    private void createTableTREINO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TREINO (" +
                "ID integer NOT NULL CONSTRAINT TREINO_PK PRIMARY KEY AUTOINCREMENT, " +
                "NOME varchar(100) NOT NULL, " +
                "ROUND integer NOT NULL, " +
                "DESCANSO datetime NOT NULL)" );
    }

    private void createTableEXERCICIO_ETAPA(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EXERCICIO_ETAPA (" +
                "ID_TREINO integer NOT NULL, " +
                "ID_ETAPA integer NOT NULL, " +
                "ORDEM integer NOT NULL CONSTRAINT EXERCICIO_ETAPA PRIMARY KEY, " +
                "PESO varchar(10), " +
                "REPETICAO integer, " +
                "DISTANCIA integer, " +
                "TEMPO datetime)" );
    }

    private void createTableETAPA(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ETAPA (" +
                "ID integer NOT NULL CONSTRAINT ETAPA_PK PRIMARY KEY AUTOINCREMENT, " +
                "DURACAO datetime NOT NULL, " +
                "DESCANSO datetime NOT NULL, " +
                "ROUND integer NOT NULL)");
    }

    private void createTableEXERCICIO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EXERCICIO (" +
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
