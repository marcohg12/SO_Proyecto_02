package memory_simulator.model;

import java.util.ArrayList;
import java.util.Collections;

public class ComputerState {
    
    private Page[] physicalMem;
    private ArrayList<Page> virtualMem;
    private ArrayList<Page> allPages;
    private int clock;
    private int thrashing;
    private ArrayList<Process> processes;
    private int numberOfProcesses;
    private int loadedPages;
    private int unloadedPages;
    private int internalFragmentation;
    private int usedMemory;
    private int usedVMemory;
    private double usedMemoryPerc;
    private double usedVMemoryPerc;
    private double thrashingPerc;
    private int physicalMemSize;
    
    public ComputerState(Page[] physicalMem, ArrayList<Page> virtualMem, 
                         int clock, int thrashing, ArrayList<Process> processes, int physicalMemSize){
        
        this.physicalMem = new Page[physicalMem.length];
        this.loadedPages = 0;
        this.unloadedPages = 0;
        this.clock = clock;
        this.processes = new ArrayList();
        this.numberOfProcesses = processes.size();
        this.thrashing = thrashing; 
        this.allPages = new ArrayList();
        this.internalFragmentation = 0;
        this.usedMemory = 0;
        this.usedVMemory = 0;
        this.usedMemoryPerc = 0.0;
        this.usedVMemoryPerc = 0.0;
        this.thrashingPerc = 0.0;
        this.physicalMemSize = physicalMemSize;
        
        // Realiza una copia de la lista de procesos
        for (int i = 0; i < processes.size(); i++){
            Process process = processes.get(i);
            this.processes.add(process.clone());
        }
        
        // Realiza una copia de la memoria física
        for (int i = 0; i < physicalMem.length; i++){
           Page page = physicalMem[i];
           if (page == null){
               this.physicalMem[i] = null;
           } else {
               this.physicalMem[i] = page.clone();
           }
        }
        
        // Realiza una copia de la memoria virtual
        this.virtualMem = new ArrayList();
        for (Page page : virtualMem){
            this.virtualMem.add(page.clone());
        }
        
        // Obtiene las páginas de la memoria física y las
        // agrega a la lista de todas las páginas
        // Además, actualiza la cantidad de páginas cargadas,
        // fragmentación interna y total de memoria usada
        for (int i = 0; i < this.physicalMem.length; i++){
            
            Page page = this.physicalMem[i];
            
            if (page == null){
                continue;
            }
            
            allPages.add(this.physicalMem[i]);
            loadedPages += 1;
            
            if (page.getSpaceUsed() < 4){
                internalFragmentation += 4 - page.getSpaceUsed();
            }
            
            usedMemory += 4;
        }
        
        // Obtiene las páginas de la memoria virtual y las
        // agrega a la lista de todas las páginas
        // Además, actualiza la cantidad de páginas no cargadas y 
        // la cantidad de memoria virtual usada
        for (Page page : virtualMem){
            allPages.add(page);
            unloadedPages += 1;
            usedVMemory += 4;
        }
        
        // Ordenamos la lista de todas las páginas por id
        Collections.sort(allPages, (Page p1, Page p2) -> Integer.compare(p1.getPageId(), p2.getPageId()));
        
        // Calculamos el porcentaje de memoria física y virtual usadas
        usedMemoryPerc = (usedMemory * 100) / this.physicalMemSize;
        usedVMemoryPerc = (usedVMemory * 100) / this.physicalMemSize;
        
        // Calculamos el porcentaje de thrashing
        thrashingPerc = (this.thrashing * 100) / this.clock;
    }

    public Page[] getPhysicalMem() {
        return physicalMem;
    }

    public ArrayList<Page> getVirtualMem() {
        return virtualMem;
    }

    public ArrayList<Page> getAllPages() {
        return allPages;
    }

    public int getClock() {
        return clock;
    }

    public int getThrashing() {
        return thrashing;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public int getLoadedPages() {
        return loadedPages;
    }

    public int getUnloadedPages() {
        return unloadedPages;
    }

    public int getInternalFragmentation() {
        return internalFragmentation;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public int getUsedVMemory() {
        return usedVMemory;
    }

    public double getUsedMemoryPerc() {
        return usedMemoryPerc;
    }

    public double getUsedVMemoryPerc() {
        return usedVMemoryPerc;
    }

    public double getThrashingPerc() {
        return thrashingPerc;
    }

    public int getPhysicalMemSize() {
        return physicalMemSize;
    }  

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }
}
