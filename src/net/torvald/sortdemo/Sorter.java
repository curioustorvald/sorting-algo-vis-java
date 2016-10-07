package net.torvald.sortdemo;

/**
 * Created by minjaesong on 16-10-06.
 */
public interface Sorter {
    void step();
    int[] getArrays();
    ColorMarking[] getMarking();
    boolean done();
}
