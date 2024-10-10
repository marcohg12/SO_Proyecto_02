
package memory_simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memory_simulator.logic.Simulation;
import memory_simulator.model.ComputerState;
import memory_simulator.model.Page;
import memory_simulator.model.PaginationAlgoType;
import static memory_simulator.model.PaginationAlgoType.FIFO_ALGO;
import static memory_simulator.model.PaginationAlgoType.MRU_ALGO;
import static memory_simulator.model.PaginationAlgoType.OPT_ALGO;
import static memory_simulator.model.PaginationAlgoType.RND_ALGO;
import static memory_simulator.model.PaginationAlgoType.SC_ALGO;

/**
 *
 * @author paubo
 */
public class SimulationWindow extends javax.swing.JFrame {

    Simulation simulation;
    ArrayList<Color> colors; 
    PaginationAlgoType algorithm;

    public SimulationWindow(PaginationAlgoType algoType, ArrayList<String> instructions, int seed){
        
        simulation = new Simulation(algoType, instructions, seed);
        algorithm = algoType;
        initComponents();
        colors = new ArrayList<>();
        colorGenerator(colors);
        startSimulation();
    }
    
    public final void startSimulation() {
        // Usar SwingWorker para ejecutar la simulación en segundo plano
        SwingWorker<Void, ComputerState> worker = new SwingWorker<Void, ComputerState>() {

            @Override
            protected Void doInBackground() throws Exception {
                // Ejecutar la simulación en segundo plano
                while (simulation.executeNext()) {
                    ComputerState state = simulation.getState();
                    publish(state);  
                    Thread.sleep(10);
                }
                return null; 
            }

            @Override
            protected void process(java.util.List<ComputerState> states) {
                if (!states.isEmpty()) {
                    ComputerState latestState = states.get(states.size() - 1);
                    update(latestState); 
                }
            }

            @Override
            protected void done() {
                System.out.println("Simulación finalizada");
            }
    };

    worker.execute();  
    }
    public void update(ComputerState state){
        processesOPT.setText(String.valueOf(state.getNumberOfProcesses()));
        simTimeOPT.setText(String.valueOf(state.getClock()));
        ramkbOPT.setText(String.valueOf(state.getUsedMemory()));
        ramperOPT.setText(String.valueOf(state.getUsedMemoryPerc()));
        vRamkbOPT.setText(String.valueOf(state.getUsedVMemory()));
        vRamPerOPT.setText(String.valueOf(state.getUsedVMemoryPerc()));
        pagesLoadedOPT.setText(String.valueOf(state.getLoadedPages()));
        pagesUnloadedOPT.setText(String.valueOf(state.getUnloadedPages()));
        trashingOPT.setText(String.valueOf(state.getThrashing()));
        trashingPerOPT.setText(String.valueOf(state.getThrashingPerc()));
        fragmentationOPT.setText(String.valueOf(state.getInternalFragmentation()));
        updateTable(state.getAllPages());
    }

       public void updateTable(ArrayList<Page> pages) {
        DefaultTableModel model = (DefaultTableModel) jTableOPT.getModel();
        model.setRowCount(0); // Limpiar la tabla

        for (Page page : pages) {
            Object[] row = new Object[8];

            row[0] = page.getPageId();  
            row[1] = page.getProcessId();  
            row[2] = page.isInPhysicalMemory() ? "X" : "";  
            row[3] = page.getPointer();
            if(page.isInPhysicalMemory()){
                row[4] = page.getPhysicalAddress();
                row[5] = "";
            }else{
                row[4] = "";
                row[5] = page.getVirtualAddress();  
            } 
            row[6] = page.getTimestamp();  
            if (algorithm == FIFO_ALGO) {
                row[7] = page.getTimestamp();
            }else if(algorithm == SC_ALGO){
                row[7] = page.getSecondChance();
            }else if(algorithm == MRU_ALGO){
                row[7] = page.getLastUsage();
            }else if (algorithm == RND_ALGO || algorithm == OPT_ALGO ){
                row[7] = "";
            }
            model.addRow(row);
        }

        // Establecer el renderizador de la tabla
        jTableOPT.setDefaultRenderer(Object.class, new ColorRenderer(colors));
    }

    private class ColorRenderer extends DefaultTableCellRenderer {
        private final ArrayList<Color> colors;

        public ColorRenderer(ArrayList<Color> colors) {
            this.colors = colors;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Obtener el valor de ProcessId de la columna 1
            int processId = (Integer) table.getValueAt(row, 1);

            // Asegúrate de que el índice de color sea válido
            int colorIndex = processId % colors.size(); // Evita un índice fuera de los límites

            // Establecer el color de fondo basado en el ProcessId
            cell.setBackground(colors.get(colorIndex));

            // Restablecer color de fondo si está seleccionado
            if (isSelected) {
                cell.setBackground(Color.BLUE); // Cambia a azul si está seleccionado
            }

            return cell;
        }
    }

    
    
    public void colorGenerator(ArrayList<Color> colors) {
        List<Float> hueValues = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            hueValues.add((float) i / 100); 
        }
        Collections.shuffle(hueValues); 

        for (int i = 0; i < 100; i++) {
            float hue = hueValues.get(i);
            float saturation = 0.7f;  
            float brightness = 0.9f; 
            Color color = Color.getHSBColor(hue, saturation, brightness);
            colors.add(color);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        processesOPT = new javax.swing.JTextField();
        simTimeOPT = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        ramkbOPT = new javax.swing.JTextField();
        ramperOPT = new javax.swing.JTextField();
        vRamkbOPT = new javax.swing.JTextField();
        vRamPerOPT = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        pagesLoadedOPT = new javax.swing.JTextField();
        pagesUnloadedOPT = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        trashingOPT = new javax.swing.JTextField();
        fragmentationOPT = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jScrollPaneOPT = new javax.swing.JScrollPane();
        jTableOPT = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField34 = new javax.swing.JTextField();
        trashingPerOPT = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMinimumSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 780));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(153, 153, 153));
        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Processes");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 560, 225, -1));

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(153, 153, 153));
        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Sim-Time");
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 560, 225, -1));

        processesOPT.setEditable(false);
        processesOPT.setBackground(new java.awt.Color(255, 255, 255));
        processesOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        processesOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(processesOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 580, 225, -1));

        simTimeOPT.setEditable(false);
        simTimeOPT.setBackground(new java.awt.Color(255, 255, 255));
        simTimeOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        simTimeOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(simTimeOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 580, 225, -1));

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(153, 153, 153));
        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("RAM KB");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 640, 112, -1));

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(153, 153, 153));
        jTextField6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("RAM %");
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 640, 112, -1));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(153, 153, 153));
        jTextField7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("V-RAM KB");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 640, 112, -1));

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(153, 153, 153));
        jTextField8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("V-RAM %");
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 640, 112, -1));

        ramkbOPT.setEditable(false);
        ramkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 660, 112, -1));

        ramperOPT.setEditable(false);
        ramperOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramperOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramperOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramperOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 660, 112, -1));

        vRamkbOPT.setEditable(false);
        vRamkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 660, 112, -1));

        vRamPerOPT.setEditable(false);
        vRamPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 660, 112, -1));

        jTextField13.setEditable(false);
        jTextField13.setBackground(new java.awt.Color(153, 153, 153));
        jTextField13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField13.setText("Pages");
        jPanel1.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 700, 224, 25));

        jTextField14.setEditable(false);
        jTextField14.setBackground(new java.awt.Color(204, 204, 204));
        jTextField14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setText("LOADED");
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 720, 112, 25));

        jTextField15.setEditable(false);
        jTextField15.setBackground(new java.awt.Color(204, 204, 204));
        jTextField15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setText("UNLOADED");
        jPanel1.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 720, 114, 25));

        pagesLoadedOPT.setEditable(false);
        pagesLoadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesLoadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesLoadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesLoadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 740, 112, 25));

        pagesUnloadedOPT.setEditable(false);
        pagesUnloadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesUnloadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesUnloadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesUnloadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 740, 114, 25));

        jTextField18.setEditable(false);
        jTextField18.setBackground(new java.awt.Color(153, 153, 153));
        jTextField18.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setText("Trashing");
        jPanel1.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 700, 112, -1));

        jTextField19.setEditable(false);
        jTextField19.setBackground(new java.awt.Color(153, 153, 153));
        jTextField19.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField19.setText("Fragmentación  ");
        jPanel1.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 700, 112, -1));

        trashingOPT.setEditable(false);
        trashingOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 720, 56, 45));

        fragmentationOPT.setEditable(false);
        fragmentationOPT.setBackground(new java.awt.Color(255, 255, 255));
        fragmentationOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        fragmentationOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fragmentationOPT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fragmentationOPTActionPerformed(evt);
            }
        });
        jPanel1.add(fragmentationOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 720, 112, 45));

        jTextField22.setEditable(false);
        jTextField22.setBackground(new java.awt.Color(153, 153, 153));
        jTextField22.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField22.setText("Processes");
        jPanel1.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 560, 225, -1));

        jTextField23.setEditable(false);
        jTextField23.setBackground(new java.awt.Color(153, 153, 153));
        jTextField23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField23.setText("Sim-Time");
        jPanel1.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 560, 225, -1));

        jTextField24.setEditable(false);
        jTextField24.setBackground(new java.awt.Color(255, 255, 255));
        jTextField24.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField24.setToolTipText("");
        jPanel1.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 580, 225, -1));

        jTextField25.setEditable(false);
        jTextField25.setBackground(new java.awt.Color(255, 255, 255));
        jTextField25.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 580, 225, -1));

        jTextField26.setBackground(new java.awt.Color(153, 153, 153));
        jTextField26.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField26.setText("RAM KB");
        jPanel1.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 640, 112, -1));

        jTextField27.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField27ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField27, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 660, 112, -1));

        jTextField28.setBackground(new java.awt.Color(153, 153, 153));
        jTextField28.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField28.setText("RAM %");
        jPanel1.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 640, 112, -1));

        jTextField29.setBackground(new java.awt.Color(153, 153, 153));
        jTextField29.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField29.setText("V-RAM %");
        jTextField29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField29ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 640, 112, -1));

        jTextField30.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField30, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 660, 112, -1));

        jTextField31.setText("jTextField31");
        jTextField31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField31ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField31, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 640, 110, -1));

        jTextField32.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField32, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 660, 112, -1));

        jTextField33.setBackground(new java.awt.Color(153, 153, 153));
        jTextField33.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField33.setText("V-RAM KB");
        jPanel1.add(jTextField33, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 640, 112, -1));

        jTableOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTableOPT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PAGE ID", "PID", "LOADED", "L-ADDR", "M-ADDR", "D-ADDR", "LOADED-T", "MARK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableOPT.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTableOPT.getTableHeader().setReorderingAllowed(false);
        jScrollPaneOPT.setViewportView(jTableOPT);

        jPanel1.add(jScrollPaneOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 510, 340));

        jTable2.setRowHeight(30);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1070, 30));

        jTextField34.setBackground(new java.awt.Color(153, 153, 153));
        jTextField34.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField34.setText("MMU OPT");
        jTextField34.setToolTipText("");
        jTextField34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField34ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField34, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 510, -1));

        trashingPerOPT.setEditable(false);
        trashingPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trashingPerOPT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trashingPerOPTActionPerformed(evt);
            }
        });
        jPanel1.add(trashingPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 720, 56, 45));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PAGE ID", "PID", "LOADED", "L-ADDR", "M-ADDR", "D-ADDR", "LOADED-T", "MARK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(600, 80));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 510, 340));

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 660, 112, -1));

        jTextField3.setBackground(new java.awt.Color(153, 153, 153));
        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("MMU ALGO");
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 510, -1));

        jTextField9.setBackground(new java.awt.Color(153, 153, 153));
        jTextField9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("Pages");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 700, 224, -1));

        jTextField10.setBackground(new java.awt.Color(204, 204, 204));
        jTextField10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("LOADED");
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 720, 112, -1));

        jTextField11.setBackground(new java.awt.Color(204, 204, 204));
        jTextField11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setText("UNLOADED");
        jPanel1.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 720, 114, -1));

        jTextField12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 740, 112, -1));

        jTextField16.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 740, 114, -1));

        jTextField17.setBackground(new java.awt.Color(153, 153, 153));
        jTextField17.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setText("Trashing");
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 700, 112, -1));

        jTextField20.setBackground(new java.awt.Color(153, 153, 153));
        jTextField20.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField20.setText("Fragmentación");
        jPanel1.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 700, 112, -1));

        jTextField21.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 720, 56, 42));

        jTextField35.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField35, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 720, 57, 42));

        jTextField36.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField36.setToolTipText("");
        jPanel1.add(jTextField36, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 720, 112, 42));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1096, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27ActionPerformed

    private void fragmentationOPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fragmentationOPTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fragmentationOPTActionPerformed

    private void trashingPerOPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trashingPerOPTActionPerformed

    }//GEN-LAST:event_trashingPerOPTActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField34ActionPerformed

    private void jTextField31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField31ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField29ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

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
            new SimulationWindow(algoType, instructions, 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fragmentationOPT;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneOPT;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTableOPT;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField pagesLoadedOPT;
    private javax.swing.JTextField pagesUnloadedOPT;
    private javax.swing.JTextField processesOPT;
    private javax.swing.JTextField ramkbOPT;
    private javax.swing.JTextField ramperOPT;
    private javax.swing.JTextField simTimeOPT;
    private javax.swing.JTextField trashingOPT;
    private javax.swing.JTextField trashingPerOPT;
    private javax.swing.JTextField vRamPerOPT;
    private javax.swing.JTextField vRamkbOPT;
    // End of variables declaration//GEN-END:variables
}
