package ObjectLabEnterpriseSoftware;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Matt
 * @programmer Morgan
 */
public class Student_Submission extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    SQLMethods sqlMethods;
    FileManager inst;
    ButtonGroup group;
    String printer;
    TomSoftMain home;

    public void studentSubmissionStart() {
        inst = new FileManager();
        initComponents();
        hideErrorFields();
        
        sqlMethods = new SQLMethods();
        home = new TomSoftMain();
        
        /*
        Fetch available classes
        */
        ResultSet result2 = sqlMethods.getCurrentClasses();
        try {
            while (result2.next()) {
                classBox.addItem(result2.getString("className") + " " + result2.getString("classSection"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrinterBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Student_Submission.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // close sockets, etc
                home.studentSubmissionButton.setVisible(false);
                home.setPrintersVisible(false);
                home.setVisible(true);
            }
        });
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fileLocation = new javax.swing.JTextField();
        Browse = new javax.swing.JButton();
        lastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        emailInfo = new javax.swing.JTextField();
        projName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Student_Submit = new javax.swing.JButton();
        classBox = new javax.swing.JComboBox();
        printerBox = new javax.swing.JComboBox();
        errorTxt1 = new javax.swing.JLabel();
        errorTxt2 = new javax.swing.JLabel();
        errorTxt3 = new javax.swing.JLabel();
        errorTxt4 = new javax.swing.JLabel();
        errorTxt6 = new javax.swing.JLabel();
        errorTxt7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        firstName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        editMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Student Submission");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        setMinimumSize(new java.awt.Dimension(507, 390));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("File Location:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 95, 20));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("First Name:");
        jLabel2.setToolTipText("");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 95, 20));

        fileLocation.setEditable(false);
        getContentPane().add(fileLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 200, 20));

        Browse.setText("Browse");
        Browse.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Browse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BrowseMouseClicked(evt);
            }
        });
        Browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseActionPerformed(evt);
            }
        });
        getContentPane().add(Browse, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 73, 23));

        lastName.setToolTipText("");
        lastName.setName("lastName"); // NOI18N
        getContentPane().add(lastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 200, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Last Name:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 95, 20));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Email:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 95, 20));

        emailInfo.setName("email"); // NOI18N
        getContentPane().add(emailInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 200, -1));

        projName.setEditable(false);
        projName.setName("projName"); // NOI18N
        getContentPane().add(projName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 200, -1));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Project Name:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 95, 20));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Class:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 95, 20));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Printer:");
        jLabel7.setToolTipText("");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 95, 20));

        Student_Submit.setText("Submit");
        Student_Submit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Student_Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_SubmitActionPerformed(evt);
            }
        });
        getContentPane().add(Student_Submit, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, 73, 23));

        classBox.setSelectedItem(null);
        getContentPane().add(classBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 120, -1));

        printerBox.setModel(new javax.swing.DefaultComboBoxModel((String []) UtilController.returnAvailablePrinters()));
        printerBox.setSelectedItem(null);
        printerBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printerBoxActionPerformed(evt);
            }
        });
        getContentPane().add(printerBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 120, -1));

        errorTxt1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt1.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt1.setText("Error Text");
        getContentPane().add(errorTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, 119, 20));

        errorTxt2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt2.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt2.setText("Error Text");
        getContentPane().add(errorTxt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, 119, 20));

        errorTxt3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt3.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt3.setText("Error Text");
        getContentPane().add(errorTxt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, 119, 20));

        errorTxt4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt4.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt4.setText("Error Text");
        getContentPane().add(errorTxt4, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, 119, 20));

        errorTxt6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt6.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt6.setText("Error Text");
        getContentPane().add(errorTxt6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 119, 20));

        errorTxt7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        errorTxt7.setForeground(new java.awt.Color(255, 0, 0));
        errorTxt7.setText("Error Text");
        getContentPane().add(errorTxt7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 240, 119, 20));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 390, 10));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Student Submission");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        firstName.setName("firstName"); // NOI18N
        getContentPane().add(firstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 200, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/black and white bg.jpg"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -26, 500, 360));

        editMenu.setText("Help");

        jMenuItem1.setText("Contents");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        editMenu.add(jMenuItem1);

        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void hideErrorFields() {
        //visibility wasn't an option to set from the beginning.
        errorTxt1.setVisible(false);
        errorTxt2.setVisible(false);
        errorTxt3.setVisible(false);
        errorTxt4.setVisible(false);
        errorTxt6.setVisible(false);
        errorTxt7.setVisible(false);
    }

    private boolean errCheck() {
        boolean isErr = false;
        hideErrorFields();
        if (fileLocation.getText().equals("")) {
            errorTxt1.setVisible(true);
            errorTxt1.setText("Select a File!");
            isErr = true;
        }

        if (firstName.getText().equals("")) {
            errorTxt2.setVisible(true);
            errorTxt2.setText("Cannot be empty.");
            isErr = true;
        }

        if (lastName.getText().equals("")) {
            errorTxt3.setVisible(true);
            errorTxt3.setText("Cannot be empty.");
            isErr = true;
        }

        //Simple Email Validation Using a JAVA MAIL method
        if (emailInfo.getText().equals("")) {
            errorTxt4.setVisible(true);
            errorTxt4.setText("Cannot be empty.");
            isErr = true;
        } else {
            try {
                new InternetAddress(emailInfo.getText()).validate();
            } catch (AddressException e) {
                errorTxt4.setVisible(true);
                errorTxt4.setText("Invalid Email!");
                isErr = true;
            }
        }
        //End Email Validation

        if (classBox.getSelectedIndex() == -1) {
            errorTxt6.setVisible(true);
            errorTxt6.setText("Select Option!");
            isErr = true;
        }

        if (printerBox.getSelectedIndex() == -1) {
            errorTxt7.setVisible(true);
            errorTxt7.setText("Select Option!");
            isErr = true;
        }

		 /* This checks for a duplicate file submission by querying the pendingjobs table and then
            traversing through the ResultSet for a match from the user input and an entry in the DB.
        */
          ResultSet existing = sqlMethods.searchPending();
        try {
            while (existing.next()) {
                String temp = existing.getString("filename");
                if (temp.substring(temp.lastIndexOf("\\") + 1).equals(projName.getText())) {
                    isErr = true;
                    JOptionPane.showMessageDialog(new java.awt.Frame(), "Filename already exists. Please rename your STL file.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student_Submission.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return isErr;
    }


    private void Student_SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Student_SubmitActionPerformed
        //Once Submit is Pressed
            // checks to see if stl files with same name exists
            if (errCheck() == false) {
                String fileName = projName.getText();

                //System.out.println("fileloc" + fileLoc);
                String fName = firstName.getText();
                String lName = lastName.getText();
                String email = emailInfo.getText();

                //break down course string to course and section
                String classText = (String) classBox.getSelectedItem();
                String[] splittedCourse = classText.split("\\s");
                printer = (String) printerBox.getSelectedItem();
                //move file to location
                String fileLoc = "";
                try {
                    System.out.println(fileLocation.getText());
                    FileUtils.copyFileToDirectory(new File(fileLocation.getText()), new File(inst.getSubmission()));
                    fileLoc = inst.getSubmission() + new File(fileLocation.getText()).getName();

                    System.out.println(printer);
                    System.out.println(fName);
                    System.out.println(lName);
                    System.out.println(splittedCourse[0]);
                    System.out.println(splittedCourse[1]);
                    System.out.println(fileName);
                    System.out.println(fileLoc);
                    System.out.println(email);

                    sqlMethods.insertIntoPendingJobs(printer, fName, lName, splittedCourse[0], splittedCourse[1], fileName, fileLoc.replace("\\", "\\\\"), email);
                    //JOptionPane.showMessageDialog(new java.awt.Frame(), "Succesfully submitted file!");
                    //dispose();
                    setVisible(false);
                    TimeUnit.SECONDS.sleep(2);
                    fileLocation.setText(null);
                    projName.setText(null);
                    firstName.setText(null);
                    lastName.setText(null);
                    emailInfo.setText(null);
                    classBox.setSelectedItem(null);
                    printerBox.setSelectedItem(null);
                    setVisible(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(new java.awt.Frame(), "IOException! File couldn't be navigated.");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Student_Submission.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
    }//GEN-LAST:event_Student_SubmitActionPerformed


    private void BrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BrowseMouseClicked
        //Creates "JFileChooser" file browser
        JFileChooser fileopen = new JFileChooser();  //in brackets, add Syncthing directory or new Drive's address for default location
        //FileFilter filter = new FileNameExtensionFilter("STL files", ".stl");
        //fileopen.addChoosableFileFilter(filter);
        fileopen.setAcceptAllFileFilterUsed(false);
        fileopen.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Object Files", "obj", "zpr", "stl");
        fileopen.setFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            //Inputs the file location into the textbox "fileName"
            fileLocation.setText(file.toString().replaceAll("'", ""));
            projName.setText(file.getName().replaceAll("'", ""));
        }
    }//GEN-LAST:event_BrowseMouseClicked


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + inst.getPDFStudent());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);  //print the error
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void BrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BrowseActionPerformed

    private void printerBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printerBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printerBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Browse;
    private javax.swing.JButton Student_Submit;
    private javax.swing.JComboBox classBox;
    private javax.swing.JMenu editMenu;
    private javax.swing.JTextField emailInfo;
    private javax.swing.JLabel errorTxt1;
    private javax.swing.JLabel errorTxt2;
    private javax.swing.JLabel errorTxt3;
    private javax.swing.JLabel errorTxt4;
    private javax.swing.JLabel errorTxt6;
    private javax.swing.JLabel errorTxt7;
    private javax.swing.JTextField fileLocation;
    private javax.swing.JTextField firstName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lastName;
    private javax.swing.JComboBox printerBox;
    private javax.swing.JTextField projName;
    // End of variables declaration//GEN-END:variables
}

