package ObjectLabEnterpriseSoftware;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class newSettingsMenu extends JFrame {
	 private UtilController controller;
	public newSettingsMenu() {
		setPreferredSize(new Dimension(225, 265));
		setIconImage(Toolkit.getDefaultToolkit().getImage(newSettingsMenu.class.getResource("/ObjectLabEnterpriseSoftware/images/main_logo.png")));
		setResizable(false);
		setFont(new Font("Segoe UI", Font.PLAIN, 14));
		setTitle("Settings");
		getContentPane().setLayout(null);
		
		JLabel titleLabel = new JLabel("Settings");
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		titleLabel.setBounds(65, 24, 92, 55);
		getContentPane().add(titleLabel);
		
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
		btnManageClasses.setBounds(48, 90, 121, 23);
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
		btnManageDevices.setBounds(48, 138, 121, 23);
		getContentPane().add(btnManageDevices);
		
		JButton btnUserGuide = new JButton("User Guide");
		btnUserGuide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.openAdminHelpPage();
			}
		});
		btnUserGuide.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		btnUserGuide.setBounds(48, 184, 121, 23);
		getContentPane().add(btnUserGuide);
		pack();
		setLocationRelativeTo(null);
	}
	
	
	
	
	
}
