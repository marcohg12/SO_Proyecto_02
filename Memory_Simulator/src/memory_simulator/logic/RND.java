package memory_simulator.logic;

import java.util.Random;
import memory_simulator.model.Page;

public class RND implements PaginationAlgorithm {
    
    private Random random;
    
    public RND(int seed){
        random = new Random(seed);
    }
    
    @Override
    public Page getPageToRemove(Page[] physicalMem) { 
        
        // Obtiene una página elatoria de la memoria RAM
 
        int randomIndex = random.nextInt(0, physicalMem.length);
        return physicalMem[randomIndex];
    }
      
}
