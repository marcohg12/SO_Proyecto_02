package memory_simulator.logic;

import memory_simulator.model.Page;

public interface PaginationAlgorithm { 
    public Page getPageToRemove(Page[] physicalMem);
}
