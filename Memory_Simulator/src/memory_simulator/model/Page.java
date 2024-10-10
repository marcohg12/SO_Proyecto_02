package memory_simulator.model;

import java.time.Instant;

public class Page {
    
    private int pageId;
    private int physicalAddress; 
    private boolean inPhysicalMemory; //LOADED
    private Instant timestamp;
    private Instant lastUsage;
    private boolean secondChance;
    private int spaceUsed;
    private int processId;
    private int virtualAddress;
    private int pointer;
    private int loadedTime;
    
    public Page(int pageId, int physicalAddress, boolean inPhysicalMemory, int spaceUsed, int processId){
        this.pageId = pageId;
        this.physicalAddress = physicalAddress;
        this.inPhysicalMemory = inPhysicalMemory;
        timestamp = null;
        lastUsage = null;
        secondChance = true;
        this.spaceUsed = spaceUsed;
        this.processId = processId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(int physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public boolean isInPhysicalMemory() {
        return inPhysicalMemory;
    }

    public void setInPhysicalMemory(boolean inPhysicalMemory) {
        this.inPhysicalMemory = inPhysicalMemory;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getSecondChance() {
        return secondChance;
    }

    public void setSecondChance(boolean secondChance) {
        this.secondChance = secondChance;
    }

    public Instant getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Instant lastUsage) {
        this.lastUsage = lastUsage;
    }

    public int getSpaceUsed() {
        return spaceUsed;
    }

    public void setSpaceUsed(int spaceUsed) {
        this.spaceUsed = spaceUsed;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getVirtualAddress() {
        return virtualAddress;
    }

    public void setVirtualAddress(int virtualAddress) {
        this.virtualAddress = virtualAddress;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public int getLoadedTime() {
        return loadedTime;
    }

    public void setLoadedTime(int loadedTime) {
        this.loadedTime = loadedTime;
    }
     
    @Override
    public int hashCode() {
        int hash = 7;
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
        final Page other = (Page) obj;
        return this.pageId == other.pageId;
    }
    
    @Override
    public Page clone(){
        Page page = new Page(this.pageId, this.physicalAddress, this.inPhysicalMemory, this.spaceUsed, this.processId);
        page.setTimestamp(this.getTimestamp());
        page.setLastUsage(this.getLastUsage());
        page.setSecondChance(this.getSecondChance());
        return page;
    }   
}
