package ObjectLabEnterpriseSoftware;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class newSettingsMenu extends JFrame {
	 private UtilController controller;
		// --nav bar views ~Alex
		private BuildView buildView;
		private newJobsMgr jobsView;
		private ReportsView reportsView;	
                private BalanceView balanceView;         
		private newSettingsMenu adminSettingsView;
		// --nav bar vars ~Alex
		private JButton navBtn_jobsMgr;
		private JButton navBtn_build;
		private JButton navBtn_reports;
                private JButton navBtn_balance;
		private JButton navBtn_settings;

		// Return to main view after logout
		private static MainView home = new MainView();
		
		
	public newSettingsMenu() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(560, 300));
		setIconImage(Toolkit.getDefaultToolkit().getImage(newSettingsMenu.class.getResource("/ObjectLabEnterpriseSoftware/images/icon.ico")));
		setResizable(false);
		setFont(new Font("Segoe UI", Font.PLAIN, 14));
		setTitle("Settings");
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		
		JLabel titleLabel = new JLabel("Settings");
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		titleLabel.setBounds(211, 11, 92, 55);
		getContentPane().add(titleLabel);
		
			JMenuBar jMenuBar1 = new JMenuBar();
			setJMenuBar(jMenuBar1);

			jMenuBar1.setPreferredSize(new Dimension(200, 30));
			setJMenuBar(jMenuBar1);

			navBtn_jobsMgr = new JButton("Jobs Manager");
			navBtn_jobsMgr.setFont(new Font("Tahoma", Font.PLAIN, 12));
			navBtn_jobsMgr.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/view_file_icon.png")));
			navBtn_jobsMgr.setPreferredSize(new Dimension(100,24));
			jMenuBar1.add(navBtn_jobsMgr);

			navBtn_build = new JButton("Enter Build");
			navBtn_build.setFont(new Font("Tahoma", Font.PLAIN, 12));
			navBtn_build.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/hammer_icon.png")));

			navBtn_build.setPreferredSize(new Dimension(100,24));

			jMenuBar1.add(navBtn_build);

			navBtn_reports = new JButton("Reports");
			navBtn_reports.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/reports_icon.png")));
			navBtn_reports.setPreferredSize(new Dimension(100,24));

			jMenuBar1.add(navBtn_reports);

                        navBtn_balance = new JButton("Balance");
		        navBtn_balance.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/stats_icon.png")));
                        navBtn_balance.setPreferredSize(new Dimension(100,24));

                        jMenuBar1.add(navBtn_balance);
                        
			navBtn_settings = new JButton("Settings");
			navBtn_settings.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/cog_icon.png")));
			navBtn_settings.setPreferredSize(new Dimension(100,24));

			jMenuBar1.add(navBtn_settings);
			getContentPane().setLayout(null);
		
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
                        
                        navBtn_balance.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				navBtn_balanceActionPerformed(evt);
                                }
                        });

			navBtn_settings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					navBtn_settingsActionPerformed(evt);
				}
			}); 

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		JButton btnManageClasses = new JButton("Manage Classes");
		btnManageClasses.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClassMgr cm = new ClassMgr();
				cm.setVisible(true);
				dispose();
			}
		});
		btnManageClasses.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		btnManageClasses.setBounds(200, 77, 121, 23);
		getContentPane().add(btnManageClasses);
		
		JButton btnManageDevices = new JButton("Manage Devices");
		btnManageDevices.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DeviceMgr dv = new DeviceMgr();
				dv.setVisible(true);
				dispose();
			}
		});
		btnManageDevices.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		btnManageDevices.setBounds(200, 111, 121, 23);
		getContentPane().add(btnManageDevices);
		
		JButton btnUserGuide = new JButton("User Guide");
		btnUserGuide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.openAdminHelpPage();
			}
		});
		btnUserGuide.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		btnUserGuide.setBounds(200, 145, 121, 23);
		getContentPane().add(btnUserGuide);
		
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		btnLogout.setBounds(10, 200, 121, 23);
		getContentPane().add(btnLogout);
		btnLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logoutButtonActionPerformed(evt);
			}
        	});
        	
		pack();
		setLocationRelativeTo(null);
		final newJobsMgr njm = new newJobsMgr();
		   addWindowListener(
	                new WindowAdapter()
	                {
	                    @Override
	                    public void windowClosing(WindowEvent we)
	                    {
	                    	njm.setVisible(true);
	                    	dispose();
	                    }
	                 }
	                );
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

        private void navBtn_balanceActionPerformed(java.awt.event.ActionEvent evt)
	{
		balanceView= new BalanceView();
		balanceView.setVisible(true);
		dispose();

	}
        
	private void navBtn_settingsActionPerformed(java.awt.event.ActionEvent evt)
	{
		adminSettingsView = new newSettingsMenu();
		adminSettingsView.setVisible(true);
		dispose();

	}
	
	
	private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_logoutButtonActionPerformed
        //GEN-HEADEREND:event_logoutButtonActionPerformed
    	dispose();
        home.setVisible(true);
	 }//GEN-LAST:event_logoutButtonActionPerformed
	 
	
}
