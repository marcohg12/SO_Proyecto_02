package memory_simulator.logic;

import java.util.Random;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class RND implements PaginationAlgorithm {
    
    public RND(){}
   

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
        
        // Insertamos la página en un marco aleatorio
        Random random = new Random();
        int randAddress = random.nextInt(100);
        
        if (physicalMem[randAddress] == null){
            mmu.insertPageInAddress(page, randAddress);
        } else {
            Page out = physicalMem[randAddress];
            mmu.swapPages(page, out);
        }           
    }
    
}
