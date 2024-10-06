package memory_simulator.logic;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

public class InstructionSetGenerator {
    private int numProcesses;
    private int numOperations;
    private Random random;  // Generador de números aleatorios

    public InstructionSetGenerator(int seed, int numProcesses, int numOperations) {
        this.numProcesses = numProcesses;
        this.numOperations = numOperations;

        // Semilla para repetir escenario
        this.random = new Random(seed);
    }

    public ArrayList<String> generateInstructionSet() {
        // Lista para almacenar las instrucciones generadas
        ArrayList<String> instructions = new ArrayList<>();
        
        // Mapa para relacionar procesos(pid) con los punteros(ptr)
        HashMap<Integer, ArrayList<Integer>> processPointers = new HashMap<>();
        
        // Lista para almacenar punteros disponibles para usar y eliminar
        ArrayList<Integer> availablePointers = new ArrayList<>();
        
        // Lista para almacenar los procesos activos
        ArrayList<Integer> activeProcesses = new ArrayList<>();
        
        // Lista para almacenar los procesos que han sido "matados" con la operación kill
        ArrayList<Integer> killedProcesses = new ArrayList<>();
        
        // Lista para almacenar punteros que han sido eliminados
        ArrayList<Integer> deletedPointers = new ArrayList<>();
        
        int operationCount = 0;
        int pointerCount = 1;

        // Generar operaciones
        while (operationCount < numOperations) {
            // Selecciona un proceso aleatorio(pid)
            int pid = random.nextInt(numProcesses) + 1;
            String instruction = "";

            // Si el proceso ha sido eliminado, no hacer nada y continuar
            if (killedProcesses.contains(pid)) {
                continue;
            }

            // iSi el proceso esta activo, generar operaciones
            if (activeProcesses.contains(pid)) {
                
                int action = random.nextInt(3); // 0: use, 1: delete, 2: kill

                // Operación "use": utiliza un puntero si es válido
                if (action == 0 && !availablePointers.isEmpty()) {
                    // Selecciona un puntero aleatorio asociado al proceso (pid)
                    int ptr = availablePointers.get(random.nextInt(availablePointers.size()));
                    
                    // Verifica si el puntero no ha sido eliminado
                    if (processPointers.containsKey(pid) && processPointers.get(pid).contains(ptr) && !deletedPointers.contains(ptr)) {
                        instruction = "use(" + ptr + ")";
                    } else {
                        continue; // Si el puntero ha sido eliminado, continua
                    }
                  
                 // Operación "delete": elimina un puntero si es válido  
                } else if (action == 1 && !availablePointers.isEmpty()) {
                    // Selecciona un puntero aleatorio asociado al proceso (pid)
                    int ptr = availablePointers.get(random.nextInt(availablePointers.size()));
                    
                    // Verifica si el puntero no ha sido eliminado
                    if (processPointers.containsKey(pid) && processPointers.get(pid).contains(ptr) && !deletedPointers.contains(ptr)) {
                        instruction = "delete(" + ptr + ")";
                        deletedPointers.add(ptr);
                        availablePointers.remove(Integer.valueOf(ptr)); // Elimina el puntero de los disponibles
                    } else {
                        continue;
                    }
                 // Operación "kill": elimina el proceso y todos sus punteros
                } else if (action == 2) {
                    // Termina los procesos con "kill"
                    instruction = "kill(" + pid + ")";
                    activeProcesses.remove(Integer.valueOf(pid));
                    killedProcesses.add(pid); // Añade el proceso a la lista de procesos "matados"

                    // Elimina todos los punteros asociados al proceso (pid)
                    ArrayList<Integer> pointersToRemove = processPointers.remove(pid);
                    if (pointersToRemove != null) {
                        availablePointers.removeAll(pointersToRemove);
                        deletedPointers.addAll(pointersToRemove);
                    }
                }
            } else {
                // Si el proceso no está activo, generar una operación "new"
                if (!killedProcesses.contains(pid)) {
                    int size = random.nextInt(1000) + 1;
                    instruction = "new(" + pid + "," + size + ")";
                    activeProcesses.add(pid);

                    // Asigna un nuevo puntero al proceso y lo añade al mapa de punteros
                    ArrayList<Integer> pointers = processPointers.getOrDefault(pid, new ArrayList<>());
                    pointers.add(pointerCount);
                    processPointers.put(pid, pointers);
                    availablePointers.add(pointerCount); // Añade el puntero a la lista de disponibles
                    pointerCount++;
                }
            }

            // Asegura de que la instrucción sea válida y se anade a la lista
            if (!instruction.isEmpty()) {
                instructions.add(instruction);
                operationCount++;
            }
        }
        return instructions;
    }

    public void saveInstructionSetToFile(String fileName, ArrayList<String> instructions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String instruction : instructions) {
                writer.write(instruction);
                writer.newLine(); // nueva línea para cada instrucción
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo el archivo: " + e.getMessage());
        }
    }
}
