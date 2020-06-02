package pe.work.karique.smiltppiappconductor.repositories;

import java.util.ArrayList;
import java.util.List;
import pe.work.karique.smiltppiappconductor.models.UserTravelState;

public class UserTravelStateRepositories {
    private static UserTravelStateRepositories userTravelStateRepositories;
    private List<UserTravelState> userTravelStates;

    private UserTravelStateRepositories() {
        userTravelStates = new ArrayList<>();
        userTravelStates.add(new UserTravelState("1", "Espera", "3"));
        userTravelStates.add(new UserTravelState("2", "A bordo", "4"));
        userTravelStates.add(new UserTravelState("3", "Finalizado", "0"));
    }

    public static UserTravelStateRepositories getInstance(){
        if (userTravelStateRepositories == null){
            userTravelStateRepositories = new UserTravelStateRepositories();
        }
        return userTravelStateRepositories;
    }

    public List<UserTravelState> getUserTravelStates() {
        return userTravelStates;
    }
}
