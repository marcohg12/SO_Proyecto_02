package memory_simulator.model;

import java.util.ArrayList;

public class Process {
    private int pId;
    private ArrayList<Integer> pointers;
    
    public Process(int pId){
        this.pId = pId;
        pointers = new ArrayList();
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public ArrayList<Integer> getPointers() {
        return pointers;
    }

    public void setPointers(ArrayList<Integer> pointers) {
        this.pointers = pointers;
    }
    
    public void insertPointer(int pointer){
        pointers.add(pointer);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Process other = (Process) obj;
        return this.pId == other.pId;
    }
    
    public Process clone(){
        Process process = new Process(this.pId);
        ArrayList<Integer> newPointers = (ArrayList<Integer>) this.pointers.clone();
        process.setPointers(newPointers);
        return process;
    }
}
