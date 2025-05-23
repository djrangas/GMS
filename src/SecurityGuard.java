import javax.swing.*;

public class SecurityGuard {
    public static boolean checkAccess(JFrame frameToClose) {
        if (!SessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Access denied. Please login first.");
            if (frameToClose != null) {
                frameToClose.dispose();
            }
            new Main();
            return false;
        }
        return true;
    }
}