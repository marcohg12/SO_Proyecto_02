package memory_simulator.model;

import java.util.ArrayList;

public class Computer {
    
    private MMU mmu;
    private ArrayList<Process> processes;
    
    Computer(){
        mmu = new MMU();
        processes = new ArrayList();
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
        }
        
        // Solicitamos la memoria para el proceso a la MMU
        int pointer = mmu.createPagesForProcess(size);
        
        // Guardamos el puntero en la lista de punteros del proceso
        process.insertPointer(pointer);
    }
    
    public void executeDelete(int pointer){
        mmu.releasePointer(pointer);
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
