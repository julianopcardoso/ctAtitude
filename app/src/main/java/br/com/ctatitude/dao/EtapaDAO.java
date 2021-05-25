package br.com.ctatitude.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.db.DatabaseConnection;
import br.com.ctatitude.model.Etapa;
import br.com.ctatitude.model.Exercicio;

/**
 * Classe {@link EtapaDAO}
 */
public class EtapaDAO {
    private final DatabaseConnection dbConnection;
    private final SQLiteDatabase db;

    /**
     * Construtor
     * @param context
     */
    public EtapaDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    /**
     * Inserir
     * @param etapa
     * @return long
     */
    public long inserir(Etapa etapa) {

        ContentValues values = new ContentValues();
        values.put("DURACAO", etapa.getDuracao());
        values.put("DESCANSO", etapa.getDescanso());
        values.put("ROUND", etapa.getRound());

        return db.insert("ETAPA", null, values);
    }

    /**
     * Alterar
     * @param etapa
     * @return boolean
     */
    public boolean alterar(Etapa etapa) {
        ContentValues values = new ContentValues();
        values.put("DURACAO", etapa.getDuracao());
        values.put("DESCANSO", etapa.getDescanso());
        values.put("ROUND", etapa.getRound());

        return db.update("ETAPA", values, "ID=?", new String[]{String.valueOf(etapa.getId())}) > 0;
    }

    /**
     * Excluir
     * @param etapa
     * @return boolean
     */
    public boolean excluir(Etapa etapa) {
        return db.delete("ETAPA", "ID=?", new String[]{String.valueOf(etapa.getId())}) > 0;
    }
}
