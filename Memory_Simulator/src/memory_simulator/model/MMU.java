package memory_simulator.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import memory_simulator.logic.PaginationAlgorithm;

public class MMU {
    
    private int physicalMemSize;
    private int maxPagesInPhysicalMem;
    private Page[] physicalMem;
    private ArrayList<Page> virtualMem;
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
        pointerCount = 0;
        clock = 0;
        thrashing = 0;
        maxPagesInPhysicalMem = physicalMemSize / pageSize;
        physicalMem = new Page[maxPagesInPhysicalMem];
        virtualMem = new ArrayList();
        memMap = new HashMap();
        this.paginationAlgorithm = paginationAlgorithm;
        
        // Iniciamos toda la memoria física en null (espacios libres)
        for (int i = 0; i < maxPagesInPhysicalMem; i++){
            physicalMem[i] = null;
        }
    }
    
    private Page insertPage(int processId, int spaceUsed){
        
        // Verifica si puede insertar la página en memoria física
        for (int i = 0; i < maxPagesInPhysicalMem; i++){
            
            // Si encontramos un espacio libre, insertarmos la página
            if (physicalMem[i] == null){
                
                Page page = new Page(pageCount, i, true, spaceUsed, processId);
                page.setTimestamp(Instant.now());
                page.setLastUsage(Instant.now());
                physicalMem[i] = page;
                pageCount += 1;
                clock += 1;
                
                return page;
            }
        }
        
        // Si no se pudo insertar en memoria física, entonces se inserta
        // en memoria virtual
        Page page = new Page(pageCount, -1, false, spaceUsed, processId);
        pageCount += 1;
        clock += 5;
        virtualMem.add(page);
        
        return page;
    }
    
    /**
     * Crea las páginas necesarias para un proceso
     * @param size El tamaño en KB del proceso
     * @return Un puntero al mapa de memoria. El puntero es la llave del mapa para
     * la lista de páginas creadas para el proceso
     */
    public int createPagesForProcess(int processId, int size){
        
        // Calculamos la cantidad de páginas necesarias para el proceso
        int pagesRequired = size / pageSize;
        if (size % 4 != 0){
            pagesRequired += 1;
        }
        
        ArrayList<Page> createdPages = new ArrayList();
        
        // Insertamos las páginas
        for (int i = 0; i < pagesRequired - 1; i++){
            Page page = insertPage(processId, 4);
            createdPages.add(page); 
        }
        Page page = insertPage(processId, size % 4);
        createdPages.add(page); 
        
        // Creamos el puntero e insertamos las páginas creadas asociadas 
        // al puntero en el mapa de memoria
        int pointer = pointerCount;
        pointerCount += 1;
        memMap.put(pointer, createdPages);
        return pointer;     
    }
    
    /**
     * Elimina las páginas asociadas a un puntero tanto de memoria física
     * como virtual. Si el puntero no tiene páginas asociadas, la
     * operación no tiene efecto
     * @param pointer El puntero asociado a las páginas a eliminar
     */
    public void releasePointer(int pointer){
        
        ArrayList<Page> pages = memMap.get(pointer);
        
        if (pages == null){
            return;
        }
        
        // Borramos las páginas asociadas al puntero de memoria
        for (Page page : pages){
            
            if (page.isInPhysicalMemory()){
                physicalMem[page.getPhysicalAddress()] = null;
            } else {
                virtualMem.remove(page);
            }
        }
        
        // Elimina el puntero del mapa de memoria
        memMap.remove(pointer);
    }
    
    /**
     * Usa las páginas asociadas a un puntero. Esta operación utiliza
     * el algoritmo de reemplazo definido en caso de que sea necesario
     * traer páginas de memoria virtual y no haya espacio disponible
     * @param pointer El puntero asociado a las páginas a usar
     */
    public void usePointer(int pointer){
        
        ArrayList<Page> pages = memMap.get(pointer);
        
        if (pages == null){
            return;
        }
        
        for (Page page : pages){
            paginationAlgorithm.usePage(this, page);
        }
    }

    public int getPhysicalMemSize() {
        return physicalMemSize;
    }

    public int getMaxPagesInPhysicalMem() {
        return maxPagesInPhysicalMem;
    }

    public Page[] getPhysicalMem() {
        return physicalMem;
    }

    public ArrayList<Page> getVirtualMem() {
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

    public void setThrashing(int thrashing) {
        this.thrashing = thrashing;
    }
    
    public void incrementClock(int increment){
        clock += increment;
    }
    
    public void incrementThrashing(int increment){
        thrashing += increment;
    }
    
    public void swapPages(Page in, Page out){
        physicalMem[out.getPhysicalAddress()] = in;
        virtualMem.remove(in);
        virtualMem.add(out);
        in.setInPhysicalMemory(true);
        out.setInPhysicalMemory(false);
        in.setPhysicalAddress(out.getPhysicalAddress());
        out.setPhysicalAddress(-1);
    }
    
    public int getEmptyAddress(){

        for (int i = 0; i < maxPagesInPhysicalMem; i++){
            if (physicalMem[i] == null){
                return i;
            }
        }
        return -1;
    }
    
    public void insertPageInAddress(Page page, int address){
        page.setInPhysicalMemory(true);
        page.setPhysicalAddress(address);
        physicalMem[address] = page;  
    }
}
