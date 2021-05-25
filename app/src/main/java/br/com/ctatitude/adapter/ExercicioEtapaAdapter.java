package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.model.ExercicioEtapa;
import br.com.ctatitude.utils.EnumSimNao;
import br.com.ctatitude.utils.Utils;
import br.com.ctatitude.view.TreinosCasdastroActivity;
import br.com.ctatitude.view.TreinosEtapaCasdastroActivity;
import br.com.ctatitude.view.TreinosEtapaExercicioCasdastroActivity;

/**
 * Classe ExercicioEtapaAdapter
 */
public class ExercicioEtapaAdapter extends RecyclerView.Adapter<ExercicioEtapaAdapter.ExercicioEtapaViewHolder> {
    private final List<ExercicioEtapa> exercicioEtapas;

    /**
     * Construtor
     * @param exercicioEtapas
     */
    public ExercicioEtapaAdapter(List<ExercicioEtapa> exercicioEtapas) {
        this.exercicioEtapas = exercicioEtapas;
    }

    /**
     * Função onCreateViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ExercicioEtapaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_exercicio_etapa, parent, false);

        return new ExercicioEtapaViewHolder(view);

    }

    /**
     * onBindViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ExercicioEtapaViewHolder holder, int position) {
        ExercicioEtapa exercicioEtapa = exercicioEtapas.get(position);
        //Preenche campos do item da lista
        holder.bind(exercicioEtapa);

        //Listener para botões Alterar e Excluir
        clickBotaoAlterar(holder, exercicioEtapa);
        clickBotaoExcluir(holder, exercicioEtapa);
    }

    /**
     * Método para click do Botão excluir
     * @param holder
     * @param exercicioEtapa
     */
    private void clickBotaoExcluir(ExercicioEtapaViewHolder holder, ExercicioEtapa exercicioEtapa) {
        holder.btnExcluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirExercicioEtapa(v, exercicioEtapa);
            }
        });
    }

    /**
     * Método para excluir exercício-etapa da base e da lista
     * Realiza exclusão quando clicar o botão excluir
     * Antes realiza confirmação
     * @param v
     * @param exercicioEtapa
     */
    private void excluirExercicioEtapa(View v, ExercicioEtapa exercicioEtapa) {
        final View view = v;

        //Exibe mensagem de confirmação de exclusão
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmacao)
                .setMessage(R.string.confirmacao_exclusao)
                .setPositiveButton(R.string.excluir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removerExercicioEtapa(exercicioEtapa);
                        Toast.makeText(getActivity(v), R.string.registro_excluido, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create()
                .show();
    }


    static final int REQUEST_UPDATE = 1;

    /**
     * Método do click do botão alterar
     * selecionado
     * @param holder
     * @param exercicioEtapa
     */
    private void clickBotaoAlterar(ExercicioEtapaViewHolder holder, ExercicioEtapa exercicioEtapa) {
        holder.btnEditar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarExercicioEtapa(v, exercicioEtapa);
            }
        });
    }

    /**
     * Método alterarExercicioEtapa
     * Chama tela de cadastro para editar o exercício-etapa
     * @param v
     * @param exercicioEtapa
     */
    private void alterarExercicioEtapa(View v, ExercicioEtapa exercicioEtapa) {
        Activity activity = getActivity(v);
        Intent intent = null;
        intent = new Intent(activity, TreinosEtapaExercicioCasdastroActivity.class);
        intent.putExtra("exercicioetapa", exercicioEtapa);
        activity.startActivityForResult(intent, REQUEST_UPDATE);
    }

    /**
     * Função getItemCount
     * @return int
     */
    @Override
    public int getItemCount() {
        return exercicioEtapas.size();
    }

    /**
     * Método removerExercicioEtapa
     * Remove o exercício-etapa da recyclerview e do objeto
     * @param exercicioEtapa
     */
    public void removerExercicioEtapa(ExercicioEtapa exercicioEtapa) {
        int position = exercicioEtapas.indexOf(exercicioEtapa);
        exercicioEtapas.remove(position);

        //Reordena lista
        for (int i = 0; i < exercicioEtapas.size(); i++) {
            exercicioEtapas.get(i).setOrdem(i+1);
        }

        notifyItemRemoved(position);
    }

    /**
     * Funão getActivity
     * Busca a activity corrente do contexto
     * @param view
     * @return Activity
     */
    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * Classe ExercicioEtapaViewHolder
     */
    class ExercicioEtapaViewHolder extends RecyclerView.ViewHolder {

        TextView nomeExercicioLista;
        TextView descricaoExercicio;

        ImageButton btnEditar;
        ImageButton btnExcluir;

        /**
         * Construtor
         * @param itemView
         */
        public ExercicioEtapaViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set views
            nomeExercicioLista = itemView.findViewById(R.id.textNomeExercicioLista);
            descricaoExercicio = itemView.findViewById(R.id.textDescricaoExercicio);
            btnEditar = itemView.findViewById(R.id.editExercicioItem);
            btnExcluir = itemView.findViewById(R.id.deleteExercicioItem);
        }

        /**
         * Método bind
         * Popula valores na tela
         * @param exercicioEtapa
         */
        public void bind(ExercicioEtapa exercicioEtapa) {
            nomeExercicioLista.setText(String.valueOf(exercicioEtapa.getExercicio().getNome()));
            descricaoExercicio.setText(formataDescricaoExercicio(exercicioEtapa));

        }

        /**
         * Função formataDescricaoExercicio
         * Formata a exibição do detalhamento
         * do exercício que irá aparecer na lista
         * @param exercicioEtapa
         * @return
         */
        private String formataDescricaoExercicio(ExercicioEtapa exercicioEtapa) {
            StringBuilder descricaoExercicio = new StringBuilder();

            if (exercicioEtapa.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getRepeticao() != null) {
                    if (!exercicioEtapa.getRepeticao().toString().trim().equals("0")) {
                        descricaoExercicio.append(exercicioEtapa.getRepeticao().toString() + " repetições");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getPesoMasc() != null && exercicioEtapa.getPesoFem() != null) {
                    if (!exercicioEtapa.getPesoMasc().toString().trim().equals("0") && !exercicioEtapa.getPesoFem().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(exercicioEtapa.getPesoMasc().toString() + "/" + exercicioEtapa.getPesoFem().toString() + "kg");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getDistancia() != null) {
                    if (!exercicioEtapa.getDistancia().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(exercicioEtapa.getDistancia().toString() + "m");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getTempo() != null) {
                    if (!exercicioEtapa.getTempo().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(Utils.convertSecondsToMinutesSeconds(exercicioEtapa.getTempo()));
                    }
                }
            }

            return descricaoExercicio.toString();
        }
    }
}
