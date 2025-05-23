import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Menus extends JPanel {

    public Menus(JFrame parentFrame) {
        setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Menu", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        setBounds(10, 56, 161, 450);
        setLayout(null);

        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setBounds(6, 30, 149, 40);
        btnDashboard.setBackground(new Color(173, 216, 230));
        btnDashboard.addActionListener(e -> {
            new Frame_2();
            parentFrame.dispose();
        });
        add(btnDashboard);

        JButton btnMember = new JButton("Member Management");
        btnMember.setBounds(6, 80, 149, 40);
        btnMember.setBackground(new Color(230, 230, 250));
        btnMember.addActionListener(e -> {
            new Frame_3();
            parentFrame.dispose();
        });
        add(btnMember);

        JButton btnRFID = new JButton("Check-in");
        btnRFID.setBounds(6, 130, 149, 40);
        btnRFID.setBackground(new Color(230, 230, 250));
        btnRFID.addActionListener(e -> {
            new Frame_4();
            parentFrame.dispose();
        });
        add(btnRFID);
        
        JButton btnEquipments = new JButton("Equipments");
        btnEquipments.setBounds(6, 182, 149, 40);
        btnEquipments.setBackground(new Color(230, 230, 250)); // optional styling to match others
        btnEquipments.addActionListener(e -> {
            new Frame_5();
            parentFrame.dispose();
        });
        add(btnEquipments);

        JSeparator separator = new JSeparator();
        separator.setBounds(6, 382, 149, 10);
        add(separator);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(6, 404, 149, 40);
        btnLogout.setBackground(new Color(255, 182, 193));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Main();
                parentFrame.dispose();
            }
        });
        add(btnLogout);
        
        

    }
}
