package memory_simulator.logic;

import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class MRU implements PaginationAlgorithm {
    
    public MRU(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem) {
        
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
