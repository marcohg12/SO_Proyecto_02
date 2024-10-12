package memory_simulator.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InstructionSetGenerator {
    
    /**
     * Genera una lista aleatoria de instrucciones
     * @param seed La semilla para generar números aleatorios
     * @param numProcesses La cantidad de procesos a generar
     * @param numOperations La cantidad de operaciones total a generar
     * @return Retorna una lista de strings, donde cada string es una instrucción
     */
    public static ArrayList<String> getInstructionSet(int seed, int numProcesses, int numOperations){
        
        ArrayList<String> instructions = new ArrayList();
        int pointerCounter = 1;
        HashMap<Integer, ArrayList<Integer>> processPointers = new HashMap();
        HashMap<Integer, Integer> instructionsPerProcess = new HashMap();
        Random random = new Random(seed);
        
        for (int i = 1; i <= numProcesses; i++) {
            instructionsPerProcess.put(i, 2);
            processPointers.put(i, new ArrayList());
        }

        int totalSum = 2 * numProcesses;
        int targetSum = numOperations;
        int remaining = targetSum - totalSum;
        
        double mean = numOperations / numProcesses;
        double stddev = 30.0;
        
        while (remaining > 0){
            
            int pId = random.nextInt(1, numProcesses + 1);
            int increment;
            int iterations = 0;
            
            do {
                double gaussian = random.nextGaussian(mean, stddev);
                increment = (int) Math.round(gaussian);
                
                if (iterations == 500){
                    increment = remaining;
                    break;
                }
                
                iterations += 1;
                
            } while (increment < 0 || increment > remaining);
            
            instructionsPerProcess.replace(pId, instructionsPerProcess.get(pId) + increment);
            remaining -= increment;
        } 
        
        while (!instructionsPerProcess.isEmpty()){
            
            List<Integer> keys = new ArrayList(instructionsPerProcess.keySet());
            int pId = keys.get(random.nextInt(keys.size()));
            int remainingInstructions = instructionsPerProcess.get(pId);
            
            if (remainingInstructions == 1){
                // La última instrucción del proceso es el KILL
                String instruction = "kill(" + Integer.toString(pId) + ")";
                instructions.add(instruction);
                instructionsPerProcess.remove(pId);
                continue;
            }
            
            double option = random.nextDouble();
            
            if (option <= 0.2){
                // Genera una instrucción NEW para el proceso
                int size = random.nextInt(1, 401);
                String instruction = "new(" + Integer.toString(pId) + "," + Integer.toString(size) + ")";
                instructions.add(instruction);
                
                processPointers.get(pId).add(pointerCounter);
                pointerCounter += 1;
                
                instructionsPerProcess.replace(pId, remainingInstructions - 1);
            }
            else if (option <= 0.6 && !processPointers.get(pId).isEmpty()){
                // Genera una instrucción USE para el proceso
                ArrayList<Integer> pointers = processPointers.get(pId);
                int index = random.nextInt(pointers.size());
                int pointer = pointers.get(index);
                
                String instruction = "use(" + Integer.toString(pointer) + ")";
                instructions.add(instruction);
                
                instructionsPerProcess.replace(pId, remainingInstructions - 1);
            }
            else if (option <= 1.0 && !processPointers.get(pId).isEmpty()) {
                // Genera una instrucción DELETE para el proceso
                ArrayList<Integer> pointers = processPointers.get(pId);
                int index = random.nextInt(pointers.size());
                int pointer = pointers.get(index);
                
                String instruction = "delete(" + Integer.toString(pointer) + ")";
                instructions.add(instruction);
                
                processPointers.get(pId).remove(index);
                
                instructionsPerProcess.replace(pId, remainingInstructions - 1);
            }
        }
        
        return instructions;
    }
        
    /**
     * Lee un archivo de texto y convierte sus líneas en una lista donde
     * cada elemento es una instrucción.
     * @param filePath La ruta del archivo a leer.
     * @return Un ArrayList con las líneas del archivo.
     */
    public static ArrayList<String> readFileToArrayList(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return lines;
    }
    
    
    
    public static String writeInstructionsToFile(ArrayList<String> instructions) {
        String fileName = "instructions.txt"; 
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String instruction : instructions) {
                writer.write(instruction);
                writer.newLine();
            }
            System.out.println("Instrucciones guardadas exitosamente en el archivo: " + file.getAbsolutePath());
            return file.getAbsolutePath(); 

        } catch (IOException e) {
            System.out.println("Ocurrió un error al escribir el archivo: " + e.getMessage());
            return null; 
        }
    }
}