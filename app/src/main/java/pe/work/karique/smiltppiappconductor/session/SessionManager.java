package pe.work.karique.smiltppiappconductor.session;

import android.content.Context;
import android.content.SharedPreferences;

import pe.work.karique.smiltppiappconductor.models.User;

public class SessionManager {

    private static final String PREFERENCE_NAME = "pe.work.karique.smiltppiappconductor";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String USER_LOGIN = "userLogin";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DNI = "dni";
    private static final String USERTYPEID = "userTypeId";
    private static final String LAT = "lat";
    private static final String LNG = "lng";

    private static SessionManager sessionManager;
    public static SessionManager getInstance(Context context){
        if (sessionManager == null){
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    private SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void setUserIsLogged(boolean userLogin) { editor.putBoolean(USER_LOGIN, userLogin).commit(); }
    public boolean getUserIsLogged() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    private void setid(String id) { editor.putString(ID, id).commit(); }
    public String getid() { return preferences.getString(ID, ""); }

    private void setname(String name) { editor.putString(NAME, name).commit(); }
    public String getname() { return preferences.getString(NAME, ""); }

    private void setdni(String username) { editor.putString(DNI, username).commit(); }
    public String getdni() { return preferences.getString(DNI, ""); }

    private void setusertypeid(String usertypeid) { editor.putString(USERTYPEID, usertypeid).commit(); }
    public String getusertypeid() { return preferences.getString(USERTYPEID, ""); }

    private void setlat(String email) { editor.putString(LAT, email).commit(); }
    public String getlat() { return preferences.getString(LAT, ""); }

    private void setlng(String speciality) { editor.putString(LNG, speciality).commit(); }
    public String getlng() { return preferences.getString(LNG, ""); }

    public void saveUserSession(User userLogin){
        setUserIsLogged(true);
        setid(userLogin.getId());
        setname(userLogin.getName());
        setdni(userLogin.getDni());
        setusertypeid(userLogin.getUserTypeId());
        setlat(userLogin.getCurrentLat());
        setlng(userLogin.getCurrentLng());
    }

    public void deleteUserSession(){
        setUserIsLogged(false);
        setid("");
        setname("");
        setdni("");
        setusertypeid("");
        setlat("");
        setlng("");
    }

}
