package memory_simulator.model;

public class Instruction {
    
    private InstructionType type;
    private int parameter1;
    private int parameter2;
    
    public Instruction(String instruction){
        
        String trimmedInstruction = instruction.trim();
        
        String[] parts = trimmedInstruction.split("\\(");
        
        if (parts[0].equals("kill")){
            type = InstructionType.KILL;
        } else if (parts[0].equals("new")){
            type = InstructionType.NEW;
        } else if (parts[0].equals("use")){
            type = InstructionType.USE;
        } else if (parts[0].equals("delete")){
            type = InstructionType.DELETE;
        }
        
        String parameters = parts[1].substring(0, parts[1].length() - 1);
        String[] parameterParts = parameters.split(",");
        parameter1 = Integer.parseInt(parameterParts[0]);
        
        if (parameterParts.length == 2){
            parameter2 = Integer.parseInt(parameterParts[1]);
        }  
    }

    public InstructionType getType() {
        return type;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public int getParameter1() {
        return parameter1;
    }

    public void setParameter1(int parameter1) {
        this.parameter1 = parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }

    public void setParameter2(int parameter2) {
        this.parameter2 = parameter2;
    }   
}
