package ObjectLabEnterpriseSoftware;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
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
    private BalanceView balanceView;
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
    private DefaultTableModel fileTableModel3; //Used to hold builds that have already been created for the selected device
    
    private Device deviceModel = null;

    FileManager inst;

    
    //this function is never called
    //Rajewski
    /*private static void updateViewData(DefaultTableModel model, ArrayList<ArrayList<Object>> view)
    {
        /* Clears up the rows in the view's model. 
        for (int rows = model.getRowCount() - 1; rows >= 0; rows--)
        {
            model.removeRow(rows);
        }

        /* Inserts data found in (ArrayList -> listOfRows) by row into the UI model to display 
        for (ArrayList<Object> row : view)
        {
            /* We need to account for the check box by adding in a boolean value = false as the first value. 
            if(UtilController.findAndVerifyFile((String)(row.get(1)))){
                row.add(0, (Boolean) false);
                model.addRow(row.toArray());
            }
            //System.out.println(row.get(1));
        }
    }
    */
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

        //Rajewski
        //Removed becuase no more device input table
        /* Now that a printer has been selected, build file location, and files that are part of the build we can validate 
         the input for the build data 
               
        for (int column = 0; column < deviceInputTable.getColumnCount(); column++)
        {
            // Test the column input to see type /
            int testColumnInput = InputValidation.getDataType((String) deviceInputTable.getValueAt(0, column));
            // Ask Device model which type the column SHOULD be /
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
        */
        
        return true;
    }

    //Rajewski
    //Does not need to be called
    /*
    /based entirely on nonexistent deviceInputTable
    /private boolean preprocessDataForSubmit()
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
    */
    
    private boolean submit()
    {
        countNumOfModels = 0;
        String filePathToBuildFile = "";
        ArrayList<Integer> selectedJobID;

        if (getAndValididateUserInput())
        {
                //Rajewski
                //no need to call this now without the input table
                //if(preprocessDataForSubmit())
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
            for (int row = 0; row < submissionsToBuildList.getRowCount(); row++)
            {
                if (submissionsToBuildList.getValueAt(row, 0) != null) /* If checked then add file to list */ // has to be in the second table in order for a file to be successfully submitted 
                {
                    selectedJobID.add(Integer.parseInt((String) submissionsToBuildList.getValueAt(row, 0)));
                    countNumOfModels++;
                }
            }
            System.out.println(selectedJobID + deviceModel.getDeviceName() + filePathToBuildFile + countNumOfModels);
            
            //Russell- Joe
            //After clicking submit, you get to here. It passes all other error checks when you have at least 
            //one file in the right hand table, and one build file selected via Browse
            //submitBuild is in UtilController
            if(UtilController.submitBuild(selectedJobID, deviceModel, filePathToBuildFile, countNumOfModels))
            {
                JOptionPane.showMessageDialog(null, "Build successfully created");
                return true;
            }
                //}
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
        //Rajewski
        //no more need for deviceInputTable calls
        //deviceInputTable.setVisible(false);

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

        fileTableModel = (DefaultTableModel) studentSubmissionApprovedTableList.getModel();
        fileTableModel2 =  (DefaultTableModel) submissionsToBuildList.getModel(); //added 
        fileTableModel3 = (DefaultTableModel) buildsForCurrentDeviceTable.getModel();
        
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
        studentSubmissionApprovedTableList = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        deviceNameComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        backToMainMenu = new javax.swing.JButton();
        buildFileLocationErrorStatusText = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        buildsForCurrentDeviceTable = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        submissionsToBuildList = new javax.swing.JTable();
        swapButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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
        getContentPane().add(Submit_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 550, 100, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Choose jobs part of build: ");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 200, 19));

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

        studentSubmissionApprovedTableList.setAutoCreateRowSorter(true);
        studentSubmissionApprovedTableList.setModel(new javax.swing.table.DefaultTableModel()
            {
                Class[] types = new Class []
                {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                    ,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                };
                boolean[] canEdit = new boolean []
                {
                    false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex)
                {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex)
                {
                    return canEdit [columnIndex];
                }

            });
            studentSubmissionApprovedTableList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            studentSubmissionApprovedTableList.setShowVerticalLines(false);
            studentSubmissionApprovedTableList.getTableHeader().setReorderingAllowed(false);
            jScrollPane3.setViewportView(studentSubmissionApprovedTableList);
            studentSubmissionApprovedTableList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 400, 210));

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

            jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jLabel3.setText("Previous Builds for Device:");
            getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 150, 20));

            backToMainMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/back_arrow_button.png"))); // NOI18N
            backToMainMenu.setToolTipText("Back");
            backToMainMenu.setBorderPainted(false);
            backToMainMenu.setContentAreaFilled(false);
            backToMainMenu.setFocusPainted(false);
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

            buildsForCurrentDeviceTable.setAutoCreateRowSorter(true);
            buildsForCurrentDeviceTable.setModel(new javax.swing.table.DefaultTableModel()
                {
                    Class[] types = new Class []
                    {
                        java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                    };

                    boolean[] canEdit = new boolean []
                    {
                        false, false, false
                    };

                    public Class getColumnClass(int columnIndex)
                    {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex)
                    {
                        return false;
                        //Rajewski
                        //no editing
                        //return canEdit [columnIndex];
                    }
                });
                buildsForCurrentDeviceTable.getTableHeader().setReorderingAllowed(false);
                jScrollPane4.setViewportView(buildsForCurrentDeviceTable);

                getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 950, 150));

                submissionsToBuildList.setModel(new javax.swing.table.DefaultTableModel()
                    {
                        Class[] types = new Class []
                        {
                            java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                            ,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                        };
                        boolean[] canEdit = new boolean []
                        {
                            false, false, false, false, false, false, false, false
                        };

                        public Class getColumnClass(int columnIndex)
                        {
                            return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex)
                        {
                            return canEdit [columnIndex];
                        }

                    });
                    submissionsToBuildList.getTableHeader().setReorderingAllowed(false);
                    jScrollPane6.setViewportView(submissionsToBuildList);
                    submissionsToBuildList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

                    getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 430, 210));

                    swapButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/swap3.png"))); // NOI18N
                    swapButton.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            swapButtonMouseClicked(evt);
                        }
                    });
                    swapButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            swapButtonActionPerformed(evt);
                        }
                    });
                    getContentPane().add(swapButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, 80, 40));

                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/white_bg.jpg"))); // NOI18N
                    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 630));
                    setJMenuBar(jMenuBar2);

                    pack();
                    setLocationRelativeTo(null);
                }// </editor-fold>//GEN-END:initComponents
    

    private void initNavBar()
    {

    	jMenuBar2.setPreferredSize(new Dimension(275, 30));
        setJMenuBar(jMenuBar2);
        
        navBtn_jobsMgr = new JButton("Jobs Manager");
        navBtn_jobsMgr.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/view_file_icon.png")));
        navBtn_jobsMgr.setPreferredSize(new Dimension(100,24));
        
        jMenuBar2.add(Box.createRigidArea(new Dimension(42,12)));
        jMenuBar2.add(navBtn_jobsMgr);
        
        navBtn_build = new JButton("Enter Build");
        navBtn_build.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/hammer_icon.png")));
        navBtn_build.setPreferredSize(new Dimension(100,24));
        jMenuBar2.add(navBtn_build);
        
        navBtn_reports = new JButton("Reports");
        navBtn_reports.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/reports_icon.png")));
        navBtn_reports.setPreferredSize(new Dimension(100,24));
        jMenuBar2.add(navBtn_reports);
        
        navBtn_balance = new JButton("Balance");
        navBtn_balance.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/stats_icon.png")));
	navBtn_balance.setPreferredSize(new Dimension(100,24));

        jMenuBar2.add(navBtn_balance);

        
        navBtn_settings = new JButton("Settings");
        navBtn_settings.setIcon(new ImageIcon(JobsView.class.getResource("/ObjectLabEnterpriseSoftware/images/cog_icon.png")));
        navBtn_settings.setPreferredSize(new Dimension(100,24));
        jMenuBar2.add(navBtn_settings);
        
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
    
    
    
    /////// Nav Bar ~Alex /////
    
    
    private void Submit_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Submit_ButtonActionPerformed
        //add stl information to build table zcorp and create incomplete entry
        if (!submit())
        {
            JOptionPane.showMessageDialog(new JPanel(), "Submit failed.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else
        {
            dispose();
            //Rajewski
            //Will now reload build view instead of go to jobs manager.
            //home.resetAdminMode();
            navBtn_buildActionPerformed(evt);
        }
    }//GEN-LAST:event_Submit_ButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
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
    //this might be what brings up the page
    //Rajewski
    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser chooser = new JFileChooser();//Select Default
        ///?
        //Rajewski
        File defaultBuildDirectory;
        String device = (String)deviceNameComboBox.getSelectedItem();
        device.toLowerCase();
        //RAJEWSKI
        //WHAT DOES THIS DO?
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
            //checks that new build file is unique
            if (!UtilController.isBuildFileLocationUsed(filepathToSelectedDeviceBuild.getText()))
            {
                //Rajewski
                //create new function for making the list of builds table visible/populated for selected device
                //call it here
                //setupUserInputBuildData();
            } else
            {
                filepathToSelectedDeviceBuild.setText("");
                buildFileLocationErrorStatusText.setText("Select a unique build file location");
                buildFileLocationErrorStatusText.setVisible(true);

                //Rajewski
                //deviceInputTable was removed and replaced with table of previous builds
                
                //invalidBuildLocationSelectedColumnModel.setColumnIdentifiers(errorTextColumnHeader);
                //deviceInputTable.setModel(invalidBuildLocationSelectedColumnModel);
                //deviceInputTable.setVisible(false);
            }
        }
    }//GEN-LAST:event_browseBtnActionPerformed

    //Rajewski
    //Removed because no longer used/needed for input table
    /*
    private void setupUserInputBuildData()
    {
        buildFileLocationErrorStatusText.setVisible(false);
        deviceModel.addField("Run time", 0); /* Should later remove this and make it a seperate parameter in the function submitBuild call (so the backend knows less about how the UI stores its data) 
        
        deviceModel.addField("Hours", 0);
        deviceModel.addField("Minutes", 0);
        deviceModel.addField("Seconds", 0);
        
        trackableFields = deviceModel.getFieldNames();
        deviceDataModel = new DefaultTableModel(trackableFields.toArray(), 1);
        
        deviceInputTable.setModel(deviceDataModel);
        deviceInputTable.setVisible(true);
    }
    */ 
    
    private void deviceNameComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deviceNameComboBoxActionPerformed
    {//GEN-HEADEREND:event_deviceNameComboBoxActionPerformed
        /* When a device is selected we put the info into the Device class and then detrmine how we update our view from here 
         From here we can determine how we update our display and what type of data we require from the user as well as the
         column data to display.
         */
    	
    	
    	clearEntries(fileTableModel);
    	clearEntries(fileTableModel2);
        clearEntries(fileTableModel3);
    	
        studentSubmissionApprovedTableList.setRowSelectionAllowed(true);
        studentSubmissionApprovedTableList.setColumnSelectionAllowed(true);
        studentSubmissionApprovedTableList.setCellSelectionEnabled(false);
        studentSubmissionApprovedTableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                
        submissionsToBuildList.setRowSelectionAllowed(true);
        submissionsToBuildList.setColumnSelectionAllowed(true);
        submissionsToBuildList.setCellSelectionEnabled(false);
    	submissionsToBuildList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
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
                	fileTableModel.addRow(new Object[] {approvedStudentSubmissions.get(i).get(0), approvedStudentSubmissions.get(i).get(1),
                			approvedStudentSubmissions.get(i).get(2), approvedStudentSubmissions.get(i).get(3), approvedStudentSubmissions.get(i).get(4),
                			approvedStudentSubmissions.get(i).get(5), approvedStudentSubmissions.get(i).get(6), approvedStudentSubmissions.get(i).get(7)
                	});
                        //Rajewski
                        //development purposes
                        /*System.out.println(approvedStudentSubmissions.get(i).get(0) + "\t" + approvedStudentSubmissions.get(i).get(1) + "\t" +
                			approvedStudentSubmissions.get(i).get(2) + "\t" + approvedStudentSubmissions.get(i).get(3) + "\t" + approvedStudentSubmissions.get(i).get(4) + "\t" +
                			approvedStudentSubmissions.get(i).get(5) + "\t" + approvedStudentSubmissions.get(i).get(6) + "\t" + approvedStudentSubmissions.get(i).get(7));*/
                }

                submissionsToBuildList.setModel(fileTableModel2);
                studentSubmissionApprovedTableList.setModel(fileTableModel);
                submissionsToBuildList.setVisible(true);
                studentSubmissionApprovedTableList.setVisible(true);
                buildLbl.setVisible(true);
                browseBtn.setVisible(true);
                filepathToSelectedDeviceBuild.setVisible(true);
                repaint();
            } else
            {
                fileTableModel.setColumnIdentifiers(new String[]
                {
                    "There are no approved student submissions for the " + deviceModel.getDeviceName()
                });
                submissionsToBuildList.setVisible(false);
                studentSubmissionApprovedTableList.setVisible(false);
                buildLbl.setVisible(false);
                browseBtn.setVisible(false);
                filepathToSelectedDeviceBuild.setVisible(false);
            }

        } else
        {
            //Rajewski
            //deleted function
            //setupUserInputBuildData();
            studentSubmissionTracked = false;

            submissionsToBuildList.setVisible(false);
            studentSubmissionApprovedTableList.setVisible(false);
            filepathToSelectedDeviceBuild.setVisible(false);
            buildLbl.setVisible(false);
            browseBtn.setVisible(false);

            fileTableModel.setColumnIdentifiers(new String[]
            {
                "Student submission for the " + deviceModel.getDeviceName() + " was added to Opt-Out of approval/denal of jobs"
            });
        }
        
        setupBuildRecordsTable();
        
    }//GEN-LAST:event_deviceNameComboBoxActionPerformed

    private void setupBuildRecordsTable(){
        //Rajewski
        //Setting up table of previous builds
        fileTableModel3.setColumnIdentifiers(new String[]
            {
                "Build File Name", "Date", "Number of Files" 

            });
        
        String currentDevice = (String) deviceNameComboBox.getSelectedItem();
        ArrayList<ArrayList<Object>> buildList = UtilController.arrayListOfBuilds(currentDevice);
        for(int i = 0; i < buildList.size(); i++)
            {
                    fileTableModel3.addRow(new Object[] { buildList.get(i).get(0), buildList.get(i).get(1),
                                    buildList.get(i).get(2)
                    });
                    //Rajewski
                    //line for testing
                    //System.out.println(buildList.get(i).get(0) + "\t" + buildList.get(i).get(1) + "\t" + buildList.get(i).get(2));
            }
        
    }
    
    private void backToMainMenuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backToMainMenuActionPerformed
    {//GEN-HEADEREND:event_backToMainMenuActionPerformed
        dispose();
        home.resetAdminMode();
    }//GEN-LAST:event_backToMainMenuActionPerformed

    private void swapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swapButtonActionPerformed
        
    }//GEN-LAST:event_swapButtonActionPerformed

    private void swapButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swapButtonMouseClicked
       // TODO add your handling code here:
        //Rajewski
        //Optimize this function later
        /*studentSubmissionApprovedTableList.setRowSelectionAllowed(true);
        studentSubmissionApprovedTableList.setColumnSelectionAllowed(true);
        studentSubmissionApprovedTableList.setCellSelectionEnabled(true);
        studentSubmissionApprovedTableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                
        submissionsToBuildList.setRowSelectionAllowed(true);
        submissionsToBuildList.setColumnSelectionAllowed(true);
        submissionsToBuildList.setCellSelectionEnabled(true);
    	submissionsToBuildList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        */
        //Rajewski
        //This is not good error checking.
        /*
        if (studentSubmissionApprovedTableList.equals(submissionsToBuildList.getSelectedRow())) //believe this check works //No, it does not Spring 2016. - Rajewski
        {
                JOptionPane.showMessageDialog(null, "File is already in the current jobs list!",
                                "Add Error", JOptionPane.ERROR_MESSAGE);
        } 
	*/
        
    	/*if(submissionsToBuildList.getSelectedRow() == 0)
    	{
    		((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(submissionsToBuildList.getSelectedRow());
			((MutableComboBoxModel) submissionsToBuildList).removeElement(submissionsToBuildList.getSelectedRow());
    	}*/
		
        //removed else if block becuase not having a selection could still mean that the other table has a selected row
        /*
        else if(submissionsToBuildList.getSelectedRow() == -1)
            {
                    JOptionPane.showMessageDialog(null, "No file selected!",
                                    "Add Error", JOptionPane.ERROR_MESSAGE);
            }
        */
                //gets all info for selected row to add to build table
        
        int k = 0;

        while(k < studentSubmissionApprovedTableList.getRowCount()){
            if(studentSubmissionApprovedTableList.isRowSelected(k)){
                fileTableModel2.addRow(new Object[] { 
                studentSubmissionApprovedTableList.getValueAt(k, 0), 
                studentSubmissionApprovedTableList.getValueAt(k, 1),	
                studentSubmissionApprovedTableList.getValueAt(k, 2),
                studentSubmissionApprovedTableList.getValueAt(k, 3),
                studentSubmissionApprovedTableList.getValueAt(k, 4),
                studentSubmissionApprovedTableList.getValueAt(k, 5),
                studentSubmissionApprovedTableList.getValueAt(k, 6),
                studentSubmissionApprovedTableList.getValueAt(k, 7)
                });
            fileTableModel.removeRow(k);
            k--;
            }
            k++;
            System.out.print(k + "\t");
        }
        System.out.println();

        studentSubmissionApprovedTableList.setModel(fileTableModel);
        submissionsToBuildList.setModel(fileTableModel2);

        /*
         * 
         * removeClassListModel.addElement(currentClassListRC.getSelectedValue());
        currentDeviceListModel.removeElement(currentClassListRC.getSelectedValue());
        //((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(submissionsToBuildList.getSelectedRow());
        //((MutableComboBoxModel) submissionsToBuildList).removeElement(submissionsToBuildList.getSelectedRow());
        //removeClassList.setModel(removeClassListModel);
         * 
         * */


        //Rajewski
        //logic for moving a row from the new build table back to the approved files table
        int j = 0;

        while(j < submissionsToBuildList.getRowCount()){
            if(submissionsToBuildList.isRowSelected(j)){
                fileTableModel.addRow(new Object[] { 
                    submissionsToBuildList.getValueAt(j, 0), 
                    submissionsToBuildList.getValueAt(j, 1),	
                    submissionsToBuildList.getValueAt(j, 2),
                    submissionsToBuildList.getValueAt(j, 3),
                    submissionsToBuildList.getValueAt(j, 4),
                    submissionsToBuildList.getValueAt(j, 5),
                    submissionsToBuildList.getValueAt(j, 6),
                    submissionsToBuildList.getValueAt(j, 7)

            });
            fileTableModel2.removeRow(j);
            j--;
            }
            j++;
        }

        studentSubmissionApprovedTableList.setModel(fileTableModel);
        submissionsToBuildList.setModel(fileTableModel2);


        setupBuildRecordsTable();
    }//GEN-LAST:event_swapButtonMouseClicked
    
    
    private void addArrowActionPerformed (java.awt.event.ActionEvent evt)
	{
		if (studentSubmissionApprovedTableList.equals(submissionsToBuildList.getSelectedRow())) //believe this check works
		{
			JOptionPane.showMessageDialog(null, "File is already in the current jobs list!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		} 
		
    	/*if(submissionsToBuildList.getSelectedRow() == 0)
    	{
    		((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(submissionsToBuildList.getSelectedRow());
			((MutableComboBoxModel) submissionsToBuildList).removeElement(submissionsToBuildList.getSelectedRow());
    	}*/
		
		else if(submissionsToBuildList.getSelectedRow() == -1)
		{
			JOptionPane.showMessageDialog(null, "No file selected!",
					"Add Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			fileTableModel2.addRow(new Object[] { submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 0), 
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 1),	
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 2),
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 3),
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 4),
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 5),
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 6),
					submissionsToBuildList.getValueAt(submissionsToBuildList.getSelectedRow(), 7)
			
			});
			
			
			
			
			fileTableModel.removeRow(submissionsToBuildList.getSelectedRow());
			
			submissionsToBuildList.setModel(fileTableModel);
			studentSubmissionApprovedTableList.setModel(fileTableModel2);
			/*
			 * 
			 * removeClassListModel.addElement(currentClassListRC.getSelectedValue());
			currentDeviceListModel.removeElement(currentClassListRC.getSelectedValue());
			//((MutableComboBoxModel) studentSubmissionApprovedTableList).addElement(submissionsToBuildList.getSelectedRow());
			//((MutableComboBoxModel) submissionsToBuildList).removeElement(submissionsToBuildList.getSelectedRow());
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
		submissionsToBuildList.setModel(fileTableModel);
		studentSubmissionApprovedTableList.setModel(fileTableModel2);
    }

    private JButton navBtn_balance;

    private JButton navBtn_jobsMgr;
    private JButton navBtn_build;
    private JButton navBtn_reports;
    private JButton navBtn_settings;
    //Rajewski
    //refactored submissionsToBuildList to submissionsToBuildList
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ErrorText;
    private javax.swing.JButton Submit_Button;
    private javax.swing.JButton backToMainMenu;
    private javax.swing.JButton browseBtn;
    private javax.swing.JLabel buildFileLocationErrorStatusText;
    private javax.swing.JLabel buildLbl;
    private javax.swing.JTable buildsForCurrentDeviceTable;
    private javax.swing.JComboBox deviceNameComboBox;
    private javax.swing.JTextField filepathToSelectedDeviceBuild;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTable studentSubmissionApprovedTableList;
    private javax.swing.JTable submissionsToBuildList;
    private javax.swing.JButton swapButton;
    // End of variables declaration//GEN-END:variables
}

