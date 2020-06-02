package pe.work.karique.smiltppiappconductor.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.smiltppiappconductor.R;
import pe.work.karique.smiltppiappconductor.models.User;
import pe.work.karique.smiltppiappconductor.network.VamosUpcAPI;
import pe.work.karique.smiltppiappconductor.session.SessionManager;
import pe.work.karique.smiltppiappconductor.util.ValidationLogic;

import android.annotation.SuppressLint;
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

public class LoginActivity extends AppCompatActivity {

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

    CardView loginCardView;
    CardView registerCardView;
    TextView warningMessageTextView;
    CustomEditText dniCustomEditText;
    CustomEditText passwordCustomEditText;
    TextView loginTextView;
    ProgressBar loginProgressBar;
    TextView registerTextView;
    ProgressBar registerProgressBar;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.getUserIsLogged()){
            startConfigurateBusActivity();
        }

        hide();
        mContentView = findViewById(R.id.driverLoginFullscreenConstraintLayout);
        loginCardView = findViewById(R.id.loginCardView);
        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });
        registerCardView = findViewById(R.id.registerCardView);
        registerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(mainIntent);
            }
        });
        warningMessageTextView = findViewById(R.id.warningMessageTextView);
        dniCustomEditText = findViewById(R.id.dniCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);

        loginTextView = findViewById(R.id.loginTextView);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        registerTextView = findViewById(R.id.registerTextView);
        registerProgressBar = findViewById(R.id.registerProgressBar);
    }

    private void validateLogin() {
        warningMessageTextView.setVisibility(View.GONE);

        if (dniCustomEditText.getText().length() == 0 ||
                passwordCustomEditText.getText().length() == 0) {
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

        login();
    }

    private void login(){
        disableLoginButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("dni", dniCustomEditText.getText().toString());
            loginJsonObject.put("password", passwordCustomEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(VamosUpcAPI.LOGIN_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_LONG).show();
                        User userLogin = User.from(response);
                        sessionManager.saveUserSession(userLogin);
                        startConfigurateBusActivity();
                        enableLoginButton();
                    }

                    @Override
                    public void onError(ANError anError) {
                        warningMessageTextView.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() == 0){
                            warningMessageTextView.setText("Error de red");
                        }
                        else {
                            warningMessageTextView.setText("DNI/Contraseña incorrectas");
                        }

                        enableLoginButton();
                    }
                });
    }

    private void startConfigurateBusActivity(){
        Intent mainIntent = new Intent(LoginActivity.this, ConfigurateBusActivity.class);
        LoginActivity.this.startActivity(mainIntent);
        finish();
    }

    private void disableLoginButton(){
        loginCardView.setEnabled(false);
        loginTextView.setVisibility(View.INVISIBLE);
        loginProgressBar.setVisibility(View.VISIBLE);
        registerCardView.setVisibility(View.INVISIBLE);
    }
    private void enableLoginButton(){
        loginCardView.setEnabled(true);
        loginTextView.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.GONE);
        registerCardView.setVisibility(View.VISIBLE);
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
