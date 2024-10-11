package memory_simulator.logic;

import memory_simulator.model.Page;

public class FIFO implements PaginationAlgorithm {
    
    public FIFO(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem){
        
        Page oldestPage = null;
        
        for (Page page : physicalMem) {
            
            if (page == null){
                continue;
            }
            
            if (oldestPage == null){
                oldestPage = page;
            }
            
            if (page.getTimestamp().isBefore(oldestPage.getTimestamp())){
                oldestPage = page;
            }
        }
        
        return oldestPage;
    }    
}
