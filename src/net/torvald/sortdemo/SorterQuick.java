package net.torvald.sortdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by minjaesong on 16-10-07.
 */
public class SorterQuick implements Sorter {

    private int[] data;
    private ColorMarking[] marking;

    private boolean done = false;

    SorterQuick(int[] array) {
        // shallow copy array
        data = new int[array.length];
        marking = new ColorMarking[array.length];
        System.arraycopy(array, 0, data, 0, array.length);

        Arrays.fill(marking, ColorMarking.PLAIN);


        right = data.length - 1;
        //stack = new int[h - l + 1];
    }

    //private int[] stack;
    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> stackPivots = new Stack<>();
    private ArrayList<Integer> doneIndices = new ArrayList<>();

    private final int UNASSIGEND = -23241;

    // TODO give proper names!
    private int left = 0; // starting index
    private int right = UNASSIGEND; // ending index
    private int pivot = UNASSIGEND;

    private int x = UNASSIGEND;
    private int i = UNASSIGEND;
    private int j = UNASSIGEND;

    private final int SCOPE_GET_PARTITION = 666;
    private final int SCOPE_QSORT_ITER = 1337;

    private int currentFunctionScope = SCOPE_QSORT_ITER;

    private boolean doneQsInit = false;

    @Override
    public void step() {
        if (!done) {

            if (!doneQsInit) {
                stack.push(left);
                stack.push(right);

                doneQsInit = true;
            }

            try {
                fillDone();

                if (currentFunctionScope == SCOPE_QSORT_ITER) {
                    // pop right and left
                    right = stack.pop();
                    left = stack.pop();


                    // enter function GET_PARTITION
                    currentFunctionScope = SCOPE_GET_PARTITION;
                }

                // set pivot elements at its correct position in sorted array
                // FUNCTION GET_PARTITION
                if (currentFunctionScope == SCOPE_GET_PARTITION) {
                    // before FOR statement
                    if (j == UNASSIGEND) {
                        x = data[right];
                        i = left - 1;
                        j = left;
                    }

                    // FOR statement
                    if (j <= right - 1) {
                        if (data[j] <= x) {
                            i++;
                            swap(i, j);
                        }
                        j++;
                    }
                    else { // after FOR statement
                        swap(i + 1, right);
                        j = UNASSIGEND;

                        stackPivots.push(i + 1);
                        doneIndices.add(i + 1);
                        pivot = stackPivots.peek(); // return p

                        // terminate function
                        currentFunctionScope = SCOPE_QSORT_ITER;
                    }
                }
                // END FUNCTION GET_PARTITION



                // mark these even if GET_PARTITION is not running
                if (j != UNASSIGEND) marking[j] = ColorMarking.EVAL;
                if (i >= 0) marking[i] = ColorMarking.PIVOT;


                //marking[right] = ColorMarking.PIVOT;
                if (pivot != UNASSIGEND) marking[pivot] = ColorMarking.DONE;

                stackPivots.forEach((Integer pvt) -> {
                    marking[pvt] = ColorMarking.DONE;
                }); // mark DONE right away



                // resume QSORT_ITER
                if (currentFunctionScope == SCOPE_QSORT_ITER) {

                    // left side of the pivot
                    if (pivot + 1 < right) {
                        stack.push(pivot + 1);
                        stack.push(right);
                    }
                    else {
                        doneIndices.add(right);
                    }

                    // right side of the pivot
                    if (pivot - 1 > left) {
                        stack.push(left);
                        stack.push(pivot - 1);
                    }
                    else {
                        doneIndices.add(left);
                    }
                }
            }
            catch (EmptyStackException e) {
                done = true;
            }
        } // end !done
        else {
            fillDone();
        }
    }

    private void fillDone() {
        Arrays.fill(marking, ColorMarking.PLAIN);
        doneIndices.forEach((Integer index) -> { marking[index] = ColorMarking.DONE; });
    }

    private void swap(int index1, int index2) {
        if (index1 < 0 || index1 >= data.length)
            throw new Error("index1 out of range: " + index1);
        if (index2 < 0 || index2 >= data.length)
            throw new Error("index2 out of range: " + index2);

        int t = data[index1];
        data[index1] = data[index2];
        data[index2] = t;
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

    class IntPair {

        int from;
        int to;

        IntPair(int lhs, int rhs) {
            from = lhs;
            to = rhs;
        }
    }
}
