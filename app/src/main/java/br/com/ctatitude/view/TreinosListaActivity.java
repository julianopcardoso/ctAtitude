package br.com.ctatitude.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.TreinoAdapter;
import br.com.ctatitude.dao.TreinoDAO;
import br.com.ctatitude.model.Treino;

/**
 * Classe TreinosListaActivity
 */
public class TreinosListaActivity extends AppCompatActivity {

    private TreinoDAO treinoDAO;
    private List<Treino> treinos;
    private TreinoAdapter TreinoAdapter;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_lista);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.treinos);
        setSupportActionBar(toolbar);

        //Set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set DAO
        treinoDAO = new TreinoDAO(this);
        treinos = treinoDAO.listar();

        //Set recyclerview
        TreinoAdapter = new TreinoAdapter(treinos);
        RecyclerView rv = findViewById(R.id.recyclerview_listatreinos);
        rv.setAdapter(TreinoAdapter);
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //Item de menu: Novo
        MenuItem novo = menu.findItem(R.id.novo);
        novo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                intent = new Intent(TreinosListaActivity.this, TreinosCasdastroActivity.class);
                startActivity(intent);
                return true;
            }
        });

        //Item de menu: Pesquisar
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        SearchView searchView = (SearchView) pesquisar.getActionView();
        searchView.setQueryHint(getString(R.string.pesquisar_hint));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TreinoAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}