package ObjectLabEnterpriseSoftware;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class BuildView extends javax.swing.JFrame
{

    private static final String NAME_OF_PAGE = "Build File Creator";
    private static MainView home = new MainView();
    private static RemoveBuildView removeWindow = new RemoveBuildView();
    private static int countNumOfModels;
    // --nav bar views ~Alex
    private BuildView buildView;
    private newJobsMgr jobsView;
    private ReportsView reportsView;	
    private newSettingsMenu adminSettingsView;
    //
    private static final DefaultTableModel invalidBuildLocationSelectedColumnModel = new DefaultTableModel();
    private static String[] errorTextColumnHeader =
    {
        "The build file selected was already selected for a previous build entry."
    };

    private static boolean studentSubmissionTracked = true;
    private ArrayList<String> trackableFields;
    private DefaultTableModel deviceDataModel; /* Used to hold data entered in by the user for the build to display */

    private DefaultTableModel fileTableModel; /* Used to hold available files (student submissions) displayed in the first table */
    private DefaultTableModel fileTableModel2; /*Used to hold current files which the add/remove come into play */
    
    private Device deviceModel = null;

    FileManager inst;

    private static void updateViewData(DefaultTableModel model, ArrayList<ArrayList<Object>> view)
    {
        /* Clears up the rows in the view's model. */
        for (int rows = model.getRowCount() - 1; rows >= 0; rows--)
        {
            model.removeRow(rows);
        }

        /* Inserts data found in (ArrayList -> listOfRows) by row into the UI model to display */
        for (ArrayList<Object> row : view)
        {
            /* We need to account for the check box by adding in a boolean value = false as the first value. */
            if(UtilController.findAndVerifyFile((String)(row.get(1)))){
                row.add(0, (Boolean) false);
                model.addRow(row.toArray());
            }
            //System.out.println(row.get(1));
        }
    }

    private void clearEntries(DefaultTableModel fileTableModel)
    {
        while (fileTableModel.getRowCount() > 0)
        {
            fileTableModel.removeRow(0);
        }
    }

    private boolean getAndValididateUserInput()
    {

        if (deviceNameComboBox.getSelectedItem().equals("") || deviceModel == null /* this is a hot fix... */)
        {
            ErrorText.setText("Select a printer from above!");
            ErrorText.setVisible(true);
            return false;
        }

        if (studentSubmissionTracked)
        {
            boolean filesSelected = false;
            /* filepathToSelectedDeviceBuild is the file location to the build file */
            if (filepathToSelectedDeviceBuild.getText().isEmpty())
            {
                ErrorText.setText("Choose a build file from Current Jobs!");
                ErrorText.setVisible(true);
                return false;
            } else
            {
                //checks to see if any selections in table exist to prevent no file submit case
                for (int z = 0; z < fileTableModel2.getRowCount(); z++) //changed to model2 so it has to be in this table in order to validate
                {
                    if (fileTableModel2.getValueAt(z, 0) != null)
                    {
                        filesSelected = true; /* At least one file was checked off for being part of the build*/

                        break;
                    }
                }

                if (!filesSelected)
                {
                    ErrorText.setText("Select Files used for build!");
                    ErrorText.setVisible(true);
                    return false;
                }
            }
        }

        /* Now that a printer has been selected, build file location, and files that are part of the build we can validate 
         the input for the build data 
         */
        for (int column = 0; column < deviceInputTable.getColumnCount(); column++)
        {
            /* Test the column input to see type */
            int testColumnInput = InputValidation.getDataType((String) deviceInputTable.getValueAt(0, column));
            /* Ask Device model which type the column SHOULD be */
            int expectedColumnInput = deviceModel.getFieldType(trackableFields.get(column));

            if (testColumnInput == -1)
            {
                ErrorText.setText("Unknown data entry for build data!");
                ErrorText.setVisible(true);
                return false;
            } else if (testColumnInput != expectedColumnInput)
            {
                ErrorText.setText("Invalid data entry for build data! Data in field " + column + " does not match expected type.");
                ErrorText.setVisible(true);
                return false;
            }
        }
        return true;
    }

    private boolean preprocessDataForSubmit()
    {
        for (int column = 0; column < deviceInputTable.getColumnCount(); column++)
        {
            if (!deviceModel.addField(trackableFields.get(column), deviceInputTable.getValueAt(0, column)))
                {
                    ErrorText.setText("Invalid data entry for build data!");
                    ErrorText.setVisible(true);
                    return false;
                }
        }
        return true;
    }
    
    private boolean submit()
    {
        countNumOfModels = 0;
        String filePathToBuildFile = "";
        ArrayList<Integer> selectedJobID;

        if (getAndValididateUserInput())
        {
            if(preprocessDataForSubmit())
            {
                ErrorText.setVisible(false);
                if (studentSubmissionTracked)
                {
                    filePathToBuildFile = filepathToSelectedDeviceBuild.getText();
                } else
                {
                    filePathToBuildFile = "not_tracked_" + UtilController.getCurrentTimeFromDB();
                }
                selectedJobID = new ArrayList<>();

                /* "", "Job ID", "File name", "First name", "Last name", "Submission date", "Printer name", "Class name", "Class section" */
                for (int row = 0; row < fileTableModel2.getRowCount(); row++)
                {
                    if (fileTableModel2.getValueAt(row, 0) != null) /* If checked then add file to list */ // has to be in the second table in order for a file to be successfully submitted 
                    {
                        selectedJobID.add(Integer.parseInt((String) fileTableModel2.getValueAt(row, 0)));
                        countNumOfModels++;
                    }
                }
                if(UtilController.submitBuild(selectedJobID, deviceModel, filePathToBuildFile, countNumOfModels))
                {
                	JOptionPane.showMessageDialog(null, "Build successfully created");
                	return true;
                }
            }
        }
        return false;
    }

    public void startMakeBuildProcess()
    {
        inst = new FileManager();
        initComponents();
        // --nav bar views ~Alex

        initNavBar();
        //
        
        buildFileLocationErrorStatusText.setVisible(false);
        deviceInputTable.setVisible(false);

        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Windows".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(BuildView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        countNumOfModels = 0;
        fileTableModel = (DefaultTableModel) studentSubmissionTableList.getModel();
        fileTableModel2 = (DefaultTableModel) studentSubmissionApprovedTableList.getModel(); //added 

        
        ErrorText.setVisible(false);
        this.setVisible(true);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // close sockets, etc
                dispose();
                home.resetAdminMode();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     * Checks if files were selected to submit
     *
     * @return boolean statement returns true if a build file is selected
     * returns false if there isn't a build file selected and aborts submit
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();
        Submit_Button = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        buildLbl = new javax.swing.JLabel();
        filepathToSelectedDeviceBuild = new javax.swing.JTextField();
        browseBtn = new javax.swing.JButton();
        ErrorText = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        studentSubmissionTableList = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        deviceNameComboBox = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        deviceInputTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        backToMainMenu = new javax.swing.JButton();
        buildFileLocationErrorStatusText = new javax.swing.JLabel();
        runtimeLabel = new javax.swing.JLabel();
        hourField = new javax.swing.JTextField();
        secondField = new javax.swing.JTextField();
        minuteField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        removeBuildOpen = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        userGuide = new javax.swing.JMenuItem();
        addArrow = new javax.swing.JButton();
        removeArrow = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        studentSubmissionApprovedTableList = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        logoutButton = new javax.swing.JButton();
        
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Art 101-001\nArt 201-002\nArt 401-004\nArt 501-005\nArt 601-006\nArt 701-007\nArt 801-009");
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane5.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(UtilController.getPageName(NAME_OF_PAGE));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 950, 10));

        Submit_Button.setBackground(new java.awt.Color(0, 255, 0));
        Submit_Button.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Submit_Button.setText("Submit Build");
        Submit_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Submit_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(Submit_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 700, 100, 30));
        
        logoutButton.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        getContentPane().add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 700, 100, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Available Jobs: ");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 200, 19));

        buildLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        buildLbl.setText("Build File Name:");
        getContentPane().add(buildLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        filepathToSelectedDeviceBuild.setEditable(false);
        filepathToSelectedDeviceBuild.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        filepathToSelectedDeviceBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filepathToSelectedDeviceBuildActionPerformed(evt);
            }
        });
        getContentPane().add(filepathToSelectedDeviceBuild, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 200, -1));

        browseBtn.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        browseBtn.setText("Browse");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });
        getContentPane().add(browseBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 70, 20));

        ErrorText.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        ErrorText.setForeground(new java.awt.Color(255, 0, 0));
        ErrorText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ErrorText.setText("Error Text");
        getContentPane().add(ErrorText, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 760, -1));

        studentSubmissionTableList.setAutoCreateRowSorter(true);
       
            jScrollPane3.setViewportView(studentSubmissionTableList);
            if (studentSubmissionTableList.getColumnModel().getColumnCount() > 0) {
                studentSubmissionTableList.getColumnModel().getColumn(0).setMinWidth(30);
                studentSubmissionTableList.getColumnModel().getColumn(0).setMaxWidth(30);
                studentSubmissionTableList.getColumnModel().getColumn(1).setResizable(false);
                studentSubmissionTableList.getColumnModel().getColumn(2).setResizable(false);
            }
            getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 415, 350));

            
            //added to create the current jobs table 
            jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jLabel11.setText("Current Jobs:");
            getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 120, 150, 20));            
            studentSubmissionApprovedTableList.setAutoCreateRowSorter(true);
                jScrollPane6.setViewportView(studentSubmissionApprovedTableList);
                if (studentSubmissionApprovedTableList.getColumnModel().getColumnCount() > 0) {
                    studentSubmissionApprovedTableList.getColumnModel().getColumn(0).setMinWidth(30);
                    studentSubmissionApprovedTableList.getColumnModel().getColumn(0).setMaxWidth(30);
                    studentSubmissionApprovedTableList.getColumnModel().getColumn(1).setResizable(false);
                    studentSubmissionApprovedTableList.getColumnModel().getColumn(2).setResizable(false);
                }
                getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 140, 415, 350));
                
            jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
            jLabel2.setText("Select Device:");
            getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

            deviceNameComboBox.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            deviceNameComboBox.setModel(new javax.swing.DefaultComboBoxModel(UtilController.arrayListToStringArray(UtilController.getListOfCurrentDevices())));
            deviceNameComboBox.setSelectedItem(null);
            deviceNameComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deviceNameComboBoxActionPerformed(evt);
                }
            });
            getContentPane().add(deviceNameComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 170, -1));

            deviceInputTable.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
            deviceInputTable.setModel(new javax.swing.table.DefaultTableModel(new Object[]{}, 1)
            );
            deviceInputTable.setRowHeight(24);
            jScrollPane4.setViewportView(deviceInputTable);

            getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 525, 950, 150));

            jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jLabel3.setText("Enter Build Data:");
            getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 150, 20));

            backToMainMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/back_arrow_button.png"))); // NOI18N
            backToMainMenu.setToolTipText("Back");
            backToMainMenu.setBorderPainted(false);
            backToMainMenu.setContentAreaFilled(false);
            backToMainMenu.setFocusPainted(false);
            backToMainMenu.setVisible(false);
            backToMainMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    backToMainMenuActionPerformed(evt);
                }
            });
            getContentPane().add(backToMainMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 40, 40));

            buildFileLocationErrorStatusText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
            buildFileLocationErrorStatusText.setForeground(new java.awt.Color(255, 0, 0));
            buildFileLocationErrorStatusText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            buildFileLocationErrorStatusText.setLabelFor(browseBtn);
            buildFileLocationErrorStatusText.setText("Error Text For build location");
            buildFileLocationErrorStatusText.setToolTipText("");
            getContentPane().add(buildFileLocationErrorStatusText, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 70, 520, 20));

            runtimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
            runtimeLabel.setText("Total Runtime:");
            //getContentPane().add(runtimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 700, -1, 20));

            hourField.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            hourField.setText("00");
          //  getContentPane().add(hourField, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 700, 20, -1));

            secondField.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            secondField.setText("00");
            secondField.setPreferredSize(new java.awt.Dimension(20, 20));
         //   getContentPane().add(secondField, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 700, -1, -1));

            minuteField.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            minuteField.setText("00");
            minuteField.setPreferredSize(new java.awt.Dimension(20, 20));
            minuteField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    minuteFieldActionPerformed(evt);
                }
            });
          //  getContentPane().add(minuteField, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 700, -1, -1));

            jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel5.setText(":");
           // getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 700, 10, 20));

            jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel6.setText(":");
           // getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 700, 10, 20));

            jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
            jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel7.setText("H");
          // getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 720, 20, -1));

            jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
            jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel8.setText("M");
          //  getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 720, 20, -1));

            jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
            jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel9.setText("S");
          //  getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 720, 20, -1));
            
            addArrow.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            addArrow.setText("--->");
            addArrow.setPreferredSize(new java.awt.Dimension(60, 23));
            addArrow.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addArrowActionPerformed(evt);
                }
            });
            getContentPane().add(addArrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 240, 90, -1));
     
            removeArrow.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
            removeArrow.setText("<---");
            removeArrow.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeArrowActionPerformed(evt);
                }
            });
            getContentPane().add(removeArrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 270, 90, -1));

            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/white_bg.jpg"))); // NOI18N
            getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -26, 980, 900));


            getContentPane().setPreferredSize(new Dimension(975,800));
            pack();
            setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents
    

    private void initNavBar()
    {

    	jMenuBar1.setPreferredSize(new Dimension(275, 30));
        setJMenuBar(jMenuBar1);
        
        navBtn_jobsMgr = new JButton("Jobs Manager");
        navBtn_jobsMgr.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/view_file_icon.png")));
        navBtn_jobsMgr.setPreferredSize(new Dimension(100,24));
        navBtn_jobsMgr.setAlignmentX(jScrollPane2.CENTER_ALIGNMENT);
        
        jMenuBar1.add(Box.createRigidArea(new Dimension(275,0)));
        jMenuBar1.add(navBtn_jobsMgr);
        
        navBtn_build = new JButton("Enter Build");
        navBtn_build.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/hammer_icon.png")));
        
        navBtn_build.setPreferredSize(new Dimension(100,24));
        navBtn_build.setAlignmentX(jScrollPane2.CENTER_ALIGNMENT);
        jMenuBar1.add(navBtn_build);
        
        navBtn_reports = new JButton("Reports");
        navBtn_reports.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/reports_icon.png")));
        navBtn_reports.setPreferredSize(new Dimension(100,24));
        navBtn_reports.setAlignmentX(jScrollPane2.CENTER_ALIGNMENT);
        jMenuBar1.add(navBtn_reports);
        
        navBtn_settings = new JButton("Settings");
        navBtn_settings.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/cog_icon.png")));
        navBtn_settings.setPreferredSize(new Dimension(100,24));
        navBtn_settings.setAlignmentX(jScrollPane2.CENTER_ALIGNMENT);
        jMenuBar1.add(navBtn_settings);

        
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
    
    
    
    /////// Nav Bar ~Alex /////
    
    
    private void Submit_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Submit_ButtonActionPerformed
        //add stl information to build table zcorp and create incomplete entry
        if (!submit())
        {
            JOptionPane.showMessageDialog(new JPanel(), "Submit failed.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else
        {
            dispose();
            home.resetAdminMode();
        }
    }//GEN-LAST:event_Submit_ButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_logoutButtonActionPerformed
        //GEN-HEADEREND:event_logoutButtonActionPerformed
    		dispose();
            home.setVisible(true);
    }//GEN-LAST:event_logoutButtonActionPerformed
    
    private void filepathToSelectedDeviceBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filepathToSelectedDeviceBuildActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filepathToSelectedDeviceBuildActionPerformed

    /**
     * Handles creating the file browser when browsing
     *
     * @param evt
     */
    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser chooser = new JFileChooser();//Select Default
        File defaultBuildDirectory;
        String device = (String)deviceNameComboBox.getSelectedItem();
        device.toLowerCase();
        defaultBuildDirectory = new File(FileManager.getDeviceToPrint(device));
        chooser.setPreferredSize(new Dimension(800, 500));
        chooser.setCurrentDirectory(defaultBuildDirectory);
        int returnVal = chooser.showDialog(null, "Select");

        if (returnVal == chooser.APPROVE_OPTION)
        {
            File myFile = chooser.getSelectedFile();
            //String fileName = myFile.getName();
            filepathToSelectedDeviceBuild.setText(myFile.getAbsolutePath().replaceAll("'", ""));
        }

        if (!filepathToSelectedDeviceBuild.getText().isEmpty() && deviceModel != null)
        {
            if (!UtilController.isBuildFileLocationUsed(filepathToSelectedDeviceBuild.getText()))
            {
                setupUserInputBuildData();
            } else
            {
                filepathToSelectedDeviceBuild.setText("");
                buildFileLocationErrorStatusText.setText("Select a unique build file location");
                buildFileLocationErrorStatusText.setVisible(true);

                invalidBuildLocationSelectedColumnModel.setColumnIdentifiers(errorTextColumnHeader);
                deviceInputTable.setModel(invalidBuildLocationSelectedColumnModel);
                deviceInputTable.setVisible(false);
            }
        }
    }//GEN-LAST:event_browseBtnActionPerformed

    private void userGuideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userGuideActionPerformed
        // TODO add your handling code here:
        UtilController.openAdminHelpPage();
    }//GEN-LAST:event_userGuideActionPerformed

    private void setupUserInputBuildData()
    {
        buildFileLocationErrorStatusText.setVisible(false);
        deviceModel.addField("Run time", 0); /* Should later remove this and make it a seperate parameter in the function submitBuild call (so the backend knows less about how the UI stores its data) */
        /*
        deviceModel.addField("Hours", 0);
        deviceModel.addField("Minutes", 0);
        deviceModel.addField("Seconds", 0);
        */
        trackableFields = deviceModel.getFieldNames();
        deviceDataModel = new DefaultTableModel(trackableFields.toArray(), 1);
        deviceInputTable.setModel(deviceDataModel);
        deviceInputTable.setVisible(true);
    }
    private void deviceNameComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deviceNameComboBoxActionPerformed
    {//GEN-HEADEREND:event_deviceNameComboBoxActionPerformed
        /* When a device is selected we put the info into the Device class and then detrmine how we update our view from here 
         From here we can determine how we update our display and what type of data we require from the user as well as the
         column data to display.
         */
    	
    	
    	clearEntries(fileTableModel);
    	clearEntries(fileTableModel2);
    	
    	
        deviceModel = UtilController.getPrinterInfo((String) deviceNameComboBox.getSelectedItem());

        if (deviceModel.getTrackSubmission())
        {
            ArrayList<ArrayList<Object>> approvedStudentSubmissions = UtilController.returnApprovedStudentSubmissionsForDevice(deviceModel.getDeviceName());

            if (approvedStudentSubmissions.size() > 0)
            {
                fileTableModel.setColumnIdentifiers(new String[]
                {
                    "Job ID", "File name", "First name", "Last name", "Submission date", "Printer name", "Class name", "Class section"
                });
                
                fileTableModel2.setColumnIdentifiers(new String[]
                {
                    "Job ID", "File name", "First name", "Last name", "Submission date", "Printer name", "Class name", "Class section"
                });
                for(int i = 0; i < approvedStudentSubmissions.size(); i++)
                {
                	
                	fileTableModel.addRow(new Object[] { approvedStudentSubmissions.get(i).get(0), approvedStudentSubmissions.get(i).get(1),
                			approvedStudentSubmissions.get(i).get(2), approvedStudentSubmissions.get(i).get(3), approvedStudentSubmissions.get(i).get(4),
                			approvedStudentSubmissions.get(i).get(5), approvedStudentSubmissions.get(i).get(6), approvedStudentSubmissions.get(i).get(7)

                	});
                	
                	
                }
                /*
                studentSubmissionTableList.setModel(fileTableModel);
                studentSubmissionApprovedTableList.setModel(fileTableModel2);
                studentSubmissionTableList.setVisible(true);
                studentSubmissionApprovedTableList.setVisible(true);
                buildLbl.setVisible(true);
                browseBtn.setVisible(true);
                filepathToSelectedDeviceBuild.setVisible(true);
                repaint();*/
            } else
            {
                fileTableModel.setColumnIdentifiers(new String[]
                {
                    "There are no approved student submissions for the " + deviceModel.getDeviceName()
                });
                studentSubmissionTableList.setVisible(false);
                studentSubmissionApprovedTableList.setVisible(false);
                buildLbl.setVisible(false);
                browseBtn.setVisible(false);
                filepathToSelectedDeviceBuild.setVisible(false);
            }

        } else
        {
            setupUserInputBuildData();
            studentSubmissionTracked = false;

            studentSubmissionTableList.setVisible(false);
            studentSubmissionApprovedTableList.setVisible(false);
            filepathToSelectedDeviceBuild.setVisible(false);
            buildLbl.setVisible(false);
            browseBtn.setVisible(false);

            fileTableModel.setColumnIdentifiers(new String[]
            {
                "Student submission for the " + deviceModel.getDeviceName() + " was added to Opt-Out of approval/denal of jobs"
            });
        }
    }//GEN-LAST:event_deviceNameComboBoxActionPerformed

    private void backToMainMenuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backToMainMenuActionPerformed
    {//GEN-HEADEREND:event_backToMainMenuActionPerformed
        dispose();
        home.resetAdminMode();
    }//GEN-LAST:event_backToMainMenuActionPerformed

    private void removeBuildOpenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeBuildOpenActionPerformed
    {//GEN-HEADEREND:event_removeBuildOpenActionPerformed

    }//GEN-LAST:event_removeBuildOpenActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
		dispose();
		removeWindow.init();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void minuteFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minuteFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minuteFieldActionPerformed
    
    
    private void addArrowActionPerformed (java.awt.event.ActionEvent evt)
	{
		if (studentSubmissionApprovedTableList.equals(studentSubmissionTableList.getSelectedRow())) //believe this check works
		{
			JOptionPane.showMessageDialog(null, "File is already in the current jobs list!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} 
		
    	/*if(studentSubmissionTableList.getSelectedRow() == 0)
    	{
    		((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(studentSubmissionTableList.getSelectedRow());
			((MutableComboBoxModel) studentSubmissionTableList).removeElement(studentSubmissionTableList.getSelectedRow());
    	}*/
		
		else if(studentSubmissionTableList.getSelectedRow() == -1)
		{
			JOptionPane.showMessageDialog(null, "No file selected!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			fileTableModel2.addRow(new Object[] { studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 0), 
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 1),	
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 2),
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 3),
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 4),
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 5),
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 6),
					studentSubmissionTableList.getValueAt(studentSubmissionTableList.getSelectedRow(), 7)
			
			});
			
			
			
			
			fileTableModel.removeRow(studentSubmissionTableList.getSelectedRow());
			
			studentSubmissionTableList.setModel(fileTableModel);
			studentSubmissionApprovedTableList.setModel(fileTableModel2);
			/*
			 * 
			 * removeClassListModel.addElement(currentClassListRC.getSelectedValue());
			currentDeviceListModel.removeElement(currentClassListRC.getSelectedValue());
			//((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(studentSubmissionTableList.getSelectedRow());
			//((MutableComboBoxModel) studentSubmissionTableList).removeElement(studentSubmissionTableList.getSelectedRow());
			//removeClassList.setModel(removeClassListModel);
			 * 
			 * */
			 
			
		}
    }
    
    private void removeArrowActionPerformed (java.awt.event.ActionEvent evt) 
    {
		fileTableModel.addRow(new Object[] { studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 0), 
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 1),	
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 2),
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 3),
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 4),
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 5),
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 6),
				studentSubmissionApprovedTableList.getValueAt(studentSubmissionApprovedTableList.getSelectedRow(), 7)
		
		});
		
		
		
		
		fileTableModel2.removeRow(studentSubmissionApprovedTableList.getSelectedRow());
		studentSubmissionTableList.setModel(fileTableModel);
		studentSubmissionApprovedTableList.setModel(fileTableModel2);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel ErrorText;
    private javax.swing.JButton Submit_Button;
    private javax.swing.JButton backToMainMenu;
    private javax.swing.JButton browseBtn;
    private javax.swing.JLabel buildFileLocationErrorStatusText;
    private javax.swing.JLabel buildLbl;
    private javax.swing.JTable deviceInputTable;
    private javax.swing.JComboBox deviceNameComboBox;
    private javax.swing.JTextField filepathToSelectedDeviceBuild;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JTextField hourField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField minuteField;
    private javax.swing.JMenu removeBuildOpen;
    private javax.swing.JLabel runtimeLabel;
    private javax.swing.JTextField secondField;
    private javax.swing.JTable studentSubmissionTableList;
    private javax.swing.JMenuItem userGuide;
    private javax.swing.JButton addArrow;
    private javax.swing.JButton removeArrow;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JTable studentSubmissionApprovedTableList;
    private javax.swing.JScrollPane jScrollPane6;
    
    // --nav bar vars ~Alex
    private JButton navBtn_jobsMgr;
    private JButton navBtn_build;
    private JButton navBtn_reports;
    private JButton navBtn_settings;
    //
    // End of variables declaration//GEN-END:variables
}
