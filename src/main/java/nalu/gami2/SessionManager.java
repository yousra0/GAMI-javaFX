package nalu.gami2;

import entity.user;

public class SessionManager {
    private static user currentUser;

    public static user getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(user user) {
        currentUser = user;
    }

    public static void clearCurrentUser() {
        currentUser = null;
    }
    public static boolean isAdmin() {
        return currentUser != null && "[\"ROLE_ADMIN\"]".equals(currentUser.getRoles());
    }

    // Vous pouvez ajouter d'autres méthodes utiles pour la gestion de la session ici
}
