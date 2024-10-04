package memory_simulator.logic;

import java.time.Instant;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class MRU implements PaginationAlgorithm {
    
    public MRU(){}
   
    private Page getMostRecentlyUsed(MMU mmu){
        
        Page mostRecentlyUsed = null;
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            Page page = physicalMem[i];
            if (mostRecentlyUsed == null && page != null){
                mostRecentlyUsed = page;
            }
            else if (page != null && page.getLastUsage().isAfter(mostRecentlyUsed.getLastUsage())){
                mostRecentlyUsed = page;
            }
        }
        
        return mostRecentlyUsed;
    }

    @Override
    public void usePage(MMU mmu, Page page) {
        
        // Primero buscamos si la página está en memoria física
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            
            // Si la página se encuentra en memoria principal lo
            // tomamos como un hit
            if (physicalMem[i].getPageId() == page.getPageId()){
                physicalMem[i].setLastUsage(Instant.now());
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
            // Si no hay espacio, intercambiamos con la página más recientemente
            // usada
            Page out = getMostRecentlyUsed(mmu);
            page.setLastUsage(Instant.now());
            mmu.swapPages(page, out);
        } else {
            // Si hay un espacio vacío entonces insertamos la página en
            // esa dirección
            page.setLastUsage(Instant.now());
            mmu.insertPageInAddress(page, emptyAddress);
        }
    }
    
}
