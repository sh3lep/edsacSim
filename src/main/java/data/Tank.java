package data;

public class Tank {
    private boolean[] cells = new boolean[17];

    public Tank() {
    }

    public Tank(boolean[] cells) {
        System.arraycopy(cells, 0, this.cells, 0, 17);
    }

    public int getAddress() {
        int address = 0;
        for (int index = 0; index < cells.length - 6; index++) {
            address += cells[index + 1] ? Math.pow(2, index) : 0;
        }
        return address;
    }

    public int gerInstruction() {
        int instruction = 0;
        int power = 0;
        for (int index = cells.length - 5; index < cells.length; index++) {
            instruction += cells[index] ? Math.pow(2, power) : 0;
            power++;
        }
        return instruction;
    }

    public boolean[] getCells() {
        return cells;
    }
}