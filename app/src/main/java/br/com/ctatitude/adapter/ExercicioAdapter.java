package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.model.Exercicio;
import br.com.ctatitude.view.ExerciciosCasdastroActivity;
import br.com.ctatitude.view.ExerciciosListaActivity;

public class ExercicioAdapter extends RecyclerView.Adapter<ExercicioAdapter.ExercicioViewHolder> implements Filterable {
    private List<Exercicio> exercicios;
    private List<Exercicio> exerciciosListaTotal;

    public ExercicioAdapter(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
        this.exerciciosListaTotal = new ArrayList<>(exercicios);
    }

    @NonNull
    @Override
    public ExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_exercicio, parent, false);

        return new ExercicioViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ExercicioViewHolder holder, int position) {
        Exercicio exercicio = exercicios.get(position);
        holder.bind(exercicio);

        holder.btnEditar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = null;
                intent = new Intent(activity,
                        ExerciciosCasdastroActivity.class);
                intent.putExtra("exercicio", exercicio);
                activity.startActivity(intent);
            }
        });

        holder.btnExcluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este registro?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Exercicio exercicio = exercicios.get(position);
                                ExercicioDAO dao = new ExercicioDAO(view.getContext());
                                boolean sucesso = dao.excluir(exercicio);
                                if (sucesso) {
                                    removerExercicio(exercicio);
                                    Toast.makeText(getActivity(v), "Registro excluído!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(v), "Erro ao excluir registro!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    public void removerExercicio(Exercicio exercicio) {
        int position = exercicios.indexOf(exercicio);
        exercicios.remove(position);
        notifyItemRemoved(position);
    }

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

    @Override
    public Filter getFilter() {
        return filter;
    }
    
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exercicio> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(exerciciosListaTotal);
            }else{
                for(Exercicio exercicio : exerciciosListaTotal){
                    if(exercicio.getNome().toLowerCase().contains(constraint.toString().toLowerCase())){
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

    class ExercicioViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        ImageButton btnEditar;
        ImageButton btnExcluir;

        public ExercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeExercicioLista);
            btnEditar = (ImageButton) itemView.findViewById(R.id.editExercicioItem);
            btnExcluir = (ImageButton) itemView.findViewById(R.id.deleteExercicioItem);
        }

        public void bind(Exercicio exercicio) {
            nome.setText(exercicio.getNome());
        }
    }
}
