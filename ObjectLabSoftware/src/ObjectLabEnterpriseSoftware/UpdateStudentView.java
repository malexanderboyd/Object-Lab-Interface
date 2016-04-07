/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectLabEnterpriseSoftware;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JOptionPane;

public class UpdateStudentView {

}

/*public class UpdateStudentView extends javax.swing.JFrame
{
	/* This code is similar to AddStudentView. This should not be the case, they should share stuff. */
/* private static final String NAME_OF_PAGE = "Update Student Information";
    private static MainView home = new MainView();
	private String userID;

    public void UpdateStudentViewStart(String id)
    {
        initComponents();

		userID = id;
        idOfUser.setText(userID);
        	
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
            java.util.logging.Logger.getLogger(UpdateStudentView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
                home.setVisible(true);
            }
        });
	setLocationRelativeTo(null);
        setVisible(true);
        errorLabel.setVisible(false);
        //only accepts integers for new student TU ID
        idOfUser.addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE)))
                {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });

    }
    //public AddStudentView() {  
    //}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
/* @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    /*
    private void initComponents() {

        newStudentLabel = new javax.swing.JLabel();
        firstName = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        netID = new javax.swing.JLabel();
        tuID = new javax.swing.JLabel();
        emailExtension = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        submit = new javax.swing.JButton();
        idOfUser = new javax.swing.JTextField();
        firstNameEntry = new javax.swing.JTextField();
        lastNameEntry = new javax.swing.JTextField();
        emailEntry = new javax.swing.JTextField();
        backButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        EditMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(UtilController.getPageName(NAME_OF_PAGE));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newStudentLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        newStudentLabel.setText("Update Student Information");
        getContentPane().add(newStudentLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        firstName.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        firstName.setText("First Name:");
        getContentPane().add(firstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, -1, -1));

        lastName.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lastName.setText("Last Name:");
        getContentPane().add(lastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, -1));

        netID.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        netID.setText("Email:");
        getContentPane().add(netID, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));

        tuID.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        tuID.setText("TU ID:");
        getContentPane().add(tuID, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));

        emailExtension.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        emailExtension.setText("ex:  jsmith1@gmail.com");
        getContentPane().add(emailExtension, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, -1, 20));

        errorLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setText("ERROR");
        errorLabel.setToolTipText("");
        getContentPane().add(errorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 120, -1));

        submit.setText("Submit");
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitMouseClicked(evt);
            }
        });
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });
        getContentPane().add(submit, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, -1, -1));

        idOfUser.setEditable(false);
        idOfUser.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        idOfUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idOfUserActionPerformed(evt);
            }
        });
        getContentPane().add(idOfUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 140, -1));

        firstNameEntry.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        getContentPane().add(firstNameEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 100, -1));

        lastNameEntry.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        getContentPane().add(lastNameEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 100, -1));

        emailEntry.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        emailEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailEntryActionPerformed(evt);
            }
        });
        getContentPane().add(emailEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 170, -1));

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/back_arrow_button.png"))); // NOI18N
        backButton.setToolTipText("Back");
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 440, 10));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ObjectLabEnterpriseSoftware/images/white_bg.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 240));

        EditMenu.setText("Help");
        jMenuBar1.add(EditMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        // TODO add your handling code here:

        String tuID = idOfUser.getText();
        String firstName = firstNameEntry.getText();
        String lastName = lastNameEntry.getText();
        String email = emailEntry.getText();
        boolean validEmailAdr = true;
       
        if (tuID.equals("") || firstName.equals("") || lastName.equals("") || email.equals(""))
        {
            JOptionPane.showMessageDialog(this, "Cannot save with empty fields!");
        } else if (!email.isEmpty())
        {
            try
            {
                new InternetAddress(email).validate();
            } catch (AddressException e)
            {
                System.out.println(e);
                JOptionPane.showMessageDialog(this, "Please enter a valid email address");
                validEmailAdr = false;
            }
            if (validEmailAdr)
            {
                if(InputValidation.isNumber(tuID)){
                
                    if ((UtilController.updateUser(tuID, firstName, lastName, email)) > 0)
                    {
                        JOptionPane.showMessageDialog(this, "Updating student data.");
                        dispose();
						home.setVisible(true);
                    } else
                    {
                        JOptionPane.showMessageDialog(this, "Error updating student info database.");
                    }
                    
                }
                else{
                    idOfUser.setText("");
                    JOptionPane.showMessageDialog(this, "Please enter a numerical TUID.");
                }
                
            }
        }


    }//GEN-LAST:event_submitActionPerformed

    private void emailEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailEntryActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        dispose();
        home.setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

    private void idOfUserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_idOfUserActionPerformed
    {//GEN-HEADEREND:event_idOfUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idOfUserActionPerformed

    private void submitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_submitMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu EditMenu;
    private javax.swing.JButton backButton;
    private javax.swing.JTextField emailEntry;
    private javax.swing.JLabel emailExtension;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel firstName;
    private javax.swing.JTextField firstNameEntry;
    private javax.swing.JTextField idOfUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lastName;
    private javax.swing.JTextField lastNameEntry;
    private javax.swing.JLabel netID;
    private javax.swing.JLabel newStudentLabel;
    private javax.swing.JButton submit;
    private javax.swing.JLabel tuID;
    // End of variables declaration//GEN-END:variables
} */
