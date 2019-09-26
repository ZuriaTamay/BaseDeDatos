package itsfcp.isc.edu.Hourhand;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HorarioPorDiaActivity extends AppCompatActivity {

    ImageButton normal = (ImageButton) findViewById(R.id.btn_editar);
    FloatingActionButton fab;
    ImageButton edi = (ImageButton) findViewById(R.id.btn_editar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_por_dia);
        normal=(ImageButton) findViewById(R.id.btn_horario_normal);
        fab=(FloatingActionButton) findViewById(R.id.btn_flotante_horario);
        edi=(ImageButton) findViewById(R.id.btn_editar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddHorarioActivity.class);
                startActivity(intent);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente = new Intent (HorarioPorDiaActivity.this, ActivityHorario.class);
                startActivity(siguiente);
            }
        });

        edi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente = new Intent(HorarioPorDiaActivity.this, EditHorarioActivity.class);
                startActivity(siguiente);
            }
        });

    }
}
