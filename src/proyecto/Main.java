package proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Instanciate the Scanner and ProcessBuilder to use it in the program
        Scanner sc = new Scanner(System.in);
        ProcessBuilder pBuilder = new ProcessBuilder();

        // Showing that the program has started correctly and describing what it does to the user
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Program started correctly");
        System.out.println("It will show the processes that are consuming more memory than the user indicates");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println();

        // Creating the variable to read the ammount of memory that the user wants to be the minimum to show the
        // processes that are using more than that memory.
        // While the user doesn't insert a number greater than zero the program won't advance to the next instructions
        int memoria;
        do {
            System.out.println("Insert which will be the minimum memory in MB the program will show");
            memoria = readInt(sc);
            if(memoria == -1) {
                System.out.println("Error: it isn't a accepted value by the program");
            }
        } while(memoria < 0);

        // The user inserted the memory in MB so I have to convert it to KB to the command to understand it
        memoria *= 1024;
        // Building the command as a String array and insert it to the ProcessBuilder
        String[] comando = {"tasklist", "/fi", "\"MEMUSAGE gt " + memoria + "\""};
        pBuilder.command(comando);

        // Asking the user if they want to see the result of the command in a filie or in the screen and showing the
        // diferent options they have to choose
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Do you want to see the result in the screen or in a file");
        System.out.println("1. Screen");
        System.out.println("2. File");
        System.out.println("----------------------------------------------------------------------------------");

        // Creating the variable to read the option the user wants.
        // While they doesn't insert an available option the program won't advance to the next instructions
        int opc;
        do {
            opc = readInt(sc);
            if(opc == -1) {
                System.out.println("Error: it isn't a valid option");
            }
        } while(opc < 1 || opc > 2);

        // If the user has chose to see the result in a file, the user has to insert the name of said file.
        // Redirecting the process output to the file
        if(opc == 2) {
            System.out.println("Insert the name of the file");
            File file = new File(sc.nextLine());
            pBuilder.redirectOutput(file);
        }

        // Creating the Process in which the program will work with if there aren't any exceptions or errors
        // If there is any error or exception the process won't start and will be null
        Process p = null;
        try {
            p = pBuilder.start();
        } catch(IOException e) {
            System.err.println("There has been some error while starting the process");
        }

        // If there hasn't been any error starting the process and the user wants to see the result in the screen
        // I'll create a BufferedReader to read the output from the process
        if(opc == 1 && p != null) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println();
            } catch(IOException ioe) {
                System.out.println("There has been some error while reading the process");
            }
        }

        // Creating a variable to show to the user if there has been some error while executing the process
        // If it's -1 or some other number distinct from 0 it indicates that there has been some error
        // It waits until the process has finished to execute the next piece of code
        int codigo = -1;
        try {
            if(p != null)
                codigo = p.waitFor();
        } catch(InterruptedException e) {
            System.out.println("There has been some error while executing the process");
        }

        // Showing to the user that the process has finished and if it has had some kind of error while its execution
        // or not
        if(codigo == 0) {
            System.out.println("El programa ha finalizado correctamente: " + codigo);
        } else {
            System.out.println("El programa ha finalizado debido a un error: " + codigo);
        }
    }

    /**
     * Method that reads a number from the Screen. If while reading it has some kind of error it'll return a -1 and a
     * message of error
     *
     * @param sc Scanner to read from
     *
     * @return read number if there hasn't been errors or <code>-1</code> if has had an error while reading
     */
    private static int readInt(Scanner sc) {
        int read;
        if(sc.hasNextInt()) {
            read = sc.nextInt();
            sc.nextLine();
        } else {
            sc.next();
            System.err.println("Error: it isn't a valid number");
            read = -1;
        }
        return read;
    }
}
