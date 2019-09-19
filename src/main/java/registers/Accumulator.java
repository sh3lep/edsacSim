package registers;

import data.Tank;
import utilities.BinaryArithmetic;

public class Accumulator {
    private boolean[] cells = new boolean[71];

    public void clear() {
        this.cells = new boolean[71];
    }

    public void add(Tank tank) {
        cells = BinaryArithmetic.add(cells, tank.getCells());
    }

    public void add(boolean[] cells) {
        this.cells = BinaryArithmetic.add(this.cells, cells);
    }

    public void sub(Tank tank) {
        cells = BinaryArithmetic.sub(cells, tank.getCells());
    }

    public void sub(boolean[] cells) {
        this.cells = BinaryArithmetic.add(this.cells, cells);
    }

    public void leftShift(int count) {
        BinaryArithmetic.leftShift(cells, count);
    }

    public void rightShift(int count) {
        BinaryArithmetic.rightShift(cells, count);
    }

    public boolean getSign() { return cells[16]; }

    public boolean[] getCells() {
        return cells;
    }
}