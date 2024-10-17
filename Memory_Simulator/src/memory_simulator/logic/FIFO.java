package memory_simulator.logic;

import memory_simulator.model.Page;

public class FIFO implements PaginationAlgorithm {
    
    public FIFO(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem){
        
        // Retorna la página más vieja en memoria RAM.
        // La página más vieja es aquella que tenga el menor
        // valor en la marca de tiempo
        
        Page oldestPage = null;
        
        for (Page page : physicalMem) {
            
            if (page == null){
                continue;
            }
            
            if (oldestPage == null && page.isReplaceable()){
                oldestPage = page;
            }
            
            if (oldestPage != null &&
                page.isReplaceable() && page.getTimestamp().isBefore(oldestPage.getTimestamp())){
                oldestPage = page;
            }
        }
        
        return oldestPage;
    }    
}
