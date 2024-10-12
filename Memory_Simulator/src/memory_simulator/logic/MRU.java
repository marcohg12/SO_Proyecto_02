package memory_simulator.logic;

import memory_simulator.model.Page;

public class MRU implements PaginationAlgorithm {
    
    public MRU(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem) {
        
        // Obtiene la página que fue usada más recientemente en memoria RAM
        // Esto se calcula a partir de la marca de tiempo de uso
        
        Page mostRecentlyUsedPage = null;
        
        for (Page page : physicalMem){
            
            if (page == null){
                continue;
            }
            
            if (mostRecentlyUsedPage == null){
                mostRecentlyUsedPage = page;
            }
            
            if (page.getLastUsage().isAfter(mostRecentlyUsedPage.getLastUsage())){
                mostRecentlyUsedPage = page;
            }
        }
        
        return mostRecentlyUsedPage;
    }      
}