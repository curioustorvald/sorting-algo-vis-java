package net.torvald.sortdemo;

import java.util.Arrays;

/**
 * Created by minjaesong on 16-10-06.
 */
class SorterBubble implements Sorter {

    private int[] data;
    private ColorMarking[] marking;

    SorterBubble(int[] array) {
        // shallow copy array
        data = new int[array.length];
        marking = new ColorMarking[array.length];
        System.arraycopy(array, 0, data, 0, array.length);

        Arrays.fill(marking, ColorMarking.PLAIN);
    }

    private int i_init = 0;
    private int j_init = 0;

    private int i = i_init;
    private int j = j_init;

    private boolean done = false;

    @Override
    public void step() {
        if (i < data.length - 1 && j < data.length - (i + 1)) {

            marking[j] = ColorMarking.EVAL;
            marking[j + 1] = ColorMarking.PIVOT;

            for (int k = 0; k < j; k++) marking[k] = ColorMarking.PLAIN; // clean up the mess left behind



            if (data[j] > data[j + 1]) {
                int t = data[j + 1];
                data[j + 1] = data[j];
                data[j] = t;
            }

            j++;

            // disassembly of 'for' statement
            if (j >= data.length - (i + 1)) {
                i++;
                marking[j] = ColorMarking.DONE;
                marking[j - 1] = ColorMarking.PLAIN; // EVAL bar, go back to PLAIN!


                j = j_init;
            }
        }
        else {
            Arrays.fill(marking, ColorMarking.DONE);
            done = true;
        }
    }

    @Override
    public int[] getArrays() {
        return data;
    }

    @Override
    public ColorMarking[] getMarking() {
        return marking;
    }

    @Override
    public boolean done() {
        return done;
    }
}
