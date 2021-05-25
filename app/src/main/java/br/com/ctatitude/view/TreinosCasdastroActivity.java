package br.com.ctatitude.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.EtapaTreinoAdapter;
import br.com.ctatitude.dao.EtapaDAO;
import br.com.ctatitude.dao.EtapaTreinoDAO;
import br.com.ctatitude.dao.ExercicioEtapaDAO;
import br.com.ctatitude.dao.TreinoDAO;
import br.com.ctatitude.model.Etapa;
import br.com.ctatitude.model.EtapaTreino;
import br.com.ctatitude.model.Treino;
import br.com.ctatitude.utils.Utils;

/**
 * Classe TreinosCasdastroActivity
 */
public class TreinosCasdastroActivity extends AppCompatActivity implements View.OnClickListener {

    private TreinoDAO treinoDAO;
    private EtapaDAO etapaDAO;
    private EtapaTreinoDAO etapaTreinoDAO;
    private ExercicioEtapaDAO exercicioEtapaDAO;

    private Treino treino = null;
    private Treino treinoEditado = null;
    private boolean acaoAlteracao = false;

    private TextView totalTreino;
    private EditText nomeTreino;
    private EditText rounds;
    private EditText descanso;

    private List<EtapaTreino> novasEtapasTreinos;
    private EtapaTreinoAdapter etapaTreinoAdapter;
    private RecyclerView rv;

    private static final int REQUEST_INSERT = 0;
    private static final int REQUEST_UPDATE = 1;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_cadastro);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.treinos);
        setSupportActionBar(toolbar);

        //set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set objeto treino
        treino = new Treino();
        List<EtapaTreino> etapasTreino = new ArrayList<>();
        treino.setEtapasTreinos(etapasTreino);
        treinoEditado = new Treino();
        treinoEditado.setEtapasTreinos(etapasTreino);
        novasEtapasTreinos = new ArrayList<>();

        //Set botões
        Button salvarTreino = findViewById(R.id.buttonSalvar);
        Button adicionarEtapa = findViewById(R.id.buttonAdicionarEtapa);
        ImageButton minusRound = findViewById(R.id.imageButtonMinus);
        ImageButton plusRound = findViewById(R.id.imageButtonPlus);

        //Set listeners
        salvarTreino.setOnClickListener(this);
        adicionarEtapa.setOnClickListener(this);
        minusRound.setOnClickListener(this);
        plusRound.setOnClickListener(this);

        //Set campos da tela
        totalTreino = findViewById(R.id.textTotalTreino);
        nomeTreino = findViewById(R.id.editTextNomeTreino);
        rounds = findViewById(R.id.editTextRounds);
        descanso = findViewById(R.id.editTextDescanso);
        descanso.setOnClickListener(this);

        //Set conexão banco de dados
        treinoDAO = new TreinoDAO(this);
        etapaDAO = new EtapaDAO(this);
        etapaTreinoDAO = new EtapaTreinoDAO(this);
        exercicioEtapaDAO = new ExercicioEtapaDAO(this);

        acaoAlteracao = false;
        // Alteração de Treino. Serealiza parâmetros da tela de lista
        Intent intent = getIntent();
        if (intent.hasExtra("treino")) {
            acaoAlteracao = true;
            treino = (Treino) intent.getSerializableExtra("treino");
            //Obter treino
            totalTreino.setText(Utils.convertSecondsToHoursMinutesSeconds(treino.getDuracao()));
            descanso.setText(Utils.convertSecondsToMinutesSeconds(treino.getDescanso()));
            rounds.setText(String.format("%02d", treino.getRound()));
            nomeTreino.setText(treino.getNome());
            //Obter Etapas
            treino.setEtapasTreinos(etapaTreinoDAO.listar(treino));

            //Obter Exercícios
            for (int i = 0; i < treino.getEtapasTreinos().size(); i++) {
                treino.getEtapasTreinos().get(i).getEtapa().setExerciciosEtapa(exercicioEtapaDAO.listar(treino.getEtapasTreinos().get(i).getEtapa()));
            }

            //Objeto que armazena estado inicial do Treino
            treinoEditado.setId(treino.getId());
            treinoEditado.setNome(treino.getNome());
            List<EtapaTreino> etapasTreinoEditado = new ArrayList<>();
            for (int i = 0; i < treino.getEtapasTreinos().size(); i++) {
                EtapaTreino etapaTreino = new EtapaTreino();
                etapaTreino.setIdEtapa(treino.getEtapasTreinos().get(i).getEtapa().getId());
                etapaTreino.setIdTreino(treino.getId());
                etapaTreino.setEtapa(new Etapa());
                etapaTreino.getEtapa().setId(treino.getEtapasTreinos().get(i).getEtapa().getId());
                etapasTreinoEditado.add(etapaTreino);
            }
            treinoEditado.setEtapasTreinos(etapasTreinoEditado);
        }

        etapaTreinoAdapter = new EtapaTreinoAdapter(treino.getEtapasTreinos());
        //Set recyclerview - lista de Etapas Treino
        rv = findViewById(R.id.recyclerview_etapas);
        rv.setAdapter(etapaTreinoAdapter);
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //Set visibilidade itens de menu
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        MenuItem novo = menu.findItem(R.id.novo);
        pesquisar.setVisible(false);
        novo.setVisible(false);
        return true;
    }

    /**
     * Método salvar
     * Salva Treino
     * @param view the view
     */
    public void salvar(View view) {
        boolean registroSalvo = false;
        Integer idTreino = 0;
        Integer idEtapa = 0;

        //Valida se o nome foi informado
        if(nomeTreino.getText().toString().trim().equals("")){
            nomeTreino.requestFocus();
            Toast.makeText(this, R.string.nome_nao_informado, Toast.LENGTH_LONG).show();
            return;
        }

        //Valida se o nome informado já existe cadastrado
        treino.setNome(nomeTreino.getText().toString());
        int nomeTreinoExistente = treinoDAO.contarNome(treino);
        if(nomeTreinoExistente > 0) {
            if (acaoAlteracao) {
                if (!treinoEditado.getNome().toLowerCase().equals(treino.getNome().toLowerCase())) {
                    Toast.makeText(this, R.string.nome_existente, Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                Toast.makeText(this, R.string.nome_existente, Toast.LENGTH_LONG).show();
                return;
            }
        }

        treino.setRound(Integer.valueOf(rounds.getText().toString()));
        treino.setDuracao(Utils.convertHoursMinutesSecondsToSeconds(totalTreino.getText().toString()));
        treino.setDescanso(Utils.convertMinutesSecondsToSeconds(descanso.getText().toString()));

        //Valida se foi informada no mínimo 1 etapa
        if(treino.getEtapasTreinos().size() == 0){
            Toast.makeText(this, R.string.minimo_etapa, Toast.LENGTH_LONG).show();
            return;
        }

        //Insere ou altera treino
        if (!acaoAlteracao) {
            idTreino = (int) treinoDAO.inserir(treino);

        } else {
            idTreino = treino.getId();
            treinoDAO.alterar(treino);

            //Remove Tudo
            for (int i = 0; i < treinoEditado.getEtapasTreinos().size(); i++) {
                exercicioEtapaDAO.excluir(treinoEditado.getEtapasTreinos().get(i).getEtapa());
                etapaTreinoDAO.excluir(treinoEditado.getEtapasTreinos().get(i).getEtapa());
                etapaDAO.excluir(treinoEditado.getEtapasTreinos().get(i).getEtapa());
            }
        }

        for (int i = 0; i < treino.getEtapasTreinos().size(); i++) {
            //Insere Etapa
            idEtapa = (int) etapaDAO.inserir(treino.getEtapasTreinos().get(i).getEtapa());
            treino.getEtapasTreinos().get(i).getEtapa().setId(idEtapa);
            treino.getEtapasTreinos().get(i).setIdEtapa(idEtapa);
            treino.getEtapasTreinos().get(i).setIdTreino(idTreino);

            //Insere Etapa Treino
            etapaTreinoDAO.inserir(treino.getEtapasTreinos().get(i));

            //Insere Lista de Exercícios da Etapa
            for (int j = 0; j < treino.getEtapasTreinos().get(i).getEtapa().getExerciciosEtapa().size(); j++) {
                treino.getEtapasTreinos().get(i).getEtapa().getExerciciosEtapa().get(j).setIdEtapa(idEtapa);
                treino.getEtapasTreinos().get(i).getEtapa().getExerciciosEtapa().get(j).setIdExercicio(
                        treino.getEtapasTreinos().get(i).getEtapa().getExerciciosEtapa().get(j).getExercicio().getId());

                exercicioEtapaDAO.inserir(treino.getEtapasTreinos().get(i).getEtapa().getExerciciosEtapa().get(j));
            }
        }

        registroSalvo = true;

        if (registroSalvo) {
            Toast.makeText(this, R.string.registro_salvo, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, R.string.erro_salvar, Toast.LENGTH_LONG).show();
        }
        Intent intent = null;
        intent = new Intent(this, TreinosListaActivity.class);
        startActivity(intent);
    }

    /**
     * Método atualizarTotalRounds
     * @param valor the valor
     */
    public void atualizarTotalRounds(Integer valor) {
        Integer valorRoundsAtual = Integer.valueOf(rounds.getText().toString());
        if ((valor + valorRoundsAtual) == 0) {
            Toast.makeText(this,
                    R.string.valor_min_rounds,
                    Toast.LENGTH_LONG).show();
            return;
        } else if ((valor + valorRoundsAtual) == 100) {
            Toast.makeText(this,
                    R.string.valor_maximo_rounds,
                    Toast.LENGTH_LONG).show();
            return;
        }
        rounds.setText(String.format("%02d", valor + valorRoundsAtual));
        atualizarTotalTreino();
    }

    /**
     * onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSalvar:
                salvar(v); //Salva treino
                break;
            case R.id.editTextDescanso:
                selecionaDescanso(); //Seleciona duração do descanso
                break;
            case R.id.buttonAdicionarEtapa:
                adicionarNovaEtapa(); //Adiciona nova etapa
                break;
            case R.id.imageButtonMinus:
                atualizarTotalRounds(-1); //Atualiza contagem dos rounds -1
                break;
            case R.id.imageButtonPlus:
                atualizarTotalRounds(1); //Atualiza contagem dos rounds +1
                break;
        }
    }

    /**
     * Método adicionarNovaEtapa
     */
    private void adicionarNovaEtapa() {
        Integer novaEtapa = treino.getEtapasTreinos().size() + 1;
        Intent intent = null;
        intent = new Intent(TreinosCasdastroActivity.this, TreinosEtapaCasdastroActivity.class);
        intent.putExtra("etapatreinonovo", novaEtapa);
        startActivityForResult(intent, REQUEST_INSERT);
    }

    /**
     * onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verifica qual foi a requisição
        if (requestCode == REQUEST_INSERT) {
            if (resultCode == RESULT_OK) { //Resultado da requisição
                EtapaTreino returnEtapa = (EtapaTreino) data.getSerializableExtra("etapatreino");
                treino.getEtapasTreinos().add(returnEtapa);
                novasEtapasTreinos.add(returnEtapa);
                //atualiza a duração total do Treino
                atualizarTotalTreino();

                etapaTreinoAdapter.notifyItemRangeInserted(treino.getEtapasTreinos().size(), treino.getEtapasTreinos().size());
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) { //Resultado da requisição

                EtapaTreino returnEtapa = (EtapaTreino) data.getSerializableExtra("etapatreino");
                Integer index = 0;
                for (EtapaTreino etapaTreino : treino.getEtapasTreinos()) {
                    if (etapaTreino.getOrdem().equals(returnEtapa.getOrdem())) {
                        index = treino.getEtapasTreinos().indexOf(etapaTreino);
                    }
                }
                treino.getEtapasTreinos().get(index).setEtapa(returnEtapa.getEtapa());

                //atualiza a duração total do Treino
                atualizarTotalTreino();

                etapaTreinoAdapter.notifyItemChanged(index);
            }
        }
    }

    /**
     * Método selecionaDescanso
     * Seleciona a duração do descanso
     */
    private void selecionaDescanso() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.timerdialog);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
        d.getWindow().setLayout(width, height);
        Button buttonSelecionar = d.findViewById(R.id.buttonSelecionar);
        Button buttonCancelar = d.findViewById(R.id.buttonCancelar);
        final NumberPicker npm = d.findViewById(R.id.numberPickerMinute);
        final NumberPicker nps = d.findViewById(R.id.numberPickerSeconds);

        npm.setMaxValue(59); // max value 59
        npm.setMinValue(00);   // min value 0
        npm.setWrapSelectorWheel(true);
        nps.setMaxValue(59); // max value 59
        nps.setMinValue(00);   // min value 0

        String[] descansoSplit = descanso.getText().toString().split(":");

        Integer min = Integer.valueOf(descansoSplit[0]);
        Integer sec = Integer.valueOf(descansoSplit[1]);
        npm.setValue(min);
        nps.setValue(sec);

        nps.setWrapSelectorWheel(true);

        buttonSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descanso.setText(String.format("%02d", npm.getValue()) + ":" + String.format("%02d", nps.getValue()));
                atualizarTotalTreino();
                d.dismiss();
            }
        });
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    /**
     * Método atualizarTotalTreino
     * Atualiza duração total do treino
     */
    public void atualizarTotalTreino() {
        Integer descansoSegundos;
        Integer totalRounds = 0;
        Integer totalTReinoAtualizado = 0;
        Integer totalEtapaTreino = 0;

        //Total descanso em segundos
        descansoSegundos = Utils.convertMinutesSecondsToSeconds(descanso.getText().toString());
        //total de rounds
        totalRounds = Integer.valueOf(rounds.getText().toString());

        //lista total etapa e descanso etapa
        for (EtapaTreino etapaTreino : treino.getEtapasTreinos()) {
            totalEtapaTreino += (etapaTreino.getEtapa().getDescanso() + etapaTreino.getEtapa().getDuracao()) * etapaTreino.getEtapa().getRound();
        }

        //atualiza valor total do treino
        totalTReinoAtualizado = (descansoSegundos * totalRounds) + (totalEtapaTreino * totalRounds);

        totalTreino.setText(Utils.convertSecondsToHoursMinutesSeconds(totalTReinoAtualizado));
    }
}