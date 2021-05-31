package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.model.EtapaTreino;
import br.com.ctatitude.utils.Utils;
import br.com.ctatitude.view.TreinosCasdastroActivity;
import br.com.ctatitude.view.TreinosEtapaCasdastroActivity;

/**
 * Classe EtapaTreinoAdapter
 */
public class EtapaTreinoAdapter extends RecyclerView.Adapter<EtapaTreinoAdapter.EtapaTreinoViewHolder> {
    private final List<EtapaTreino> etapaTreinos;

    /**
     * Construtor
     *
     * @param etapaTreinos
     */
    public EtapaTreinoAdapter(List<EtapaTreino> etapaTreinos) {
        this.etapaTreinos = etapaTreinos;
    }

    /**
     * Função onCreateViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public EtapaTreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_etapa, parent, false);

        return new EtapaTreinoViewHolder(view);
    }

    /**
     * Método onBindViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull EtapaTreinoViewHolder holder, int position) {
        EtapaTreino etapaTreino = etapaTreinos.get(position);
        //Preenche campos do item da lista
        holder.bind(etapaTreino);

        //Listener para botões Alterar e Excluir
        clickBotaoAlterar(holder, etapaTreino);
        clickBotaoExcluir(holder, etapaTreino);
    }

    /**
     * Método para click do Botão excluir
     * @param holder
     * @param etapaTreino
     */
    private void clickBotaoExcluir(EtapaTreinoViewHolder holder, EtapaTreino etapaTreino) {
        holder.btnExcluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirEtapaTreino(v, etapaTreino);
            }
        });
    }

    /**
     * Método para excluir etapa-treino da base e da lista
     * Realiza exclusão quando clicar o botão excluir
     * Antes realiza confirmação
     * @param v
     * @param etapaTreino
     */
    private void excluirEtapaTreino(View v, EtapaTreino etapaTreino) {
        final View view = v;
        //Exibe mensagem de confirmação de exclusão
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmacao)
                .setMessage(R.string.confirmacao_exclusao)
                .setPositiveButton(R.string.excluir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Remove EtapaTreino
                        removerEtapaTreino(etapaTreino);
                        //Atualiza contador de duração do treino
                        ((TreinosCasdastroActivity) v.getContext()).atualizarTotalTreino();
                        Toast.makeText(getActivity(v), R.string.registro_excluido, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create()
                .show();
    }

    //Variável que define a requisição
    static final int REQUEST_UPDATE = 1;

    /**
     * Método do click do botão alterar
     * selecionado
     * @param holder
     * @param etapaTreino
     */
    private void clickBotaoAlterar(EtapaTreinoViewHolder holder, EtapaTreino etapaTreino) {
        holder.btnEditar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarEtapaTreino(v, etapaTreino);
            }
        });
    }

    /**
     * Método alterarEtapaTreino
     * Chama tela de cadastro para editar a etapa-treino
     * @param v
     * @param etapaTreino
     */
    private void alterarEtapaTreino(View v, EtapaTreino etapaTreino) {
        Activity activity = getActivity(v);
        Intent intent = null;
        intent = new Intent(activity, TreinosEtapaCasdastroActivity.class);
        //Manda parâmetros para tela de cadastro de etapa-treino
        intent.putExtra("etapatreino", etapaTreino);
        activity.startActivityForResult(intent, REQUEST_UPDATE);
    }

    /**
     * Função getItemCount
     * @return int
     */
    @Override
    public int getItemCount() {
        return etapaTreinos.size();
    }

    /**
     * Método removerEtapaTreino
     * Remove o etapaTreino da recyclerview e do objeto
     * @param etapaTreino
     */
    public void removerEtapaTreino(EtapaTreino etapaTreino) {
        int position = etapaTreinos.indexOf(etapaTreino);
        etapaTreinos.remove(position);

        for (int i = 0; i < etapaTreinos.size(); i++) {
            etapaTreinos.get(i).setOrdem(i + 1);
        }

        notifyItemRemoved(position);
        notifyDataSetChanged();
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
     * Classe EtapaTreinoViewHolder
     */
    class EtapaTreinoViewHolder extends RecyclerView.ViewHolder {

        TextView ordem;
        TextView duracaoEtapaTreino;
        TextView descansoEtapaTreino;
        TextView roundsEtapaTreino;

        ImageButton btnEditar;
        ImageButton btnExcluir;

        /**
         * Construtor
         * @param itemView
         */
        public EtapaTreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set views
            ordem = itemView.findViewById(R.id.textOrdemEtapaLista);
            duracaoEtapaTreino = itemView.findViewById(R.id.textTimerDuracaoEtapaLista);
            descansoEtapaTreino = itemView.findViewById(R.id.textTimerDescansoEtapaLista);
            roundsEtapaTreino = itemView.findViewById(R.id.textValorRoundsEtapaLista);
            btnEditar = itemView.findViewById(R.id.editEtapaItem);
            btnExcluir = itemView.findViewById(R.id.deleteEtapaItem);
        }

        /**
         * Método bind
         * Popula valores na tela
         * @param etapaTreino
         */
        public void bind(EtapaTreino etapaTreino) {
            ordem.setText(String.valueOf(etapaTreino.getOrdem()));
            duracaoEtapaTreino.setText(Utils.convertSecondsToMinutesSeconds(etapaTreino.getEtapa().getDuracao()));
            descansoEtapaTreino.setText(Utils.convertSecondsToMinutesSeconds(etapaTreino.getEtapa().getDescanso()));
            roundsEtapaTreino.setText(String.format("%02d", etapaTreino.getEtapa().getRound()));
        }
    }
}
