package pe.work.karique.smiltppiappconductor.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import pe.work.karique.smiltppiappconductor.R;
import pe.work.karique.smiltppiappconductor.session.SessionManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class ConfigurateBusActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    AppCompatImageButton logOutAppCompatImageButton;
    CardView loginCardView;

    MaterialSpinner routeMaterialSpinner;
    MaterialSpinner busNameMaterialSpinner;
    SwitchCompat activateSwitchCompat;
    TextView warningMessageTextView;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurate_bus);
        sessionManager = SessionManager.getInstance(this);

        hide();
        mContentView = findViewById(R.id.configurateBusFullscreenConstraintLayout);
        logOutAppCompatImageButton = findViewById(R.id.logOutAppCompatImageButton);
        logOutAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
            }
        });

        routeMaterialSpinner = findViewById(R.id.routeMaterialSpinner);
        routeMaterialSpinner.setItems("Ruta 1 (Villa - Monterrico)");
        routeMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });

        busNameMaterialSpinner = findViewById(R.id.busNameMaterialSpinner);
        busNameMaterialSpinner.setItems("UPC");
        busNameMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });

        activateSwitchCompat = findViewById(R.id.activateSwitchCompat);
        warningMessageTextView = findViewById(R.id.warningMessageTextView);

        loginCardView = findViewById(R.id.loginCardView);
        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBusDetailsActivity();
            }
        });

        hide();
    }

    private void showBusDetailsActivity(){
        if (!activateSwitchCompat.isChecked()){
            warningMessageTextView.setVisibility(View.VISIBLE);
            warningMessageTextView.setText("Activar la opción Visible para pasajeros");
        }
        else {
            Intent mainIntent = new Intent(ConfigurateBusActivity.this, BusDetailsActivity.class);
            ConfigurateBusActivity.this.startActivity(mainIntent);
        }
    }
    private void showLogOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea cerrar sesión?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void logOut(){
        sessionManager.deleteUserSession();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    protected void onResume() {
        hide();
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
