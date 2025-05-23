import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.text.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class Frame_2 {

	private JFrame frame;
	private JTable table_1;
	private JTextField textField_p_5, textField;
	private JDateChooser dateChooser_p_1;
	private JCheckBox chckbxNewCheckBox, chckbxExpired;
	JLabel lblActive, lblExpired, lblRegular, lblPremium, lblVIP;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_2 window = new Frame_2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame_2() {
//		if (!SecurityGuard.checkAccess(frame)) return;
		initialize();
		loadTableData();
		loadKeyMetrics();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 960, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		 		
		// Menu Start
		Menus menuPanel = new Menus(frame);
		frame.getContentPane().add(menuPanel);
		// Menu End
		
		// Search Start
 		JTextField searchField = new JTextField();
 		searchField.setBounds(180, 20, 200, 30);
 		frame.getContentPane().add(searchField);

 		JButton btnSearch = new JButton("Search");
 		btnSearch.addActionListener(new ActionListener() {
 		    public void actionPerformed(ActionEvent e) {
 		    	String keyword = searchField.getText();
 		        boolean isActiveChecked = chckbxNewCheckBox.isSelected();
 		        boolean isExpiredChecked = chckbxExpired.isSelected();
 		        searchMember(keyword, isActiveChecked, isExpiredChecked);
 		    }
 		});
 		btnSearch.setBounds(377, 24, 85, 25);
 		frame.getContentPane().add(btnSearch);

 		JButton btnRefresh = new JButton("Refresh");
 		btnRefresh.addActionListener(new ActionListener() {
 		    public void actionPerformed(ActionEvent e) {
 		        searchField.setText("");
 		        chckbxNewCheckBox.setSelected(false);
 		        chckbxExpired.setSelected(false);
 		        loadTableData();
 		    }
 		});
 		btnRefresh.setBounds(454, 24, 85, 25);
 		frame.getContentPane().add(btnRefresh);
 		// Search End
 		
		// Table Start
	    table_1 = new JTable();
        JScrollPane scrollPane1 = new JScrollPane(table_1);
        scrollPane1.setBounds(179, 62, 321, 282);
        frame.getContentPane().add(scrollPane1);
	     // Table End
        
        // Member Form Start
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Member Form", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(512, 54, 264, 189);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Card ID");
		lblNewLabel.setBounds(6, 31, 96, 16);
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(114, 26, 130, 26);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("Check");
		btnNewButton_2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String cardId = textField.getText().trim();
		        if (cardId.isEmpty()) {
		            JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
		            return;
		        }
		        String checkQuery = "SELECT COUNT(*) FROM members WHERE card_id = ?";
		        try (Connection conn = DBConnection.getConnection();
		             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
		            checkStmt.setString(1, cardId);
		            ResultSet rs = checkStmt.executeQuery();
		            rs.next();
		            int count = rs.getInt(1);

		            if (count > 0) {
		                JOptionPane.showMessageDialog(frame, "It work!", "Success", JOptionPane.INFORMATION_MESSAGE);
		            } else {
		                JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
		            }

		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		btnNewButton_2.setBounds(162, 51, 85, 29);
		panel_1.add(btnNewButton_2);
		
		JLabel lblNewLabel_p_9 = new JLabel("Play Month");
		lblNewLabel_p_9.setBounds(6, 89, 120, 16);
		panel_1.add(lblNewLabel_p_9);
		JButton btnNewButton_increase = new JButton("+");
		btnNewButton_increase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                int value = Integer.parseInt(textField_p_5.getText());
		        textField_p_5.setText(String.valueOf(value + 1));
			}
		});
		btnNewButton_increase.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_increase.setForeground(Color.RED);
		btnNewButton_increase.setBounds(214, 90, 38, 16);
		panel_1.add(btnNewButton_increase);
		textField_p_5 = new JTextField();
		textField_p_5.setText("0");
		textField_p_5.setBounds(164, 89, 38, 16);
		panel_1.add(textField_p_5);
		JButton btnNewButton_decrease = new JButton("-");
		btnNewButton_decrease.setBounds(114, 89, 38, 16);
		panel_1.add(btnNewButton_decrease);
		btnNewButton_decrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                int value = Integer.parseInt(textField_p_5.getText());
				if (value > 0) {
		            textField_p_5.setText(String.valueOf(value - 1));
		        }
			}
		});
		btnNewButton_decrease.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_decrease.setForeground(Color.RED);
		
		JLabel lblNewLabel_3 = new JLabel("Start Date");
		lblNewLabel_3.setBounds(6, 118, 96, 16);
		panel_1.add(lblNewLabel_3);
		
		dateChooser_p_1 = new JDateChooser();
		dateChooser_p_1.setDateFormatString("yyyy-MM-dd");
		dateChooser_p_1.setBounds(114, 118, 120, 20);
		panel_1.add(dateChooser_p_1);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                addMembership();
                loadKeyMetrics();
			}
		});
		btnNewButton.setBounds(139, 150, 105, 29);
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	updateMembership();
		    	loadKeyMetrics();
            }
		});
		btnNewButton_1.setBounds(16, 150, 117, 29);
		panel_1.add(btnNewButton_1);
		// Member Form End
		
		// Filter Start
        chckbxNewCheckBox = new JCheckBox("Active");
        chckbxNewCheckBox.setBounds(179, 356, 71, 23);
        frame.getContentPane().add(chckbxNewCheckBox);
        
        chckbxExpired = new JCheckBox("Expired");
        chckbxExpired.setBounds(179, 376, 79, 23);
        frame.getContentPane().add(chckbxExpired);
        
        chckbxNewCheckBox.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean isActiveChecked = chckbxNewCheckBox.isSelected();
        		boolean isExpiredChecked = chckbxExpired.isSelected();
        		loadTableData(isActiveChecked, isExpiredChecked);
        	}
        });

        chckbxExpired.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isActiveChecked = chckbxNewCheckBox.isSelected();
                boolean isExpiredChecked = chckbxExpired.isSelected();
                loadTableData(isActiveChecked, isExpiredChecked);
            }
        });
        // Filter End
        
        // Key Metrics Start
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Key Metrics", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        panel_2.setBounds(788, 53, 161, 189);
        panel_2.setLayout(null);
        frame.getContentPane().add(panel_2);

        String[] categories = {"Active", "Expired", "Regular", "Premium", "VIP"};
        JLabel[] valueLabels = new JLabel[5];
        int yPosition = 33;

        for (int i = 0; i < categories.length; i++) {
            JLabel categoryLabel = new JLabel(categories[i]);
            categoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
            categoryLabel.setBounds(10, yPosition, 80, 20);
            panel_2.add(categoryLabel);

            JLabel valueLabel = new JLabel("0");
            valueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
            valueLabel.setForeground(new Color(0, 102, 204));
            valueLabel.setBounds(100, yPosition, 100, 20);
            panel_2.add(valueLabel);

            valueLabels[i] = valueLabel;

            yPosition += 28;

            if (i == 1) {
                JSeparator separator1 = new JSeparator();
                separator1.setBounds(10, yPosition, 240, 5);
                panel_2.add(separator1);
                yPosition += 10;
            }
        }

        lblActive = valueLabels[0];
        lblExpired = valueLabels[1];
        lblRegular = valueLabels[2];
        lblPremium = valueLabels[3];
        lblVIP = valueLabels[4];
        // Key Metrics End
        
	}
	
    private void loadTableData() {
	    String selectQuery = "SELECT card_id, start_date, play_month, exp_date, " +
	                         "CASE WHEN exp_date >= CURDATE() THEN 'Active' ELSE 'Expired' END AS status " +
	                         "FROM player_membership";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {
	    	DefaultTableModel model = new DefaultTableModel(
	    			new Object[][] {},
	    			new String[] { "Card ID", "Start Date", "Play Months", "Expiration Date", "Status" }  // Column headers
	        );

	        table_1.setModel(model);
	        model.setRowCount(0);  
	        while (rs.next()) {
	            String cardId = rs.getString("card_id");
	            String startDate = rs.getString("start_date");
	            int playMonth = rs.getInt("play_month");
	            String expDate = rs.getString("exp_date");
	            String status = rs.getString("status");
	            model.addRow(new Object[] { cardId, startDate, playMonth, expDate, status });
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

    private void searchMember(String cardId, boolean isActiveChecked, boolean isExpiredChecked) {
	    DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
	    tableModel.setRowCount(0);
	    String query = "SELECT card_id, start_date, play_month, exp_date, " +
	                   "CASE WHEN exp_date >= CURDATE() THEN 'Active' ELSE 'Expired' END AS status " +
	                   "FROM player_membership WHERE card_id LIKE ? ";
	    if (isActiveChecked && isExpiredChecked) {
	    } else if (isActiveChecked) {
	        query += "AND exp_date >= CURDATE()";
	    } else if (isExpiredChecked) {
	        query += "AND exp_date < CURDATE()";
	    }

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	    	pstmt.setString(1, "%" + cardId + "%");
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String id = rs.getString("card_id");
	            String startDate = rs.getString("start_date");
	            int playMonths = rs.getInt("play_month");
	            String expDate = rs.getString("exp_date");
	            String status = rs.getString("status");
	            tableModel.addRow(new Object[]{id, startDate, playMonths, expDate, status});
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

    private void loadTableData(boolean isActiveChecked, boolean isExpiredChecked) {
	    String selectQuery = "SELECT card_id, start_date, play_month, exp_date, " +
	                         "CASE WHEN exp_date >= CURDATE() THEN 'Active' ELSE 'Expired' END AS status " +
	                         "FROM player_membership WHERE 1=1";
	    
	    if (isActiveChecked && isExpiredChecked) {
	    } else if (isActiveChecked) {
	        selectQuery += " AND exp_date >= CURDATE()";
	    } else if (isExpiredChecked) {
	        selectQuery += " AND exp_date < CURDATE()";
	    }
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        DefaultTableModel model = new DefaultTableModel(
	            new Object[][] {},
	            new String[] { "Card ID", "Start Date", "Play Months", "Expiration Date", "Status" }  // Column headers
	        );
	        table_1.setModel(model);
	        model.setRowCount(0);  
	        while (rs.next()) {
	            String cardId = rs.getString("card_id");
	            String startDate = rs.getString("start_date");
	            int playMonth = rs.getInt("play_month");
	            String expDate = rs.getString("exp_date");
	            String status = rs.getString("status");
	            model.addRow(new Object[] { cardId, startDate, playMonth, expDate, status });
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

    private void addMembership() {
	    String cardId = textField.getText().trim();
	    int playMonths;
	    try {
	        playMonths = Integer.parseInt(textField_p_5.getText().trim());
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    if (cardId.isEmpty() || dateChooser_p_1.getDate() == null || playMonths <= 0) {
	    	JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String startDate = sdf.format(dateChooser_p_1.getDate());
	    String checkQuery = "SELECT COUNT(*) FROM members WHERE card_id = ?";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
	        checkStmt.setString(1, cardId);
	        ResultSet rs = checkStmt.executeQuery();
	        rs.next();
	        int count = rs.getInt(1);
	        if (count == 0) {
	            JOptionPane.showMessageDialog(frame, "Error: Card ID does not exist in members table!", "Error", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        String checkMembershipQuery = "SELECT COUNT(*) FROM player_membership WHERE card_id = ?";
	        PreparedStatement checkMembershipStmt = conn.prepareStatement(checkMembershipQuery);
	        checkMembershipStmt.setString(1, cardId);
	        ResultSet membershipRs = checkMembershipStmt.executeQuery();
	        membershipRs.next();
	        int membershipCount = membershipRs.getInt(1);

	        if (membershipCount > 0) {
	            JOptionPane.showMessageDialog(frame, "Card ID is already associated with an active membership.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    String insertQuery = "INSERT INTO player_membership (card_id, start_date, play_month, exp_date) " +
	                         "VALUES (?, ?, ?, DATE_ADD(?, INTERVAL ? MONTH))";
	    try (Connection conn = DBConnection.getConnection();
	    		PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
	    	pstmt.setString(1, cardId);
	        pstmt.setString(2, startDate);
	        pstmt.setInt(3, playMonths);
	        pstmt.setString(4, startDate);
	        pstmt.setInt(5, playMonths);
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            JOptionPane.showMessageDialog(frame, "Membership added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            loadTableData();  // Refresh table
	            textField.setText("");  
	            textField_p_5.setText("0");  
	            dateChooser_p_1.setDate(null);  
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
    
    private void updateMembership() {
    	String cardId = textField.getText().trim();
        int playMonths;

        try {
            playMonths = Integer.parseInt(textField_p_5.getText().trim());
        } catch (NumberFormatException ex) {
        	JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cardId.isEmpty() || dateChooser_p_1.getDate() == null || playMonths <= 0) {
        	JOptionPane.showMessageDialog(frame, "Something wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(dateChooser_p_1.getDate());

        // Check if the card_id exists in the members table
        String checkQuery = "SELECT COUNT(*) FROM members WHERE card_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, cardId);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                JOptionPane.showMessageDialog(frame, "Error: Card ID does not exist in members table!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updateQuery = "UPDATE player_membership SET start_date = ?, play_month = ?, exp_date = DATE_ADD(?, INTERVAL ? MONTH) " +
                             "WHERE card_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, startDate);
            pstmt.setInt(2, playMonths);
            pstmt.setString(3, startDate);
            pstmt.setInt(4, playMonths);
            pstmt.setString(5, cardId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "Membership updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                textField.setText("");  
                textField_p_5.setText("0");  
                dateChooser_p_1.setDate(null);  
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Membership update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadKeyMetrics() {
        try {
            Connection con = DBConnection.getConnection();

            // Active members
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM player_membership WHERE exp_date >= CURDATE()");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) lblActive.setText(String.valueOf(rs.getInt(1)));

            // Expired members
            ps = con.prepareStatement("SELECT COUNT(*) FROM player_membership WHERE exp_date < CURDATE()");
            rs = ps.executeQuery();
            if (rs.next()) lblExpired.setText(String.valueOf(rs.getInt(1)));

            // Regular
            ps = con.prepareStatement("SELECT COUNT(*) FROM members WHERE membership_type = 'Regular'");
            rs = ps.executeQuery();
            if (rs.next()) lblRegular.setText(String.valueOf(rs.getInt(1)));

            // Premium
            ps = con.prepareStatement("SELECT COUNT(*) FROM members WHERE membership_type = 'Premium'");
            rs = ps.executeQuery();
            if (rs.next()) lblPremium.setText(String.valueOf(rs.getInt(1)));

            // VIP
            ps = con.prepareStatement("SELECT COUNT(*) FROM members WHERE membership_type = 'VIP'");
            rs = ps.executeQuery();
            if (rs.next()) lblVIP.setText(String.valueOf(rs.getInt(1)));

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}