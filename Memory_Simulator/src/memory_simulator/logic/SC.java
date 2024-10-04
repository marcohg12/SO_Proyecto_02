package memory_simulator.logic;

import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class SC implements PaginationAlgorithm {
    
    public SC(){}
   

    @Override
    public void usePage(MMU mmu, Page page) {
        
        // Primero buscamos si la página está en memoria física
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            
            // Si la página se encuentra en memoria principal lo
            // tomamos como un hit
            if (physicalMem[i].getPageId() == page.getPageId()){
                mmu.incrementClock(1);
                physicalMem[i].setSecondChance(true);
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
            // Si no hay espacio, realizamos el proceso de recorrer la
            // memoria actualizando los bits de second chance hasta encontrar
            // el primero en 0 (aquí iría la nueva página)
            
            int i = 0;
            int length = mmu.getMaxPagesInPhysicalMem();
            
            while(true){
                
                Page pageInMem = physicalMem[i];
                
                if (pageInMem.getSecondChance() == false){
                    page.setSecondChance(true);
                    mmu.swapPages(page, pageInMem);
                    break;
                }
                
                pageInMem.setSecondChance(false);   
                i = (i + 1) % length;    
            }
            
        } else {
            // Si hay un espacio vacío entonces insertamos la página en
            // esa dirección
            page.setSecondChance(true);
            mmu.insertPageInAddress(page, emptyAddress);
        }
    }
    
}
