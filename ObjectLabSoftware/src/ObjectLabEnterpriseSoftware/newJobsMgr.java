package ObjectLabEnterpriseSoftware;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import ObjectLabEnterpriseSoftware.AdminSettingsView;
import ObjectLabEnterpriseSoftware.BuildView;
import ObjectLabEnterpriseSoftware.JobsView;
import ObjectLabEnterpriseSoftware.ReportsView;
import ObjectLabEnterpriseSoftware.SQLMethods;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.derby.tools.sysinfo;

import javax.swing.JPanel;
import java.awt.Button;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Toolkit;


/*
 * 
 *  Jobs Manager V2
 *  @Author=M. Alex Boyd
 * 	@Date=4/22/16
 * 
 * 	To-Dos
 * 	- Add check to make sure jobs are not already approved before approving
 *	- Add Error text for missing stat input
 * 
 * 
 * 
 */



















public class newJobsMgr extends JFrame {
	// --nav bar views ~Alex
	private BuildView buildView;
	private newJobsMgr jobsView;
	private ReportsView reportsView;	
	private newSettingsMenu adminSettingsView;
	//
	public newJobsMgr() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(newJobsMgr.class.getResource("/ObjectLabEnterpriseSoftware/images/icon.ico")));
		setTitle("Administration Panel");
		setPreferredSize(new Dimension(635,605));
		setAlwaysOnTop(false);
		getContentPane().setBackground(Color.WHITE);
		initWindow();
		setLocationRelativeTo(null);
	}

	private void initWindow()
	{
		
		JMenuBar jMenuBar1 = new JMenuBar();
		setJMenuBar(jMenuBar1);

		jMenuBar1.setPreferredSize(new Dimension(200, 30));
		setJMenuBar(jMenuBar1);

		navBtn_jobsMgr = new JButton("Jobs Manager");
		navBtn_jobsMgr.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/view_file_icon.png")));
		navBtn_jobsMgr.setPreferredSize(new Dimension(100,24));


		jMenuBar1.add(Box.createRigidArea(new Dimension(83, 12)));
		jMenuBar1.add(navBtn_jobsMgr);

		navBtn_build = new JButton("Enter Build");
		navBtn_build.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/hammer_icon.png")));

		navBtn_build.setPreferredSize(new Dimension(100,24));

		jMenuBar1.add(navBtn_build);

		navBtn_reports = new JButton("Reports");
		navBtn_reports.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/reports_icon.png")));
		navBtn_reports.setPreferredSize(new Dimension(100,24));

		jMenuBar1.add(navBtn_reports);

		navBtn_settings = new JButton("Settings");
		navBtn_settings.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/cog_icon.png")));
		navBtn_settings.setPreferredSize(new Dimension(100,24));

		jMenuBar1.add(navBtn_settings);
		getContentPane().setLayout(null);

		jobStatusCombo = new JComboBox();
		jobStatusCombo.setBounds(163, 63, 89, 20);
		jobStatusCombo.addItem("All Jobs");
		jobStatusCombo.addItem("Pending");
		jobStatusCombo.addItem("Approved");
		getContentPane().add(jobStatusCombo);

		JLabel lblJobStatus = new JLabel("Job Status:");
		lblJobStatus.setBounds(86, 63, 78, 17);
		lblJobStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblJobStatus.setLabelFor(jobStatusCombo);
		getContentPane().add(lblJobStatus);

		final JComboBox deviceCombo = new JComboBox();
		deviceCombo.setBounds(342, 63, 125, 20);
		deviceCombo.addItem(" ");
		/// Adds tracked devices to comboBox dropdown window
		SQLMethods dbconn = new SQLMethods();
		ResultSet queryResult = dbconn.getTrackedDevices();
		try {
			while(queryResult.next())
			{
				deviceCombo.addItem(queryResult.getString(1));
			}
		} catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		//  

		deviceCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JComboBox cb = (JComboBox) e.getSource();
				String selectedDevice = (String) cb.getSelectedItem();
				System.out.println(selectedDevice);
				updateJobWindow(selectedDevice);
				if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("approved")) // hide approve/reject/review buttons and panel
				{
					toggleButtons(false);
				}
				else if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("pending")) // hide approve/reject/review buttons and panel
				{
					toggleButtons(true);
				}
			
			}

		});

		jobStatusCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(deviceCombo.getSelectedItem().toString() != "")
				{
					String selectedDevice = (String) deviceCombo.getSelectedItem();
					System.out.println(selectedDevice);
					updateJobWindow(selectedDevice);
					if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("approved")) // hide approve/reject/review buttons and panel
					{
						toggleButtons(false);
					}
					else if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("pending")) // hide approve/reject/review buttons and panel
					{
						toggleButtons(true);
					}
				}
			}
		});

		getContentPane().add(deviceCombo);

		JLabel deviceLabel = new JLabel("Device:");
		deviceLabel.setBounds(283, 64, 60, 14);
		deviceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		deviceLabel.setLabelFor(deviceCombo);
		getContentPane().add(deviceLabel);

		JScrollPane jobListingsPane = new JScrollPane();
		jobListingsPane.setBounds(10, 94, 598, 166);
		getContentPane().add(jobListingsPane);

		jobsTable = new JTable();
		jobsModel = 
				new DefaultTableModel() {
			Class[] columnTypes = new Class[] {
					Boolean.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		jobsModel.setColumnCount(7);
		jobsModel.setColumnIdentifiers(new String[] {
				"Selected?", "File Name", "First Name", "Last Name", "Date", "Class", "Section"
		});

		jobsTable.setModel(jobsModel);
		jobListingsPane.setViewportView(jobsTable);

		JLabel titleLabel = new JLabel("Jobs Manager");
		titleLabel.setBounds(226, 11, 159, 41);
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		getContentPane().add(titleLabel);

		JButton logoutButton = new JButton("Logout");
		logoutButton.setBounds(10, 459, 89, 23);
		logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				logout();
			}
		});
		logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		logoutButton.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(logoutButton);

		approveButton = new JButton("Approve");
		approveButton.setBounds(331, 459, 89, 23);
		approveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				approveJobs();
			}
		});
		approveButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		getContentPane().add(approveButton);

		rejectButton = new JButton("Reject");
		rejectButton.setBounds(426, 459, 89, 23);
		rejectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				rejectJobs();
			}
		});
		rejectButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		getContentPane().add(rejectButton);

		reviewButton = new JButton("Review");
		reviewButton.setBounds(519, 459, 89, 23);
		reviewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				openInDefaultProgram(evt);
			}
		});
		reviewButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		getContentPane().add(reviewButton);

		lblFillInData = new JLabel("Please enter device statistic tracking data before approving:");
		lblFillInData.setBounds(10, 271, 375, 23);
		lblFillInData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblFillInData.setVisible(false);
		getContentPane().add(lblFillInData);

		jobStatPanel = new JPanel();
		jobStatPanel.setBounds(10, 305, 598, 143);
		jobStatPanel.setVisible(false); // stat panel doesn't show up until device is selected.
		getContentPane().add(jobStatPanel);
		jobStatPanel.setLayout(null);

		deviceNameLabel = new JLabel("filler");
		deviceNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		deviceNameLabel.setBounds(227, 11, 139, 39);
		jobStatPanel.add(deviceNameLabel);

		trackingStatLabel1 = new JLabel("New label");
		trackingStatLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		trackingStatLabel1.setBounds(0, 53, 141, 20);
		jobStatPanel.add(trackingStatLabel1);

		trackingStatLabel2 = new JLabel("New label");
		trackingStatLabel2.setLabelFor(trackingStatLabel2);
		trackingStatLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		trackingStatLabel2.setBounds(0, 90, 141, 20);
		jobStatPanel.add(trackingStatLabel2);

		trackingStatInput1 = new JTextField();
		trackingStatLabel1.setLabelFor(trackingStatInput1);
		trackingStatInput1.setBounds(161, 55, 175, 20);
		jobStatPanel.add(trackingStatInput1);
		trackingStatInput1.setColumns(10);

		trackingStatInput2 = new JTextField();
		trackingStatInput2.setBounds(160, 92, 176, 20);
		jobStatPanel.add(trackingStatInput2);
		trackingStatInput2.setColumns(10);


		navBtn_jobsMgr.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				navBtn_jobsMgrActionPerformed(evt);
			}
		});

		navBtn_build.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				navBtn_buildActionPerformed(evt);
			}
		});

		navBtn_reports.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				navBtn_reportsActionPerformed(evt);
			}
		});

		navBtn_settings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				navBtn_settingsActionPerformed(evt);
			}
		}); 

		pack();
	}

	
	protected void rejectJobs() {
		ArrayList<Integer> selectedDevices = new ArrayList<Integer>();
		for(int j = 0; j < jobsTable.getRowCount(); j++)
		{
			if(jobsTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")) // get all the selected rows
			{
				selectedDevices.add(j); // adds row # for all selected rows.
			}
		}
        if (selectedDevices.size() >= 0)
        {
        	System.out.println("Test");
         //   String desc = JOptionPane.showInputDialog("Please enter why you're rejecting the submission: ");

                /* Hand off the data in the selected row found in our tablemodel to this method so we can 
                 * reject the correct file -Nick 
                 */
                boolean success = UtilController.rejectStudentSubmission(
                        (String) jobsModel.getValueAt(selectedDevices.get(0), 1),
                        "Rejected"
                );

                if (success) // always rejects, commenting out email utlity for now ~Alex
                {
                   JOptionPane op = new JOptionPane("Submission successfully rejected.", JOptionPane.INFORMATION_MESSAGE);
                   JDialog dialog = op.createDialog("Job Manager: Submission Rejected");
                   dialog.setAlwaysOnTop(true);
                   dialog.setModal(true);
                   dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                   dialog.setVisible(true);
                   
                    jobsModel.fireTableDataChanged();
        			jobStatusCombo.setSelectedIndex(1);
        			jobsTable.repaint();
               
                } else
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Something went wrong! You shouldn't see this, ever!");
                }
            }
         else
        {
            JOptionPane.showMessageDialog(new JFrame(), "Please select a submission file to reject!");
        }
	}

	private void toggleButtons(boolean status) // toggles the approve,reject,review buttons along with stat panel so you can't approve the already approved
	{
		jobStatPanel.setVisible(status);
		lblFillInData.setVisible(status);
		approveButton.setVisible(status);
		rejectButton.setVisible(status);
		reviewButton.setVisible(status);
	}
	
	
	
	protected void approveJobs() { // handles the approveButton

		ArrayList<Integer> selectedDevices = new ArrayList<Integer>();
		for(int j = 0; j < jobsTable.getRowCount(); j++)
		{
			if(jobsTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")) // get all the selected rows
			{
				selectedDevices.add(j); // adds row # for all selected rows.
			}
		}

		if(trackingStatInput1.getText().isEmpty())
		{
			System.out.println("Missing stat 1");
		}
		else if(trackingStatInput2.getText().isEmpty())
		{
			System.out.println("Missing stat 2");
		}
		else
		{


			int rowDataLocation = getSelectedRowNum(jobsModel, selectedDevices.get(0), 0);


			/* Hand off the data in the selected row found in our tablemodel to this method so we can 
			                 approve the correct file to be printed... -Nick 
			 */
			UtilController.approveStudentSubmission(
					(String) jobsModel.getValueAt(rowDataLocation, 1), trackingStatInput1.getText(), trackingStatInput2.getText());

			trackingStatInput1.setText("");
			trackingStatInput2.setText("");
			jobsModel.fireTableDataChanged();
			jobStatusCombo.setSelectedIndex(2);
			jobsTable.repaint();
		}

		jobsModel.fireTableDataChanged();
		jobsTable.repaint();
	}

	public static int getSelectedRowNum(DefaultTableModel dm, int selectedRow, int column)
	{
		if (selectedRow < 0)
		{
			return -1;
		}

		for (int i = 0; i < dm.getRowCount(); i++)
		{
			if (dm.getValueAt(i, column).equals(dm.getValueAt(selectedRow, column)))
			{
				return i;
			}
		}

		return -1;
	}


	protected void openInDefaultProgram(MouseEvent evt) {
		ArrayList<Integer> selectedDevices = new ArrayList<Integer>();
		for(int j = 0; j < jobsTable.getRowCount(); j++)
		{
			if(jobsTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")) // get all the selected rows
			{
				selectedDevices.add(j); // adds row # for all selected rows.
			}
		}

	        if (selectedDevices.size() > -1)
	        {
	            int rowDataLocation = getSelectedRowNum(jobsModel, selectedDevices.get(0), 0);

	
	            File fileLocation = UtilController.getFilePath(
	                    (String) jobsModel.getValueAt(rowDataLocation, 2),
	                    (String) jobsModel.getValueAt(rowDataLocation, 3),
	                    (String) jobsModel.getValueAt(rowDataLocation, 1),
	                    (String) jobsModel.getValueAt(rowDataLocation, 4)
	            );//jobsModel.addRow(new Object[] {(Boolean) false, fileName, fName, lName, date, className, classSection });
	            
	            UtilController.checkFileExists(fileLocation.getPath());
	            try
	            {
	                Desktop.getDesktop().open(fileLocation);
	            } catch (IOException ex)
	            {
	                Logger.getLogger(JobsView.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } else
	        {
	            JOptionPane.showMessageDialog(new JFrame(), "Please select a file to review!");
	        }
	}

	protected void updateJobWindow(final String selectedDevice) { // used to display all jobs according to device

		showDeviceStats(selectedDevice); // used to display stat tracking relating to device.

		ArrayList rows = new ArrayList();
		jobsModel.setRowCount(0);
		jobsTable.repaint();
		/// Adds tracked devices to comboBox dropdown window
		Thread runner = new Thread() {
			public void run()
			{
				try {
					SQLMethods dbconn = new SQLMethods();
					System.out.println("Trying to find " + selectedDevice);
					ResultSet queryResult;
					if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("All Jobs"))
					{
						queryResult = dbconn.searchAllJobsStatusPrinter(selectedDevice);
					} else if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("Approved"))
					{
						queryResult = dbconn.searchJobsStatusPrinter("approved", selectedDevice);	

					} else if(jobStatusCombo.getSelectedItem().toString().equalsIgnoreCase("Pending"))
					{
						queryResult = dbconn.searchJobsStatusPrinter("pending", selectedDevice);
					}
					else
					{
						queryResult = dbconn.searchAllJobsStatusPrinter(selectedDevice);
					}
					while(queryResult.next())
					{
						int jobId = queryResult.getInt(1);
						String fileName = queryResult.getString(2);
						String fName = queryResult.getString(3);
						String lName = queryResult.getString(4);
						String date = queryResult.getString(5);
						String deviceName = queryResult.getString(6);
						String className = queryResult.getString(7);
						String classSection = queryResult.getString(8);
						System.out.println("Adding row...");
						jobsModel.addRow(new Object[] {(Boolean) false, fileName, fName, lName, date, className, classSection });
					}
					jobsTable.setModel(jobsModel);
					jobsTable.repaint();
					dbconn.closeDBConnection();
				} catch(Exception e)
				{
					System.out.println("Error: " + e);
				}
			}

		};
		runner.run();
	}
	/*"SELECT job.job_id, job.file_name, users.first_name, users.last_name, "
					+ "job.submission_date ,job.printer_name, class_name, class_section  " 
					+ "FROM job, users , class " + "WHERE job.status = * AND printer_name = ? "
					+ "AND users.towson_id = job.student_id AND job.class_id = class.class_id;*/


	private void showDeviceStats(String selectedDevice) { // used to update 

		jobStatPanel.setVisible(true);
		lblFillInData.setVisible(true);
		deviceNameLabel.setText(selectedDevice);

		if(selectedDevice.equals("Laser Printer")) { // hard coding these quick and dirttay
			trackingStatLabel1.setText("Material");
			trackingStatLabel2.setText("Cut Time (H:M:S)");
		}
		else if(selectedDevice.equals("Objet Desktop 30")) { // hard coding these quick and dirttay
			trackingStatLabel1.setText("Build material (grams)");
			trackingStatLabel2.setText("Support material (grams)");
		}
		else if(selectedDevice.equals("Solidscape R66+"))
		{
			trackingStatLabel1.setText("Build Time");
			trackingStatLabel2.setText("Resolution");
		}
		else if(selectedDevice.equals("Z Printer 250"))
		{
			trackingStatLabel1.setText("Volume (cubic in)");
			trackingStatLabel2.setText("Color");
		}
		else if(selectedDevice.equals(" ") || selectedDevice.equals(""))
		{
			jobStatPanel.setVisible(false);
			lblFillInData.setVisible(false);
		}



	}

	private void navBtn_jobsMgrActionPerformed(java.awt.event.ActionEvent evt)
	{
		jobsView = new newJobsMgr();
		jobsView.setVisible(true);
		dispose();

	}

	private void navBtn_buildActionPerformed(java.awt.event.ActionEvent evt)
	{
		buildView = new BuildView();
		buildView.startMakeBuildProcess();
		dispose();

	}

	private void navBtn_reportsActionPerformed(java.awt.event.ActionEvent evt)
	{
		reportsView = new ReportsView();
		reportsView.ReportsPage();
		dispose();

	}

	private void navBtn_settingsActionPerformed(java.awt.event.ActionEvent evt)
	{
		adminSettingsView = new newSettingsMenu();
		adminSettingsView.setVisible(true);
		dispose();

	}

	private void logout()
	{
		MainView mv = new MainView();
		mv.setVisible(true);
		dispose();
	}



	/////// Nav Bar ~Alex /////
	// --nav bar vars ~Alex
	private JButton navBtn_jobsMgr;
	private JButton navBtn_build;
	private JButton navBtn_reports;
	private JButton navBtn_settings;

	///
	// etc vars
	// 
	private  JPanel jobStatPanel;
	private  JPanel jobListingsPane;
	private JTextField trackingStatInput1;
	private JTextField trackingStatInput2;
	private JLabel trackingStatLabel1;
	private JLabel trackingStatLabel2;
	private JLabel deviceNameLabel;
	private JLabel lblFillInData;
	private JTable jobsTable;
	private DefaultTableModel jobsModel;
	private JComboBox jobStatusCombo;
	private JButton approveButton;
	private JButton rejectButton;
	private JButton reviewButton;
}
