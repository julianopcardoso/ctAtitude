package br.com.ctatitude.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.db.DatabaseConnection;
import br.com.ctatitude.model.Exercicio;

/**
 * Classe ExercicioDAO
 */
public class ExercicioDAO {
    private final DatabaseConnection dbConnection;
    private final SQLiteDatabase db;

    /**
     * Construtor
     * @param context
     */
    public ExercicioDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    /**
     * Inserir
     * @param exercicio
     * @return boolean
     */
    public boolean inserir(Exercicio exercicio) {

        ContentValues values = new ContentValues();
        values.put("NOME", exercicio.getNome());
        values.put("DESCRICAO", exercicio.getDescricao());
        values.put("PESO", exercicio.getPeso());
        values.put("REPETICAO", exercicio.getRepeticoes());
        values.put("DISTANCIA", exercicio.getDistancia());
        values.put("TEMPO", exercicio.getTempo());

        return db.insert("EXERCICIO", null, values) > 0;
    }

    /**
     * Alterar
     * @param exercicio
     * @return boolean
     */
    public boolean alterar(Exercicio exercicio) {
        ContentValues values = new ContentValues();
        values.put("NOME", exercicio.getNome());
        values.put("DESCRICAO", exercicio.getDescricao());
        values.put("PESO", exercicio.getPeso());
        values.put("REPETICAO", exercicio.getRepeticoes());
        values.put("DISTANCIA", exercicio.getDistancia());
        values.put("TEMPO", exercicio.getTempo());

        return db.update("EXERCICIO", values, "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
    }

    /**
     * Excluir
     * @param exercicio
     * @return boolean
     */
    public boolean excluir(Exercicio exercicio) {
        return db.delete("EXERCICIO", "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
    }

    /**
     * Contar
     * @param exercicio
     * @return int
     */
    public int contarNome(Exercicio exercicio){
        int retorno;

        String countQuery = "SELECT COUNT(*) FROM EXERCICIO WHERE  LOWER(NOME)=?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{exercicio.getNome().toLowerCase()});
        cursor.moveToNext();
        retorno = cursor.getInt(0);

        cursor.close();

        return retorno;
    }

    /**
     * Listar
     * @return list
     */
    public List<Exercicio> listar() {
        List<Exercicio> exercicios = new ArrayList<>();
        Cursor cursor = db.query("EXERCICIO", new String[]{"ID", "NOME", "DESCRICAO", "PESO", "REPETICAO", "DISTANCIA", "TEMPO"},
                null, null, null, null, "NOME");
        while (cursor.moveToNext()) {
            Exercicio exercicio = new Exercicio();
            exercicio.setId(cursor.getInt(0));
            exercicio.setNome(cursor.getString(1));
            exercicio.setDescricao(cursor.getString(2));
            exercicio.setPeso(cursor.getString(3));
            exercicio.setRepeticoes(cursor.getString(4));
            exercicio.setDistancia(cursor.getString(5));
            exercicio.setTempo(cursor.getString(6));
            exercicios.add(exercicio);
        }
        return exercicios;
    }
}
