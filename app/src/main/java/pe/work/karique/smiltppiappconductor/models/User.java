package pe.work.karique.smiltppiappconductor.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String name;
    private String dni;
    private String userTypeId;
    private String active;
    private String currentLat;
    private String currentLng;

    public static User from(JSONObject jsonObject){
        User user = new User();
        try {
            user.setId(jsonObject.getString("id"));
            user.setName(jsonObject.getString("name"));
            user.setDni(jsonObject.getString("dni"));
            user.setUserTypeId(jsonObject.getString("userTypeId"));
            user.setActive(jsonObject.getString("active"));
            user.setCurrentLat(jsonObject.getString("currentLat"));
            user.setCurrentLng(jsonObject.getString("currentLng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User() {
    }

    public User(String id, String name, String dni, String userTypeId, String active, String currentLat, String currentLng) {
        this.id = id;
        this.name = name;
        this.dni = dni;
        this.userTypeId = userTypeId;
        this.active = active;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }
}
