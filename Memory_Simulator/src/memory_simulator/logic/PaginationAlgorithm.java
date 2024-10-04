package memory_simulator.logic;

import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public interface PaginationAlgorithm {
    
    public void usePage(MMU mmu, Page page);
}
