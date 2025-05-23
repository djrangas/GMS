public class SessionManager {
    private static boolean loggedIn = false;

    public static void login() {
        loggedIn = true;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }
}
