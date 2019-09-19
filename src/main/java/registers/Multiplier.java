package registers;

import data.Tank;
import utilities.BinaryArithmetic;

public class Multiplier {
    private boolean[] cells = new boolean[35];

    public Tank multiply(Tank tank) {
        return new Tank(BinaryArithmetic.multiply(cells, tank.getCells()));
    }

    public void copy(Tank tank) {
        cells = new boolean[35];
        boolean[] tankCells = tank.getCells();
        System.arraycopy(tankCells, 0, cells, 0, tankCells.length);
    }

    public boolean[] getCells() {
        return cells;
    }
}