package memory_simulator.logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InstructionSetGenerator {
    // Número de procesos
    private int numProcesses;  
    
    // Número de operaciones a generar
    private int numOperations; 
    
    // Generador de números aleatorios
    private Random random;  
    
    // Procesos activos 
    private Set<Integer> activeProcesses;  
    
    // Procesos que se pueden matar
    private Set<Integer> processesReadyToKill;  
    
    // Mapa de procesos a punteros
    private Map<Integer, Set<Integer>> processToPointerMap;  
    
    // Punteros activos
    private Set<Integer> activePointers;  
    
    // Número de instrucciones "kill" 
    private int remainingKillInstructions; 
    
    // Contador de punteros
    private int pointerCount;  
    
    // Nombre del archivo de salida
    private static final String OUTPUT_FILE_NAME = "instructions.txt"; 

    // Recibe semilla, numero de procesos y numero de operaciones
    public InstructionSetGenerator(int seed, int numProcesses, int numOperations) {
        this.numProcesses = numProcesses;
        this.numOperations = numOperations;
        this.random = new Random(seed);

        // Inicializar las estructuras de datos
        this.activeProcesses = new HashSet<>();
        this.processesReadyToKill = new HashSet<>();
        this.processToPointerMap = new HashMap<>();
        this.activePointers = new HashSet<>();

        // Inicializar los procesos activos
        for (int i = 1; i <= numProcesses; i++) {
            activeProcesses.add(i);
            processToPointerMap.put(i, new HashSet<>());
        }

        // Definir cuántas instrucciones "kill" son permitidas 
        this.remainingKillInstructions = (int) (numProcesses * 0.4);

        this.pointerCount = 0;  
        
    }

   
    public ArrayList<String> generateInstructions() {
        ArrayList<String> instructions = new ArrayList<>();
        int operationCounter = 0;

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {

            while (operationCounter < numOperations) {
                // Seleccionamos una instrucción aleatoria, pero controlando cada operación
                if (!activeProcesses.isEmpty() && random.nextInt(3) == 0) {
                    // Operación "new": Crea un nuevo proceso si hay procesos activos disponibles
                    int processId = getRandomElement(activeProcesses);
                    int size = random.nextInt(20001) + 5000;  // Tamaño aleatorio entre 5,000 y 25,000
                    String instruction = "new(" + processId + "," + size + ")";
                    instructions.add(instruction);
                    fileWriter.write(instruction + "\n");

                    // Incrementar el contador de punteros y agregarlo a los punteros activos
                    pointerCount++;
                    activePointers.add(pointerCount);
                    processToPointerMap.get(processId).add(pointerCount);

                    // Añadir el proceso a la lista de procesos que pueden ser "matados"
                    processesReadyToKill.add(processId);
                    operationCounter++;
                }

                if (!activePointers.isEmpty() && random.nextInt(2) == 0) {
                    // Operación "use": Utiliza un puntero activo
                    int pointer = getRandomElement(activePointers);
                    String instruction = "use(" + pointer + ")";
                    instructions.add(instruction);
                    fileWriter.write(instruction + "\n");
                    operationCounter++;
                }

                if (!activePointers.isEmpty() && random.nextInt(2) == 1) {
                    // Operación "delete": Elimina un puntero activo
                    int pointer = getRandomElement(activePointers);
                    activePointers.remove(pointer);  // Eliminar el puntero de la lista de activos

                    // Encontrar el proceso al que pertenece el puntero
                    processToPointerMap.values().forEach(pointers -> pointers.remove(pointer));

                    String instruction = "delete(" + pointer + ")";
                    instructions.add(instruction);
                    fileWriter.write(instruction + "\n");
                    operationCounter++;
                }

                if (!processesReadyToKill.isEmpty() && remainingKillInstructions > 0 && random.nextInt(3) == 1) {
                    // Operación "kill": Elimina un proceso si es permitido
                    remainingKillInstructions--;
                    int processId = getRandomElement(processesReadyToKill);

                    // Eliminar todos los punteros asociados a este proceso
                    Set<Integer> pointersToRemove = processToPointerMap.get(processId);
                    pointersToRemove.forEach(activePointers::remove);  
                    
                    // Elimina el proceso de la tabla de símbolos
                    processToPointerMap.remove(processId);  
                    
                    // Elimina el proceso de los procesos activos
                    activeProcesses.remove(processId);  
                    
                    // Remover de la lista de procesos que pueden ser matados
                    processesReadyToKill.remove(processId);  

                    String instruction = "kill(" + processId + ")";
                    instructions.add(instruction);
                    fileWriter.write(instruction + "\n");
                    operationCounter++;
                }
            }

            // Matar los procesos que aún no se han matado
            while (!processesReadyToKill.isEmpty()) {
                int processId = processesReadyToKill.iterator().next();
                processesReadyToKill.remove(processId);

                String instruction = "kill(" + processId + ")";
                instructions.add(instruction);
                fileWriter.write(instruction + "\n");
            }

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }

        return instructions;
    }

    // Obtiene un elemento aleatorio de la colección.
    private <T> T getRandomElement(Set<T> set) {
        int randomIndex = random.nextInt(set.size());
        return new ArrayList<>(set).get(randomIndex);
    }
    
    /**
     * Lee un archivo de texto y convierte sus líneas en un ArrayList<String>.
     *
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
}