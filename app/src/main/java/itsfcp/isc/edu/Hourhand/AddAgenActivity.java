package itsfcp.isc.edu.Hourhand;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class AddAgenActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_PET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_resumen1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        AddAgendaFragment addEditLawyerFragment = (AddAgendaFragment)
                getSupportFragmentManager().findFragmentById(R.id.escenario1);
        if (addEditLawyerFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.escenario1, addEditLawyerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
