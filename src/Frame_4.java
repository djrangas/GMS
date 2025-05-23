import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Frame_4 {

	private static JFrame frame;
    private static DefaultTableModel model;
	private static SerialPort arduinoPort;
	private static JTextField rfidField;
	private WebcamScannerZXing scanner;
	private JPanel cameraPanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_4 window = new Frame_4();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame_4() {
//		if (!SecurityGuard.checkAccess(frame)) return;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 960, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		Menus menuPanel = new Menus(frame);
		frame.getContentPane().add(menuPanel);
				
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
		
        String[] columns = {"RFID Tag ID", "Name", "Check-in Time", "Check-in Date"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setBounds(183, 57, 589, 305);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(183, 57, 589, 305);
        frame.getContentPane().add(scrollPane);
        
        rfidField = new JTextField();
        rfidField.setBounds(784, 59, 170, 36);
        frame.getContentPane().add(rfidField);
        rfidField.setColumns(10);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(873, 275, 81, 29);
        frame.getContentPane().add(btnAdd);
        
        cameraPanel = new JPanel();
        cameraPanel.setBorder(new TitledBorder(null, "Camera Area", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
        cameraPanel.setBounds(784, 107, 170, 156);
        frame.getContentPane().add(cameraPanel);
        
//		 connectToArduino();
		btnAdd.addActionListener(e -> addAction());

        frame.setVisible(true);
//		 new Thread(Frame_4::readFromArduino).start();
        scanner = new WebcamScannerZXing(cameraPanel, rfidField);

        
        JToggleButton tglbtnNewToggleButton = new JToggleButton("Start Scanning");
        tglbtnNewToggleButton.setBounds(784, 316, 161, 29);
        frame.getContentPane().add(tglbtnNewToggleButton);

        tglbtnNewToggleButton.addActionListener(e -> {
            if (tglbtnNewToggleButton.isSelected()) {
                tglbtnNewToggleButton.setText("Stop Scanning");
                if (scanner != null) {
                    scanner.restartScanner(rfidField); // start webcam
                }
            } else {
                tglbtnNewToggleButton.setText("Start Scanning");
                if (scanner != null) {
                    scanner.stopScanner(); // stop webcam
                }
            }
        });

    }

	private static void connectToArduino() {
        arduinoPort = SerialPort.getCommPort("cu.usbmodem11101"); // Change this to your correct port
        arduinoPort.setBaudRate(9600);
        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        if (arduinoPort.openPort()) {
            System.out.println("Connected to Arduino\n");
        } else {
            System.out.println("Failed to connect to Arduino\n");
        }
    }

	private static void addAction() {
		String cardId = rfidField.getText().trim();
			if (!cardId.isEmpty()) {
				checkInMember(cardId);
			} else {
				JOptionPane.showMessageDialog(frame, "Please enter an RFID Card ID!", "Error", JOptionPane.ERROR_MESSAGE);
			}
	}

	private void clearAction() {
	    rfidField.setText("");
//	    if (scanner != null) {
//	        scanner.stopScanner(); // Stop previous thread if running
//	    }
//	    scanner = new WebcamScannerZXing(cameraPanel, rfidField); // Restart scanner
	}


//	private static void readFromArduino() {
//        if (arduinoPort == null || !arduinoPort.isOpen()) return;
//        InputStream inputStream = arduinoPort.getInputStream();
//        Scanner scanner = new Scanner(inputStream);
//
//        while (true) {
//            if (scanner.hasNextLine()) {
//                String data = scanner.nextLine().trim(); // Read data
//                SwingUtilities.invokeLater(() -> {
//                    if (data.equals("ADD")) addAction();
//                    else if (data.equals("CLEAR")) clearAction();
//                    else rfidField.setText(data); // Display RFID UID
//                });
//            }
//        }
//    }

    private static void checkInMember(String cardId) {
        
        String currentTime = getCurrentTime();
        String currentDate = getCurrentDate();

        try (Connection conn = DBConnection.getConnection()) {
            // Check if the card ID exists in members table
            String queryCheck = "SELECT name FROM members WHERE card_id = ?";
            PreparedStatement pstmtCheck = conn.prepareStatement(queryCheck);
            pstmtCheck.setString(1, cardId);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");

                // Insert into rfid_checkin table
                String queryInsert = "INSERT INTO rfid_checkin (card_id, name, checkin_time, checkin_date) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert);
                pstmtInsert.setString(1, cardId);
                pstmtInsert.setString(2, name);
                pstmtInsert.setString(3, currentTime);
                pstmtInsert.setString(4, currentDate);
                pstmtInsert.executeUpdate();

                // Update JTable with new check-in data
                model.addRow(new Object[]{cardId, name, currentTime, currentDate});

                // sendCommandToArduino("1");
                JOptionPane.showMessageDialog(null, "Check-in successful for: " + name);
            } else {
                // sendCommandToArduino("0");
                JOptionPane.showMessageDialog(null, "RFID Card ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pstmtCheck.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getCurrentTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(timeFormatter);
    }

    private static String getCurrentDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(dateFormatter);
    }

    private static void sendCommandToArduino(String command) {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            try (OutputStream outputStream = arduinoPort.getOutputStream()) {
                outputStream.write(command.charAt(0));
                outputStream.flush();
                System.out.println("Sent command: " + command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Arduino is not connected.");
        }
    }
}
