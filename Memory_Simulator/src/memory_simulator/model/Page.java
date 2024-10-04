package memory_simulator.model;

public class Page {
    
    private int pageId;
    private int physicalAddress;
    private boolean inPhysicalMemory;
    private int timestamp;
    private int lastAccess;
    private int secondChance;
    
    Page(int pageId, int physicalAddress, boolean inPhysicalMemory){
        this.pageId = pageId;
        this.physicalAddress = physicalAddress;
        this.inPhysicalMemory = inPhysicalMemory;
        timestamp = 0;
        lastAccess = 0;
        secondChance = 1;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(int lastAccess) {
        this.lastAccess = lastAccess;
    }

    public int getSecondChance() {
        return secondChance;
    }

    public void setSecondChance(int secondChance) {
        this.secondChance = secondChance;
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
}
