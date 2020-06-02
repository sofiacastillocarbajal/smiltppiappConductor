package pe.work.karique.smiltppiappconductor.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.smiltppiappconductor.R;
import pe.work.karique.smiltppiappconductor.adapters.UserTravelStatesAdapter;
import pe.work.karique.smiltppiappconductor.models.UserTravelState;
import pe.work.karique.smiltppiappconductor.repositories.UserTravelStateRepositories;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BusDetailsActivity extends AppCompatActivity {

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

    private RecyclerView userTravelStatesRecyclerView;
    private UserTravelStatesAdapter userTravelStatesAdapter;
    private RecyclerView.LayoutManager userTravelStateLayoutManager;
    private List<UserTravelState> userTravelStates;

    AppCompatImageButton closeAppCompatImageButton;
    AppCompatImageView speakerAppCompatImageView;
    ProgressBar busProgressBar;
    TextView nextStopTextView;

    private int STEP_6 = 1;
    private int STEP_7 = 2;
    private int STEP_8 = 3;
    private int STEP_9 = 4;
    private int STEP = STEP_6;

    private String STEP_6_TEXT = "Próximo paradero. Puente primavera. 3 personas en espera, 4 personas a bordo, 0 personas finalizadas";
    private String STEP_7_TEXT = "Próximo paradero. Avenida Encalada con Avenida Primavera. 2 personas en espera, 5 personas a bordo, 2 personas finalizadas";
    private String STEP_8_TEXT = "Próximo paradero. UPC Monterrico. 0 personas en espera, 7 personas a bordo, 2 personas finalizadas";
    private String STEP_9_TEXT = "Ha llegado a la última parada.";
    String text = "";
    TextToSpeech tts;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        hide();
        mContentView = findViewById(R.id.busDetailsFullscreenConstraintLayout);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");

        nextStopTextView = findViewById(R.id.nextStopTextView);
        busProgressBar = findViewById(R.id.busProgressBar);
        logOutAppCompatImageButton = findViewById(R.id.logOutAppCompatImageButton);
        logOutAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userTravelStatesRecyclerView = findViewById(R.id.userTravelStatesRecyclerView);
        userTravelStates = UserTravelStateRepositories.getInstance().getUserTravelStates();
        userTravelStatesAdapter = new UserTravelStatesAdapter(userTravelStates);
        userTravelStatesAdapter.setOnUserClicked(new UserTravelStatesAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(UserTravelState userTravelState) {

            }
        });
        userTravelStateLayoutManager = new LinearLayoutManager(this);
        userTravelStatesRecyclerView.setAdapter(userTravelStatesAdapter);
        userTravelStatesRecyclerView.setLayoutManager(userTravelStateLayoutManager);

        speakerAppCompatImageView = findViewById(R.id.speakerAppCompatImageView);
        closeAppCompatImageButton = findViewById(R.id.closeAppCompatImageButton);
        closeAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(BusDetailsActivity.this, "El lenguaje de este dispositivo no está soportado", Toast.LENGTH_LONG).show();
                    } else {
                        STEP = STEP_6;
                        talk(STEP_6_TEXT);
                    }
                } else
                    Toast.makeText(BusDetailsActivity.this, "La inicialización falló", Toast.LENGTH_LONG).show();
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                new Thread(){
                    public void run(){
                        BusDetailsActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                busProgressBar.setVisibility(View.GONE);
                                speakerAppCompatImageView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();
            }
            @Override
            public void onDone(String s) {
                new Thread(){
                    public void run(){
                        BusDetailsActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                busProgressBar.setVisibility(View.VISIBLE);
                                if (STEP == STEP_9){
                                    finish();
                                }
                                else {
                                    speakerAppCompatImageView.setVisibility(View.GONE);
                                    waitAndStartTalk(7);
                                }
                            }
                        });
                    }
                }.start();
            }
            @Override
            public void onError(String s) {}
        });
    }

    private void waitAndStartTalk(int seconds){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                STEP++;
                if (STEP == STEP_7){
                    userTravelStates.get(0).setNumberOfUsers("2");
                    userTravelStates.get(1).setNumberOfUsers("5");
                    userTravelStates.get(2).setNumberOfUsers("2");
                    userTravelStatesAdapter.notifyDataSetChanged();
                    nextStopTextView.setText("Avenida Encalada con Avenida Primavera.");
                    talk(STEP_7_TEXT);
                }
                else if (STEP == STEP_8){
                    userTravelStates.get(0).setNumberOfUsers("0");
                    userTravelStates.get(1).setNumberOfUsers("7");
                    userTravelStates.get(2).setNumberOfUsers("2");
                    userTravelStatesAdapter.notifyDataSetChanged();
                    nextStopTextView.setText("UPC Monterrico.");
                    talk(STEP_8_TEXT);
                }
                else if (STEP == STEP_9){
                    talk(STEP_9_TEXT);
                }
            }
        }, seconds * 1000);
    }
    private void talk(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
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

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }
}
