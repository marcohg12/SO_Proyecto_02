package memory_simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    
    MMU(){
        physicalMemSize = 400;
        pageSize = 4;
        pageCount = 0;
        pointerCount = 0;
        clock = 0;
        maxPagesInPhysicalMem = physicalMemSize / pageSize;
        physicalMem = new Page[maxPagesInPhysicalMem];
        virtualMem = new ArrayList();
        memMap = new HashMap();
        
        // Iniciamos toda la memoria física en null (espacios libres)
        for (int i = 0; i < maxPagesInPhysicalMem; i++){
            physicalMem[i] = null;
        }
    }
    
    private Page insertPage(){
        
        // Verifica si puede insertar la página en memoria física
        for (int i = 0; i < maxPagesInPhysicalMem; i++){
            
            // Si encontramos un espacio libre, insertarmos la página
            if (physicalMem[i] == null){
                
                Page page = new Page(pageCount, i, true);
                physicalMem[i] = page;
                pageCount += 1;
                clock += 1;
                
                return page;
            }
        }
        
        // Si no se pudo insertar en memoria física, entonces se inserta
        // en memoria virtual
        Page page = new Page(pageCount, -1, false);
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
    public int createPagesForProcess(int size){
        
        // Calculamos la cantidad de páginas necesarias para el proceso
        int pagesRequired = size / pageSize;
        if (size % 4 != 0){
            pagesRequired += 1;
        }
        
        ArrayList<Page> createdPages = new ArrayList();
        
        // Insertamos las páginas
        for (int i = 0; i < pagesRequired; i++){
            Page page = insertPage();
            createdPages.add(page); 
        }
        
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
    
}
