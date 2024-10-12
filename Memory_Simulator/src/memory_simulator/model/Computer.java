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
    
    /**
     * Retorna el estado de la computadora. Incluye el contenido de
     * memoria física, virtual y una lista con todas las páginas ordenadas
     * por ID de forma ascendente. También retorna estadísticas de la ejecución
     * actual.
     * @return Retorna un objeto ComputerState con el estado de la computadora.
     */
    public ComputerState getState(){
        return new ComputerState(mmu.getPhysicalMem(), mmu.getVirtualMem(), 
                                 mmu.getClock(), mmu.getThrashing(), 
                                 processes, mmu.getPhysicalMemSize());
    }
    
    /**
     * Ejecuta una instrucción NEW en la computadora. Crea un puntero
     * para las páginas asignadas en memoria y lo retorna al proceso.
     * @param pId El identificador del proceso.
     * @param size El tamaño en KB de memoria solitiada.
     */
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
    
    /**
     * Ejecuta una instrucción DELETE. Se eliminan todas las páginas
     * asociadas al puntero de la memoria y la entrada del mapa
     * de memoria para el puntero.
     * @param pointer El puntero a liberar.
     */
    public void executeDelete(int pointer){
        
        mmu.releasePointer(pointer);
        
        // Eliminamos el puntero del proceso correspondiente
        for (Process p : processes){
            if (p.getPointers().contains(pointer)){
                p.getPointers().remove(Integer.valueOf(pointer));
                break;
            }
        }
    }
    
    /**
     * Ejecuta una instrucción USE. Carga todas las páginas asociadas al
     * puntero a memoria RAM. 
     * @param pointer El puntero a utilizar. 
     */
    public void executeUse(int pointer){
        mmu.usePointer(pointer);
    }
    
    /**
     * Ejecuta una instrucción KILL. Libera todos los punteros (y sus páginas)
     * asociados al proceso.
     * @param pId El identificador del proceso.
     */
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
