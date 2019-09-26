package itsfcp.isc.edu.Hourhand.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import itsfcp.isc.edu.Hourhand.helpers.ActivityHelper;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private List<agenda> agendas = new ArrayList<>();
    Activity agendaActivity;
    Database conn;
    String idagend;
    agenda idposicion;



    public AgendaAdapter(List<agenda> items, Activity agendaActivity){
        this.agendaActivity = agendaActivity;
        this.agendas = items;
    }
    public void updateItems(List<agenda> items){
        for(agenda agenda : items){
            System.out.println(agenda.getTitulo_agenda());
        }
        agendas.clear();
        agendas.addAll(items);
        notifyDataSetChanged();
    }

    public void removeUItemOfList(){
        agendas.remove(idposicion);
        notifyDataSetChanged();
    }

    @Override
    public AgendaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemagenda, parent, false);
        return new AgendaAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final agenda agendaItem = agendas.get(position);
        holder.agend = agendaItem;
        idagend = String.valueOf((agendaItem.getId_agenda()));
        idposicion = agendas.get(position);
        holder.titulo_agenda.setText(agendaItem.getTitulo_agenda());
        holder.fecha_agenda.setText(agendaItem.getFecha_agenda());
        holder.hora_agenda.setText(agendaItem.getHora_agenda());
        holder.decrip_agenda.setText(agendaItem.getDecrip_agenda());
        holder.deletee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                idagend = String.valueOf((agendaItem.getId_agenda()));
                if(isChecked){
                    idposicion = agendas.get(position);
                    System.out.println("Markar: "+agendaItem.getId_agenda());
                    markForDelete(1);
                }else{
                    markForDelete(0);
                }
            }
        });
    }
    public void markForDelete(Integer check){
        Integer val;
        Database conn = new Database(agendaActivity, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE agenda SET check_agenda = " + check + " WHERE id_agenda='" + idagend + "' ");
        db.close();
}
    public void deleteItems(){
        Database conn = new Database(agendaActivity, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM  agenda  WHERE check_agenda = 1 ");
        removeUItemOfList();
        notifyDataSetChanged();
        db.close();
    }


    @Override
    public int getItemCount() {
        return agendas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.sb_agenda_name)
        TextView titulo_agenda;
        @BindView(R.id.sb_agenda_fecha)
        TextView fecha_agenda;
        @BindView(R.id.sb_agenda_hora)
        TextView hora_agenda;
        @BindView(R.id.sb_agenda_description)
        TextView decrip_agenda;
        @BindView(R.id.sb_check_agenda)
        CheckBox deletee;

        public agenda agend;



        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this,view);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + titulo_agenda.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            agenda.setInstance(agend);
            System.out.println("AGENDA ID");
            System.out.println(agend.getId_agenda());
            ActivityHelper.launchNewActivity(agendaActivity, AddAgendaActivity.class, false );

        }

    }


}
