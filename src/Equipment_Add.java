import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class Equipment_Add {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JDateChooser dateChooser_p_1;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Equipment_Add window = new Equipment_Add();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Equipment_Add() {
		initialize();
	}
	
	public JFrame getFrame() {
        return frame;
    }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 316, 274);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 303, 230);
		frame.getContentPane().add(panel);
		
		

		JComboBox<String> comboBox_1 = new JComboBox<>();
		comboBox_1.setBounds(127, 45, 170, 27);
		comboBox_1.addItem("Cardio");
		comboBox_1.addItem("Weight");
		comboBox_1.addItem("Other");
		comboBox_1.setSelectedItem("Other"); // Default
		panel.add(comboBox_1);

		
		JLabel lblNewLabel = new JLabel("Equipment Name");
		lblNewLabel.setBounds(6, 10, 115, 16);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Type");
		lblNewLabel_1.setBounds(6, 49, 61, 16);
		panel.add(lblNewLabel_1);
		
		JLabel lblCost = new JLabel("Cost");
		lblCost.setBounds(6, 158, 61, 16);
		panel.add(lblCost);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(127, 153, 170, 26);
		panel.add(textField);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField_2.getText().trim();
				String type = (String) comboBox_1.getSelectedItem();
				int quantity = Integer.parseInt(textField_1.getText().trim());
				String costText = textField.getText().trim();
				double cost = costText.isEmpty() ? 0.0 : Double.parseDouble(costText);
				java.util.Date utilDate = dateChooser_p_1.getDate();

				if (name.isEmpty() || utilDate == null) {
					JOptionPane.showMessageDialog(frame, "Please fill in Name and Purchase Date.");
					return;
				}

				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

				try (Connection conn = DBConnection.getConnection()) {
					String sql = "INSERT INTO Equipment (name, type, quantity, purchase_date, cost) VALUES (?, ?, ?, ?, ?)";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, name);
					stmt.setString(2, type);
					stmt.setInt(3, quantity);
					stmt.setDate(4, sqlDate);
					stmt.setDouble(5, cost);

					int result = stmt.executeUpdate();
					if (result > 0) {
						JOptionPane.showMessageDialog(frame, "Equipment Added!");
						textField_2.setText("");
						textField_1.setText("0");
						textField.setText("");
						dateChooser_p_1.setDate(null);
						comboBox_1.setSelectedItem("Other");
					} else {
						JOptionPane.showMessageDialog(frame, "Failed to add equipment.");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
				}
			}
		});

		btnAdd.setBounds(180, 195, 117, 29);
		panel.add(btnAdd);
		
		JLabel lblNewLabel_2 = new JLabel("Quantity");
		lblNewLabel_2.setBounds(6, 85, 61, 16);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Purchase Date");
		lblNewLabel_3.setBounds(6, 120, 115, 16);
		panel.add(lblNewLabel_3);
		
		dateChooser_p_1 = new JDateChooser();
		dateChooser_p_1.setDateFormatString("yyyy-MM-dd");
		dateChooser_p_1.setBounds(127, 118, 170, 23);
		panel.add(dateChooser_p_1);
		
		JButton btnNewButton_decrease = new JButton("-");
		btnNewButton_decrease.setForeground(Color.RED);
		btnNewButton_decrease.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_decrease.setBounds(127, 84, 38, 16);
		panel.add(btnNewButton_decrease);
		
		textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(177, 84, 38, 16);
		panel.add(textField_1);
		
		JButton btnNewButton_increase = new JButton("+");
		btnNewButton_increase.setForeground(Color.RED);
		btnNewButton_increase.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_increase.setBounds(227, 85, 38, 16);
		panel.add(btnNewButton_increase);
		
		textField_2 = new JTextField();
		textField_2.setBounds(127, 7, 170, 26);
		panel.add(textField_2);
		textField_2.setColumns(10);
	}

}
