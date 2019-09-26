package itsfcp.isc.edu.Hourhand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class EditHorarioActivity extends AppCompatActivity {
    ImageButton atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_horario);

        //Conexion de spinners
        Spinner sp_edit_semestre;
        sp_edit_semestre = findViewById(R.id.sp_edit_semestre);
        Spinner sp_edit_grupo = findViewById(R.id.sp_edit_grupo);
        Spinner sp_edit_materia = findViewById(R.id.sp_edit_materia);
        atras = findViewById(R.id.btn_atras);

        // Semestre
        String[] semestre = {"Semestre", "I","II","III","IV","V","VI","VII","VIII"};
        sp_edit_semestre.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, semestre));

        // Grupo
        String[] grupo = {"Grupo", "A","B"};
        sp_edit_grupo.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, grupo));

        // Materia
        String[] materia = {"Materia","ciencias","Matematicas"};
        sp_edit_materia.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, materia));

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente = new Intent (EditHorarioActivity.this, ActivityHorario.class);
                startActivity(siguiente);
            }
        });


    }

}
