package itsfcp.isc.edu.Hourhand;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class ActivityHorario extends AppCompatActivity {
    FloatingActionButton fab;
    ImageButton cambiar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        //Enlazar al boton
        fab=(FloatingActionButton) findViewById(R.id.btn_flotante_horario);
        cambiar=(ImageButton) findViewById(R.id.btn_rotar_horario);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddHorarioActivity.class);
                startActivity(intent);
            }
        });

        /*cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente = new Intent (ActivityHorario.this, HorarioPorDiaActivity.class);
                startActivity(siguiente);
            }
        });*/


    }
}
