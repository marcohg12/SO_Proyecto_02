package memory_simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileNameExtensionFilter;

import memory_simulator.logic.InstructionSetGenerator;
import memory_simulator.model.PaginationAlgoType;
import static memory_simulator.model.PaginationAlgoType.FIFO_ALGO;
import static memory_simulator.model.PaginationAlgoType.MRU_ALGO;
import static memory_simulator.model.PaginationAlgoType.RND_ALGO;
import static memory_simulator.model.PaginationAlgoType.SC_ALGO;

public class PrincipalWindow extends javax.swing.JFrame {
    
    ArrayList<String> instructions;
    PaginationAlgoType algorithm;
    int seed;
    Color gray;
    String path; 

    public PrincipalWindow() {
        gray = new Color(153,153,153);
        initComponents();
    }
    
    public void selectAlgorithm(){
        if (comboBoxAlgorithm.getSelectedItem().equals("FIFO")) {
            algorithm = FIFO_ALGO;
        }else if(comboBoxAlgorithm.getSelectedItem().equals("SC")){
            algorithm = SC_ALGO;
        }else if(comboBoxAlgorithm.getSelectedItem().equals("MRU")){
            algorithm = MRU_ALGO;
        }else if(comboBoxAlgorithm.getSelectedItem().equals("RND")){
            algorithm = RND_ALGO;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textRandomSeed = new javax.swing.JTextField();
        comboBoxAlgorithm = new javax.swing.JComboBox<>();
        comboBoxAlgorithm.setRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value);

                // Cambia los colores dependiendo de si el elemento está seleccionado o no
                if (isSelected) {
                    label.setBackground(Color.BLACK);  // Fondo cuando está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando está seleccionado
                } else {
                    label.setBackground(Color.DARK_GRAY);      // Fondo cuando no está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando no está seleccionado
                }

                label.setOpaque(true);  // Asegura que el fondo se pinte
                return label;
            }
        });
        comboBoxProcesses = new javax.swing.JComboBox<>();
        comboBoxProcesses.setRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value);

                // Cambia los colores dependiendo de si el elemento está seleccionado o no
                if (isSelected) {
                    label.setBackground(Color.BLACK);  // Fondo cuando está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando está seleccionado
                } else {
                    label.setBackground(Color.DARK_GRAY);      // Fondo cuando no está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando no está seleccionado
                }

                label.setOpaque(true);  // Asegura que el fondo se pinte
                return label;
            }
        });
        comboBoxOperations = new javax.swing.JComboBox<>();
        comboBoxOperations.setRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value);

                // Cambia los colores dependiendo de si el elemento está seleccionado o no
                if (isSelected) {
                    label.setBackground(Color.BLACK);  // Fondo cuando está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando está seleccionado
                } else {
                    label.setBackground(Color.DARK_GRAY);      // Fondo cuando no está seleccionado
                    label.setForeground(Color.WHITE);      // Texto cuando no está seleccionado
                }

                label.setOpaque(true);  // Asegura que el fondo se pinte
                return label;
            }
        });
        buttonGenerate = new javax.swing.JButton();
        buttonLoad = new javax.swing.JButton();
        generateSimulation = new javax.swing.JButton();
        buttonDonwload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inicialización Simulador de Memoria");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setAlignmentY(0.0F);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 590, -1));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Semilla random ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Algoritmo a simular ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, -1, -1));

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Número de procesos a simular");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, -1, -1));

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Número de operaciones a simular");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, -1, -1));

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cargar un archivo con instrucciones");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 540, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/memory_simulator/view/images/ImagePrincipalWindow.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 380, 700));

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Archivo");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 440, -1, -1));

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Generar un archivo de forma aleatoria");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 470, -1, -1));

        textRandomSeed.setBackground(new java.awt.Color(153, 153, 153));
        textRandomSeed.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        textRandomSeed.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textRandomSeed.setDoubleBuffered(true);
        textRandomSeed.setDragEnabled(true);
        textRandomSeed.setSelectionColor(new java.awt.Color(255, 255, 255));
        textRandomSeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRandomSeedActionPerformed(evt);
            }
        });
        jPanel1.add(textRandomSeed, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 100, 25));

        comboBoxAlgorithm.setBackground(new java.awt.Color(0, 0, 0));
        comboBoxAlgorithm.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        comboBoxAlgorithm.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxAlgorithm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FIFO", "SC", "MRU", "RND" }));
        jPanel1.add(comboBoxAlgorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 100, 25));

        comboBoxProcesses.setBackground(new java.awt.Color(0, 0, 0));
        comboBoxProcesses.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        comboBoxProcesses.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxProcesses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "50", "100" }));
        jPanel1.add(comboBoxProcesses, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 100, 25));

        comboBoxOperations.setBackground(new java.awt.Color(0, 0, 0));
        comboBoxOperations.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        comboBoxOperations.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxOperations.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "500", "1000", "5000" }));
        jPanel1.add(comboBoxOperations, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 380, 100, 25));

        buttonGenerate.setBackground(new java.awt.Color(153, 153, 153));
        buttonGenerate.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        buttonGenerate.setText("Generar");
        buttonGenerate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonGenerateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonGenerateMouseExited(evt);
            }
        });
        buttonGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGenerateActionPerformed(evt);
            }
        });
        jPanel1.add(buttonGenerate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 500, 100, 25));

        buttonLoad.setBackground(new java.awt.Color(153, 153, 153));
        buttonLoad.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        buttonLoad.setText("Cargar");
        buttonLoad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonLoadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonLoadMouseExited(evt);
            }
        });
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });
        jPanel1.add(buttonLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 570, 100, 25));

        generateSimulation.setBackground(new java.awt.Color(153, 153, 153));
        generateSimulation.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        generateSimulation.setText("Generar Simulación");
        generateSimulation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                generateSimulationMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                generateSimulationMouseExited(evt);
            }
        });
        generateSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateSimulationActionPerformed(evt);
            }
        });
        jPanel1.add(generateSimulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 640, 200, 30));

        buttonDonwload.setBackground(new java.awt.Color(153, 153, 153));
        buttonDonwload.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        buttonDonwload.setText("Descargar");
        buttonDonwload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonDonwloadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonDonwloadMouseExited(evt);
            }
        });
        buttonDonwload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDonwloadActionPerformed(evt);
            }
        });
        jPanel1.add(buttonDonwload, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 500, 100, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textRandomSeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomSeedActionPerformed

    }//GEN-LAST:event_textRandomSeedActionPerformed

    private void buttonGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGenerateActionPerformed
        boolean errorFound = false;
        
        int processes = Integer.parseInt((String) comboBoxProcesses.getSelectedItem());
        int operations = Integer.parseInt((String) comboBoxOperations.getSelectedItem());
        seed = 0;
        
        try{
            seed = Integer.parseInt(textRandomSeed.getText());
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Debe ingresar una semilla válida (número entero)", "Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true; 
        }
        
        // No genera el instruction set a menos de que tenga una semilla válida
        if(errorFound == false){
            instructions =  InstructionSetGenerator.getInstructionSet(seed, processes, operations);
            JOptionPane.showMessageDialog(null, "Se ha generado el archivo", "", JOptionPane.INFORMATION_MESSAGE);
            path = InstructionSetGenerator.writeInstructionsToFile(instructions); 
        }
    }//GEN-LAST:event_buttonGenerateActionPerformed

    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            instructions = InstructionSetGenerator.readFileToArrayList(filePath);
        }else{
            JOptionPane.showMessageDialog(null, "No se seleccionó un archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonLoadActionPerformed

    private void generateSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateSimulationActionPerformed
        if(instructions == null){
            JOptionPane.showMessageDialog(null, "No se cargo o generó un archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }else if("".equals(textRandomSeed.getText())){
            JOptionPane.showMessageDialog(null, "Debe ingresar una semilla válida (número entero)", "Error", JOptionPane.ERROR_MESSAGE);
        }else if(instructions != null & !"".equals(textRandomSeed.getText())){
        
            selectAlgorithm();
            SimulationWindow window = new SimulationWindow(algorithm, instructions, seed);
            window.setVisible(true);
            this.setVisible(false);
        }
        
    }//GEN-LAST:event_generateSimulationActionPerformed

    private void buttonGenerateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonGenerateMouseEntered
        buttonGenerate.setBackground(Color.WHITE);
    }//GEN-LAST:event_buttonGenerateMouseEntered

    private void buttonGenerateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonGenerateMouseExited
        buttonGenerate.setBackground(gray);
    }//GEN-LAST:event_buttonGenerateMouseExited

    private void buttonDonwloadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDonwloadMouseEntered
        buttonDonwload.setBackground(Color.WHITE);
    }//GEN-LAST:event_buttonDonwloadMouseEntered

    private void buttonDonwloadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDonwloadMouseExited
        buttonDonwload.setBackground(gray);
    }//GEN-LAST:event_buttonDonwloadMouseExited

    private void buttonLoadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonLoadMouseEntered
        buttonLoad.setBackground(Color.WHITE);
    }//GEN-LAST:event_buttonLoadMouseEntered

    private void buttonLoadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonLoadMouseExited
        buttonLoad.setBackground(gray);
    }//GEN-LAST:event_buttonLoadMouseExited

    private void generateSimulationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateSimulationMouseEntered
        generateSimulation.setBackground(Color.WHITE);
    }//GEN-LAST:event_generateSimulationMouseEntered

    private void generateSimulationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateSimulationMouseExited
        generateSimulation.setBackground(gray);
    }//GEN-LAST:event_generateSimulationMouseExited

    private void buttonDonwloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDonwloadActionPerformed
        if(path!=null){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo como");
            fileChooser.setSelectedFile(new File("InstructionSet.txt"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File destinationFile = fileChooser.getSelectedFile();
                try (InputStream in = new FileInputStream(path);
                     OutputStream out = new FileOutputStream(destinationFile)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    JOptionPane.showMessageDialog(null, "Archivo descargado con éxito.");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al descargar el archivo: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "No se ha generado un archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonDonwloadActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDonwload;
    private javax.swing.JButton buttonGenerate;
    private javax.swing.JButton buttonLoad;
    private javax.swing.JComboBox<String> comboBoxAlgorithm;
    private javax.swing.JComboBox<String> comboBoxOperations;
    private javax.swing.JComboBox<String> comboBoxProcesses;
    private javax.swing.JButton generateSimulation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField textRandomSeed;
    // End of variables declaration//GEN-END:variables
}
