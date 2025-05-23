import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class Frame_3 {
    
	private JFrame frame;
	private JTable table;
	private JCheckBox chckbxNewCheckBox, chckbxNewCheckBox_1, chckbxNewCheckBox_2;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_3 window = new Frame_3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame_3() {
//		if (!SecurityGuard.checkAccess(frame)) return;
		initialize();
		loadTableData();
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
		        searchMember(keyword);
		    }
		});
		btnSearch.setBounds(377, 24, 85, 25);
		frame.getContentPane().add(btnSearch);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        searchField.setText("");
		        chckbxNewCheckBox.setSelected(false);
		        chckbxNewCheckBox_1.setSelected(false); 
		        chckbxNewCheckBox_2.setSelected(false); 
		        loadTableData();		        
		    }
		});
		btnRefresh.setBounds(454, 24, 85, 25);
		frame.getContentPane().add(btnRefresh);
		// Search End
				
		// Table Start
		table = new JTable();
		JScrollPane scrollPane1 = new JScrollPane(table);
		scrollPane1.setBounds(179, 62, 631, 347);
		frame.getContentPane().add(scrollPane1);
		// Table End
		
		// View Detail, Delete, Edit Start
		JButton btnNewButton_6 = new JButton("View Detail");
		btnNewButton_6.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow != -1) {
		            String cardID = table.getValueAt(selectedRow, 1).toString();

		            String query = "SELECT * FROM members WHERE card_id=?";
		            try (Connection conn = DBConnection.getConnection();
		                 PreparedStatement stmt = conn.prepareStatement(query)) {

		                stmt.setString(1, cardID);
		                ResultSet rs = stmt.executeQuery();

		                if (rs.next()) {
		                    String name = rs.getString("name");
		                    String email = rs.getString("email");
		                    String phone = rs.getString("phone");
		                    String dateOfBirth = rs.getString("date_of_birth");
		                    String gender = rs.getString("gender");
		                    String address = rs.getString("address");
		                    String membershipType = rs.getString("membership_type");
		                    String createDate = rs.getString("create_date");

		                    JOptionPane.showMessageDialog(frame,
		                        "Name: " + name + "\n" +
		                        "Email: " + email + "\n" +
		                        "Phone: " + phone + "\n" +
		                        "Date of Birth: " + dateOfBirth + "\n" +
		                        "Gender: " + gender + "\n" +
		                        "Address: " + address + "\n" +
		                        "Membership Type: " + membershipType + "\n" +
		                        "Create Date: " + createDate + "\n" +
		                        "Card ID: " + cardID,
		                        "Member Details", JOptionPane.INFORMATION_MESSAGE);
		                } else {
		                    JOptionPane.showMessageDialog(frame, "No details found for Card ID: " + cardID, "Error", JOptionPane.ERROR_MESSAGE);
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(frame, "Please select a row to view details.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
		btnNewButton_6.setBounds(180, 424, 106, 29);
		frame.getContentPane().add(btnNewButton_6);
		
		JButton btnNewButton_p_1_1 = new JButton("Delete");
		btnNewButton_p_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    int selectedRow = table.getSelectedRow();
			    if (selectedRow != -1) {
			        String cardID = table.getValueAt(selectedRow, 1).toString();

			        int confirmation = JOptionPane.showConfirmDialog(frame, 
			            "Are you sure you want to delete this member?", "Delete Confirmation",
			            JOptionPane.YES_NO_OPTION);

			        if (confirmation == JOptionPane.YES_OPTION) {
			            try (Connection conn = DBConnection.getConnection()) {

			                // Step 1: Delete from player_membership first
			                String deletePlayerMembership = "DELETE FROM player_membership WHERE card_id=?";
			                try (PreparedStatement stmt1 = conn.prepareStatement(deletePlayerMembership)) {
			                    stmt1.setString(1, cardID);
			                    stmt1.executeUpdate();
			                }

			                // Step 2: Delete from members
			                String deleteMember = "DELETE FROM members WHERE card_id=?";
			                try (PreparedStatement stmt2 = conn.prepareStatement(deleteMember)) {
			                    stmt2.setString(1, cardID);
			                    int rowsAffected = stmt2.executeUpdate();

			                    if (rowsAffected > 0) {
			                        ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
			                        JOptionPane.showMessageDialog(frame, "Member deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			                    } else {
			                        JOptionPane.showMessageDialog(frame, "Member deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
			                    }
			                }

			            } catch (SQLException ex) {
			                ex.printStackTrace();
			                JOptionPane.showMessageDialog(frame, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
			            }
			        }
			    } else {
			        JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
			    }
			}

		});
		btnNewButton_p_1_1.setBounds(288, 424, 92, 29);
		frame.getContentPane().add(btnNewButton_p_1_1);
		
		JButton btnNewButton_p_1_1_1 = new JButton("Edit");
		btnNewButton_p_1_1_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow != -1) {
		            // Assuming Card ID is in column index 1 like in your View Detail button
		            String cardID = table.getValueAt(selectedRow, 1).toString();

		            String query = "SELECT * FROM members WHERE card_id=?";
		            try (Connection conn = DBConnection.getConnection();
		                 PreparedStatement stmt = conn.prepareStatement(query)) {

		                stmt.setString(1, cardID);
		                ResultSet rs = stmt.executeQuery();

		                if (rs.next()) {
		                    // Fetch current data from DB
		                    String currentName = rs.getString("name");
		                    String currentEmail = rs.getString("email");
		                    String currentPhone = rs.getString("phone");
		                    String currentGender = rs.getString("gender");
		                    String currentDob = rs.getString("date_of_birth");
		                    String currentAddress = rs.getString("address");
		                    String currentMembershipType = rs.getString("membership_type");
		                    String currentCreateDate = rs.getString("create_date");

		                    // Prompt for new values
		                    String newName = JOptionPane.showInputDialog(frame, "Edit Name:", currentName);
		                    String newEmail = JOptionPane.showInputDialog(frame, "Edit Email:", currentEmail);
		                    String newPhone = JOptionPane.showInputDialog(frame, "Edit Phone:", currentPhone);
		                    String newGender = JOptionPane.showInputDialog(frame, "Edit Gender:", currentGender);
		                    String newDob = JOptionPane.showInputDialog(frame, "Edit Date of Birth:", currentDob);
		                    String newAddress = JOptionPane.showInputDialog(frame, "Edit Address:", currentAddress);
		                    String newMembershipType = JOptionPane.showInputDialog(frame, "Edit Membership Type:", currentMembershipType);
		                    String newCreateDate = JOptionPane.showInputDialog(frame, "Edit Create Date:", currentCreateDate);

		                    // If user pressed cancel on any dialog, skip update
		                    if (newName != null && newEmail != null && newPhone != null && newGender != null &&
		                        newDob != null && newAddress != null && newMembershipType != null && newCreateDate != null) {

		                        String updateQuery = "UPDATE members SET name=?, email=?, phone=?, gender=?, date_of_birth=?, address=?, membership_type=?, create_date=? WHERE card_id=?";
		                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
		                            updateStmt.setString(1, newName);
		                            updateStmt.setString(2, newEmail);
		                            updateStmt.setString(3, newPhone);
		                            updateStmt.setString(4, newGender);
		                            updateStmt.setString(5, newDob);
		                            updateStmt.setString(6, newAddress);
		                            updateStmt.setString(7, newMembershipType);
		                            updateStmt.setString(8, newCreateDate);
		                            updateStmt.setString(9, cardID);

		                            int rowsAffected = updateStmt.executeUpdate();

		                            if (rowsAffected > 0) {
		                                // Optional: update table model for visible columns
		                                table.setValueAt(newName, selectedRow, 0); // Name
		                                table.setValueAt(cardID, selectedRow, 1); // Card ID stays same
		                                table.setValueAt(newMembershipType, selectedRow, 2);
		                                table.setValueAt(newCreateDate, selectedRow, 3);

		                                JOptionPane.showMessageDialog(frame, "Member details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		                            } else {
		                                JOptionPane.showMessageDialog(frame, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
		                            }
		                        }
		                    }
		                } else {
		                    JOptionPane.showMessageDialog(frame, "No member found for Card ID: " + cardID, "Error", JOptionPane.ERROR_MESSAGE);
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(frame, "Please select a row to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
		btnNewButton_p_1_1_1.setBounds(386, 424, 92, 29);
		frame.getContentPane().add(btnNewButton_p_1_1_1);

		// View Detail, Delete, Edit End
		
		JButton btnNewButton_1 = new JButton("Export QR");
		btnNewButton_1.setBounds(581, 424, 117, 29);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow != -1) {
		            String name = table.getValueAt(selectedRow, 0).toString();
		            String cardId = table.getValueAt(selectedRow, 6).toString();

		            // Content to encode in QR
		            String qrContent =cardId;

		            // Ask user where to save
		            JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setDialogTitle("Save QR Code");
		            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));

		            // Set default file name
		            fileChooser.setSelectedFile(new java.io.File(cardId + "_" + name + ".png"));

		            int userSelection = fileChooser.showSaveDialog(frame);

		            if (userSelection == JFileChooser.APPROVE_OPTION) {
		                java.io.File fileToSave = fileChooser.getSelectedFile();
		                String filePath = fileToSave.getAbsolutePath();

		                if (!filePath.toLowerCase().endsWith(".png")) {
		                    filePath += ".png";
		                }

		                try {
		                    generateQRCode(qrContent, filePath);
		                    JOptionPane.showMessageDialog(frame, "QR Code saved at:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
		                } catch (Exception ex) {
		                    ex.printStackTrace();
		                    JOptionPane.showMessageDialog(frame, "Failed to generate QR code: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		                }
		            }
		        } else {
		            JOptionPane.showMessageDialog(frame, "Please select a row from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});

		JButton btnNewButton = new JButton("Add Register");
		btnNewButton.setBounds(693, 424, 117, 29);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Registration_Form registerWindow = new Registration_Form();
		        registerWindow.showWindow();
		    }
		});

		chckbxNewCheckBox = new JCheckBox("Regular");
		chckbxNewCheckBox.setBounds(822, 59, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		chckbxNewCheckBox_1 = new JCheckBox("Premium");
		chckbxNewCheckBox_1.setBounds(822, 94, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_1);
		
		chckbxNewCheckBox_2 = new JCheckBox("VIP");
		chckbxNewCheckBox_2.setBounds(822, 129, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_2);
		
		chckbxNewCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isRegularChecked = chckbxNewCheckBox.isSelected();
                boolean isPremiumChecked = chckbxNewCheckBox_1.isSelected();
                boolean isVIPChecked = chckbxNewCheckBox_2.isSelected();
                loadTableData(isRegularChecked, isPremiumChecked, isVIPChecked);
            }
        });

		chckbxNewCheckBox_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isRegularChecked = chckbxNewCheckBox.isSelected();
                boolean isPremiumChecked = chckbxNewCheckBox_1.isSelected();
                boolean isVIPChecked = chckbxNewCheckBox_2.isSelected();
                loadTableData(isRegularChecked, isPremiumChecked, isVIPChecked);
            }
        });
        
        chckbxNewCheckBox_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isRegularChecked = chckbxNewCheckBox.isSelected();
                boolean isPremiumChecked = chckbxNewCheckBox_1.isSelected();
                boolean isVIPChecked = chckbxNewCheckBox_2.isSelected();
                loadTableData(isRegularChecked, isPremiumChecked, isVIPChecked);
            }
        });

	}
	

	private void loadTableData(boolean isRegularChecked, boolean isPremiumChecked, boolean isVIPChecked) {
//	    String baseQuery = "SELECT name, email, phone, gender, date_of_birth, address, card_id, membership_type, create_date FROM members WHERE 1=1";
	    String baseQuery = "SELECT name, card_id, membership_type, create_date FROM members WHERE 1=1";
	    
	    // Use a list to handle multiple selections
	    List<String> conditions = new ArrayList<>();

	    if (isRegularChecked) {
	        conditions.add("membership_type = 'Regular'");
	    }
	    if (isPremiumChecked) {
	        conditions.add("membership_type = 'Premium'");
	    }
	    if (isVIPChecked) {
	        conditions.add("membership_type = 'VIP'");
	    }

	    // Append conditions if any
	    if (!conditions.isEmpty()) {
	        String joinedConditions = String.join(" OR ", conditions);
	        baseQuery += " AND (" + joinedConditions + ")";
	    }

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(baseQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        DefaultTableModel model = new DefaultTableModel(
//	        		new String[] { "Name", "Email", "Phone", "Gender", "Date of Birth", "Address", "Card ID", "Membership Type", "Create Date" }, 0
		            new String[] { "Name", "Card ID", "Membership Type", "Create Date" }, 0
	        );

	        while (rs.next()) {
	            model.addRow(new Object[] {
	                rs.getString("name"),
//	                rs.getString("email"),
//	                rs.getString("phone"),
//	                rs.getString("gender"),
//	                rs.getString("date_of_birth"),
//	                rs.getString("address"),
	                rs.getString("card_id"),
	                rs.getString("membership_type"),
	                rs.getString("create_date")
	            });
	        }

	        table.setModel(model);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void loadTableData() {
//	    String query = "SELECT name, email, phone, gender, date_of_birth, address, card_id, membership_type, create_date FROM members";
	    String query = "SELECT name, card_id, membership_type, create_date FROM members";
	    try (Connection conn = DBConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {
//	        String[] columnNames = {"Name", "Email", "Phone", "Gender", "Date of Birth", "Address", "Card ID", "Membership Type", "Create Date"};
	        String[] columnNames = {"Name", "Card ID", "Membership Type", "Create Date"};
	        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
	        while (rs.next()) {
	            String name = rs.getString("name");
//	            String email = rs.getString("email");
//	            String phone = rs.getString("phone");
//	            String gender = rs.getString("gender");
//	            String date_of_birth = rs.getString("date_of_birth");
//	            String address = rs.getString("address");
	            String cardID = rs.getString("card_id");
	            String membershipType = rs.getString("membership_type");
	            String createDate = rs.getString("create_date");
//	            model.addRow(new Object[]{name, email, phone, gender, date_of_birth, address, cardID, membershipType, createDate});
	            model.addRow(new Object[]{name, cardID, membershipType, createDate});
	        }
	        table.setModel(model);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void searchMember(String keyword) {
	    String query = "SELECT * FROM members WHERE name LIKE ? OR phone LIKE ? OR email LIKE ? OR card_id LIKE ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, "%" + keyword + "%");
	        stmt.setString(2, "%" + keyword + "%");
	        stmt.setString(3, "%" + keyword + "%");
	        stmt.setString(4, "%" + keyword + "%");
	        
	        ResultSet rs = stmt.executeQuery();
	        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Email", "Phone", "Gender", "Date of Birth", "Address", "Card ID", "Membership", "Create Date"}, 0);
	        while (rs.next()) {
	            model.addRow(new Object[]{
	                rs.getString("name"),
	                rs.getString("email"),
	                rs.getString("phone"),
	                rs.getString("gender"),
	                rs.getString("date_of_birth"),
	                rs.getString("address"),
	                rs.getString("card_id"),
	                rs.getString("membership_type"),
	                rs.getString("create_date")
	            });
	        }
	        table.setModel(model);
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void generateQRCode(String text, String filePath) throws WriterException, java.io.IOException {
	    int width = 300;
	    int height = 300;

	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

	    Path path = FileSystems.getDefault().getPath(filePath);
	    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

}