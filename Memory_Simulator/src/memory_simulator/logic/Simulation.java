package memory_simulator.logic;

import memory_simulator.model.Computer;
import memory_simulator.model.ComputerState;
import memory_simulator.model.PaginationAlgoType;

public class Simulation {
    
    private Computer computer;
    private PaginationAlgorithm paginationAlgorithm;
    
    public Simulation(PaginationAlgoType algoType){
        
        if (algoType == PaginationAlgoType.FIFO_ALGO){
            paginationAlgorithm = new FIFO();
        }
        else if (algoType == PaginationAlgoType.MRU_ALGO){
            paginationAlgorithm = new MRU();
        }
        else if (algoType == PaginationAlgoType.SC_ALGO){
            paginationAlgorithm = new SC();
        }
        else if (algoType == PaginationAlgoType.RND_ALGO){
            paginationAlgorithm = new RND();
        }
        else if (algoType == PaginationAlgoType.OPT_ALGO){
        }
        
        computer = new Computer(paginationAlgorithm);
        
    }
    
    public ComputerState getState(){
        return computer.getState();
    } 
}
