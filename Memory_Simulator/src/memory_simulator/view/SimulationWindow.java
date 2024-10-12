
package memory_simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import memory_simulator.logic.Simulation;
import memory_simulator.model.ComputerState;
import memory_simulator.model.Page;
import memory_simulator.model.PaginationAlgoType;
import static memory_simulator.model.PaginationAlgoType.FIFO_ALGO;
import static memory_simulator.model.PaginationAlgoType.MRU_ALGO;
import static memory_simulator.model.PaginationAlgoType.OPT_ALGO;
import static memory_simulator.model.PaginationAlgoType.RND_ALGO;
import static memory_simulator.model.PaginationAlgoType.SC_ALGO;

public final class SimulationWindow extends javax.swing.JFrame {

    private Simulation simulationALGO;
    private Simulation simulationOPT;
    private ArrayList<Color> colors; 
    private PaginationAlgoType algorithm;
    private JPanel[] ramOPT;
    private JPanel[] ramALGO;
    private volatile boolean paused = false; 
    
    public SimulationWindow(PaginationAlgoType algoType, ArrayList<String> instructions, int seed){
        initComponents();
        
        // Se inician las simulaciones
        simulationALGO = new Simulation(algoType, instructions, seed);
        simulationOPT = new Simulation(OPT_ALGO, instructions, seed);
        algorithm = algoType;
        
        // Generar listas de colores para cada algoritmo 
        colors = new ArrayList<>();
        colorGenerator(colors);
        
        // Creación de las barras que simulan la ram
        ramOPT = makeRAMBar(jPanelRamOPT);
        ramALGO = makeRAMBar(jPanelRamALGO);
        
        // Se inicia la simulación
        startSimulation(); 
    }
    
    public JPanel[] makeRAMBar(JPanel panel){
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); 
        JPanel[] cells = new JPanel[100];
        for (int i = 0; i < 100; i++) {
            cells[i] = new JPanel(); 
            cells[i].setPreferredSize(new Dimension(10, 10));  
            panel.add(cells[i]);
        }
        return cells; 
    }
    
    public final void startSimulation() {

        SwingWorker<Void, ArrayList<ComputerState>> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                boolean isALGOFinished = false;
                boolean isOPTFinished = false;
                
                while (!isALGOFinished && !isOPTFinished){     
                    synchronized (SimulationWindow.this) {
                    // Mientras 'paused' sea true, el hilo esperará
                        while (paused) {
                            SimulationWindow.this.wait();  // Esperar si está pausado
                        }
                    }
  
                    isALGOFinished = !simulationALGO.executeNext();
                    isOPTFinished = !simulationOPT.executeNext();
                    ArrayList<ComputerState> states = new ArrayList();
                    states.add(simulationOPT.getState());
                    states.add(simulationALGO.getState());
                    publish(states);
                    Thread.sleep(10);
                }
                
                return null; 
            }

            @Override
            protected void process(java.util.List<ArrayList<ComputerState>> states) {
                if (!states.isEmpty()) {
                    ArrayList<ComputerState> latestStates = states.get(states.size() - 1);
                    updateOPT(latestStates.get(0));
                    updateALGO(latestStates.get(1));
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e){
                    System.out.println(e);
                    Throwable cause = e.getCause();
                    cause.printStackTrace();
                }
                System.out.println("Simulaciones finalizadas");
            }
        };

        worker.execute();
    }

    public void updateOPT(ComputerState state){
        processesOPT.setText(String.valueOf(state.getNumberOfProcesses()));
        simTimeOPT.setText(String.valueOf(state.getClock())+ "s");
        ramkbOPT.setText(String.valueOf(state.getUsedMemory()));
        ramperOPT.setText(String.valueOf(state.getUsedMemoryPerc()));
        vRamkbOPT.setText(String.valueOf(state.getUsedVMemory()));
        vRamPerOPT.setText(String.valueOf(state.getUsedVMemoryPerc())+ "%");
        pagesLoadedOPT.setText(String.valueOf(state.getLoadedPages()));
        pagesUnloadedOPT.setText(String.valueOf(state.getUnloadedPages()));
        trashingOPT.setText(String.valueOf(state.getThrashing())+ "s");
        if(state.getThrashingPerc() >= 50){
            trashingPerOPT.setBackground(Color.RED); 
        }
        trashingPerOPT.setText(String.valueOf(state.getThrashingPerc())+ "%");
        fragmentationOPT.setText(String.valueOf(state.getInternalFragmentation()));
        updateTable(state.getAllPages(), jTableOPT, OPT_ALGO);
        updatePanelColors(ramOPT, state.getPhysicalMem(), colors);
    }
    
    public void updateALGO(ComputerState state){
        if (algorithm == FIFO_ALGO) {
            textmmuALGO.setText("MMU-FIFO");
            textramALGO.setText("RAM-FIFO");
        }else if(algorithm == SC_ALGO){
            textmmuALGO.setText("MMU-SC");
            textramALGO.setText("RAM-SC");
        }else if(algorithm == MRU_ALGO){
            textmmuALGO.setText("MMU-MRU");
            textramALGO.setText("RAM-MRU");
        }else if (algorithm == RND_ALGO){
            textmmuALGO.setText("MMU-RND");
            textramALGO.setText("RAM-RND");
        }
        processesALGO.setText(String.valueOf(state.getNumberOfProcesses()));
        simTimeALGO.setText(String.valueOf(state.getClock())+ "s");
        ramkbALGO.setText(String.valueOf(state.getUsedMemory()));
        ramperALGO.setText(String.valueOf(state.getUsedMemoryPerc()));
        vRamkbALGO.setText(String.valueOf(state.getUsedVMemory()));
        vRamPerALGO.setText(String.valueOf(state.getUsedVMemoryPerc())+ "%");
        pagesLoadedALGO.setText(String.valueOf(state.getLoadedPages()));
        pagesUnloadedALGO.setText(String.valueOf(state.getUnloadedPages()));
        trashingALGO.setText(String.valueOf(state.getThrashing())+ "s");
        if(state.getThrashingPerc() >= 50){
            trashingPerALGO.setBackground(Color.RED); 
        }
        trashingPerALGO.setText(String.valueOf(state.getThrashingPerc())+ "%");
        fragmentationALGO.setText(String.valueOf(state.getInternalFragmentation()));
        updateTable(state.getAllPages(), jTableALGO, algorithm);
        updatePanelColors(ramALGO, state.getPhysicalMem(), colors);
    }

    public void updateTable(ArrayList<Page> pages, JTable table, PaginationAlgoType algoType) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 

        for (Page page : pages) {
            boolean sc;
            Object[] row = new Object[8];

            row[0] = page.getPageId();  
            row[1] = page.getProcessId();  
            row[2] = page.isInPhysicalMemory() ? "X" : "";  
            row[3] = page.getPointer();
            if(page.isInPhysicalMemory()){
                row[4] = page.getPhysicalAddress();
                row[5] = "";
                
                row[6] = formatTime(page.getTimestamp()) + "s";  
                
                if(algoType == SC_ALGO){
                    
                    sc = page.getSecondChance();
                    
                    if(sc == true){
                        row[7] = 1;
                    }else if(sc == false){
                        row[7] = 0;
                    }
                }else if(algoType == MRU_ALGO){
                    row[7] = formatTime(page.getLastUsage()) + "s";
                }else if (algoType == RND_ALGO || algoType == OPT_ALGO || algoType == FIFO_ALGO){
                    row[7] = "";
                }
            
            }else{
                row[4] = "";
                row[5] = page.getVirtualAddress();  
            } 
            
            model.addRow(row);
        }

        // Para los colores
        table.setDefaultRenderer(Object.class, createColorRenderer(colors));
    }
    
    public String formatTime(Instant timestamp) {
        ZonedDateTime zonedDateTime = timestamp.atZone(ZoneId.systemDefault());  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss");  
        return zonedDateTime.format(formatter);  
    }

    // Función que se encarga de renderizar las celdas de las tablas para poder aplicar color
    public TableCellRenderer createColorRenderer(ArrayList<Color> colors) {
        return (table, value, isSelected, hasFocus, row, column) -> {
            Component cell = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int processId = (Integer) table.getValueAt(row, 1);
            int colorIndex = processId % colors.size();
            cell.setBackground(colors.get(colorIndex));
            return cell;
        };
    }
    
    
    public void updatePanelColors(JPanel[] panels, Page[] values, ArrayList<Color> colors) {
        int panelCount = panels.length; 
        int valueCount = values.length; 

        for (int i = 0; i < panelCount; i++) {
            panels[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            if (i < valueCount) {
                if (values[i] != null) {
                    int value = values[i].getProcessId(); 
                    if (value >= 0 && value < colors.size()) {
                        panels[i].setBackground(colors.get(value));
                    } else {
                        panels[i].setBackground(Color.WHITE);
                    }
                } else {
                    panels[i].setBackground(Color.WHITE);
                }
            } else {
                panels[i].setBackground(Color.WHITE);
            }
        }
        for (JPanel panel : panels) {
            panel.revalidate();
            panel.repaint();
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
        processesALGO = new javax.swing.JTextField();
        simTimeALGO = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        vRamkbALGO = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        ramkbALGO = new javax.swing.JTextField();
        ramperALGO = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jScrollPaneOPT = new javax.swing.JScrollPane();
        jTableOPT = new javax.swing.JTable();
        trashingPerOPT = new javax.swing.JTextField();
        jScrollPaneALGO = new javax.swing.JScrollPane();
        jTableALGO = new javax.swing.JTable();
        vRamPerALGO = new javax.swing.JTextField();
        textmmuALGO = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        pagesLoadedALGO = new javax.swing.JTextField();
        pagesUnloadedALGO = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        trashingALGO = new javax.swing.JTextField();
        trashingPerALGO = new javax.swing.JTextField();
        fragmentationALGO = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jPanelRamOPT = new javax.swing.JPanel();
        jTextField12 = new javax.swing.JTextField();
        jPanelRamALGO = new javax.swing.JPanel();
        textramALGO = new javax.swing.JTextField();
        jButtonPause = new javax.swing.JButton();
        jButtonBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMaximumSize(new java.awt.Dimension(1170, 820));
        setMinimumSize(new java.awt.Dimension(1170, 820));
        setSize(new java.awt.Dimension(1170, 820));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMaximumSize(new java.awt.Dimension(1170, 820));
        jPanel1.setMinimumSize(new java.awt.Dimension(1170, 820));
        jPanel1.setPreferredSize(new java.awt.Dimension(1170, 820));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(153, 153, 153));
        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Processes");
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 270, 25));

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(153, 153, 153));
        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Sim-Time");
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 270, 25));

        processesOPT.setEditable(false);
        processesOPT.setBackground(new java.awt.Color(255, 255, 255));
        processesOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        processesOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(processesOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 565, 270, 25));

        simTimeOPT.setEditable(false);
        simTimeOPT.setBackground(new java.awt.Color(255, 255, 255));
        simTimeOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        simTimeOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(simTimeOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 565, 270, 25));

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(153, 153, 153));
        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("RAM KB");
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 135, 25));

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(153, 153, 153));
        jTextField6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("RAM %");
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 610, 135, 25));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(153, 153, 153));
        jTextField7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("V-RAM KB");
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 610, 135, 25));

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(153, 153, 153));
        jTextField8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("V-RAM %");
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 610, 135, 25));

        ramkbOPT.setEditable(false);
        ramkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 635, 135, 25));

        ramperOPT.setEditable(false);
        ramperOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramperOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramperOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramperOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 635, 135, 25));

        vRamkbOPT.setEditable(false);
        vRamkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 635, 135, 25));

        vRamPerOPT.setEditable(false);
        vRamPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 635, 135, 25));

        jTextField13.setEditable(false);
        jTextField13.setBackground(new java.awt.Color(153, 153, 153));
        jTextField13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField13.setText("Pages");
        jPanel1.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 270, 25));

        jTextField14.setEditable(false);
        jTextField14.setBackground(new java.awt.Color(204, 204, 204));
        jTextField14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setText("LOADED");
        jPanel1.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 705, 135, 25));

        jTextField15.setEditable(false);
        jTextField15.setBackground(new java.awt.Color(204, 204, 204));
        jTextField15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setText("UNLOADED");
        jPanel1.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 705, 135, 25));

        pagesLoadedOPT.setEditable(false);
        pagesLoadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesLoadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesLoadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesLoadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 730, 135, 25));

        pagesUnloadedOPT.setEditable(false);
        pagesUnloadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesUnloadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesUnloadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesUnloadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 730, 135, 25));

        jTextField18.setEditable(false);
        jTextField18.setBackground(new java.awt.Color(153, 153, 153));
        jTextField18.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setText("Trashing");
        jPanel1.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 680, 135, 25));

        jTextField19.setEditable(false);
        jTextField19.setBackground(new java.awt.Color(153, 153, 153));
        jTextField19.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField19.setText("Fragmentation");
        jPanel1.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 680, 135, 25));

        trashingOPT.setEditable(false);
        trashingOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 705, 67, 50));

        fragmentationOPT.setEditable(false);
        fragmentationOPT.setBackground(new java.awt.Color(255, 255, 255));
        fragmentationOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        fragmentationOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(fragmentationOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 705, 135, 50));

        jTextField22.setEditable(false);
        jTextField22.setBackground(new java.awt.Color(153, 153, 153));
        jTextField22.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField22.setText("Processes");
        jPanel1.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 540, 270, 25));

        jTextField23.setEditable(false);
        jTextField23.setBackground(new java.awt.Color(153, 153, 153));
        jTextField23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField23.setText("Sim-Time");
        jPanel1.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 540, 270, 25));

        processesALGO.setEditable(false);
        processesALGO.setBackground(new java.awt.Color(255, 255, 255));
        processesALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        processesALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        processesALGO.setToolTipText("");
        jPanel1.add(processesALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 565, 270, 25));

        simTimeALGO.setEditable(false);
        simTimeALGO.setBackground(new java.awt.Color(255, 255, 255));
        simTimeALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        simTimeALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(simTimeALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 565, 270, 25));

        jTextField26.setEditable(false);
        jTextField26.setBackground(new java.awt.Color(153, 153, 153));
        jTextField26.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField26.setText("RAM KB");
        jPanel1.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 610, 135, 25));

        vRamkbALGO.setEditable(false);
        vRamkbALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamkbALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamkbALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 635, 135, 25));

        jTextField28.setEditable(false);
        jTextField28.setBackground(new java.awt.Color(153, 153, 153));
        jTextField28.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField28.setText("RAM %");
        jPanel1.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 610, 135, 25));

        jTextField29.setEditable(false);
        jTextField29.setBackground(new java.awt.Color(153, 153, 153));
        jTextField29.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField29.setText("V-RAM %");
        jPanel1.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(995, 610, 135, 25));

        ramkbALGO.setEditable(false);
        ramkbALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramkbALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramkbALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 635, 135, 25));

        ramperALGO.setEditable(false);
        ramperALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramperALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramperALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 635, 135, 25));

        jTextField33.setEditable(false);
        jTextField33.setBackground(new java.awt.Color(153, 153, 153));
        jTextField33.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField33.setText("V-RAM KB");
        jPanel1.add(jTextField33, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 610, 135, 25));

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

        jPanel1.add(jScrollPaneOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 540, 360));

        trashingPerOPT.setEditable(false);
        trashingPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 705, 67, 50));

        jTableALGO.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPaneALGO.setViewportView(jTableALGO);

        jPanel1.add(jScrollPaneALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 540, 360));

        vRamPerALGO.setEditable(false);
        vRamPerALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamPerALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamPerALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(995, 635, 135, 25));

        textmmuALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        textmmuALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textmmuALGO.setText("MMU ALGO");
        jPanel1.add(textmmuALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 135, 540, -1));

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(153, 153, 153));
        jTextField9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("Pages");
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 680, 270, 25));

        jTextField10.setEditable(false);
        jTextField10.setBackground(new java.awt.Color(204, 204, 204));
        jTextField10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("LOADED");
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 705, 135, 25));

        jTextField11.setEditable(false);
        jTextField11.setBackground(new java.awt.Color(204, 204, 204));
        jTextField11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setText("UNLOADED");
        jPanel1.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 705, 135, 25));

        pagesLoadedALGO.setEditable(false);
        pagesLoadedALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesLoadedALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesLoadedALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 730, 135, 25));

        pagesUnloadedALGO.setEditable(false);
        pagesUnloadedALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesUnloadedALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesUnloadedALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 730, 135, 25));

        jTextField17.setEditable(false);
        jTextField17.setBackground(new java.awt.Color(153, 153, 153));
        jTextField17.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setText("Trashing");
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 680, 135, 25));

        jTextField20.setEditable(false);
        jTextField20.setBackground(new java.awt.Color(153, 153, 153));
        jTextField20.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField20.setText("Fragmentation");
        jPanel1.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(995, 680, 135, 25));

        trashingALGO.setEditable(false);
        trashingALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 705, 67, 50));

        trashingPerALGO.setEditable(false);
        trashingPerALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingPerALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingPerALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(928, 705, 67, 50));

        fragmentationALGO.setEditable(false);
        fragmentationALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        fragmentationALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fragmentationALGO.setToolTipText("");
        jPanel1.add(fragmentationALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(995, 705, 135, 50));

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("MMU OPT");
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 540, -1));

        javax.swing.GroupLayout jPanelRamOPTLayout = new javax.swing.GroupLayout(jPanelRamOPT);
        jPanelRamOPT.setLayout(jPanelRamOPTLayout);
        jPanelRamOPTLayout.setHorizontalGroup(
            jPanelRamOPTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelRamOPTLayout.setVerticalGroup(
            jPanelRamOPTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanelRamOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 30, 1000, 20));

        jTextField12.setEditable(false);
        jTextField12.setBackground(new java.awt.Color(255, 255, 255));
        jTextField12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField12.setText("RAM-OPT");
        jPanel1.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 5, 1003, -1));

        jPanelRamALGO.setPreferredSize(new java.awt.Dimension(1000, 20));

        javax.swing.GroupLayout jPanelRamALGOLayout = new javax.swing.GroupLayout(jPanelRamALGO);
        jPanelRamALGO.setLayout(jPanelRamALGOLayout);
        jPanelRamALGOLayout.setHorizontalGroup(
            jPanelRamALGOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelRamALGOLayout.setVerticalGroup(
            jPanelRamALGOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanelRamALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 90, 1000, 20));

        textramALGO.setEditable(false);
        textramALGO.setBackground(new java.awt.Color(255, 255, 255));
        textramALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        textramALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textramALGO.setText("RAM-ALGO");
        jPanel1.add(textramALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 65, 1003, -1));

        jButtonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/memory_simulator/view/images/paused.png"))); // NOI18N
        jButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPauseActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPause, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 40, 40));

        jButtonBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/memory_simulator/view/images/back.png"))); // NOI18N
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 10, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPauseActionPerformed
        if(paused == false){
            jButtonPause.setIcon(new ImageIcon(getClass().getResource("images/play.png")));
            paused = true;
        }else if(paused == true){
            paused = false;
            synchronized (this) {
                notify(); // Despierta al hilo si estaba en pausa
            }
            jButtonPause.setIcon(new ImageIcon(getClass().getResource("images/paused.png")));
        }
    }//GEN-LAST:event_jButtonPauseActionPerformed

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        PrincipalWindow window = new PrincipalWindow();
        window.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButtonBackActionPerformed

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
    private javax.swing.JTextField fragmentationALGO;
    private javax.swing.JTextField fragmentationOPT;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonPause;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelRamALGO;
    private javax.swing.JPanel jPanelRamOPT;
    private javax.swing.JScrollPane jScrollPaneALGO;
    private javax.swing.JScrollPane jScrollPaneOPT;
    private javax.swing.JTable jTableALGO;
    private javax.swing.JTable jTableOPT;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField pagesLoadedALGO;
    private javax.swing.JTextField pagesLoadedOPT;
    private javax.swing.JTextField pagesUnloadedALGO;
    private javax.swing.JTextField pagesUnloadedOPT;
    private javax.swing.JTextField processesALGO;
    private javax.swing.JTextField processesOPT;
    private javax.swing.JTextField ramkbALGO;
    private javax.swing.JTextField ramkbOPT;
    private javax.swing.JTextField ramperALGO;
    private javax.swing.JTextField ramperOPT;
    private javax.swing.JTextField simTimeALGO;
    private javax.swing.JTextField simTimeOPT;
    private javax.swing.JTextField textmmuALGO;
    private javax.swing.JTextField textramALGO;
    private javax.swing.JTextField trashingALGO;
    private javax.swing.JTextField trashingOPT;
    private javax.swing.JTextField trashingPerALGO;
    private javax.swing.JTextField trashingPerOPT;
    private javax.swing.JTextField vRamPerALGO;
    private javax.swing.JTextField vRamPerOPT;
    private javax.swing.JTextField vRamkbALGO;
    private javax.swing.JTextField vRamkbOPT;
    // End of variables declaration//GEN-END:variables
}
