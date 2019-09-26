package itsfcp.isc.edu.Hourhand.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import itsfcp.isc.edu.Hourhand.Database;
import itsfcp.isc.edu.Hourhand.R;
import itsfcp.isc.edu.Hourhand.entidades.addhorario;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

public class AddhorarioAdapter extends RecyclerView.Adapter<AddhorarioAdapter.ViewHolder> {

    private List<addhorario> addhorarios = new ArrayList<>();
    Activity addhorarioActivity;
    Database conn;
    String idhorario;
    private Context mContext;

    public AddhorarioAdapter(List<addhorario> items, Activity horarioActivity){
        this.addhorarioActivity = horarioActivity;
    }
    public void updateItems(List<addhorario> items){
        for(addhorario horario : items){
            System.out.println(horario.getMateria());
        }
        addhorarios.clear();
        addhorarios.addAll(items);
        notifyDataSetChanged();
    }

    public void removeUItemOfList(int id){
        addhorarios.remove(id);
        notifyDataSetChanged();
    }

    @Override
    public AddhorarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemaddhorario, parent, false);
        conn = new Database(parent.getContext(), "BDHorario", null, 1);
        return new AddhorarioAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AddhorarioAdapter.ViewHolder holder, final int position) {
        final addhorario horarioItem = addhorarios.get(position);
        holder.addhorarioss = horarioItem;
        idhorario= String.valueOf((horarioItem.getId_horario()) );
        holder.Nombre_materia.setText(horarioItem.getMateria());
        holder.nom_pro.setText(horarioItem.getMaestro());
        holder.addhorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                idhorario= String.valueOf((horarioItem.getId_horario()) );
                DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case  DialogInterface.BUTTON_POSITIVE:
                                Agregardatoenhorario();
                                adddatoenhorario();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(addhorarioActivity);
                builder.setMessage("¿Deseas agregar esta materia?")
                        .setPositiveButton("Si", dialogDeleteItem)
                        .setNegativeButton("No", dialogDeleteItem).show();
            }
        });
    }
    private void showToast(View v) {
        Toast.makeText(v.getContext(), "Se ha agregado la materia", Toast.LENGTH_SHORT).show();
    }
    private void Agregardatoenhorario() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={idhorario};
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_ID_HORARIO,idhorario);
        db.insert(utilidades.TABLA_HORARIO_IRREGULARES,utilidades.CAMPO_ID_HORARIO,values);
        db.close();
    }
    private void adddatoenhorario() {
        Database conn = new Database(addhorarioActivity, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE horario SET visible = 1 WHERE id_horario='" + idhorario + "' ");
        db.close();
    }

    @Override
    public int getItemCount() {
        return addhorarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.idmateria)
        TextView Nombre_materia;
        @BindView(R.id.idprofesor)
        TextView nom_pro;
        @BindView(R.id.sb_remove_horario)
        ImageView addhorario;

        public addhorario addhorarioss;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this,view);
        }
        @Override
        public String toString() {
            return super.toString() + " '" +    Nombre_materia.getText() + " ";
        }

        @Override
        public void onClick(View view) {

        }

    }

}

