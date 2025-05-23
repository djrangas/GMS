import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.toedter.calendar.JDateChooser;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

public class Frame_5 {

	private JFrame frame;
	private JTable table;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTable table_1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_5 window = new Frame_5();
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
	public Frame_5() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 960, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		// Search Start
		JTextField searchField = new JTextField();
		searchField.setBounds(180, 20, 200, 30);
		frame.getContentPane().add(searchField);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		     
		    }
		});
		btnSearch.setBounds(377, 24, 85, 25);
		frame.getContentPane().add(btnSearch);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		       	        
		    }
		});
		btnRefresh.setBounds(454, 24, 85, 25);
		frame.getContentPane().add(btnRefresh);
		// Search End
		
		// Menu Start
		Menus menuPanel = new Menus(frame);
		frame.getContentPane().add(menuPanel);
		
		table = new JTable();
		table.setBounds(180, 62, 457, 128);
		frame.getContentPane().add(table);
		
		JCheckBox chckbxCardio = new JCheckBox("Cardio");
		chckbxCardio.setBounds(180, 466, 128, 23);
		frame.getContentPane().add(chckbxCardio);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Weight");
		chckbxNewCheckBox.setBounds(252, 466, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Other");
		chckbxNewCheckBox_1.setBounds(334, 466, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_1);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Repair");
		chckbxNewCheckBox_2.setBounds(252, 202, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_2);
		
		JCheckBox chckbxNewCheckBox_4 = new JCheckBox("Need");
		chckbxNewCheckBox_4.setBounds(180, 202, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_4);
		
		JButton btnEdit = new JButton("Add New");
		btnEdit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Equipment_Add equipmentAddWindow = new Equipment_Add();
		        equipmentAddWindow.getFrame().setVisible(true); // Open the Equipment_Add frame
		    }
		});
		btnEdit.setBounds(180, 20, 117, 29); // You can adjust the position based on your layout
		frame.getContentPane().add(btnEdit);

		btnEdit.setBounds(530, 465, 117, 29);
		frame.getContentPane().add(btnEdit);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "What we repair?", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(649, 60, 303, 165);
		frame.getContentPane().add(panel_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(127, 24, 170, 27);
		panel_1.add(comboBox_2);
		
		JLabel lblNewLabel_4 = new JLabel("Equipment Name");
		lblNewLabel_4.setBounds(6, 28, 115, 16);
		panel_1.add(lblNewLabel_4);
		
		JLabel lblCost_1 = new JLabel("Cost");
		lblCost_1.setBounds(6, 97, 61, 16);
		panel_1.add(lblCost_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(127, 92, 170, 26);
		panel_1.add(textField_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Quantity");
		lblNewLabel_2_1.setBounds(6, 64, 61, 16);
		panel_1.add(lblNewLabel_2_1);
		
		JButton btnNewButton_decrease_1 = new JButton("-");
		btnNewButton_decrease_1.setForeground(Color.RED);
		btnNewButton_decrease_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_decrease_1.setBounds(127, 63, 38, 16);
		panel_1.add(btnNewButton_decrease_1);
		
		textField_3 = new JTextField();
		textField_3.setText("0");
		textField_3.setBounds(177, 63, 38, 16);
		panel_1.add(textField_3);
		
		JButton btnNewButton_increase_1 = new JButton("+");
		btnNewButton_increase_1.setForeground(Color.RED);
		btnNewButton_increase_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_increase_1.setBounds(227, 64, 38, 16);
		panel_1.add(btnNewButton_increase_1);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(177, 130, 117, 29);
		panel_1.add(btnNewButton);
		
		table_1 = new JTable();
	    loadTableData();
        JScrollPane scrollPane1 = new JScrollPane(table_1);
        scrollPane1.setBounds(180, 238, 457, 216);
        frame.getContentPane().add(scrollPane1);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "What we need?", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1_1.setBounds(649, 253, 303, 168);
		frame.getContentPane().add(panel_1_1);
		
		JLabel lblNewLabel_4_1 = new JLabel("Equipment Name");
		lblNewLabel_4_1.setBounds(6, 31, 115, 16);
		panel_1_1.add(lblNewLabel_4_1);
		
		JLabel lblCost_1_1 = new JLabel("Cost");
		lblCost_1_1.setBounds(6, 100, 61, 16);
		panel_1_1.add(lblCost_1_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(127, 95, 170, 26);
		panel_1_1.add(textField);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(180, 133, 117, 29);
		panel_1_1.add(btnAdd);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Quantity");
		lblNewLabel_2_1_1.setBounds(6, 67, 61, 16);
		panel_1_1.add(lblNewLabel_2_1_1);
		
		JButton btnNewButton_decrease_1_1 = new JButton("-");
		btnNewButton_decrease_1_1.setForeground(Color.RED);
		btnNewButton_decrease_1_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_decrease_1_1.setBounds(127, 66, 38, 16);
		panel_1_1.add(btnNewButton_decrease_1_1);
		
		textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(177, 66, 38, 16);
		panel_1_1.add(textField_1);
		
		JButton btnNewButton_increase_1_1 = new JButton("+");
		btnNewButton_increase_1_1.setForeground(Color.RED);
		btnNewButton_increase_1_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_increase_1_1.setBounds(227, 67, 38, 16);
		panel_1_1.add(btnNewButton_increase_1_1);
		
		textField_4 = new JTextField();
		textField_4.setBounds(127, 26, 170, 26);
		panel_1_1.add(textField_4);
		textField_4.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Delete");
		btnNewButton_1.setBounds(520, 202, 117, 29);
		frame.getContentPane().add(btnNewButton_1);
		// Menu End
	}
	
	private void loadTableData() {
	    String selectQuery = "SELECT id, name, type, quantity, purchase_date, cost FROM Equipment";
	    
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        DefaultTableModel model = new DefaultTableModel(
	            new Object[][] {},
	            new String[] { "ID", "Name", "Type", "Quantity", "Purchase Date", "Cost" }  // Column headers
	        );
	        table_1.setModel(model);
	        model.setRowCount(0);  // Reset the table data before adding new rows
	        
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            String type = rs.getString("type");
	            int quantity = rs.getInt("quantity");
	            String purchaseDate = rs.getString("purchase_date");
	            double cost = rs.getDouble("cost");
	            
	            model.addRow(new Object[] { id, name, type, quantity, purchaseDate, cost });
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

}
