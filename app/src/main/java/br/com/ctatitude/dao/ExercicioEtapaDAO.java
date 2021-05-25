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
import br.com.ctatitude.model.Exercicio;
import br.com.ctatitude.model.ExercicioEtapa;

/**
 * Classe ExercicioEtapaDAO
 */
public class ExercicioEtapaDAO {
    private final DatabaseConnection dbConnection;
    private final SQLiteDatabase db;

    /**
     * Construtor
     * @param context
     */
    public ExercicioEtapaDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    /**
     * Inserir
     * @param exercicioEtapa
     * @return boolean
     */
    public boolean inserir(ExercicioEtapa exercicioEtapa) {

        ContentValues values = new ContentValues();
        values.put("ID_EXERCICIO", exercicioEtapa.getIdExercicio());
        values.put("ID_ETAPA", exercicioEtapa.getIdEtapa());
        values.put("ORDEM", exercicioEtapa.getOrdem());
        if(exercicioEtapa.getPesoMasc() != null && exercicioEtapa.getPesoMasc() > 0) {
            values.put("PESO_MASC", exercicioEtapa.getPesoMasc());
        }

        if(exercicioEtapa.getPesoFem() != null && exercicioEtapa.getPesoFem() > 0) {
            values.put("PESO_FEM", exercicioEtapa.getPesoFem());
        }
        if(exercicioEtapa.getRepeticao() != null && exercicioEtapa.getRepeticao() > 0) {
            values.put("REPETICAO", exercicioEtapa.getRepeticao());
        }
        if(exercicioEtapa.getDistancia() != null && exercicioEtapa.getDistancia() > 0) {
            values.put("DISTANCIA", exercicioEtapa.getDistancia());
        }
        values.put("TEMPO", exercicioEtapa.getTempo());

        return db.insert("EXERCICIO_ETAPA", null, values) > 0;
    }

    /**
     * Alterar
     * @param exercicioEtapa
     * @return boolean
     */
    public boolean alterar(ExercicioEtapa exercicioEtapa) {
        return true;
    }

    /**
     * Excluir
     * @param etapa
     * @return boolean
     */
    public boolean excluir(Etapa etapa) {
        return db.delete("EXERCICIO_ETAPA", "ID_ETAPA=?", new String[]{String.valueOf(etapa.getId())}) > 0;
    }

    /**
     * Excluir
     * @param exercicio
     * @return boolean
     */
    public boolean excluir(Exercicio exercicio) {
        return db.delete("EXERCICIO_ETAPA", "ID_ETAPA=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
    }

    /**
     * Contar
     * @param exercicio
     * @return int
     */
    public int contar(Exercicio exercicio){
        int retorno;

        String countQuery = "SELECT COUNT(*) FROM EXERCICIO_ETAPA WHERE ID_EXERCICIO=?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(exercicio.getId())});
        cursor.moveToNext();
        retorno = cursor.getInt(0);

        cursor.close();

        return retorno;
    }

    /**
     * Listar
     * @param etapa
     * @return list
     */
    public List<ExercicioEtapa> listar(Etapa etapa) {
        List<ExercicioEtapa> exerciciosEtapa = new ArrayList<>();

        String selectQuery = "SELECT " +
                "EXT.ID_EXERCICIO, " +
                "EXE.NOME, " +
                "EXE.PESO, " +
                "EXE.REPETICAO, " +
                "EXE.DISTANCIA, " +
                "EXE.TEMPO, " +
                "EXT.ORDEM, " +
                "EXT.PESO_MASC, EXT.PESO_FEM, EXT.REPETICAO, " +
                "EXT.DISTANCIA, EXT.TEMPO " +
                "FROM EXERCICIO_ETAPA EXT " +
                "JOIN ETAPA ETA ON EXT.ID_ETAPA=ETA.ID " +
                "JOIN EXERCICIO EXE ON EXE.ID=EXT.ID_EXERCICIO " +
                "WHERE ETA.ID = ? " +
                "ORDER BY EXT.ORDEM";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(etapa.getId())});
        while (cursor.moveToNext()) {
            ExercicioEtapa exercicioEtapa = new ExercicioEtapa();

            exercicioEtapa.setIdExercicio(cursor.getInt(0));
            exercicioEtapa.setIdEtapa(etapa.getId());

            exercicioEtapa.setExercicio(new Exercicio());
            exercicioEtapa.getExercicio().setId(cursor.getInt(0));
            exercicioEtapa.getExercicio().setNome(cursor.getString(1));

            exercicioEtapa.getExercicio().setPeso(cursor.getString(2));
            exercicioEtapa.getExercicio().setRepeticoes(cursor.getString(3));
            exercicioEtapa.getExercicio().setDistancia(cursor.getString(4));
            exercicioEtapa.getExercicio().setTempo(cursor.getString(5));

            exercicioEtapa.setOrdem(cursor.getInt(6));
            exercicioEtapa.setPesoMasc(cursor.getInt(7));
            exercicioEtapa.setPesoFem(cursor.getInt(8));
            exercicioEtapa.setRepeticao(cursor.getInt(9));
            exercicioEtapa.setDistancia(cursor.getInt(10));
            exercicioEtapa.setTempo(cursor.getInt(11));

            exerciciosEtapa.add(exercicioEtapa);
        }
        cursor.close();
        return exerciciosEtapa;
    }
}
