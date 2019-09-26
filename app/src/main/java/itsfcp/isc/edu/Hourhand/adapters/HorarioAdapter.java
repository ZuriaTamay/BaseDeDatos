package itsfcp.isc.edu.Hourhand.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import itsfcp.isc.edu.Hourhand.AddAgendaActivity;
import itsfcp.isc.edu.Hourhand.AgendaFragment;
import itsfcp.isc.edu.Hourhand.Database;
import itsfcp.isc.edu.Hourhand.R;
import itsfcp.isc.edu.Hourhand.ResumenActivity;
import itsfcp.isc.edu.Hourhand.SpinnerActivity;
import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.entidades.horario;
import itsfcp.isc.edu.Hourhand.helpers.ActivityHelper;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {

    private List<horario> horarios = new ArrayList<>();
    Activity horarioActivity;
    Database conn;
    String idhorario;

    public HorarioAdapter(List<horario> items, Activity horarioActivity){
        this.horarioActivity = horarioActivity;
    }
    public void updateItems(List<horario> items){
        for(horario horario : items){
            System.out.println(horario.getMateria());
        }
        horarios.clear();
        horarios.addAll(items);
        notifyDataSetChanged();
    }

    public void removeUItemOfList(int id){
        horarios.remove(id);
        notifyDataSetChanged();
    }

    @Override
    public HorarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhorario, parent, false);
        conn = new Database(parent.getContext(), "BDHorario", null, 1);
        return new HorarioAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HorarioAdapter.ViewHolder holder, final int position) {
        final horario horarioItem = horarios.get(position);
        holder.horarioss = horarioItem;
        idhorario = String.valueOf((horarioItem.getId_horario()));
        holder.Nombre_materia.setText(horarioItem.getMateria());
        holder.hora_inicio.setText(horarioItem.getInicio());
        holder.hora_fin.setText(horarioItem.getFin());
        holder.nom_pro.setText(horarioItem.getMaestro());
        holder.datoF.setText(horarioItem.getAula());
        holder.deletehorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                idhorario= String.valueOf((horarioItem.getId_horario()) );
                DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case  DialogInterface.BUTTON_POSITIVE:
                                deletedatoenhorario();
                                deletedatoenhorarioirregular();
                                removeUItemOfList(position);
                                showToast(v);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(horarioActivity);
                builder.setMessage("¿Deseas eliminar esta materia?")
                        .setPositiveButton("Si", dialogDeleteItem)
                        .setNegativeButton("No", dialogDeleteItem).show();
            }
        });


    }
    private void showToast(View v) {
        Toast.makeText(v.getContext(), "Se ha eliminado la materia", Toast.LENGTH_SHORT).show();
    }
    private void deletedatoenhorario() {
        Database conn = new Database(horarioActivity, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE horario SET visible = 0 WHERE id_horario='" + idhorario + "' ");
        db.close();
    }
    private void deletedatoenhorarioirregular() {
        Database conn = new Database(horarioActivity, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM horario_irregulares WHERE id_horario='" + idhorario + "' ");
        db.close();
    }
   /* private void Eliminarmateria(){
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={idhorario};
        db.delete(utilidades.TABLA_HORARIO, utilidades.CAMPO_ID_HORARIOS+"=?",parametros);
        db.close();
    }*/

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.idmateria)
        TextView Nombre_materia;
        @BindView(R.id.hora_inicio)
        TextView hora_inicio;
        @BindView(R.id.hora_fin)
        TextView hora_fin;
        @BindView(R.id.idprofesor)
        TextView nom_pro;
        @BindView(R.id.sb_remove_horario)
        ImageView deletehorario;
        @BindView(R.id.dato)
        TextView datoF;


        public horario horarioss;

        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this,view);
        }
        @Override
        public String toString() {
            return super.toString() + " '" +    Nombre_materia.getText() + " ";
        }

        @Override
        public boolean onLongClick(final View view) {
            DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case  DialogInterface.BUTTON_POSITIVE:
                            idhorario = String.valueOf((horarioss.getId_horario()));
                           // Eliminarmateria();
                            removeUItemOfList(getAdapterPosition());
                            showToast(view);
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(horarioActivity);
            builder.setMessage("Desea eliminar esta materia?")
                    .setPositiveButton("Si", dialogDeleteItem)
                    .setNegativeButton("No", dialogDeleteItem).show();


            return true;
        }
        private void showToast(View v) {
            Toast.makeText(v.getContext(), "Se ha eliminado esta materia", Toast.LENGTH_SHORT).show();
        }

    }

}
