package memory_simulator;

import java.util.ArrayList;
import memory_simulator.logic.Simulation;
import memory_simulator.model.ComputerState;
import memory_simulator.model.PaginationAlgoType;

public class Memory_Simulator {

    public static void main(String[] args) {
        
        ArrayList<String> instructions = new ArrayList();
        instructions.add("new(1,250)");
        instructions.add("new(1,50)");
        instructions.add("new(2,5320)");
        instructions.add("use(1)");
        instructions.add("use(3)");
        instructions.add("use(2)");
        instructions.add("use(1)");
        instructions.add("delete(1)");
        instructions.add("kill(1)");
        instructions.add("kill(2)");
        instructions.add("kill(3)");
        Simulation simulation = new Simulation(PaginationAlgoType.FIFO_ALGO, instructions);
        int i = 0;
        while (simulation.executeNext()){
            ComputerState state = simulation.getState();
            i += 1;
            System.out.println(i);
        }
    }
    
}
