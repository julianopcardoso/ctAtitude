package br.com.ctatitude.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Classe DatabaseConnection
 */
public class DatabaseConnection extends SQLiteOpenHelper {

    private static final String name = "ctatitude.db";
    private static final int version = 1;

    /**
     * Construtor
     * @param context
     */
    public DatabaseConnection(Context context) {
        super(context, name, null, version);
    }

    /**
     * onCreate
     * Cria tabelas do banco de dados
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableEXERCICIO(db);
        createTableETAPA(db);
        createTableEXERCICIO_ETAPA(db);
        createTableTREINO(db);
        createTableETAPA_TREINO(db);
    }

    /**
     * createTableETAPA_TREINO
     * Cria tabela ETAPA_TREINO
     * @param db
     */
    private void createTableETAPA_TREINO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ETAPA_TREINO (" +
                "ID_TREINO integer NOT NULL, " +
                "ID_ETAPA integer NOT NULL, " +
                "ORDEM integer NOT NULL, " +
                "CONSTRAINT ETAPA_TREINO_PK PRIMARY KEY (ID_TREINO,ID_ETAPA, ORDEM), "+
                "FOREIGN KEY(ID_TREINO) REFERENCES TREINO(ID), " +
                "FOREIGN KEY(ID_ETAPA) REFERENCES ETAPA(ID))" );
    }

    /**
     * createTableTREINO
     * Cria tabela TREINO
     * @param db
     */
    private void createTableTREINO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TREINO (" +
                "ID integer NOT NULL CONSTRAINT TREINO_PK PRIMARY KEY AUTOINCREMENT, " +
                "NOME varchar(100) NOT NULL, " +
                "ROUND integer NOT NULL, " +
                "DESCANSO integer NOT NULL," +
                "DURACAO integer NOT NULL )" );
    }

    /**
     * createTableEXERCICIO_ETAPA
     * Cria tabela EXERCICIO_ETAPA
     * @param db
     */
    private void createTableEXERCICIO_ETAPA(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EXERCICIO_ETAPA (" +
                "ID_EXERCICIO integer NOT NULL, " +
                "ID_ETAPA integer NOT NULL, " +
                "ORDEM integer NOT NULL, " +
                "PESO_MASC integer, " +
                "PESO_FEM integer, " +
                "REPETICAO integer, " +
                "DISTANCIA integer, " +
                "TEMPO integer, " +
                "CONSTRAINT EXERCICIO_ETAPA_PK PRIMARY KEY (ID_EXERCICIO,ID_ETAPA, ORDEM), "+
                "FOREIGN KEY(ID_EXERCICIO) REFERENCES EXERCICIO(ID), " +
                "FOREIGN KEY(ID_ETAPA) REFERENCES ETAPA(ID))" );
    }

    /**
     * createTableETAPA
     * Cria tabela ETAPA
     * @param db
     */
    private void createTableETAPA(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ETAPA (" +
                "ID integer NOT NULL CONSTRAINT ETAPA_PK PRIMARY KEY AUTOINCREMENT, " +
                "DURACAO integer NOT NULL, " +
                "DESCANSO integer NOT NULL, " +
                "ROUND integer NOT NULL)");
    }

    /**
     * createTableEXERCICIO
     * Cria tabela EXERCICIO
     * @param db
     */
    private void createTableEXERCICIO(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EXERCICIO (" +
                "ID integer NOT NULL CONSTRAINT EXERCICIO_PK PRIMARY KEY AUTOINCREMENT, " +
                "NOME varchar(100) NOT NULL UNIQUE, " +
                "DESCRICAO varchar(500) NOT NULL, " +
                "PESO varchar(1) NOT NULL, " +
                "REPETICAO varchar(1) NOT NULL, " +
                "DISTANCIA varchar(1) NOT NULL, " +
                "TEMPO varchar(1) NOT NULL)" );
    }

    /**
     * onUpgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
