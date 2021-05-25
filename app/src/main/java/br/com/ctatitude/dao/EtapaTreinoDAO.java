package br.com.ctatitude.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.db.DatabaseConnection;
import br.com.ctatitude.model.Etapa;
import br.com.ctatitude.model.EtapaTreino;
import br.com.ctatitude.model.Treino;

/**
 * Classe EtapaTreinoDAO
 */
public class EtapaTreinoDAO {
    private final DatabaseConnection dbConnection;
    private final SQLiteDatabase db;

    /**
     * Construtor
     * @param context
     */
    public EtapaTreinoDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    /**
     * Inserir
     * @param etapaTreino
     * @return boolean
     */
    public boolean inserir(EtapaTreino etapaTreino) {

        ContentValues values = new ContentValues();
        values.put("ID_TREINO", etapaTreino.getIdTreino());
        values.put("ID_ETAPA", etapaTreino.getIdEtapa());
        values.put("ORDEM", etapaTreino.getOrdem());

        return db.insert("ETAPA_TREINO", null, values) > 0;
    }

    /**
     * Excluir
     * @param etapa
     * @return boolean
     */
    public boolean excluir(Etapa etapa) {
        return db.delete("ETAPA_TREINO", "ID_ETAPA=?", new String[]{String.valueOf(etapa.getId())}) > 0;
    }

    /**
     * Excluir
     * @param treino
     * @return boolean
     */
    public boolean excluir(Treino treino) {
        return db.delete("ETAPA_TREINO", "ID_TREINO=?", new String[]{String.valueOf(treino.getId())}) > 0;
    }

    /**
     * Listar
     * @param treino
     * @return list
     */
    public List<EtapaTreino> listar(Treino treino) {
        List<EtapaTreino> etapasTreino = new ArrayList<>();

        String selectQuery = "SELECT " +
                "ETT.ORDEM, ETA.ID, ETA.DURACAO, ETA.DESCANSO, " +
                "ETA.ROUND FROM ETAPA_TREINO ETT " +
                "JOIN ETAPA ETA ON ETT.ID_ETAPA=ETA.ID " +
                "WHERE ETT.ID_TREINO = ? " +
                "ORDER BY ETT.ORDEM";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(treino.getId())});
        while (cursor.moveToNext()) {
            EtapaTreino etapaTreino = new EtapaTreino();

            etapaTreino.setIdTreino(treino.getId());
            etapaTreino.setIdEtapa(cursor.getInt(1));
            etapaTreino.setOrdem(cursor.getInt(0));

            etapaTreino.setEtapa(new Etapa());
            etapaTreino.getEtapa().setId(cursor.getInt(1));
            etapaTreino.getEtapa().setDuracao(cursor.getInt(2));
            etapaTreino.getEtapa().setDescanso(cursor.getInt(3));
            etapaTreino.getEtapa().setRound(cursor.getInt(4));

            etapasTreino.add(etapaTreino);
        }
        return etapasTreino;
    }
}
