/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.app.agency;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
import javax.swing.DefaultListModel;
import Gateways.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mpesic
 */
public class BookingAgencyFrame extends javax.swing.JFrame {


    private DefaultListModel<BookingAgencyListLine> listModel = new DefaultListModel<>();
    private String agencyName;
    private BookingAppGateway bookingAppGateway;
    private Map<AgencyRequest, String> holder;
    private Map<String, Integer> aggregationholder;

    /**
     * Creates new form TravelApprovalFrame
     *
     * @param agencyName
     */
    
    public BookingAgencyFrame(String agencyName, String agencyRequestQueue) {
        initComponents();
        setTitle(agencyName);
        this.agencyName = agencyName;
        holder = new HashMap<>();
        aggregationholder = new HashMap<>();
        bookingAppGateway = new BookingAppGateway(agencyRequestQueue) {
            public void onBookingRequest(AgencyRequest request, String requestID, int aggregatotionID) {
                listModel.addElement(new BookingAgencyListLine(request, null));
                holder.put(request, requestID);
                aggregationholder.put(requestID, aggregatotionID);
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jbSendAgencyReply = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfTotalPrice = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 4, 0, 4, 0, 4, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        getContentPane().setLayout(layout);

        jList1.setModel(listModel);
        jScrollPane2.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        jbSendAgencyReply.setLabel("send price offer");
        jbSendAgencyReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSendAgencyReplyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        getContentPane().add(jbSendAgencyReply, gridBagConstraints);

        jLabel1.setText("total price for this booking:");
        jPanel1.add(jLabel1);
        jLabel1.getAccessibleContext().setAccessibleName("total price:");

        jtfTotalPrice.setMinimumSize(new java.awt.Dimension(100, 100));
        jtfTotalPrice.setName(""); // NOI18N
        jtfTotalPrice.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel1.add(jtfTotalPrice);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSendAgencyReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSendAgencyReplyActionPerformed
        BookingAgencyListLine jListLine = jList1.getSelectedValue();
        double price = Double.parseDouble(this.jtfTotalPrice.getText());
        String id = holder.get(jListLine.getRequest());
        System.out.println(id);
        int aggregationId = aggregationholder.get(id);
        AgencyReply reply = new AgencyReply(this.agencyName, price);
        if (jListLine != null) {
            jListLine.setReply(reply);
            bookingAppGateway.replyToBroker(reply, id, aggregationId);
            jList1.repaint();
        }
    }//GEN-LAST:event_jbSendAgencyReplyActionPerformed

    /**
     * @param args the command line arguments
     */
    /* public static void main(String args[]) {
        /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
 /*  try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TravelApprovalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TravelApprovalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TravelApprovalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TravelApprovalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
 /*    java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TravelApprovalFrame().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<booking.app.agency.BookingAgencyListLine> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbSendAgencyReply;
    private javax.swing.JTextField jtfTotalPrice;
    // End of variables declaration//GEN-END:variables
}