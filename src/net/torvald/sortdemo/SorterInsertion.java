package net.torvald.sortdemo;

import java.util.Arrays;

/**
 * Created by minjaesong on 16-10-06.
 */
public class SorterInsertion implements Sorter {

    private int[] data;
    private ColorMarking[] marking;

    SorterInsertion(int[] array) {
        // shallow copy array
        data = new int[array.length];
        marking = new ColorMarking[array.length];
        System.arraycopy(array, 0, data, 0, array.length);

        Arrays.fill(marking, ColorMarking.PLAIN);
    }

    private int i_init = 1;
    private int j_init = 0;

    private boolean endWhile = true;

    private int i = i_init;
    private int j = j_init;

    private boolean done = false;


    @Override
    public void step() {
        if (i < data.length && j < i) {

            if (endWhile) {
                j = i;
            }

            for (int k = 0; k <= i; k++) marking[k] = ColorMarking.DONE; // mark completed bars



            if (j > 0 && data[j - 1] > data[j]) {
                endWhile = false;
                int t = data[j - 1];
                data[j - 1] = data[j];
                data[j] = t;

                j--;
                marking[j] = ColorMarking.EVAL;
                if (j > 0) marking[j - 1] = ColorMarking.PIVOT;
            }
            else {
                endWhile = true;
                i++;
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
