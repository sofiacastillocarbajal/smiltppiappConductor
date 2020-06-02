package pe.work.karique.smiltppiappconductor.util;

public class ValidationLogic {

    public static boolean checkPassword(String password) {
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch))
                numberFlag = true;
            else if (Character.isUpperCase(ch))
                capitalFlag = true;
            else if (Character.isLowerCase(ch))
                lowerCaseFlag = true;
        }
        return numberFlag && capitalFlag && lowerCaseFlag;
    }

}
