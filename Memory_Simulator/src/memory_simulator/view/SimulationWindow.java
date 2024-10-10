
package memory_simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

/**
 *
 * @author paubo
 */
public final class SimulationWindow extends javax.swing.JFrame {

    Simulation simulationALGO;
    Simulation simulationOPT;
    ArrayList<Color> colors; 
    PaginationAlgoType algorithm;
    JPanel[] ramOPT;
    JPanel[] ramALGO;
    
    public SimulationWindow(PaginationAlgoType algoType, ArrayList<String> instructions, int seed){
        initComponents();
        
        // Se inician las simulaciones
        simulationALGO = new Simulation(algoType, instructions, seed);
        simulationOPT = new Simulation(SC_ALGO, instructions, seed);
        algorithm = algoType;
        
        // Generar listas de colores para cada algoritmo 
        colors = new ArrayList<>();;
        colorGenerator(colors);
        
        // Creación de las barras que simulan la ram
        ramOPT = makeRAMBar(jPanelRamOPT);
        ramALGO = makeRAMBar(jPanelRamALGO);
        
        // Se inicia la simulación
        startSimulation(); 
    }
    
    public JPanel[] makeRAMBar(JPanel panel){
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); 

        // Crear un arreglo para almacenar los 100 JPanels
        JPanel[] cells = new JPanel[100];

        // Crear 100 JPanels y añadirlos al mainPanel
        for (int i = 0; i < 100; i++) {
            cells[i] = new JPanel(); // Asignar el nuevo JPanel al arreglo
            cells[i].setPreferredSize(new Dimension(11, 10)); 
            panel.add(cells[i]);
        }
        
        return cells; 
    }
    
    public final void startSimulation() {
        // Primer SwingWorker para la simulación con algoType (simulationALGO)
        SwingWorker<Void, ComputerState> workerALGO = new SwingWorker<Void, ComputerState>() {

            @Override
            protected Void doInBackground() throws Exception {
                // Ejecutar la simulación ALGO en segundo plano
                while (simulationALGO.executeNext()) {
                    ComputerState state = simulationALGO.getState();
                    publish(state);  
                    Thread.sleep(10);  // Pausa para simular tiempo de procesamiento
                }
                return null; 
            }

            @Override
            protected void process(java.util.List<ComputerState> states) {
                if (!states.isEmpty()) {
                    ComputerState latestState = states.get(states.size() - 1);
                    updateALGO(latestState);  // Actualizar la simulación ALGO
                }
            }

            @Override
            protected void done() {
                System.out.println("Simulación ALGO finalizada");
            }
        };

        // Segundo SwingWorker para la simulación con OPT_ALGO (simulationOPT)
        SwingWorker<Void, ComputerState> workerOPT = new SwingWorker<Void, ComputerState>() {

            @Override
            protected Void doInBackground() throws Exception {
                // Ejecutar la simulación OPT en segundo plano
                while (simulationOPT.executeNext()) {
                    ComputerState state = simulationOPT.getState();
                    publish(state);  
                    Thread.sleep(10);  
                }
                return null; 
            }

            @Override
            protected void process(java.util.List<ComputerState> states) {
                if (!states.isEmpty()) {
                    ComputerState latestState = states.get(states.size() - 1);
                    updateOPT(latestState);  // Actualizar la simulación OPT
                }
            }

            @Override
            protected void done() {
                System.out.println("Simulación OPT finalizada");
            }
        };

        // Ejecutar ambos SwingWorkers en paralelo
        workerALGO.execute();
        workerOPT.execute();
    }

    public void updateOPT(ComputerState state){
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
        simTimeALGO.setText(String.valueOf(state.getClock()));
        ramkbALGO.setText(String.valueOf(state.getUsedMemory()));
        ramperALGO.setText(String.valueOf(state.getUsedMemoryPerc()));
        vRamkbALGO.setText(String.valueOf(state.getUsedVMemory()));
        vRamPerALGO.setText(String.valueOf(state.getUsedVMemoryPerc()));
        pagesLoadedALGO.setText(String.valueOf(state.getLoadedPages()));
        pagesUnloadedALGO.setText(String.valueOf(state.getUnloadedPages()));
        trashingALGO.setText(String.valueOf(state.getThrashing()));
        trashingPerALGO.setText(String.valueOf(state.getThrashingPerc()));
        fragmentationALGO.setText(String.valueOf(state.getInternalFragmentation()));
        updateTable(state.getAllPages(), jTableALGO, algorithm);
        updatePanelColors(ramALGO, state.getPhysicalMem(), colors);
    }

    public void updateTable(ArrayList<Page> pages, JTable table, PaginationAlgoType algoType) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
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
            if (algoType == FIFO_ALGO) {
                row[7] = page.getTimestamp();
            }else if(algoType == SC_ALGO){
                row[7] = page.getSecondChance();
            }else if(algoType == MRU_ALGO){
                row[7] = page.getLastUsage();
            }else if (algoType == RND_ALGO || algoType == OPT_ALGO ){
                row[7] = "";
            }
            model.addRow(row);
        }

        // Para los colores
        table.setDefaultRenderer(Object.class, createColorRenderer(colors));
    }

    // Función que se encarga de renderizar las celdas de las tablas para poder aplicar color
    public TableCellRenderer createColorRenderer(ArrayList<Color> colors) {
        return (table, value, isSelected, hasFocus, row, column) -> {
            Component cell = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Obtener el valor de ProcessId de la columna 1
            int processId = (Integer) table.getValueAt(row, 1);

            // Asegúrate de que el índice de color esté dentro de los límites
            int colorIndex = processId % colors.size();

            // Establecer el color de fondo
            cell.setBackground(colors.get(colorIndex));
            return cell;
        };
    }
    
    
    public void updatePanelColors(JPanel[] panels, Page[] values, ArrayList<Color> colors) {
        int panelCount = panels.length; // Número de paneles
        int valueCount = values.length; // Número de valores en el array de Page

        // Recorremos todos los paneles
        for (int i = 0; i < panelCount; i++) {
            panels[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            if (i < valueCount) {
                // Verifica que el valor en el array de Page no sea null
                if (values[i] != null) {
                    // Si hay un valor en el array de Page para este índice, se usa el valor para obtener el color correspondiente
                    int value = values[i].getProcessId(); // Asegúrate de que getProcessId() esté bien implementado

                    // Verifica si el valor es un índice válido para el arreglo de colores
                    if (value >= 0 && value < colors.size()) {
                        // Asigna el color correspondiente
                        panels[i].setBackground(colors.get(value));
                    } else {
                        // Si el valor excede el tamaño del arreglo de colores, asignar color por defecto (blanco)
                        panels[i].setBackground(Color.WHITE);
                    }
                } else {
                    // Si el objeto Page es null, establecer en blanco
                    panels[i].setBackground(Color.WHITE);
                }
            } else {
                // Si no hay más valores en el array de Page, establecer en blanco
                panels[i].setBackground(Color.WHITE);
            }
        }

        // Actualizar los paneles después de asignar los colores
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMinimumSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMaximumSize(new java.awt.Dimension(1090, 780));
        jPanel1.setMinimumSize(new java.awt.Dimension(1090, 780));
        jPanel1.setPreferredSize(new java.awt.Dimension(1090, 780));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(153, 153, 153));
        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Processes");
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 255, 25));

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(153, 153, 153));
        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Sim-Time");
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 540, 255, 25));

        processesOPT.setEditable(false);
        processesOPT.setBackground(new java.awt.Color(255, 255, 255));
        processesOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        processesOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(processesOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 565, 255, 25));

        simTimeOPT.setEditable(false);
        simTimeOPT.setBackground(new java.awt.Color(255, 255, 255));
        simTimeOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        simTimeOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(simTimeOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 565, 255, 25));

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(153, 153, 153));
        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("RAM KB");
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 127, 25));

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(153, 153, 153));
        jTextField6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("RAM %");
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 610, 127, 25));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(153, 153, 153));
        jTextField7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("V-RAM KB");
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 610, 127, 25));

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(153, 153, 153));
        jTextField8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("V-RAM %");
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 610, 126, 25));

        ramkbOPT.setEditable(false);
        ramkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 635, 127, 25));

        ramperOPT.setEditable(false);
        ramperOPT.setBackground(new java.awt.Color(255, 255, 255));
        ramperOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramperOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramperOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 635, 127, 25));

        vRamkbOPT.setEditable(false);
        vRamkbOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamkbOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamkbOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamkbOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 635, 127, 25));

        vRamPerOPT.setEditable(false);
        vRamPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        vRamPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 635, 126, 25));

        jTextField13.setEditable(false);
        jTextField13.setBackground(new java.awt.Color(153, 153, 153));
        jTextField13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField13.setText("Pages");
        jPanel1.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 254, 25));

        jTextField14.setEditable(false);
        jTextField14.setBackground(new java.awt.Color(204, 204, 204));
        jTextField14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setText("LOADED");
        jPanel1.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 705, 127, 25));

        jTextField15.setEditable(false);
        jTextField15.setBackground(new java.awt.Color(204, 204, 204));
        jTextField15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setText("UNLOADED");
        jPanel1.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 705, 127, 25));

        pagesLoadedOPT.setEditable(false);
        pagesLoadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesLoadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesLoadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesLoadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 730, 127, 25));

        pagesUnloadedOPT.setEditable(false);
        pagesUnloadedOPT.setBackground(new java.awt.Color(255, 255, 255));
        pagesUnloadedOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesUnloadedOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesUnloadedOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 730, 127, 25));

        jTextField18.setEditable(false);
        jTextField18.setBackground(new java.awt.Color(153, 153, 153));
        jTextField18.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setText("Trashing");
        jPanel1.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 680, 127, 25));

        jTextField19.setEditable(false);
        jTextField19.setBackground(new java.awt.Color(153, 153, 153));
        jTextField19.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField19.setText("Fragmentation");
        jPanel1.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 680, 127, 25));

        trashingOPT.setEditable(false);
        trashingOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 705, 63, 50));

        fragmentationOPT.setEditable(false);
        fragmentationOPT.setBackground(new java.awt.Color(255, 255, 255));
        fragmentationOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        fragmentationOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(fragmentationOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 705, 127, 50));

        jTextField22.setEditable(false);
        jTextField22.setBackground(new java.awt.Color(153, 153, 153));
        jTextField22.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField22.setText("Processes");
        jPanel1.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 540, 255, 25));

        jTextField23.setEditable(false);
        jTextField23.setBackground(new java.awt.Color(153, 153, 153));
        jTextField23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField23.setText("Sim-Time");
        jPanel1.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(825, 540, 255, 25));

        processesALGO.setEditable(false);
        processesALGO.setBackground(new java.awt.Color(255, 255, 255));
        processesALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        processesALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        processesALGO.setToolTipText("");
        jPanel1.add(processesALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 565, 255, 25));

        simTimeALGO.setEditable(false);
        simTimeALGO.setBackground(new java.awt.Color(255, 255, 255));
        simTimeALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        simTimeALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(simTimeALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(825, 565, 255, 25));

        jTextField26.setEditable(false);
        jTextField26.setBackground(new java.awt.Color(153, 153, 153));
        jTextField26.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField26.setText("RAM KB");
        jPanel1.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 610, 112, 25));

        vRamkbALGO.setEditable(false);
        vRamkbALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamkbALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamkbALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 635, 112, 25));

        jTextField28.setEditable(false);
        jTextField28.setBackground(new java.awt.Color(153, 153, 153));
        jTextField28.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField28.setText("RAM %");
        jPanel1.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 610, 112, 25));

        jTextField29.setEditable(false);
        jTextField29.setBackground(new java.awt.Color(153, 153, 153));
        jTextField29.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField29.setText("V-RAM %");
        jPanel1.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 610, 112, 25));

        ramkbALGO.setEditable(false);
        ramkbALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramkbALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramkbALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 635, 112, 25));

        ramperALGO.setEditable(false);
        ramperALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        ramperALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(ramperALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 635, 112, 25));

        jTextField33.setEditable(false);
        jTextField33.setBackground(new java.awt.Color(153, 153, 153));
        jTextField33.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField33.setText("V-RAM KB");
        jPanel1.add(jTextField33, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 610, 112, 25));

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

        jPanel1.add(jScrollPaneOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 510, 360));

        trashingPerOPT.setEditable(false);
        trashingPerOPT.setBackground(new java.awt.Color(255, 255, 255));
        trashingPerOPT.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingPerOPT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingPerOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 705, 64, 50));

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

        jPanel1.add(jScrollPaneALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 510, 360));

        vRamPerALGO.setEditable(false);
        vRamPerALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        vRamPerALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(vRamPerALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 635, 112, 25));

        textmmuALGO.setBackground(new java.awt.Color(153, 153, 153));
        textmmuALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        textmmuALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textmmuALGO.setText("MMU ALGO");
        jPanel1.add(textmmuALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 510, -1));

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(153, 153, 153));
        jTextField9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("Pages");
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 680, 224, 25));

        jTextField10.setEditable(false);
        jTextField10.setBackground(new java.awt.Color(204, 204, 204));
        jTextField10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("LOADED");
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 705, 112, 25));

        jTextField11.setEditable(false);
        jTextField11.setBackground(new java.awt.Color(204, 204, 204));
        jTextField11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setText("UNLOADED");
        jPanel1.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 705, 114, 25));

        pagesLoadedALGO.setEditable(false);
        pagesLoadedALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesLoadedALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesLoadedALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 730, 112, 25));

        pagesUnloadedALGO.setEditable(false);
        pagesUnloadedALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        pagesUnloadedALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(pagesUnloadedALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 730, 114, 25));

        jTextField17.setEditable(false);
        jTextField17.setBackground(new java.awt.Color(153, 153, 153));
        jTextField17.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setText("Trashing");
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 680, 112, 25));

        jTextField20.setEditable(false);
        jTextField20.setBackground(new java.awt.Color(153, 153, 153));
        jTextField20.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField20.setText("Fragmentation");
        jPanel1.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 680, 112, 25));

        trashingALGO.setEditable(false);
        trashingALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 705, 56, 50));

        trashingPerALGO.setEditable(false);
        trashingPerALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        trashingPerALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(trashingPerALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 705, 57, 50));

        fragmentationALGO.setEditable(false);
        fragmentationALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        fragmentationALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fragmentationALGO.setToolTipText("");
        jPanel1.add(fragmentationALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(907, 705, 112, 50));

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(153, 153, 153));
        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("MMU OPT");
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 510, -1));

        javax.swing.GroupLayout jPanelRamOPTLayout = new javax.swing.GroupLayout(jPanelRamOPT);
        jPanelRamOPT.setLayout(jPanelRamOPTLayout);
        jPanelRamOPTLayout.setHorizontalGroup(
            jPanelRamOPTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
        );
        jPanelRamOPTLayout.setVerticalGroup(
            jPanelRamOPTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanelRamOPT, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 30, 1100, 20));

        jTextField12.setEditable(false);
        jTextField12.setBackground(new java.awt.Color(153, 153, 153));
        jTextField12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField12.setText("RAM-OPT");
        jPanel1.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 10, 1101, -1));

        javax.swing.GroupLayout jPanelRamALGOLayout = new javax.swing.GroupLayout(jPanelRamALGO);
        jPanelRamALGO.setLayout(jPanelRamALGOLayout);
        jPanelRamALGOLayout.setHorizontalGroup(
            jPanelRamALGOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
        );
        jPanelRamALGOLayout.setVerticalGroup(
            jPanelRamALGOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanelRamALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 90, 1100, 20));

        textramALGO.setEditable(false);
        textramALGO.setBackground(new java.awt.Color(153, 153, 153));
        textramALGO.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        textramALGO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textramALGO.setText("RAM-ALGO");
        jPanel1.add(textramALGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 70, 1101, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1150, javax.swing.GroupLayout.PREFERRED_SIZE)
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
