package memory_simulator.model;

import java.util.ArrayList;
import memory_simulator.logic.PaginationAlgorithm;

public class Computer {
    
    private MMU mmu;
    private ArrayList<Process> processes;
    private PaginationAlgorithm paginationAlgorithm;
    
    public Computer(PaginationAlgorithm paginationAlgorithm){
        this.paginationAlgorithm = paginationAlgorithm;
        mmu = new MMU(this.paginationAlgorithm);
        processes = new ArrayList();
    }
    
    public ComputerState getState(){
        return new ComputerState(mmu.getPhysicalMem(), mmu.getVirtualMem(), 
                                 mmu.getClock(), mmu.getThrashing(), 
                                 processes, mmu.getPhysicalMemSize());
    }
    
    public void executeNew(int pId, int size){
        
        // Creamos un objeto para el proceso si no existe anteriormente
        Process process = null;
        for (Process p : processes){
            if (p.getpId() == pId){
                process = p;
                break;
            }
        }
        
        if (process == null){
            process = new Process(pId);
            processes.add(process);
        }
        
        // Solicitamos la memoria para el proceso a la MMU
        int pointer = mmu.createPagesForProcess(pId, size);
        
        // Guardamos el puntero en la lista de punteros del proceso
        process.insertPointer(pointer);
    }
    
    public void executeDelete(int pointer){
        mmu.releasePointer(pointer);
    }
    
    public void executeUse(int pointer){
        mmu.usePointer(pointer);
    }
    
    public void executeKill(int pId){
        
        // Obtenemos el objeto del proceso
        Process process = null;
        for (Process p : processes){
            if (p.getpId() == pId){
                process = p;
                break;
            }
        }
        
        if (process == null){
            return;
        }
        
        // Obtenemos la lista de punteros del proceso
        ArrayList<Integer> pointers = process.getPointers();
        
        // Eliminamos todos los punteros del proceso
        for (Integer pointer : pointers){
            mmu.releasePointer(pointer);
        }
        
        // Eliminamos el proceso de la lista de procesos
        processes.remove(process);
    }  
}
