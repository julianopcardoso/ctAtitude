package br.com.ctatitude.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.db.DatabaseConnection;
import br.com.ctatitude.model.Exercicio;

public class ExercicioDAO {
    private DatabaseConnection dbConnection;
    private SQLiteDatabase db;

    public ExercicioDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    public boolean inserir(Exercicio exercicio) {
        ContentValues values = new ContentValues();
        values.put("NOME", exercicio.getNome().toString());
        values.put("DESCRICAO", exercicio.getDescricao().toString());
        values.put("PESO", exercicio.getPeso().toString());
        values.put("REPETICAO", exercicio.getRepeticoes().toString());
        values.put("DISTANCIA", exercicio.getDistancia().toString());
        values.put("TEMPO", exercicio.getTempo().toString());

        return db.insert("EXERCICIO", null, values) > 0;
    }

    public boolean alterar(Exercicio exercicio) {
        ContentValues values = new ContentValues();
        values.put("NOME", exercicio.getNome().toString());
        values.put("DESCRICAO", exercicio.getDescricao().toString());
        values.put("PESO", exercicio.getPeso().toString());
        values.put("REPETICAO", exercicio.getRepeticoes().toString());
        values.put("DISTANCIA", exercicio.getDistancia().toString());
        values.put("TEMPO", exercicio.getTempo().toString());

        return db.update("EXERCICIO", values, "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
    }

    public boolean excluir(Exercicio exercicio) {
        return db.delete("EXERCICIO", "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
    }

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
