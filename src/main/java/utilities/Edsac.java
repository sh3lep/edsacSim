package utilities;

import data.Tank;
import registers.Accumulator;
import registers.Multiplier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Edsac {
    private int reg = -1;
    private int stepsCount = -1;
    private boolean bySteps = false;
    private String outputFileName;

    private int size = -1;
    private ArrayList<Tank> instructionsList = new ArrayList<>(1024);

    private Accumulator acc = new Accumulator(); // id 0
    private Multiplier mult = new Multiplier(); // id 1
    private Tank orderTank; // id 2
    private int SCT;


    public void init(ArrayList<Tank> instructionsList, String outputFileName,
                     int count, boolean reg, boolean regInteger, boolean bySteps, boolean isIO1, int initialSCT) {
        this.instructionsList = instructionsList;
        Tank sizeInstruction = instructionsList.get(1);
        if (isIO1) {
            if (sizeInstruction.gerInstruction() != 5) {
                System.out.println("error: unexpected value of instruction");
                return;
            } else {
                size = sizeInstruction.getAddress();
            }
        } else {
            size = instructionsList.size();
        }
        if (reg) {
            this.reg = 0;
        } else if (regInteger) {
            this.reg = 1;
        }
        this.stepsCount = count;
        this.bySteps = bySteps;
        this.outputFileName = outputFileName;
        this.SCT = initialSCT;
    }

    public void start() {
        List<String> output = new LinkedList<>();
        Scanner sc = new Scanner(System.in);
        int steps = 0;
        while (SCT != size || (stepsCount != -1 && steps != stepsCount)) {
            orderTank = instructionsList.get(SCT);
            int address = orderTank.getAddress() - 1;
            if (address == -1) address = 0;
            int shift;

            Tank addressedTank;
            if (instructionsList.size() - 1 < address) {
                addressedTank = new Tank();
                instructionsList.set(address, addressedTank);
            } else {
                addressedTank = instructionsList.get(address);
            }
            switch (orderTank.gerInstruction()) {
                case 28: // A
                    acc.add(addressedTank);
                    break;
                case 12: // S
                    acc.sub(addressedTank);
                    break;
                case 21: // H
                    mult.copy(addressedTank);
                    break;
                case 31: // V
                    acc.add(BinaryArithmetic.multiply(acc.getCells(), mult.getCells()));
                    break;
                case 22: // N
                    acc.sub(BinaryArithmetic.multiply(acc.getCells(), mult.getCells()));
                    break;
                case 5: // T
                    instructionsList.set(address, new Tank(acc.getCells()));
                    acc.clear();
                    break;
                case 7: // U
                    instructionsList.set(address, new Tank(acc.getCells()));
                    break;
                case 30: // C
                    acc.add(BinaryArithmetic.collate(acc.getCells(), mult.getCells()));
                    break;
                case 4: // R
                    shift = 1;
                    for (int n = 1; n <= Math.pow(2,address); n++) {
                        if (Math.pow(2, n - 2) == address) {
                            shift = n;
                            break;
                        }
                    }
                    acc.rightShift(shift);
                    break;
                case 25: // L
                    shift = 1;
                    for (int n = 1; n <= Math.pow(2,address); n++) {
                        if (Math.pow(2, n - 2) == address) {
                            shift = n;
                            break;
                        }
                    }
                    acc.leftShift(shift);
                    break;
                case 3: // E
                    if (!acc.getSign()) {
                        SCT = address - 1;
                    }
                    break;
                case 27: // G
                    if (acc.getSign()) {
                        SCT = address - 1;
                    }
                    break;
                case 26: // X
                    break;
                case 6: // Y
                    break;
                case 13: // Z
                    SCT = size - 1;
                    break;
                case 0:
                    SCT++;
                    continue;
                default:
                    System.out.println("error: unexpected value of instruction");
                    return;
            }
            SCT++;
            if (reg != -1) {
                System.out.println("SCT " + SCT);
                System.out.println(getRegStatus(0));
                System.out.println(getRegStatus(1));
                System.out.println(getRegStatus(2));
            }
            if (bySteps) {
                boolean next = false;
                while (!next) {
                    switch (sc.nextLine()) {
                        case "registers":
                        case "r":
                            System.out.println("SCT " + SCT);
                            System.out.println(getRegStatus(0));
                            System.out.println(getRegStatus(1));
                            System.out.println(getRegStatus(2));
                            break;
                        case "accumulator":
                        case "acc":
                            System.out.println(getRegStatus(0));
                            break;
                        case "order":
                        case "o":
                            System.out.println(getRegStatus(2));
                            break;
                        case "multiplier":
                        case "m":
                            System.out.println(getRegStatus(1));
                            break;
                        case "sct":
                        case "s":
                            System.out.println("SCT " + SCT);
                        case "all":
                        case "a":
                            System.out.println("SCT " + SCT);
                            System.out.println(getRegStatus(0));
                            System.out.println(getRegStatus(1));
                            System.out.println(getRegStatus(2));
                            System.out.println("Tanks:");
                            for (int index = 0; index < instructionsList.size() - 1; index++) {
                                System.out.println((index + 1) + " " + getTankStatus(instructionsList.get(index)));
                            }
                            break;
                        case "next":
                        case "n":
                            next = true;
                            break;
                        default:
                            break;
                    }
                }
            }
            steps++;
        }
        output.add(getRegStatus(0));
        output.add(getRegStatus(1));
        output.add("Tanks:");
        for (int index = 0; index < instructionsList.size() - 1; index++) {
            output.add((index + 1) + " " + getTankStatus(instructionsList.get(index)));
        }
        writeInFile(output);
    }

    private void writeInFile(List<String> output) {
        Path file = Paths.get(outputFileName);
        try {
            Files.write(file, output, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    private String getTankStatus(Tank tank) {
        StringBuilder tankStatus = new StringBuilder();
        int tankStatusInteger = 0;
        int index;
        final char high = '*';
        final char low = '·';
        index = 0;
        for (boolean bit : tank.getCells()) {
            if (bit) {
                tankStatus.append(high);
                tankStatusInteger += Math.pow(2, index);
            } else {
                tankStatus.append(low);
            }
            index++;
        }
        if (reg == 1) {
            return "[" + tankStatusInteger + ']';
        } else {
            return '[' + tankStatus.reverse().toString() + ']';
        }
    }

    private String getRegStatus(int regId) {
        String regName;
        StringBuilder regStatus = new StringBuilder();
        int regStatusInteger = 0;
        final char high = '*';
        final char low = '·';
        boolean[] registerValue;
        switch (regId) {
            case 0:
                regName = "Accumulator ";
                registerValue = acc.getCells().clone();
                break;
            case 1:
                regName = "Multiplier ";
                registerValue = mult.getCells().clone();
                break;
            case 2:
                regName = "Order tank ";
                registerValue = orderTank.getCells().clone();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + regId);
        }
        int index = 0;
        if (registerValue[registerValue.length - 1] && reg == 1) {
            BinaryArithmetic.additionalCode(registerValue);
            for (boolean bit : registerValue) {
                regStatusInteger += bit ? Math.pow(2, index) : 0;
                index++;
            }
            regStatusInteger *= -1;
        } else {
            for (boolean bit : registerValue) {
                if (bit) {
                    regStatus.append(high);
                    regStatusInteger += Math.pow(2, index);
                } else {
                    regStatus.append(low);
                }
                index++;
            }
        }
        if (reg == 1) {
            return regName + ": [" + regStatusInteger + ']';

        } else {
            return regName + ": [" + regStatus.reverse().toString() + ']';
        }
    }
}