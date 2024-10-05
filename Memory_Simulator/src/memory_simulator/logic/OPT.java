package memory_simulator.logic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import memory_simulator.model.Instruction;
import memory_simulator.model.InstructionType;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class OPT implements PaginationAlgorithm {
    
    ArrayList<Integer> pointerUsages;
    
    public OPT(ArrayList<String> instructions){
        pointerUsages = new ArrayList();
        
        for (String instructionString : instructions){
            Instruction instruction = new Instruction(instructionString);
            if (instruction.getType() == InstructionType.USE){
                pointerUsages.add(instruction.getParameter1());
            }
        }
    }
    
    public void removeUsage(){
        pointerUsages.removeFirst();
    }
    
    private ArrayList<Integer> getPageUsages(MMU mmu){
        
        HashMap<Integer, ArrayList<Page>> memMap = mmu.getMemMap();
        ArrayList<Integer> pageUsages = new ArrayList();
        
        for (Integer pointer : pointerUsages){
            ArrayList<Page> pages = memMap.get(pointer);
            for (Page page : pages){
                pageUsages.add(page.getPageId());
            }
        }
        
        return pageUsages;
    }
    
    private int getPageIdWithLatestUsage(MMU mmu){
        
        Page[] physicalMem = mmu.getPhysicalMem();
        ArrayList<Integer> pagesInMemIds = new ArrayList();
        ArrayList<Integer> pageUsages = getPageUsages(mmu);
        
        for (Page page : physicalMem){
            if (page != null){
                pagesInMemIds.add(page.getPageId());
            }
        }
        
        // Si no, buscamos la que se usa más lejos en el futuro
        int farthest = -1;
        int farthestPageId = -1;
        
        for (int i = 0; i < pagesInMemIds.size(); i++){
            
            int j;
            for (j = 0; j < pageUsages.size(); j++){
                if (pagesInMemIds.get(i) == pageUsages.get(i)){
                    if (j > farthest){
                        farthest = j;
                        farthestPageId = pagesInMemIds.get(i);
                    }
                    break;
                }
            }
            
            // Si la página en memoria nunca se encontró en usos futuros
            // entonces se escoge como reemplazo
            if (j == pageUsages.size()){
                farthestPageId = pagesInMemIds.get(i);
                break;
            }
        }
        
        return farthestPageId;    
    }
    
    @Override
    public void usePage(MMU mmu, Page page){

        // Primero buscamos si la página está en memoria física
        Page[] physicalMem = mmu.getPhysicalMem();
        
        for (int i = 0; i < mmu.getMaxPagesInPhysicalMem(); i++){
            
            // Si la página se encuentra en memoria principal lo
            // tomamos como un hit
            if (physicalMem[i].getPageId() == page.getPageId()){
                physicalMem[i].setLastUsage(Instant.now());
                mmu.incrementClock(1);
                return;
            }
        }
        
        // Si no, la página está en memoria virtual
        // y lo tomamos como un fault
        mmu.incrementClock(5);
        mmu.incrementThrashing(5);
        
        // Verificamos si hay espacio vacío en la memoria
        int emptyAddress = mmu.getEmptyAddress();
        
        if (emptyAddress == -1){
            // Si no hay espacio, intercambiamos con la página que será
            // usada más tarde en el futuro
            int pageOutId = getPageIdWithLatestUsage(mmu);
            
            for (Page pageInMem : physicalMem){
                if (pageInMem != null && pageInMem.getPageId() == pageOutId){
                    mmu.swapPages(page, pageInMem);
                    return;
                }
            }
            
        } else {
            // Si hay un espacio vacío entonces insertamos la página en
            // esa dirección
            mmu.insertPageInAddress(page, emptyAddress);
        }    
    }
    
}
