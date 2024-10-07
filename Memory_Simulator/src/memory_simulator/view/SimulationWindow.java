/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package memory_simulator.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JTable;
import memory_simulator.logic.Simulation;
import memory_simulator.model.PaginationAlgoType;

/**
 *
 * @author paubo
 */
public class SimulationWindow extends javax.swing.JFrame {

    Simulation simulator;
    
    public SimulationWindow(PaginationAlgoType algoType, ArrayList<String> instructions) {
        
        simulator = new Simulation(algoType, instructions);
        // Columnas y datos de la tabla
        String[] columnNames = {"PAGE ID", "PID", "LOADED", "L-ADDR", "M-ADDR", "D-ADDR", "LOADED-T", "MARK"};
        Object[][] data = {
            {"1", "1", "X", "1", "1", "0", "0s", "Green"},
            // Aquí puedes agregar más filas de datos si es necesario
        };

        // Inicializar componentes
        initComponents();  // Asegúrate de que jScrollPaneAlgo y jPanel1 ya existan y estén inicializados aquí.

        // Crear la tabla con los datos y columnas
        JTable table = new JTable(data, columnNames);

        // Agregar la tabla al JScrollPane existente (jScrollPaneAlgo)
        jScrollPaneAlgo.setViewportView(table);
        jPanel1.add(jScrollPaneAlgo, BorderLayout.CENTER);

        // Agregar el panel a la ventana
        this.add(jPanel1);
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPaneOPT = new javax.swing.JScrollPane();
        jScrollPaneAlgo = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setMinimumSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel1.setLayout(null);
        jPanel1.add(jScrollPaneOPT);
        jScrollPaneOPT.setBounds(25, 150, 450, 350);
        jPanel1.add(jScrollPaneAlgo);
        jScrollPaneAlgo.setBounds(525, 150, 450, 350);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            PaginationAlgoType algoType = PaginationAlgoType.FIFO_ALGO;  
            ArrayList<String> instructions = new ArrayList<>();
                new SimulationWindow(algoType, instructions).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneAlgo;
    private javax.swing.JScrollPane jScrollPaneOPT;
    // End of variables declaration//GEN-END:variables
}
