package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.dao.ExercicioEtapaDAO;
import br.com.ctatitude.model.Exercicio;
import br.com.ctatitude.view.ExerciciosCasdastroActivity;

/**
 * Classe ExercicioAdapter
 * Adaptador para lista de exercícios
 */
public class ExercicioAdapter extends RecyclerView.Adapter<ExercicioAdapter.ExercicioViewHolder> implements Filterable {
    private final List<Exercicio> exercicios;
    private List<Exercicio> exerciciosListaTotal;

    /**
     * Construtor
     * @param exercicios
     */
    public ExercicioAdapter(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
        this.exerciciosListaTotal = new ArrayList<>(exercicios);
    }

    /**
     * Função ExercicioViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_exercicio, parent, false);

        return new ExercicioViewHolder(view);
    }

    /**
     * Método onBindViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ExercicioViewHolder holder, int position) {
        Exercicio exercicio = exercicios.get(position);

        //Preenche campos do item da lista
        holder.bind(exercicio);

        //Listener para botões Alterar e Excluir
        clickBotaoAlterar(holder, exercicio);
        clickBotaoExcluir(holder, exercicio);
    }

    /**
     * Método para click do Botão excluir
     * @param holder
     * @param exercicio
     */
    private void clickBotaoExcluir(ExercicioViewHolder holder, Exercicio exercicio) {
        holder.btnExcluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Exclui exercício
                excluirExercicio(v, exercicio);
            }
        });
    }

    /**
     * Método para excluir exercício da base e da lista
     * Realiza exclusão quando clicar o botão excluir
     * Antes realiza confirmação
     * @param v
     * @param exercicio
     */
    private void excluirExercicio(View v, Exercicio exercicio) {
        final View view = v;

        //Exibe mensagem de confirmação de exclusão
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmacao)
                .setMessage(R.string.confirmacao_exclusao)
                .setPositiveButton(R.string.excluir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Verifica se o exercício está cadastrado em algum treino
                        //Caso esteja, não é possível remover. Notificar usuário
                        ExercicioEtapaDAO exercicioEtapaDAO = new ExercicioEtapaDAO(view.getContext());
                        if(exercicioEtapaDAO.contar(exercicio) > 0) {
                            Toast.makeText(getActivity(v), R.string.exercicio_nao_pode_ser_excluido, Toast.LENGTH_LONG).show();
                        }else {
                            //Realiza exclusão do exercício
                            ExercicioDAO dao = new ExercicioDAO(view.getContext());
                            boolean sucesso = dao.excluir(exercicio);
                            if (sucesso) {
                                //Chama método para atualizar a recyclerview e o objeto
                                removerExercicio(exercicio);
                                Toast.makeText(getActivity(v), R.string.registro_excluido, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(v), R.string.erro_exclusao_registro, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create()
                .show();
    }

    /**
     * Método do click do botão alterar
     * selecionado
     * @param holder
     * @param exercicio
     */
    private void clickBotaoAlterar(ExercicioViewHolder holder, Exercicio exercicio) {
        holder.btnEditar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarExercicio(v, exercicio);
            }
        });
    }

    /**
     * Método alterarExercicio
     * Chama tela de cadastro para editar o exercício
     * @param v
     * @param exercicio
     */
    private void alterarExercicio(View v, Exercicio exercicio) {
        Activity activity = getActivity(v);
        Intent intent = null;
        intent = new Intent(activity,
                ExerciciosCasdastroActivity.class);
        //Manda o exercício selecionado como parametro para tela de cadastro
        intent.putExtra("exercicio", exercicio);
        activity.startActivity(intent);
    }

    /**
     * Função getItemCount
     * @return int
     */
    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    /**
     * Método removerExercicio
     * Remove o exercício da recyclerview e do objeto
     * @param exercicio
     */
    public void removerExercicio(Exercicio exercicio) {
        int position = exercicios.indexOf(exercicio);
        exercicios.remove(position);
        exerciciosListaTotal.clear();
        exerciciosListaTotal = new ArrayList<>(exercicios);
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
     * Função getFilter
     * @return
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Filtra exercício na lista
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exercicio> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(exerciciosListaTotal);
            } else {
                for (Exercicio exercicio : exerciciosListaTotal) {
                    if (exercicio.getNome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(exercicio);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exercicios.clear();
            exercicios.addAll((Collection<? extends Exercicio>) results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Classe ExercicioViewHolder
     */
    class ExercicioViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        ImageButton btnEditar;
        ImageButton btnExcluir;

        /**
         * Construtor
         * @param itemView
         */
        public ExercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set views
            nome = itemView.findViewById(R.id.textNomeExercicioLista);
            btnEditar = itemView.findViewById(R.id.editExercicioItem);
            btnExcluir = itemView.findViewById(R.id.deleteExercicioItem);
        }

        /**
         * Método bind
         * Popula valores na tela
         * @param exercicio
         */
        public void bind(Exercicio exercicio) {
            nome.setText(exercicio.getNome());
        }
    }
}
