
import data.Tank;
import javafx.util.Pair;
import utilities.Edsac;
import utilities.Parser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    static final String startLogo = "" +
            "  ______ _____   _____         _____          _           \n" +
            " |  ____|  __ \\ / ____|  /\\   / ____|        (_)          \n" +
            " | |__  | |  | | (___   /  \\ | |   ______ ___ _ _ __ ___  \n" +
            " |  __| | |  | |\\___ \\ / /\\ \\| |  |______/ __| | '_ ` _ \\ \n" +
            " | |____| |__| |____) / ____ \\ |____     \\__ \\ | | | | | |\n" +
            " |______|_____/|_____/_/    \\_\\_____|    |___/_|_| |_| |_|\n" +
            "                                                          \n\n";
    static final String help = "" +
            "$ filename [COMMAND [params]]\n\n" +
            "filename - name of file with program\n" +
            "enter quit to exit the program\n" +
            "COMMANDS:\n" +
            "\t-c, --count: steps count\n" +
            "\t-h, --help: help info\n" +
            "\t-o, --output: set output file\n" +
            "\t\tThe default name of the output file is output + filename\n" +
            "\t\texample: filename=MyProg.txt outputFilename=outputMyProg.txt\n" +
            "\t-r, --registers: print registers state on each step\n" +
            "\t\texample: order tank:[················*] ·=0 *=1\n" +
            "\t-i, --integers: same as -r but registers as integers\n" +
            "\t-s, --steps: run program by steps (stop on each instruction) with registers state\n" +
            "\t-io2 --initialorders2: run program with initial orders 2";

    public static void main(String[] args) {
        System.out.println(startLogo);
        System.out.println(help);
        Edsac edsac = new Edsac();
        Scanner sc = new Scanner(System.in);
        String input;
        while (true) {
            input = sc.nextLine();
            if (input.equals("quit"))
                return;
            String[] commands = input.trim().split(" ");
            String fileName = commands[0];
            if (!Files.isReadable(Paths.get(fileName))) {
                System.out.println(fileName + " does not exist");
                continue;
            }
            String outputFileName = null;
            int count = -1;
            boolean reg = false;
            boolean regInteger = false;
            boolean bySteps = false;
            boolean isIO1 = true;
            for (int index = 1; index < commands.length; index++) {
                switch (commands[index]) {
                    case "-c":
                    case "--count":
                        count = Integer.getInteger(commands[++index]);
                        break;
                    case "-h":
                    case "--help":
                        if (index == 2) {
                            System.out.println(help);
                        } else {
                            System.out.println("error: input error");
                        }
                        index = commands.length;
                        break;
                    case "-o":
                    case "--output":
                        outputFileName = commands[++index];
                        break;
                    case "-r":
                    case "--registers":
                        if (regInteger) {
                            System.out.println("error: you can not use -r with -i");
                            index = commands.length;
                        } else {
                            reg = true;
                        }
                        break;
                    case "-i":
                    case "--integers":
                        if (reg) {
                            System.out.println("error: you can not use -r with -i");
                            index = commands.length;
                        } else {
                            regInteger = true;
                        }
                        break;
                    case "-s":
                    case "--steps":
                        bySteps = true;
                        break;
                    case "-io2":
                    case "--initialorders2":
                        isIO1 = false;
                        break;
                }
            }
            if (outputFileName == null) outputFileName = "output" + fileName;
            Pair<ArrayList<Tank>, Integer> parsingResult = Parser.getInstructionsList(fileName, isIO1);
            ArrayList<Tank> instructionsList = parsingResult.getKey();
            int initialSCT;
            if (isIO1) {
                initialSCT = 2;
            } else {
                initialSCT = parsingResult.getValue() - 1;
            }
            if (instructionsList == null) {
                continue;
            }
            edsac.init(instructionsList,
                    outputFileName,
                    count, reg, regInteger, bySteps, isIO1, initialSCT);
            edsac.start();
            System.out.println("successful");
        }
    }
}