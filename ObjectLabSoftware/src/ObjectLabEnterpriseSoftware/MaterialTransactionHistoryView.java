package ObjectLabEnterpriseSoftware;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class MaterialTransactionHistoryView extends JFrame {

	private JPanel contentPane;
	private JLabel lblStudentName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaterialTransactionHistoryView frame = new MaterialTransactionHistoryView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MaterialTransactionHistoryView() {
		setTitle("Material Transaction History");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblStudentName = new JLabel("student name");
		lblStudentName.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudentName.setBounds(113, 11, 216, 31);
		contentPane.add(lblStudentName);
		
		setVisible(true);
	}

	public void showHistory(String fName, String lName, String studentID) {
		System.out.println("showHistory()");
		lblStudentName.setText(fName + " " + lName);
		
	}
}
