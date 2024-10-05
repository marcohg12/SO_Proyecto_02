package memory_simulator.logic;

import java.util.ArrayList;
import memory_simulator.model.Computer;
import memory_simulator.model.ComputerState;
import memory_simulator.model.Instruction;
import memory_simulator.model.InstructionType;
import memory_simulator.model.PaginationAlgoType;

public class Simulation {
    
    private Computer computer;
    private PaginationAlgorithm paginationAlgorithm;
    private ArrayList<String> instructions;
    private int index;
    
    public Simulation(PaginationAlgoType algoType, ArrayList<String> instructions){
        
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
            paginationAlgorithm = new OPT(instructions);
        }
        
        computer = new Computer(paginationAlgorithm);
        this.instructions = instructions;
        index = 0;
    }
    
    public ComputerState getState(){
        return computer.getState();
    } 
    
    public boolean executeNext(){
        
        while (index < instructions.size()){
            
            Instruction instruction = new Instruction(instructions.get(index));
            
            if (instruction.getType() == InstructionType.NEW){
                computer.executeNew(instruction.getParameter1(), instruction.getParameter2());
            } else if (instruction.getType() == InstructionType.DELETE){
                computer.executeDelete(instruction.getParameter1());
            } else if (instruction.getType() == InstructionType.KILL){
                computer.executeKill(instruction.getParameter1());
            } else if (instruction.getType() == InstructionType.USE){
                computer.executeUse(instruction.getParameter1());
            }
            
            index += 1;
            
            return true;
        }
        
        return false;
    }
}
