package memory_simulator.logic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import memory_simulator.model.Instruction;
import memory_simulator.model.InstructionType;
import memory_simulator.model.MMU;
import memory_simulator.model.Page;

public class OPT implements PaginationAlgorithm {
    
    private ArrayList<Integer> pageUsages;
    
    public OPT(ArrayList<String> instructions){
        
        HashMap<Integer, ArrayList<Integer>> memMap = new HashMap();
        pageUsages = new ArrayList();
        int pageCount = 0;
        int pointerCount = 1;
        
        // Ejecuta un análisis estático de las instrucciones de la sesión
        // para generar una lista con los usos de cada página para toda
        // la sesión
        for (String instructionString : instructions){
            
            Instruction instruction = new Instruction(instructionString);
            
            if (instruction.getType() == InstructionType.NEW){
                
                ArrayList<Integer> pagesIds = new ArrayList();
                
                // Calculamos las páginas que crea el new
                int size = instruction.getParameter2();
                int pagesToCreate = size / 4;
                if (size % 4 != 0){
                    pagesToCreate += 1;
                }
                
                // Creamos las páginas
                for (int i = 0; i < pagesToCreate; i++){
                    pagesIds.add(pageCount);
                    pageUsages.add(pageCount);
                    pageCount += 1;
                }
                
                // Registramos el puntero con las respectivas páginas
                memMap.put(pointerCount, pagesIds);
                pointerCount += 1;
            }
            else if (instruction.getType() == InstructionType.USE){
                
                ArrayList<Integer> pagesIdsToBeUsed = memMap.get(instruction.getParameter1());
                
                for (int i = 0; i < pagesIdsToBeUsed.size(); i++){
                    pageUsages.add(pagesIdsToBeUsed.get(i));
                }
            }
        }
    }
    
    public void removeUsage(){
        pageUsages.removeFirst();
    }

    @Override
    public Page getPageToRemove(Page[] physicalMem) {
        
        // Obtiene la página que será usada más tarde en el futuro
        // Si existe una página en memoria RAM que nunca se vuelve a usar,
        // entonces escoge esa página
        
        int latestIndex = -1;
        Page latestPage = null;
        
        for (Page page : physicalMem){
            
            if (page == null){
                continue;
            }
            
            int j;
            for (j = 0; j < pageUsages.size(); j++){
                
                if (page.getPageId() == pageUsages.get(j)){
                    
                    if (j > latestIndex){
                        latestIndex = j;
                        latestPage = page;
                    }
                    break;
                }
            }
            
            if (j == pageUsages.size()){
                latestPage = page;
                break;
            }
        }
        
        return latestPage;
    }   
}
