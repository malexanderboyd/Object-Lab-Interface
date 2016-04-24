package ObjectLabEnterpriseSoftware;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class ClassMgr extends JFrame {
	public static final String [] arrayOfTowsonDepartments = {"AFST","AHLT","AMST","ANTH",
			"ARAB","ARED","ART","ARTH","ASST","ASTR","BCLA","BIOL","BUSX","CDCE","CHEM","CHNS",
			"CIS","CLST","COMM","COSC","CRMJ","DANC","DFST","DVMT","DVRD","DVWR","EBTM","ECED",
			"ECON","ECSE","EDUC","EESE","ELED","EMF","ENGL","ENTR","ENVS","ESOL","FIN","FLPN","FMST",
			"FREN","FRSC","GENL","GEOG","GEOL","GERM","GERO","GRK","HCMN","HEBR","HIST",
			"HLTH","HONR","IDFA","IDHP","IDIS","IDNM","INLA","INST","ISTC","ITAL","ITEC",
			"JPNS","KNES","LAST","LATN","LEGL","LGBT","LWAC","MATH","MBBB","MCOM","MKTG",
			"MNGT","MTRO","MUED","MUSA","MUSC","NURS","OCTH","PHEA","PHIL","PHSC","PHYS",
			"PORT","POSC","PSYC","REED","RLST","RUSS","SCED","SCIE","SOCI","SOSC","SPAN",
			"SPED","SPPA","THEA","WMST","WRIT"};
	
	
	

	public ClassMgr() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassMgr.class.getResource("/ObjectLabEnterpriseSoftware/images/icon.ico")));
		setPreferredSize(new Dimension(550, 370));
		setResizable(false);
		setFont(new Font("Segoe UI", Font.PLAIN, 14));
		setTitle("Class Manager");
		initWindow();
		updateView();
	} // end of constructor
	
	private void initWindow()
	{
		

		
		JLabel titleLabel = new JLabel("Class Manager");
		titleLabel.setBounds(189, 11, 178, 40);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setVisible(true);
		getContentPane().setLayout(null);
		getContentPane().add(titleLabel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBounds(10, 59, 516, 264);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(tabbedPane);
		tabbedPane.setVisible(true);
		JPanel manageClassPanel = new JPanel();
		tabbedPane.addTab("Manage Classes", null, manageClassPanel, null);
		manageClassPanel.setLayout(null);
		
		JLabel jLabel3 = new JLabel();
		jLabel3.setText("Inactive Classes");
		jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 12));
		jLabel3.setBounds(20, 25, 110, 19);
		manageClassPanel.add(jLabel3);
		
		 jScrollPane3 = new JScrollPane();
		jScrollPane3.setBounds(20, 44, 139, 189);
		manageClassPanel.add(jScrollPane3);
		
		allClassList = new JList();
		jScrollPane3.setViewportView(allClassList);
		
		 addArrow = new JButton();
		addArrow.setText("Add ->");
		addArrow.setPreferredSize(new Dimension(60, 23));
		addArrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		addArrow.setBounds(169, 99, 90, 23);
		manageClassPanel.add(addArrow);
		
		 removeArrow = new JButton();
		removeArrow.setText("<- Remove");
		removeArrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		removeArrow.setBounds(169, 129, 90, 23);
		removeArrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeArrowActionPerformed(evt);
			}
		});
		manageClassPanel.add(removeArrow);
		
		 jScrollPane4 = new JScrollPane();
		jScrollPane4.setBounds(269, 44, 139, 190);
		manageClassPanel.add(jScrollPane4);
		
		 currentClassList = new JList();
		jScrollPane4.setViewportView(currentClassList);
		
		JLabel lblActiveClasses = new JLabel();
		lblActiveClasses.setText("Active Classes");
		lblActiveClasses.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblActiveClasses.setBounds(269, 28, 129, 19);
		manageClassPanel.add(lblActiveClasses);
		
		saveBtn = new JButton();
		saveBtn.setText("Apply Changes");
		saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		saveBtn.setBounds(157, 236, 109, 23);
		manageClassPanel.add(saveBtn);
		
		JPanel addClassPanel = new JPanel();
		tabbedPane.addTab("Add Class", null, addClassPanel, null);
		addClassPanel.setBorder(null);
		addClassPanel.setLayout(null);
		
		JLabel classNumLabel = new JLabel("Class Number:");
		classNumLabel.setBounds(0, 97, 95, 20);
		classNumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addClassPanel.add(classNumLabel);
		
		JLabel lblNewLabel = new JLabel("Department:");
		lblNewLabel.setBounds(0, 11, 82, 20);
		addClassPanel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		JScrollPane classScrollPane = new JScrollPane();
		classScrollPane.setBounds(112, 14, 86, 75);
		addClassPanel.add(classScrollPane);
		
		classList = new JList(arrayOfTowsonDepartments);
		classScrollPane.setViewportView(classList);
		classList.setBackground(Color.LIGHT_GRAY);
		
		classNumberInput = new JTextField();
		classNumberInput.setBounds(112, 99, 118, 20);
		classNumLabel.setLabelFor(classNumberInput);
		addClassPanel.add(classNumberInput);
		classNumberInput.setColumns(10);
		
		JLabel sectionNumberLabel = new JLabel("Section Number:");
		sectionNumberLabel.setBounds(0, 124, 105, 20);
		sectionNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addClassPanel.add(sectionNumberLabel);
		
		SectionNumberInput = new JTextField();
		SectionNumberInput.setBounds(112, 126, 118, 20);
		SectionNumberInput.setColumns(10);
		addClassPanel.add(SectionNumberInput);
		
		JLabel professorInputLabel = new JLabel("Professor:");
		professorInputLabel.setBounds(0, 155, 105, 20);
		professorInputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addClassPanel.add(professorInputLabel);
		
		professorInput = new JTextField();
		professorInput.setBounds(112, 157, 118, 20);
		professorInputLabel.setLabelFor(professorInput);
		addClassPanel.add(professorInput);
		professorInput.setColumns(10);
		
		JLabel lbllastName = new JLabel("(last name)");
		lbllastName.setBounds(0, 173, 72, 20);
		lbllastName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addClassPanel.add(lbllastName);
		
		addClassButton = new JButton("Add Class");
		addClassButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addClass(arg0);
			}
		});
		addClassButton.setBounds(233, 187, 89, 23);
		addClassPanel.add(addClassButton);
		
		JPanel removeClassPanel = new JPanel();
		tabbedPane.addTab("Remove Class", null, removeClassPanel, null);
		removeClassPanel.setLayout(null);
		
		removePrinterButton = new JButton("Remove");
		removePrinterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				  removeClassButtonActionPerformed(arg0);
			}
		});
		removePrinterButton.setBounds(159, 236, 89, 23);
		removeClassPanel.add(removePrinterButton);
		
		lblAvailableClasses = new JLabel();
		lblAvailableClasses.setText("Available Classes");
		lblAvailableClasses.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblAvailableClasses.setBounds(10, 25, 110, 19);
		removeClassPanel.add(lblAvailableClasses);
		
		spAvaiableClassesRC = new JScrollPane();
		spAvaiableClassesRC.setBounds(10, 44, 139, 189);
		removeClassPanel.add(spAvaiableClassesRC);
		
		currentClassListRC = new JList();
		spAvaiableClassesRC.setViewportView(currentClassListRC);
		
		addArrowRC = new JButton();
		addArrowRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addArrowRCActionPerformed(evt);
			}
		});
		addArrowRC.setText("Add ->");
		addArrowRC.setPreferredSize(new Dimension(60, 23));
		addArrowRC.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		addArrowRC.setBounds(159, 99, 90, 23);
		removeClassPanel.add(addArrowRC);
		
		removeArrowRC = new JButton();
		removeArrowRC.setText("<- Remove");
		removeArrowRC.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		removeArrowRC.setBounds(159, 143, 90, 23);
		removeArrowRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeArrowRCActionPerformed(evt);
			}
		});
		removeClassPanel.add(removeArrowRC);
		
		JScrollPane spToBeRemovedRC = new JScrollPane();
		spToBeRemovedRC.setBounds(259, 44, 139, 190);
		removeClassPanel.add(spToBeRemovedRC);
		
		removeClassList = new JList();
		spToBeRemovedRC.setViewportView(removeClassList);
		
		JLabel lblClassesToRemove = new JLabel();
		lblClassesToRemove.setText("Classes to remove");
		lblClassesToRemove.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblClassesToRemove.setBounds(259, 28, 129, 19);
		removeClassPanel.add(lblClassesToRemove);
		
		backButton = new JLabel("");
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				newSettingsMenu ns = new newSettingsMenu();
				ns.setVisible(true);
				dispose();
			}
		});
		backButton.setIcon(new ImageIcon(ClassMgr.class.getResource("/ObjectLabEnterpriseSoftware/images/back_arrow_button.png")));
		backButton.setBounds(21, 11, 32, 33);
		getContentPane().add(backButton);
		
		
        addArrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addArrowActionPerformed(evt);
            }
        });
        
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        
        pack();
        
        setLocationRelativeTo(null);
	}
	
	
	protected void addClass(MouseEvent arg0) {
		if (InputValidation.isEmpty(classList.getSelectedValue().toString()) | InputValidation.isEmpty(classNumberInput.getText())
				| InputValidation.isEmpty(SectionNumberInput.getText()) | InputValidation.isEmpty(professorInput.getText()))
		{
			JOptionPane.showMessageDialog(null, "Please enter all Class Values",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} else if (classList.getSelectedValue().toString().contains(" ") | classNumberInput.getText().contains(" ")
				| SectionNumberInput.getText().contains(" ") | professorInput.getText().contains(" "))
		{
			JOptionPane.showMessageDialog(null, "Values can only be one word each",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} else if (!InputValidation.isNumber(classNumberInput.getText())
				| !InputValidation.isNumber(SectionNumberInput.getText()))
		{
			JOptionPane.showMessageDialog(null, "Class Number and Section Numbers must "
					+ "contain only numbers",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} else if (!InputValidation.isAlpha(classList.getSelectedValue().toString()) | !InputValidation.isAlpha(professorInput.getText()))
		{
			JOptionPane.showMessageDialog(null, "Names must only contain letters.",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} else
		{
			String input = classList.getSelectedValue().toString().toUpperCase() + " "
					+ classNumberInput.getText() + " " + SectionNumberInput.getText();
			DefaultListModel temp = new DefaultListModel();
			temp.addElement(input);

			if (allClassListModel.contains(temp.elementAt(0)) || currentClassListModel.contains(temp.elementAt(0)))
			{
				JOptionPane.showMessageDialog(null, "Class already in all class list",
						"Add Error", JOptionPane.ERROR_MESSAGE);
			} else
			{
				allClassListModel.addElement(input.toUpperCase());
				UtilController.insertNewClass(classList.getSelectedValue().toString().toUpperCase(), classNumberInput.getText(), SectionNumberInput.getText(), professorInput.getText().trim());
			}

			temp.clear();
			
            updateView();
		}
		
	}


	private DefaultListModel allClassListModel;
	private DefaultListModel currentClassListModel;
	private DefaultListModel removeClassListModel = new DefaultListModel();
    newSettingsMenu settings;
	private static FileManager inst = null;

	private void updateView()
	{
            if(allClassListModel != null)
                allClassListModel.clear();
            
            if(currentClassListModel != null)
                currentClassListModel.clear();
            
            allClassListModel = UtilController.returnNonCurrentClasses(); /* false */
            currentClassListModel = UtilController.returnCurrentClasses(); /* true */
           
            allClassList.setModel(allClassListModel);
            currentClassList.setModel(currentClassListModel);
            currentClassListRC.setModel(allClassListModel);
            removeClassList.setModel(removeClassListModel);
	}





    private void addArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addArrowActionPerformed
		if (currentClassListModel.contains(allClassList.getSelectedValue()))
		{
			JOptionPane.showMessageDialog(null, "Class already in current class list",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} 
		else if(allClassList.getSelectedValue() == null)
		{
			JOptionPane.showMessageDialog(null, "No class selected!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			currentClassListModel.addElement(allClassList.getSelectedValue());
			allClassListModel.removeElement(allClassList.getSelectedValue());
		}
    }//GEN-LAST:event_addArrowActionPerformed
    
    private void addArrowRCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addArrowActionPerformed
		if (removeClassListModel.contains(currentClassListRC.getSelectedValue()))
		{
			JOptionPane.showMessageDialog(null, "Class already in current class list",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} 
		else if(currentClassListRC.getSelectedValue() == null)
		{
			JOptionPane.showMessageDialog(null, "No class selected!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			removeClassListModel.addElement(currentClassListRC.getSelectedValue());
			allClassListModel.removeElement(currentClassListRC.getSelectedValue());
			
			removeClassList.setModel(removeClassListModel);
			
		}
    }//GEN-LAST:event_addArrowActionPerformed

    
    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        ArrayList<String> classes = new ArrayList<>();
        
        for (int i = 0; i < currentClassListModel.getSize(); i++)
            classes.add(currentClassListModel.elementAt(i).toString());
        
        UtilController.updateAvailableClasses(classes);
        updateView();
    }//GEN-LAST:event_saveBtnActionPerformed

    
    
    private void removeArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeArrowActionPerformed
		int i;
		for (i = 0; i < currentClassListModel.getSize(); i++)
		{
			if (currentClassListModel.elementAt(i).equals(currentClassList.getSelectedValue()))
			{
				allClassListModel.addElement(currentClassListModel.elementAt(i));
				currentClassListModel.removeElementAt(i);
			}
		}
    }//GEN-LAST:event_removeArrowActionPerformed

    
    
    
    private void removeArrowRCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeArrowActionPerformed
		int i;
		for (i = 0; i < removeClassListModel.getSize(); i++)
		{
			if (removeClassListModel.elementAt(i).equals(removeClassList.getSelectedValue()))
			{
				allClassListModel.addElement(removeClassListModel.elementAt(i));
				removeClassListModel.removeElementAt(i);
			}
		}
    }//GEN-LAST:event_removeArrowActionPerformed

    
    
    private void removeClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeClassButtonActionPerformed

            if(JOptionPane.showConfirmDialog(null, "Continue? If you delete this class you will no longer be able "
                    + "to select this class when building jobs.\nFiles associated with this class will not be deleted "
                    + "but they will no longer be associated with this class. Click 'YES' to CONFIRM DELETION this is permanent!","Warning",JOptionPane.YES_OPTION)==JOptionPane.YES_OPTION){
            	for (int i = 0; i < removeClassListModel.getSize(); i++)
        		{
        				String selected = (String) removeClassListModel.elementAt(i);
        				System.out.println(selected);
        				int id = Integer.parseInt(selected.split(" ")[0]);
        				System.out.println(id);
        		        UtilController.removeClass(id);
        		}     
            	removeClassListModel.clear();
            	updateView();
            }
        else
            JOptionPane.showMessageDialog(null, "Please select a class to remove.");
    }//GEN-LAST:event_removeClassButtonActionPerformed

    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton saveBtn;
    private javax.swing.JList allClassList;
    private javax.swing.JList currentClassList;
    private javax.swing.JButton addArrow;
    private javax.swing.JButton removeArrow;
    private JList removeClassList;
    private JButton addArrowRC;
    private JButton removeArrowRC;
    private JButton addClassButton;

	private JTextField classNumberInput;
	private JTextField SectionNumberInput;
	private JTextField professorInput;
	private JList currentClassListRC;
	private JScrollPane spAvaiableClassesRC ;
	private JLabel lblAvailableClasses;
	private JButton removePrinterButton;
	private JList classList ;
	private JLabel backButton;
}// end of class
