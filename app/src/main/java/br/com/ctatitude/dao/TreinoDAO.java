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
import br.com.ctatitude.model.Treino;

/**
 * Classe TreinoDAO
 */
public class TreinoDAO {
    private final DatabaseConnection dbConnection;
    private final SQLiteDatabase db;

    /**
     * Contrutor
     * @param context
     */
    public TreinoDAO(Context context) {
        dbConnection = new DatabaseConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    /**
     * Inserir
     * @param treino
     * @return long
     */
    public long inserir(Treino treino) {

        ContentValues values = new ContentValues();
        values.put("NOME", treino.getNome());
        values.put("ROUND", treino.getRound());
        values.put("DESCANSO", treino.getDescanso());
        values.put("DURACAO", treino.getDuracao());

        return db.insert("TREINO", null, values);
    }

    /**
     * Alterar
     * @param treino
     * @return boolean
     */
    public boolean alterar(Treino treino) {

        ContentValues values = new ContentValues();
        values.put("NOME", treino.getNome());
        values.put("ROUND", treino.getRound());
        values.put("DESCANSO", treino.getDescanso());
        values.put("DURACAO", treino.getDuracao());

        return db.update("TREINO", values, "ID=?", new String[]{String.valueOf(treino.getId())}) > 0;
    }

    /**
     * Excluir
     * @param treino
     * @return boolean
     */
    public boolean excluir(Treino treino) {
        return db.delete("TREINO", "ID=?", new String[]{String.valueOf(treino.getId())}) > 0;
    }

    /**
     * Contar
     * @param treino
     * @return int
     */
    public int contarNome(Treino treino){
        int retorno;

        String countQuery = "SELECT COUNT(*) FROM TREINO WHERE  LOWER(NOME)=?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{treino.getNome().toLowerCase()});
        cursor.moveToNext();
        retorno = cursor.getInt(0);

        cursor.close();

        return retorno;
    }

    /**
     * Listar
     * @return
     */
    public List<Treino> listar() {
        List<Treino> treinos = new ArrayList<>();
        Cursor cursor = db.query("TREINO", new String[]{"ID", "NOME", "ROUND", "DESCANSO", "DURACAO"},
                null, null, null, null, "NOME");
        while (cursor.moveToNext()) {
            Treino treino = new Treino();
            treino.setId(cursor.getInt(0));
            treino.setNome(cursor.getString(1));
            treino.setRound(Integer.valueOf(cursor.getString(2)));
            treino.setDescanso(Integer.valueOf(cursor.getString(3)));
            treino.setDuracao(Integer.valueOf(cursor.getString(4)));
            treinos.add(treino);
        }
        return treinos;
    }
}
