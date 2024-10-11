package memory_simulator.logic;

import java.time.Instant;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class SC implements PaginationAlgorithm {
    
    public SC(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem) {
        
        int index = 0;
        
        while(true){
            
            Page page = physicalMem[index];
            
            if (page.getSecondChance() == false){
                return page;
            } else {
                page.setSecondChance(false);
                index = (index + 1) % physicalMem.length;
            }
        }
    }
  
}
