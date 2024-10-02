import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;

public class Simpletron {

    private final int pagesize = 100;

    int[] memory = new int[10000]; // 10 000 memory locations
    int accumulator = 0; // responsible for all arithmetics.
    int instructionCounter = 0; // Holds the next instruction to execute
    int indexRegister = 0; // holds the current instruction.
    int instructionRegister = 0; // Current instruction
    Scanner input = new Scanner(System.in);

    public Simpletron() {
        accumulator = 0;
        instructionCounter = 0;
        indexRegister = 0;
        instructionRegister = 0;
    }

    public void execute() {
        boolean running = true;

        while (running) {
            if (instructionCounter < 0 || instructionCounter >= memory.length) {
                System.out.println("Error: Instruction counter out of bounds. Halting.");
                break;
            }

            instructionRegister = memory[instructionCounter];
            int opcode = getOpCode(instructionRegister);
            int operand = getOperand(instructionRegister);
            instructionCounter++;

            running = run(opcode, operand, null);
        }
    }

    public void executeFiles(Scanner fileInput) {
        boolean running = true;
        instructionCounter = 0; // Reset instruction counter before execution

        while (running) {

            if (instructionCounter < 0 || instructionCounter >= memory.length) {
                System.out.println("Error: Instruction counter out of bounds. Halting.");
                break;
            }

            instructionRegister = memory[instructionCounter];
            int opcode = getOpCode(instructionRegister);
            int operand = getOperand(instructionRegister);

            if (opcode == 45) {
                running = false; // Stop the loop
                HALT();
                break;
            }

            instructionCounter++; // Increment the instruction counter

            // Execute the instruction based on opcode and operand
            running = run(opcode, operand, fileInput); // Run the instruction
        }
    }

    public static int getOpCode(int instruction) {
        return Math.abs(instruction / 10000);
    }

    public static int getOperand(int instruction) {
        return instruction % 10000;
    }

    public void READ(int operand) {
        System.out.print("Enter a word: ");
        memory[operand] = input.nextInt();
    }

    public void WRITE(int operand) {
        System.out.println("Memory[" + operand + "] = " + memory[operand]);
    }

    public void LOAD(int operand) {
        accumulator = memory[operand];
    }

    public void LOADIM(int operand) {
        accumulator = operand;
    }

    public void LOADX(int operand) {
        indexRegister = memory[operand];
    }

    public void LOADIDX() {
        accumulator = memory[indexRegister];
    }

    public void STORE(int operand) {
        memory[operand] = accumulator;
    }

    public void STOREIDX() {
        memory[indexRegister] = accumulator;
    }

    public void ADD(int operand) {
        accumulator += memory[operand];
    }

    public void ADDX() {
        accumulator += memory[indexRegister];
    }

    public void SUBTRACT(int operand) {
        accumulator -= memory[operand];
    }

    public void SUBTRACTX() {
        accumulator -= memory[indexRegister];
    }

    public void DIVIDE(int operand) {
        if (memory[operand] != 0) {
            accumulator /= memory[operand];
        } else {
            System.out.println("Mathematical Error: Division by zero.");
            HALT();
        }
    }

    public void DIVIDEX() {
        if (memory[indexRegister] != 0) {
            accumulator /= memory[indexRegister];
        } else {
            System.out.println("Mathematical Error: Division by zero.");
            HALT();
        }
    }

    public void MULTIPLY(int operand) {
        accumulator *= memory[operand];
    }

    public void MULTIPLYX() {
        accumulator *= memory[indexRegister];
    }

    public void INC() {
        indexRegister++;
    }

    public void DEC() {
        indexRegister--;
    }

    public void BRANCH(int operand) {
        instructionCounter = operand;
    }

    public void BRANCHNEG(int operand) {
        if (accumulator < 0) {
            instructionCounter = operand;
        }
    }

    public void BRANCHZERO(int operand) {
        if (accumulator == 0) {
            instructionCounter = operand;
        }
    }

    public void SWAP() {
        int temp = accumulator;
        accumulator = indexRegister;
        indexRegister = temp;
    }

    public void HALT() {
        System.out.println("Simpletron halted!!!");
        dumpCore();
    }

    public boolean run(int opcode, int operand, Scanner fileInput) {
        if (opcode == 10) {
            READ(operand);
        } else if (opcode == 11) {
            WRITE(operand);
        } else if (opcode == 20) {
            LOAD(operand);
        } else if (opcode == 21) {
            LOADIM(operand);
        } else if (opcode == 22) {
            LOADX(operand);
        } else if (opcode == 23) {
            LOADIDX();
        } else if (opcode == 25) {
            STORE(operand);
        } else if (opcode == 26) {
            STOREIDX();
        } else if (opcode == 30) {
            ADD(operand);
        } else if (opcode == 31) {
            ADDX();
        } else if (opcode == 32) {
            SUBTRACT(operand);
        } else if (opcode == 33) {
            SUBTRACTX();
        } else if (opcode == 34) {
            DIVIDE(operand);
        } else if (opcode == 35) {
            DIVIDEX();
        } else if (opcode == 36) {
            MULTIPLY(operand);
        } else if (opcode == 37) {
            MULTIPLYX();
        } else if (opcode == 38) {
            INC();
        } else if (opcode == 39) {
            DEC();
        } else if (opcode == 40) {
            BRANCH(operand);
        } else if (opcode == 41) {
            BRANCHNEG(operand);
        } else if (opcode == 42) {
            BRANCHZERO(operand);
        } else if (opcode == 43) {
            SWAP();
        } else if (opcode == 45) {
            HALT();
            return false;
        } else {
            System.out.println("Invalid Opcode: " + opcode);
            return false;
        }
        return true;
    }

    public void dumpCore() {
        System.out.println("\nREGISTERS: ");
        System.out.printf("accumulator: %6d\n", accumulator);
        System.out.printf("instructionCounter: %6d\n", instructionCounter);
        System.out.printf("instructionRegister: %6d\n", instructionRegister);
        System.out.printf("indexRegister: %6d\n", indexRegister);

        System.out.println(memory[100]);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of pages to dump: "); // Ask user for the number of pages to dump
        int numPages = scanner.nextInt();


        System.out.println("\nMEMORY: ");
        for (int page = 0; page < numPages; page++) {
            int startAddress = page * pagesize;
            System.out.printf("Page %d:\n", page);

            for (int i = startAddress; i < startAddress + pagesize; i += 10) {
                for (int j = 0; j < 10; j++) {
                    System.out.printf("%06d ", memory[i + j]);
                }
                System.out.println();
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Simpletron s = new Simpletron();
        Scanner input = new Scanner(System.in);

        System.out.println("*** Welcome to Simpletron V2! ***");
        String user;

        do {
            System.out.print("*** Do you have a file that contains your SML program (Y/N)? :");
            user = input.nextLine();

            if (user.equals("y") || user.equals("Y")) {
                System.out.print("Please enter the name of the file you would like to run in SML: ");
                String filePath = input.nextLine();
            
                try {
                    Scanner inputFile = new Scanner(new File(filePath));
                    
                    // Read integers from the file into memory
                    while (inputFile.hasNextInt()) {
                        int line = inputFile.nextInt();
                        s.memory[s.instructionCounter] = line;
                        s.instructionCounter++;
                    }
            
                    // Close the file after reading
                    inputFile.close();
            
                    // Reset instruction counter for execution
                    s.instructionCounter = 0; 
                    s.executeFiles(new Scanner(new File(filePath))); // Create a new Scanner for execution
            
                } catch (FileNotFoundException e) {
                    System.out.println("Error, file not found: " + filePath);
                }
            }
            


            else if (user.equals("n") || user.equals("N")) {
                System.out.println("*** Please enter your program instruction (or data word) one at a time ***");
                System.out.println("*** Type -1 to end the input and execute your program ***");

                int instruction;
                int i = 0;

                while (true) {
                    System.out.printf("%04d ? ", i);
                    instruction = input.nextInt();

                    if (instruction == -1) {
                        break;
                    }

                    s.memory[i] = instruction;
                    i++;
                }

                s.instructionCounter = 0; // Reset for execution
                s.execute();
                s.dumpCore();
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        } while (!user.equals("n") && !user.equals("N"));
        input.close();
    }
}
