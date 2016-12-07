/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectLabEnterpriseSoftware;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FilenameUtils;

import javax.sql.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
/**
 *
 * @author Russell
 */
public class newStudentView extends javax.swing.JFrame {
    private static final String NAME_OF_PAGE = "Student View";
    private static DefaultTableModel model;
    private static DefaultTableModel model2;
    private static final MainView home = new MainView();
    private MaterialTransactionHistoryView materialTransView;
    private JPanel contentPane;
    private JTable table;
    private String userID;
    private String userName;
    private String projName;
    
    public newStudentView()
    {
    	//newStudentView("sturne18", "Scott");
    }
    public void newStudentView(String id, String studentName) {
        getContentPane().setBackground(Color.WHITE);
        setTitle("Student View");
        initComponents();
        //jTable3.setVisible(false);
        //scrollPane_3.setVisible(false);
        //initDragDrop();
        userID = id;
        userName = studentName;
        model = (DefaultTableModel) jTable1.getModel();
        model2 = (DefaultTableModel) jTable2.getModel();
        updateFileStatusWindow(id, studentName);
        updateMaterialWindow(id, studentName);
        setVisible(true);
    }
    public void updateMaterialWindow(String id, String studentName) {
        model2.setRowCount(0);
        
        try {
        SQLMethods dbconn = new SQLMethods();
        ResultSet queryResult;                  

        queryResult = dbconn.getStudentInfo(id); 
        
        while(queryResult.next())
        {
                String towson_id = queryResult.getString(1);
                String material1 = queryResult.getString(2);
                String material2 = queryResult.getString(3);
                String material3  = queryResult.getString(4);
                System.out.println("Adding row...");
                model2.addRow(new Object[] {towson_id, material1, material2, material3});
        }
            jTable2.setModel(model2);
            jTable2.repaint();
            dbconn.closeDBConnection();
        } catch(Exception e)
        {
                System.out.println("Error: " + e);
        }
    }
    
    public void updateFileStatusWindow(String id, String studentName) {
        model.setRowCount(0);
        
        try {
        SQLMethods dbconn = new SQLMethods();
        ResultSet queryResult;                  

        queryResult = dbconn.getStudentFileStatus(id); 
        
        while(queryResult.next())
        {
                String file_name = queryResult.getString(1);
                String date = queryResult.getString(2);
                String comment = queryResult.getString(3);
                String status  = queryResult.getString(4);
                System.out.println("Adding row...");
                model.addRow(new Object[] {file_name, date, comment, status});
        }
            jTable1.setModel(model);
            jTable1.repaint();
            dbconn.closeDBConnection();
        } catch(Exception e)
        {
                System.out.println("Error: " + e);
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					newStudentView frame = new newStudentView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
            }
        });
    }
    
    // private void initDragDrop()
    //{
        /*JScrollPane scrollPane_3 = new JScrollPane((Component) null);
        contentPane = new JPanel();
		scrollPane_3.setBounds(65, 220, 320, 150);
		contentPane.add(scrollPane_3);
		table = new JTable();
                // # rows from start
		table.setModel(new DefaultTableModel(new Object[][] {},
                        // Column Name
                        new String[] {"File Name", "File Path"}));
                table.setAutoCreateRowSorter(true);
		scrollPane_3.setViewportView(table);
		table.setShowGrid(true);
                table.setShowHorizontalLines(true);
                table.setShowVerticalLines(true);
                table.setGridColor(Color.GRAY);

                table.setFillsViewportHeight(true);
                table.setPreferredSize(new Dimension(320, 150));*/

                /*
                jTable3.setDropTarget(new DropTarget() {
                @Override
                public synchronized void dragOver(DropTargetDragEvent dtde) {
                    Point point = dtde.getLocation();
                    int row = jTable3.rowAtPoint(point);
                    if (row < 0) {
                        jTable3.clearSelection();
                    } else {
                        jTable3.setRowSelectionInterval(row, row);
                    }
                    dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                }

            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    Transferable t = dtde.getTransferable();
                    List fileList = null;
                    try {
                        fileList = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                        if (fileList.size() > 0) {
                            jTable3.clearSelection();
                            Point point = dtde.getLocation();
                            int row = jTable3.rowAtPoint(point);
                            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
                            for (Object value : fileList) {
                                if (value instanceof File) {
                                    File f = (File) value;
                                   
                                    if (row < 0) {
                                        model.addRow(new Object[]{f.getName(), f.getAbsolutePath()});
                                    } else {
                                        model.insertRow(row, new Object[]{f.getName(), f.getAbsolutePath()});
                                        row++;
                                    }
                                }
                            }
                        }
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    dtde.rejectDrop();
                }
            }

        });

        getContentPane().add(scrollPane_3, BorderLayout.CENTER);
    }
    */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8_StudentSubmission = new javax.swing.JLabel();
        jLabel8_StudentSubmission1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student View");
        setName("Student View"); // NOI18N
        setPreferredSize(new java.awt.Dimension(830, 455));

        jLabel8_StudentSubmission.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8_StudentSubmission.setText("Student Information");

        jLabel8_StudentSubmission1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8_StudentSubmission1.setText("Student Submission");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "File Name", "Date ", "Comment", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Logout");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Towson Id", "Z Corp Plaster", "Objet Build", "Objet Support"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel1.setText("File Status");

        jLabel2.setText("Material Balance");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel((String []) UtilController.returnAvailableClasses()));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel((String []) UtilController.returnAvailableDevicesStudentSubmissionRequired()));

        jButton2.setText("Browse");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Submit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Class:");

        jLabel4.setText("Printer:");

        jLabel5.setText("File Location:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel8_StudentSubmission1)
                        .addGap(181, 181, 181)
                        .addComponent(jLabel8_StudentSubmission))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(173, 173, 173)
                                .addComponent(jButton3)))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(165, 165, 165))
                            .addComponent(jButton1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(159, 159, 159))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8_StudentSubmission1)
                    .addComponent(jLabel8_StudentSubmission))
                .addGap(39, 39, 39)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
              dispose();
              home.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MousePressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        //Creates "JFileChooser" file browser
        JFileChooser fileopen = new JFileChooser();
        File desktop = new File(System.getProperty("user.home") + "\\Desktop");
        fileopen.setCurrentDirectory(desktop);
        //in brackets, add Syncthing directory or new Drive's address for default location
        fileopen.setAcceptAllFileFilterUsed(false);
        fileopen.setMultiSelectionEnabled(false);
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("Object Files", "obj", "zpr", "stl");
        //fileopen.setFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION)
        {
            File file = fileopen.getSelectedFile();
            //Inputs the file location into the textbox "fileName"
            jTextField1.setText(file.toString().replaceAll("'", ""));
            String projectFileName = file.getName().replaceAll("'", "");
            String finalProjectFileName = projectFileName.replaceAll("(\\.\\S+?$)", UtilController.getCurrentTimeFromDB() + "$1");
            projName = finalProjectFileName;
        }
    }//GEN-LAST:event_jButton2MousePressed

    
    private boolean errCheck()
    {
    	String fullFilePath = jTextField1.getText();
        String fileName = projName;
        String printer = (String) jComboBox2.getSelectedItem();
        /* parse classBox string and pull out the primary key and store it in an integer */
        String classText = (String) jComboBox1.getSelectedItem();

        boolean isErr = false;
        //hideErrorFields();
        if (fullFilePath == null || fullFilePath == "")
        {
        	JOptionPane.showMessageDialog(null, "Select a file.", "File Choice Empty", JOptionPane.ERROR_MESSAGE);
            isErr = true;
        }

        //End Email Validation
        else if (jComboBox1.getSelectedIndex() == -1)
        {
        	JOptionPane.showMessageDialog(null, "Select a class.", "Class Choice Empty", JOptionPane.ERROR_MESSAGE);
            isErr = true;
        }

        else if (jComboBox2.getSelectedIndex() == -1)
        {
        	JOptionPane.showMessageDialog(null, "Select a device.", "Device Choice Empty", JOptionPane.ERROR_MESSAGE);
            isErr = true;
        }
        
        else if (extensionCheck() == false)
        {
        	JOptionPane.showMessageDialog(null, "Your file extension is incorrect. No submission has occured.",
        			"Invalid Submission", JOptionPane.ERROR_MESSAGE);
        	isErr = true;
        }
        return isErr;
    }

    private boolean extensionCheck()
    {
    	boolean isValidExtension = false;
    	
    	String printer = (String) jComboBox2.getSelectedItem(); 
    	String fullFilePath = jTextField1.getText();
    	String extension = "." + FilenameUtils.getExtension(fullFilePath);
    	
    	isValidExtension = UtilController.checkExtension(printer, extension);
    	
    	return isValidExtension;
    	
    }
    
    // Submit button
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        // Verifies user and file information is inputted in its entirety
        if (errCheck() == false)
        {
            // Recieve our input from the UI and hand it off to back end to submit / store file information 
            String fullFilePath, fileName, classText, printer;
            int classFK;
            
            fullFilePath = jTextField1.getText();
            fileName = projName;
            printer = (String) jComboBox2.getSelectedItem();
            
            // parse classBox string and pull out the primary key and store it in an integer 
            classText = (String) jComboBox1.getSelectedItem();
            classFK = (Integer.parseInt(classText.split(" ")[0]));

            UtilController.submitStudentFile(userID, fullFilePath, fileName, printer, classFK);

            JOptionPane.showMessageDialog(new java.awt.Frame(), "Successfully submitted file! Let your professor or lab assistant know you've submitted.");
            dispose();
            
			//Reset view after successful submission to allow for multiple submissions without having to login each time
            Reset_StudentSubmissionFields();
            updateFileStatusWindow(userID, userName);
        } 
        else
        {
            dispose();
            Reset_StudentSubmissionFields();
        }
    }

    private void Reset_StudentSubmissionFields()
    {
        setVisible(false);
        jTextField1.setText(null);
        //projName.setText(null);
        //classBox.setSelectedItem(null);
        //printerBox.setSelectedItem(null);
        setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
       JTable table =(JTable) evt.getSource();
       Point p = evt.getPoint();
       int row = table.rowAtPoint(p);
       if (evt.getClickCount() == 2) {
           // double-click a row
           System.out.println("double clicked...");
           //System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());

           //String fName = table.getValueAt(table.getSelectedRow(), 0).toString();
           //String lName = table.getValueAt(table.getSelectedRow(), 1).toString();
           //String id = table.getValueAt(table.getSelectedRow(), 2).toString();

           materialTransView = new MaterialTransactionHistoryView();
           materialTransView.showHistory(userName, UtilController.getStudentLname(), userID);
           //dispose();
       }        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8_StudentSubmission;
    private javax.swing.JLabel jLabel8_StudentSubmission1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
