package br.com.ctatitude.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.ctatitude.db.DatabaseConnection;
import br.com.ctatitude.model.Etapa;
import br.com.ctatitude.model.Treino;

public class TreinoDAO {
    private DatabaseConnection dbConnection;
    private SQLiteDatabase db;

    public TreinoDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    public boolean inserir(Treino treino) {

        /*ContentValues values = new ContentValues();
        values.put("NOME", exercicio.getNome().toString());
        values.put("DESCRICAO", exercicio.getDescricao().toString());
        values.put("PESO", exercicio.getPeso().toString());
        values.put("REPETICAO", exercicio.getRepeticoes().toString());
        values.put("DISTANCIA", exercicio.getDistancia().toString());
        values.put("TEMPO", exercicio.getTempo().toString());

        return db.insert("EXERCICIO", null, values) > 0;*/
        return true;
    }

    public boolean alterar(Treino treino) {

        //return db.update("EXERCICIO", values, "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
        return true;
    }

    public boolean excluir(Treino treino) {
        //return db.delete("EXERCICIO", "ID=?", new String[]{String.valueOf(exercicio.getId())}) > 0;
        return true;
    }

    public List<Treino> listar() {
        /*List<Etapa> etapas = new ArrayList<>();
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
        return exercicios;*/
        return null;
    }
}
