package memory_simulator.logic;

import java.time.Instant;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class FIFO implements PaginationAlgorithm {
    
    public FIFO(){}
   
    private Page getOldestPage(MMU mmu){
        
        Page oldest = null;
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            Page page = physicalMem[i];
            if (oldest == null && page != null){
                oldest = page;
            }
            else if (page != null && page.getTimestamp().isBefore(oldest.getTimestamp())){
                oldest = page;
            }
        }
        
        return oldest;
    }

    @Override
    public void usePage(MMU mmu, Page page) {
        
        // Primero buscamos si la página está en memoria física
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            
            // Si la página se encuentra en memoria principal lo
            // tomamos como un hit
            if (physicalMem[i].getPageId() == page.getPageId()){
                mmu.incrementClock(1);
                return;
            }
        }
        
        // Si no, la página está en memoria virtual
        // y lo tomamos como un fault
        mmu.incrementClock(5);
        mmu.incrementThrashing(5);
        
        // Verificamos si hay espacio vacío en la memoria
        int emptyAddress = mmu.getEmptyAddress();
        
        if (emptyAddress == -1){
            // Si no hay espacio, intercambiamos con la página más vieja
            // (equivalente a la primera en la cola)
            Page out = getOldestPage(mmu);
            page.setTimestamp(Instant.now());
            mmu.swapPages(page, out);
        } else {
            // Si hay un espacio vacío entonces insertamos la página en
            // esa dirección
            page.setTimestamp(Instant.now());
            mmu.insertPageInAddress(page, emptyAddress);
        }
    } 
}
