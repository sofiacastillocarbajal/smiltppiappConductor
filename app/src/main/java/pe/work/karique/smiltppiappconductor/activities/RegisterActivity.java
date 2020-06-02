package pe.work.karique.smiltppiappconductor.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.smiltppiappconductor.R;
import pe.work.karique.smiltppiappconductor.network.VamosUpcAPI;
import pe.work.karique.smiltppiappconductor.util.ValidationLogic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

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

    CustomEditText dniCustomEditText;
    CustomEditText nameCustomEditText;
    CustomEditText passwordCustomEditText;
    CustomEditText repeatPasswordCustomEditText;
    CardView registerCardView;
    AppCompatImageButton closeAppCompatImageButton;
    TextView warningMessageTextView;
    TextView registerTextView;
    ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hide();
        mContentView = findViewById(R.id.registerFullscreenConstraintLayout);
        dniCustomEditText = findViewById(R.id.dniCustomEditText);
        nameCustomEditText = findViewById(R.id.nameCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);
        repeatPasswordCustomEditText = findViewById(R.id.repeatPasswordCustomEditText);
        registerCardView = findViewById(R.id.registerCardView);
        registerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        closeAppCompatImageButton = findViewById(R.id.closeAppCompatImageButton);
        closeAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        warningMessageTextView = findViewById(R.id.warningMessageTextView);
        registerTextView = findViewById(R.id.registerTextView);
        registerProgressBar = findViewById(R.id.registerProgressBar);
    }

    private void register(){
        warningMessageTextView.setVisibility(View.GONE);

        if (dniCustomEditText.getText().length() == 0 ||
                nameCustomEditText.getText().length() == 0 ||
                passwordCustomEditText.getText().length() == 0 ||
                repeatPasswordCustomEditText.getText().length() == 0){
            warningMessageTextView.setVisibility(View.VISIBLE);
            warningMessageTextView.setText("No deje ningún campo vacío");
            return;
        }

        if (passwordCustomEditText.getText().toString().length() <= 7 ||
                !ValidationLogic.checkPassword(passwordCustomEditText.getText().toString())) {
            warningMessageTextView.setVisibility(View.VISIBLE);
            warningMessageTextView.setText("Ingrese una contraseña valida (Mas de 7 caracteres, Una mayuscula, una minuscula y un numero)");
            return;
        }

        if (!passwordCustomEditText.getText().toString().equals(repeatPasswordCustomEditText.getText().toString())){
            warningMessageTextView.setVisibility(View.VISIBLE);
            warningMessageTextView.setText("Las contraseñas ingresadas no son iguales");
            return;
        }

        registerDriver();
    }

    private void registerDriver() {
        desableRegisterButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("name", nameCustomEditText.getText().toString());
            loginJsonObject.put("dni", dniCustomEditText.getText().toString());
            loginJsonObject.put("userTypeId", 1);
            loginJsonObject.put("password", passwordCustomEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(VamosUpcAPI.USERS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Registrado correctamente, ya puede iniciar sesión", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 409){
                            warningMessageTextView.setVisibility(View.VISIBLE);
                            warningMessageTextView.setText("El dni de usuario que ingresó ya existe");
                            enableRegisterButton();
                        }
                        else {
                            warningMessageTextView.setVisibility(View.VISIBLE);
                            warningMessageTextView.setText("Hubo un error al crear el usuario");
                            enableRegisterButton();
                        }
                    }
                });
    }

    private void desableRegisterButton(){
        registerCardView.setEnabled(false);
        registerTextView.setVisibility(View.INVISIBLE);
        registerProgressBar.setVisibility(View.VISIBLE);
    }

    private void enableRegisterButton(){
        registerCardView.setEnabled(true);
        registerTextView.setVisibility(View.VISIBLE);
        registerProgressBar.setVisibility(View.GONE);
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

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
}
