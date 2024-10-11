package memory_simulator.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import memory_simulator.logic.FIFO;
import memory_simulator.logic.OPT;
import memory_simulator.logic.PaginationAlgorithm;

public class MMU {
    
    private int physicalMemSize;
    private Page[] physicalMem;
    private LinkedList<Page> virtualMem;
    private HashMap<Integer, ArrayList<Page>> memMap;
    private int pageSize;
    private int pageCount;
    private int pointerCount;
    private int clock;
    private int thrashing;
    private PaginationAlgorithm paginationAlgorithm;
    
    public MMU(PaginationAlgorithm paginationAlgorithm){
        
        physicalMemSize = 400;
        pageSize = 4;
        pageCount = 0;
        pointerCount = 1;
        clock = 0;
        thrashing = 0;
        physicalMem = new Page[physicalMemSize / pageSize];
        virtualMem = new LinkedList();
        memMap = new HashMap();
        this.paginationAlgorithm = paginationAlgorithm;
        
        // Iniciamos toda la memoria física en null (espacios libres)
        for (int i = 0; i < physicalMem.length; i++){
            physicalMem[i] = null;
        }
    }
    
    private int getEmptyAddress(){
        for (int i = 0; i < physicalMem.length; i++){
            if (physicalMem[i] == null){
                return i;
            }
        }
        return -1;
    }
    
    private Page removePageFromVirtualMem(int pageId){
        
        for (int i = 0; i < virtualMem.size(); i++){
            
            if (virtualMem.get(i).getPageId() == pageId){
                return virtualMem.remove(i);
            }
        }
        
        return null;
    }
    
    public int createPagesForProcess(int pId, int size){
        
        int processPointer = pointerCount;
        pointerCount += 1;
        ArrayList<Page> pointerPages = new ArrayList();
        
        // Calculamos cuantas páginas se necesitan para el proceso
        int pagesToCreate = size / 4;
        if (size % 4 != 0){
            pagesToCreate += 1;
        }
        
        // Insertamos las páginas en memoria
        for (int i = 0; i < pagesToCreate; i++){
            
            // Removemos el uso de la página para el algoritmo óptimo
            if (paginationAlgorithm instanceof OPT){
                ((OPT)paginationAlgorithm).removeUsage();
            }
            
            int spaceUsed = 4;
            
            if (i == pagesToCreate - 1){
                spaceUsed = size % 4;
            }
            
            int emptyAddress = getEmptyAddress();
            Page page;
            
            if (emptyAddress != -1){
                
                // Si tenemos un espacio libre en memoria, entonces
                // insertamos la página en esa dirección
                page = new Page(pageCount, emptyAddress, true, spaceUsed, pId);
                page.setTimestamp(Instant.now());
                page.setLastUsage(Instant.now());
                page.setSecondChance(true);
                physicalMem[emptyAddress] = page;
                
                // Actualizamos los tiempos
                clock += 1;
            } 
            else {
                
                // Si no, escogemos una página de memoria física
                // para intercambiar
                Page pageToRemove = paginationAlgorithm.getPageToRemove(physicalMem);
                page = new Page(pageCount, pageToRemove.getPhysicalAddress(), true, spaceUsed, pId);
                page.setTimestamp(Instant.now());
                page.setLastUsage(Instant.now());
                page.setSecondChance(true);
                
                // Intercambiamos las páginas
                pageToRemove.setInPhysicalMemory(false);
                pageToRemove.setPhysicalAddress(-1);
                virtualMem.add(pageToRemove);
                physicalMem[page.getPhysicalAddress()] = page;
                
                // Actualizamos los tiempos
                clock += 5;
                thrashing += 5;
            }
            
            // Agregamos la página a las páginas asociadas al puntero
            pointerPages.add(page);
            
            // Actualizamos los contadores
            pageCount += 1;
        }
        
        memMap.put(processPointer, pointerPages);
        return processPointer;
    }
    
    public void releasePointer(int pointer){
        
        // Obtenemos las páginas asociadas al puntero
        ArrayList<Page> pagesToRemove = memMap.get(pointer);
        
        for (Page page : pagesToRemove){
            
            // Eliminamos la página de memoria real o virtual
            // como corresponda
            if (page.isInPhysicalMemory()){
                physicalMem[page.getPhysicalAddress()] = null;
            } else {
                virtualMem.remove(page);
            }
        }
        
        // Elimina el puntero del mapa de memoria
        memMap.remove(pointer);
    }
    
    public void usePointer(int pointer){
        
        // Obtenemos las páginas asociadas al puntero
        ArrayList<Page> pagesToUse = memMap.get(pointer);
        
        for (Page page : pagesToUse){
            
            // Removemos el uso de la página para el algoritmo óptimo
            if (paginationAlgorithm instanceof OPT){
                ((OPT)paginationAlgorithm).removeUsage();
            }
            
            if (page.isInPhysicalMemory()){
                
                // Hit de página
                page.setLastUsage(Instant.now());
                page.setSecondChance(true);
                clock += 1;
            } else {
                
                // Fallo de página
                clock += 5;
                thrashing += 5;
                
                int emptyAddress = getEmptyAddress();
                Page pageToLoad;
                
                if (emptyAddress != -1){
                    
                    // Si hay un espacio libre para la página, entonces
                    // la insertamos en ese lugar
                    pageToLoad = removePageFromVirtualMem(page.getPageId());
                    pageToLoad.setTimestamp(Instant.now());
                    pageToLoad.setLastUsage(Instant.now());
                    pageToLoad.setSecondChance(true);
                    pageToLoad.setInPhysicalMemory(true);
                    pageToLoad.setPhysicalAddress(emptyAddress);
                    physicalMem[emptyAddress] = pageToLoad;
                } else {
                    
                    // Si no hay espacio, entonces obtenemos la página
                    // para intercambiar
                    Page pageToRemove = paginationAlgorithm.getPageToRemove(physicalMem);
                    int addressToInsert = pageToRemove.getPhysicalAddress();
                    
                    // Enviamos la página a remover a memoria virtual
                    pageToRemove.setInPhysicalMemory(false);
                    pageToRemove.setPhysicalAddress(-1);
                    virtualMem.add(pageToRemove);
                    
                    // Cargamos la página necesitada en memoria real
                    pageToLoad = removePageFromVirtualMem(page.getPageId());
                    pageToLoad.setTimestamp(Instant.now());
                    pageToLoad.setLastUsage(Instant.now());
                    pageToLoad.setSecondChance(true);
                    pageToLoad.setInPhysicalMemory(true);
                    pageToLoad.setPhysicalAddress(addressToInsert);
                    physicalMem[addressToInsert] = pageToLoad;
                }
            }
        }
    }

    public int getPhysicalMemSize() {
        return physicalMemSize;
    }

    public Page[] getPhysicalMem() {
        return physicalMem;
    }

    public LinkedList<Page> getVirtualMem() {
        return virtualMem;
    }

    public HashMap<Integer, ArrayList<Page>> getMemMap() {
        return memMap;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public int getClock() {
        return clock;
    }

    public int getThrashing() {
        return thrashing;
    }

    public PaginationAlgorithm getPaginationAlgorithm() {
        return paginationAlgorithm;
    }
}
