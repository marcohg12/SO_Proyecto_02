package memory_simulator.logic;

import memory_simulator.model.Page;

public class SC implements PaginationAlgorithm {
    
    public SC(){}
    
    @Override
    public Page getPageToRemove(Page[] physicalMem) {
        
        // Recorre la memoria actualizando el flag de Second Chance
        // hasta encontrar la primera página con el flag en False, la cual
        // retorna para hacer el intercambio
        
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
