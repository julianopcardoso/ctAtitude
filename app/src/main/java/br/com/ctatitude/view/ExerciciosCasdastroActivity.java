package br.com.ctatitude.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.utils.EnumSimNao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import br.com.ctatitude.R;
import br.com.ctatitude.model.Exercicio;

/**
 * Classe ExerciciosCasdastroActivity
 */
public class ExerciciosCasdastroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nomeExercicio;
    private EditText descricaoExercicio;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch repeticoes;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch peso;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch distancia;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch tempo;

    private ExercicioDAO exercicioDAO;
    private Exercicio exercicioEditado = null;
    private boolean acaoAlteracao = false;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicios_cadastro);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.exercicios);
        setSupportActionBar(toolbar);

        //Set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set salvar button
        Button salvarExercicio = findViewById(R.id.buttonSalvar);
        salvarExercicio.setOnClickListener(this);

        //Set conexão banco de dados
        exercicioDAO = new ExercicioDAO(this);

        //Set campos da tela
        nomeExercicio = findViewById(R.id.editTextNomeExercicio);
        descricaoExercicio = findViewById(R.id.editTextDescricao);
        repeticoes = findViewById(R.id.switchRepeticoes);
        peso = findViewById(R.id.switchPeso);
        distancia = findViewById(R.id.switchDistancia);
        tempo = findViewById(R.id.switchTempo);

        acaoAlteracao = false;

        //Alteração de Exercício. Serealiza parâmetros da tela de lista
        Intent intent = getIntent();
        if (intent.hasExtra("exercicio")) {
            acaoAlteracao = true;
            exercicioEditado = (Exercicio) intent.getSerializableExtra("exercicio");

            nomeExercicio.setText(exercicioEditado.getNome());
            descricaoExercicio.setText(exercicioEditado.getDescricao());
            repeticoes.setChecked(exercicioEditado.getRepeticoes().equals(EnumSimNao.SIM.getValor()));
            distancia.setChecked(exercicioEditado.getDistancia().equals(EnumSimNao.SIM.getValor()));
            peso.setChecked(exercicioEditado.getPeso().equals(EnumSimNao.SIM.getValor()));
            tempo.setChecked(exercicioEditado.getTempo().equals(EnumSimNao.SIM.getValor()));
        }
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //Set visibilidade dos itens de menu
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        MenuItem novo = menu.findItem(R.id.novo);
        pesquisar.setVisible(false);
        novo.setVisible(false);
        return true;
    }

    /**
     * Método salvar
     * Valida e Salva exercício no banco de dados
     * @param view the view
     */
    public void salvar(View view) {
        Exercicio exercicio = new Exercicio();

        //Valida se o nome foi informado
        if (nomeExercicio.getText().toString().trim().equals("")) {
            nomeExercicio.requestFocus();
            Toast.makeText(this, R.string.nome_nao_informado, Toast.LENGTH_LONG).show();
            return;
        }

        //Valida se a descricao foi informada
        if (descricaoExercicio.getText().toString().trim().equals("")) {
            descricaoExercicio.requestFocus();
            Toast.makeText(this, R.string.descricao_nao_informada, Toast.LENGTH_LONG).show();
            return;
        }

        //Set nome do exercício
        exercicio.setNome(nomeExercicio.getText().toString());

        //Verifica se já existe exercício cadastrado com o mesmo nome
        int nomeExercicioExistente = exercicioDAO.contarNome(exercicio);
        if(nomeExercicioExistente > 0) {
            if (acaoAlteracao) {
                if (!exercicioEditado.getNome().toLowerCase().equals(exercicio.getNome().toLowerCase())) {
                    Toast.makeText(this, R.string.nome_existente, Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                Toast.makeText(this, R.string.nome_existente, Toast.LENGTH_LONG).show();
                return;
            }
        }

        exercicio.setDescricao(descricaoExercicio.getText().toString());

        if (repeticoes.isChecked()) {
            exercicio.setRepeticoes(EnumSimNao.SIM.getValor());
        } else {
            exercicio.setRepeticoes(EnumSimNao.NAO.getValor());
        }

        if (peso.isChecked()) {
            exercicio.setPeso(EnumSimNao.SIM.getValor());
        } else {
            exercicio.setPeso(EnumSimNao.NAO.getValor());
        }

        if (distancia.isChecked()) {
            exercicio.setDistancia(EnumSimNao.SIM.getValor());
        } else {
            exercicio.setDistancia(EnumSimNao.NAO.getValor());
        }

        if (tempo.isChecked()) {
            exercicio.setTempo(EnumSimNao.SIM.getValor());
        } else {
            exercicio.setTempo(EnumSimNao.NAO.getValor());
        }

        boolean registroSalvo = false;

        //Se ação é alteração, realiza alteração
        //Senão realiza inclusão do exercício
        if (!acaoAlteracao) {
            registroSalvo = exercicioDAO.inserir(exercicio);
        } else {
            exercicio.setId(exercicioEditado.getId());
            registroSalvo = exercicioDAO.alterar(exercicio);
        }

        if (registroSalvo) {
            Toast.makeText(this, R.string.registro_salvo, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.erro_salvar, Toast.LENGTH_LONG).show();
        }

        //Ação alteração, volta para tela de lista
        if (acaoAlteracao) {
            Intent intent = null;
            intent = new Intent(this, ExerciciosListaActivity.class);
            startActivity(intent);
        } else {
            nomeExercicio.setText("");
            descricaoExercicio.setText("");
            repeticoes.setChecked(false);
            peso.setChecked(false);
            distancia.setChecked(false);
            tempo.setChecked(false);
        }
    }

    /**
     * onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        //Executa botão salvar
        salvar(v);
    }
}