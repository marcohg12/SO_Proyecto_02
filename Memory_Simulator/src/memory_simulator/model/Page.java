package memory_simulator.model;

import java.time.Instant;

public class Page {
    
    private int pageId;               // Identificador de página
    private int physicalAddress;      // Dirección de la página en memoria RAM
    private boolean inPhysicalMemory; // Flag que indica si está en memoria RAM
    private Instant timestamp;        // Marca de tiempo de cuando la página fue cargada a memoria
    private Instant lastUsage;        // Marca de tiempo de la última vez que la página fue referenciada
    private boolean secondChance;     // Marca de uso para el algoritmo de Second Chance
    private int spaceUsed;            // Espacio en KB utilizado en la página
    private int processId;            // Identificador del proceso dueño de la página
    private int virtualAddress;       // Dirección de la página en memoria virtual
    private int pointer;              // Puntero asociado a la página
    private boolean replaceable;      // Flag que indica si la página se puede reemplazar
    
    public Page(int pageId, int physicalAddress, boolean inPhysicalMemory, int spaceUsed, int processId){
        this.pageId = pageId;
        this.physicalAddress = physicalAddress;
        this.inPhysicalMemory = inPhysicalMemory;
        timestamp = null;
        lastUsage = null;
        secondChance = true;
        replaceable = true;
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

    public boolean isReplaceable() {
        return replaceable;
    }

    public void setReplaceable(boolean replaceable) {
        this.replaceable = replaceable;
    }
     
    @Override
    public int hashCode() {
        return Integer.hashCode(pageId);
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
