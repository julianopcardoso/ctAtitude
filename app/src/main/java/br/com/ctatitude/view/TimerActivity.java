package br.com.ctatitude.view;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.ExercicioEtapaAdapter;
import br.com.ctatitude.adapter.ExercicioTimerAdapter;
import br.com.ctatitude.dao.EtapaDAO;
import br.com.ctatitude.dao.EtapaTreinoDAO;
import br.com.ctatitude.dao.ExercicioEtapaDAO;
import br.com.ctatitude.dao.TreinoDAO;
import br.com.ctatitude.model.EtapaTreino;
import br.com.ctatitude.model.Treino;
import br.com.ctatitude.utils.Utils;

/**
 * Classe TimerActivity
 */
public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    private TextView countDown;
    private MediaPlayer player;

    private TextView nomeTreinoTimer;
    private TextView contadorRoundTreino;
    private TextView nomeEtapaRound;
    private TextView contadorRoundEtapa;
    private TextView valorDurcaoEtapa;
    private TextView valorDescansoEtapa;
    private TextView duracaoTotalTreino;
    private TextView duracaoDescansoTreino;
    private ImageButton buttonPlay;
    private ImageButton buttonPause;
    private ImageButton buttonStop;
    private ImageButton buttonExpand;
    private ScrollView scrollDetalheEtapa;
    private CardView cardViewEtapaAtual;

    private boolean novoRoundEtapa;
    private boolean running;
    private boolean workStart;
    private boolean inicioTreino;

    private long timeLeftInMillis = 6000;

    private Integer cronometro;

    private Integer totalRoundsTreino;
    private Integer totalRoundsEtapa;
    private Integer totalEtapas;
    private Integer roundAtualTreino;
    private Integer roundAtualEtapa;
    private Integer etapaAtual;

    private Treino treino;
    private TreinoDAO treinoDAO;
    private EtapaDAO etapaDAO;
    private EtapaTreinoDAO etapaTreinoDAO;
    private ExercicioEtapaDAO exercicioEtapaDAO;

    private ExercicioTimerAdapter exercicioTimerAdapter;
    private RecyclerView rv;

    /**
     * onDestroy
     * Para todos contadores e player
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        player.stop();
        player.release();
    }

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.timer);
        setSupportActionBar(toolbar);

        //Set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set views
        duracaoDescansoTreino = findViewById(R.id.textValorDuracaoDescansoTreino);
        duracaoTotalTreino = findViewById(R.id.textViewCronometro);
        countDown = findViewById(R.id.textCounterDown);
        nomeTreinoTimer = findViewById(R.id.textNomeTreinoTimer);
        contadorRoundTreino = findViewById(R.id.textContadorRoundTreino);
        nomeEtapaRound = findViewById(R.id.textNomeEtapaTimer);
        contadorRoundEtapa = findViewById(R.id.textContadorRoundEtapaTimer);
        valorDescansoEtapa = findViewById(R.id.textValorDescansoEtapaTimer);
        valorDurcaoEtapa = findViewById(R.id.textValorDuracaoEtapaTimer);
        buttonPlay = findViewById(R.id.playTimer);
        buttonPause = findViewById(R.id.pauseTimer);
        buttonStop = findViewById(R.id.stopTimer);
        buttonExpand = findViewById(R.id.buttonExpand);
        rv = findViewById(R.id.recyclerview_exercicios_timer);
        scrollDetalheEtapa = findViewById(R.id.scrollDetalheEtapa);
        cardViewEtapaAtual = findViewById(R.id.cardview_EtapaAtual);

        //Set listeners
        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                collapseExpandDetalhesEtapa();
            }
        });

        //Set objeto treino
        treino = new Treino();
        List<EtapaTreino> etapasTreino = new ArrayList<>();
        treino.setEtapasTreinos(etapasTreino);

        //Set conexão banco de dados
        treinoDAO = new TreinoDAO(this);
        etapaDAO = new EtapaDAO(this);
        etapaTreinoDAO = new EtapaTreinoDAO(this);
        exercicioEtapaDAO = new ExercicioEtapaDAO(this);

        //Get valores da lista de timers
        Intent intent = getIntent();
        if (intent.hasExtra("treinotimer")) {
            treino = (Treino) intent.getSerializableExtra("treinotimer");
            //Obter Etapas
            treino.setEtapasTreinos(etapaTreinoDAO.listar(treino));

            //Obter Exercícios
            for (int i = 0; i < treino.getEtapasTreinos().size(); i++) {
                treino.getEtapasTreinos().get(i).getEtapa()
                        .setExerciciosEtapa(exercicioEtapaDAO.listar(treino.getEtapasTreinos().get(i).getEtapa()));
            }

            //Atribui valores iniciais
            atribuiValoresIniciais();
            //Atualiza etapa
            atualizaEtapa();
        }

        //Realiza a abertura do Timer
        workStart = true;
        aberturaTimer();
    }

    /**
     * Método collapseExpandDetalhesEtapa
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void collapseExpandDetalhesEtapa() {
        if (scrollDetalheEtapa.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardViewEtapaAtual,
                    new AutoTransition());
            scrollDetalheEtapa.setVisibility(View.GONE);
            buttonExpand.setImageResource(R.drawable.ic_expand_more);
        } else {
            TransitionManager.beginDelayedTransition(cardViewEtapaAtual,
                    new AutoTransition());
            scrollDetalheEtapa.setVisibility(View.VISIBLE);
            buttonExpand.setImageResource(R.drawable.ic_expand_less);
        }
    }

    /**
     * Método atribuiValoresIniciais
     */
    private void atribuiValoresIniciais() {
        nomeTreinoTimer.setText(treino.getNome());
        duracaoDescansoTreino.setText(Utils.convertSecondsToMinutesSeconds(treino.getDescanso()));
        roundAtualTreino = 1;
        totalEtapas = treino.getEtapasTreinos().size();
        totalRoundsTreino = treino.getRound();
        totalRoundsEtapa = treino.getEtapasTreinos().get(0).getEtapa().getRound();
        roundAtualEtapa = 1;
        etapaAtual = 1;
        contadorRoundTreino.setText(String.format("%02d/%02d", roundAtualTreino, treino.getRound()));
        countDown.setTextColor(Color.parseColor("#000000"));
        scrollDetalheEtapa.setVisibility(View.GONE);
        buttonExpand.setImageResource(R.drawable.ic_expand_more);
    }

    /**
     * Método aberturaTimer
     */
    private void aberturaTimer() {
        //Inicializa com campos da invisíveis
        findViewById(R.id.textTimerDuracaoRound).setVisibility(View.INVISIBLE);
        findViewById(R.id.textTimerDuracaoTotal).setVisibility(View.INVISIBLE);
        findViewById(R.id.textRoundTreino).setVisibility(View.INVISIBLE);
        findViewById(R.id.textDuracaoDescansoTreino).setVisibility(View.INVISIBLE);
        duracaoDescansoTreino.setVisibility(View.INVISIBLE);
        duracaoTotalTreino.setVisibility(View.INVISIBLE);
        contadorRoundTreino.setVisibility(View.INVISIBLE);
        buttonPlay.setVisibility(View.INVISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonStop.setVisibility(View.INVISIBLE);
        cardViewEtapaAtual.setVisibility(View.INVISIBLE);
        countDown.setTextSize(100);

        //Executa player inicial
        player = MediaPlayer.create(TimerActivity.this, R.raw.get_ready_in);
        player.start();

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                long seconds = (timeLeftInMillis / 1000) % 60;
                if (workStart) {
                    switch ((int) seconds) {
                        case 3:
                            countDown.setTextColor(getResources().getColor(R.color.red));
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.three);
                            player.start();
                            break;
                        case 2:
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.two);
                            player.start();
                            break;
                        case 1:
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.one);
                            player.start();
                            break;
                        case 0:
                            if (workStart) {
                                player.release();
                                player = MediaPlayer.create(TimerActivity.this, R.raw.work);
                                player.start();
                                countDown.setTextColor(getResources().getColor(R.color.black));
                                countDown.setText(R.string.work);
                                workStart = false;
                            }
                            break;
                    }
                }
                if ((int) seconds > 0) {
                    updateCountDownText();
                }
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                running = true;
                workStart = true;
                cronometro = 0;
                //Set campos visíveis
                duracaoTotalTreino.setVisibility(View.VISIBLE);
                findViewById(R.id.textTimerDuracaoRound).setVisibility(View.VISIBLE);
                findViewById(R.id.textTimerDuracaoTotal).setVisibility(View.VISIBLE);
                findViewById(R.id.textRoundTreino).setVisibility(View.VISIBLE);
                countDown.setTextSize(24);
                findViewById(R.id.textDuracaoDescansoTreino).setVisibility(View.VISIBLE);

                duracaoDescansoTreino.setVisibility(View.VISIBLE);
                contadorRoundTreino.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.VISIBLE);
                buttonPause.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                cardViewEtapaAtual.setVisibility(View.VISIBLE);

                defineTimerTreino();
            }
        }.start();
    }

    /**
     * Método atualizaEtapa[
     */
    private void atualizaEtapa() {
        nomeEtapaRound.setText(String.format("%s %d/%d", getResources().getString(R.string.etapa), treino.getEtapasTreinos().get(etapaAtual - 1).getOrdem(),treino.getEtapasTreinos().size()));
        contadorRoundEtapa.setText(String.format("%02d/%02d", roundAtualEtapa, treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getRound()));
        valorDurcaoEtapa.setText(Utils.convertSecondsToMinutesSeconds(treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getDuracao()));
        valorDescansoEtapa.setText(Utils.convertSecondsToMinutesSeconds(treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getDescanso()));
        exercicioTimerAdapter = new ExercicioTimerAdapter(treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getExerciciosEtapa());
        rv.setAdapter(exercicioTimerAdapter);
    }

    /**
     * onClick
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playTimer:
                start(); //Executa timer
                break;
            case R.id.pauseTimer:
                pause(); //Pausa o timer
                break;
            case R.id.stopTimer:
                stop(); //Para o timer
                break;
        }
    }

    /**
     * Método defineTimerTreino
     */
    private void defineTimerTreino() {
        if (roundAtualTreino <= totalRoundsTreino) {
            if (etapaAtual > totalEtapas) {
                etapaAtual = 1;
                roundAtualTreino += 1;

                if (treino.getDescanso() > 0) {
                    //Descanso do treino
                    player.release();
                    player = MediaPlayer.create(TimerActivity.this, R.raw.rest);
                    player.start();

                    cronometro -= 2;
                    countDownTimer.onTick((treino.getDescanso() + 1) * 1000);
                    executaTimerTreino();
                } else {
                    defineTimerTreino();
                }
            } else {
                contadorRoundTreino.setText(String.format("%02d/%02d", roundAtualTreino, treino.getRound()));
                if (novoRoundEtapa) {
                    novoRoundEtapa = false;
                    if (treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getDescanso() > 0) {

                        //Descanso da etapa
                        player.release();
                        player = MediaPlayer.create(TimerActivity.this, R.raw.rest);
                        player.start();
                        cronometro -= 2;
                        countDownTimer.onTick((treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getDescanso() + 1) * 1000);
                        executaTimerTreino();
                    } else {
                        defineTimerTreino();
                    }
                } else {
                    if (roundAtualEtapa > totalRoundsEtapa) {
                        roundAtualEtapa = 1;
                        etapaAtual = etapaAtual + 1;
                        if (totalEtapas >= etapaAtual) {
                            totalRoundsEtapa = treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getRound();
                        }
                        defineTimerTreino();

                    } else {
                        atualizaEtapa();
                        roundAtualEtapa += 1;
                        novoRoundEtapa = true;
                        //Executa timer
                        if (!workStart) {
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.work);
                            player.start();
                            cronometro -= 2;
                        } else {
                            workStart = false;
                            cronometro -= 1;
                        }

                        countDownTimer.onTick((treino.getEtapasTreinos().get(etapaAtual - 1).getEtapa().getDuracao() + 1) * 1000);
                        executaTimerTreino();
                    }
                }
            }
        } else {
            //Encerra tudo
            player = MediaPlayer.create(TimerActivity.this, R.raw.well_done);
            player.start();

            countDownTimer.cancel();
            timeLeftInMillis = 0;
            running = false;
            inicioTreino = true;
            novoRoundEtapa = false;
            workStart = false;

            atribuiValoresIniciais();
            atualizaEtapa();
            updateCountDownText();
        }
    }

    /**
     * Método executaTimerTreino
     */
    private void executaTimerTreino() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                long hours = (timeLeftInMillis / 1000) / 3600;
                long minutes = ((timeLeftInMillis / 1000) / 60) % 60;
                long seconds = (timeLeftInMillis / 1000) % 60;

                cronometro += 1;

                if (hours == 0 && minutes == 0) {
                    switch ((int) seconds) {
                        case 3:
                            countDown.setTextColor(getResources().getColor(R.color.red));
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.three);
                            player.start();
                            break;
                        case 2:
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.two);
                            player.start();

                            break;
                        case 1:
                            player.release();
                            player = MediaPlayer.create(TimerActivity.this, R.raw.one);
                            player.start();
                            break;
                        case 0:
                            duracaoTotalTreino.setText(Utils.convertSecondsToHoursMinutesSeconds(cronometro));
                            onFinish();
                            break;
                    }
                }
                updateCountDownText();
                duracaoTotalTreino.setText(Utils.convertSecondsToHoursMinutesSeconds(cronometro));
            }

            @Override
            public void onFinish() {
                running = false;
                countDownTimer.cancel();
                countDown.setTextColor(getResources().getColor(R.color.black));
                defineTimerTreino();
            }
        }.start();
    }

    /**
     * onCreateOptionsMenu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //Set visivilidade dos itens de menu
        MenuItem novo = menu.findItem(R.id.novo);
        novo.setVisible(false);
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        pesquisar.setVisible(false);
        return true;
    }

    /**
     * Método updateCountDownText
     */
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countDown.setText(timeLeftFormatted);
    }

    /**
     * Método start
     * Start timer
     */
    private void start() {
        if (!running) {
            if (inicioTreino) {
                timeLeftInMillis = 6000;
                running = true;
                inicioTreino = false;
                aberturaTimer();
            } else {
                cronometro -= 1;
                executaTimerTreino();
                running = true;
            }
        }
    }

    /**
     * Método pause
     * Pausa o timer
     */
    private void pause() {
        if (running) {
            countDownTimer.cancel();
            running = false;
        }
    }

    /**
     * Método stop
     * Para o timer
     */
    private void stop() {
        player.release();
        countDownTimer.cancel();
        cronometro = 0;
        timeLeftInMillis = 0;
        running = false;
        inicioTreino = true;
        novoRoundEtapa = false;
        workStart = true;

        atribuiValoresIniciais();
        atualizaEtapa();
        updateCountDownText();
    }
}
