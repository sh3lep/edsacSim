package utilities;

import data.Tank;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static Pair<ArrayList<Tank>, Integer> getInstructionsList(String fileName, boolean isIO1) {
        ArrayList<Tank> instructionsList = new ArrayList<>(1024);
        instructionsList.trimToSize();
        instructionsList.add(new Tank());
        if (!isIO1) {
            boolean[] subprogInstruction = new boolean[17];
            subprogInstruction[subprogInstruction.length - 5] = true;
            subprogInstruction[subprogInstruction.length - 4] = true;
            subprogInstruction[subprogInstruction.length - 3] = true;
            subprogInstruction[2] = true;
            instructionsList.add(new Tank(subprogInstruction));
        }
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("error: file " + fileName + " does not found");
            return null;
        }
        int currentMark = -1;
        while (sc.hasNext()) {
            Pair<Tank, Integer> convertResult = convertTextToInstructions(sc.nextLine(), isIO1, currentMark);
            Integer newMark = convertResult.getValue();
            if (newMark == -1) {
                Tank tank = convertResult.getKey();
                if (tank != null) instructionsList.add(tank);
            } else {
                if (newMark == -4) {
                    break;
                } else {
                    if (newMark == -2) {
                        currentMark = instructionsList.size() + 1;
                    } else {
                        if (newMark >= 0) {
                            for (int i = instructionsList.size(); i < newMark - 1; i++) {
                                instructionsList.add(new Tank());
                            }
                        }
                    }
                }
            }
        }
        sc.close();
        while (instructionsList.size() < 1025) {
            instructionsList.add(new Tank());
        }
        return new Pair<>(instructionsList, currentMark);
    }

    private static Pair<Tank, Integer> convertTextToInstructions(String line, boolean isIO1, int currentMark) {
        if (line.isEmpty()) return null;
        Tank tank = null;
        boolean[] cells = new boolean[17];
        String inputLine = line.trim().toUpperCase();
        Pattern instructionPattern;
        if (isIO1) {
            instructionPattern = Pattern.compile("[PQWERTYUIOJπSZK*.FθDφHNMΔLXGABCV@][0-9]{0,4}[SL]");
        } else {
            instructionPattern = Pattern.compile("[PQWERTYUIOJπSZK*.FθDφHNMΔLXGABCV@][0-9]{0,4}[DFKθ@]|EZPF");
        }

        Matcher matcher = instructionPattern.matcher(inputLine);
        int newMark = -1;
        if (!matcher.find()) {
            boolean comment = false;
            for (char symbol : inputLine.toCharArray()) {
                switch (symbol) {
                    case '[':
                        comment = true;
                        break;
                    case ']':
                        if (comment) {
                            comment = false;
                        } else {
                            System.out.println("error: unexpected value");
                        }
                        break;
                    default:
                        if (!comment) {
                            System.out.println("error: unexpected value");
                        }
                }
            }
        } else {
            String instruction = inputLine.substring(matcher.start(), matcher.end());
            switch (instruction.charAt(0)) {
                case 'P': // P=00000
                    break;
                case 'Q': // Q=00001
                    cells[cells.length - 5] = true;
                    break;
                case 'W': // W=00010
                    cells[cells.length - 4] = true;
                    break;
                case 'E': // E=00011
                    if (instruction.length() == 4) {
                        if (instruction.substring(0, 4).toLowerCase().equals("ezpf")) {
                            return new Pair<>(new Tank(), -4);
                        }
                    }
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    break;
                case 'R': // R=00100
                    cells[cells.length - 3] = true;
                    break;
                case 'T': // T=00101
                    cells[cells.length - 5] = true;
                    cells[cells.length - 3] = true;
                    break;
                case 'Y': // Y=00110
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    break;
                case 'U': // U=00111
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    break;
                case 'I': // I=01000
                    cells[cells.length - 2] = true;
                    break;
                case 'O': // O=01001
                    cells[cells.length - 5] = true;
                    cells[cells.length - 2] = true;
                    break;
                case 'J': // J=01010
                    cells[cells.length - 4] = true;
                    cells[cells.length - 2] = true;
                    break;
                case 'π': // π=01011
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 2] = true;
                    break;
                case 'S': // S=01100
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    break;
                case 'Z': // Z=01101
                    cells[cells.length - 5] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    break;
                case 'K': // K=01110
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    break;
                case '*': // *=01111
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    break;
                case '.': // .=10000
                    cells[cells.length - 1] = true;
                    break;
                case 'F': // F=10001
                    cells[cells.length - 5] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'θ':
                case '@': // θ=10010
                    cells[cells.length - 4] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'D': // D=10011
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'φ': // φ=10100
                    cells[cells.length - 3] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'H': // H=10101
                    cells[cells.length - 5] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'N': // N=10110
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'M': // M=10111
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'Δ': // Δ=11000
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'L': // L=11001
                    cells[cells.length - 5] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'X': // X=11010
                    cells[cells.length - 4] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'G': // G=11011
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'A': // A=11100
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'B': // B=11101
                    cells[cells.length - 5] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'C': // C=11110
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                case 'V': // V=11111
                    cells[cells.length - 5] = true;
                    cells[cells.length - 4] = true;
                    cells[cells.length - 3] = true;
                    cells[cells.length - 2] = true;
                    cells[cells.length - 1] = true;
                    break;
                default:
                    System.out.println("error: unexpected value (not in list of instructions symbols) "
                            + instruction.charAt(0));
            }
            int address = 0;
            switch (instruction.charAt(instruction.length() - 1)) {
                case 'L': // L=1
                    if (isIO1) {
                        cells[0] = true;
                    } else {
                        System.out.println("error: unexpected value (not in list of instructions symbols) "
                                + instruction.charAt(0));
                    }
                    break;
                case 'S': // S=0
                    if (!isIO1) {
                        System.out.println("error: unexpected value (not in list of instructions symbols) "
                                + instruction.charAt(0));
                    }
                    break;
                case 'D':
                    if (!isIO1) {
                        cells[0] = true;
                    } else {
                        System.out.println("error: unexpected value (not in list of instructions symbols) "
                                + instruction.charAt(0));
                    }
                    break;
                case 'F':
                    if (isIO1) {
                        System.out.println("error: unexpected value (not in list of instructions symbols) "
                                + instruction.charAt(0));
                    }
                    break;
                case 'K':
                    if (!isIO1) {
                        if (cells[cells.length - 5] &&
                                cells[cells.length - 4] &&
                                cells[cells.length - 2] &&
                                cells[cells.length - 1]) {
                            newMark = -2;
                        } else {
                            if (cells[cells.length - 5] &&
                                    cells[cells.length - 3]) {
                                newMark = -3;
                            }
                        }
                    } else {
                        System.out.println("error: unexpected K in IO1");
                    }
                    break;
                case 'θ':
                case '@':
                    if (!isIO1) {
                        if (currentMark != -1) {
                            address += currentMark;
                        } else {
                            System.out.println("error: unexpected relative call");
                        }
                    } else {
                        System.out.println("error: unexpected relative call in IO1");
                    }
                    break;
                default:
                    System.out.println("error: unexpected value (not S/L) "
                            + instruction.charAt(instruction.length() - 1));
            }
            if (instruction.length() > 2) {
                String addressString = instruction.substring(1, instruction.length() - 1);
                address += Integer.parseInt(addressString.trim());
                if (address > 2047) {
                    System.out.println("error: unexpected value(wrong address value) "
                            + addressString);
                }
            }
            if (newMark == -3) {
                newMark = address;
            } else {
                int index = 1;
                while (address > 0) {
                    cells[index++] = address % 2 != 0;
                    address /= 2;
                }
            }
            tank = new Tank(cells);
        }
        return new Pair<>(tank, newMark);
    }
}