package net.torvald.sortdemo;

import org.newdawn.slick.*;

/**
 * Created by minjaesong on 16-10-06.
 */
class SorterWindow {

    private float width;
    private float height;

    private Sorter sorter;

    private int updateCounter;
    private int timer = 0;

    private int dataSize;
    private int[] data;

    static float gutter;

    private final Color colourPlain = Color.white;
    private final Color colourEval = new Color(0xff4000);
    private final Color colourPivot = new Color(0x00d7ff);
    private final Color colourDone = new Color(74, 255, 0);

    SorterWindow(Sorter sorter, int w, int h) throws SlickException {
        width = w; height = h;
        this.sorter = sorter;

        data = sorter.getArrays();
        dataSize = data.length;


        float eachBarSize = width / dataSize;
        gutter = eachBarSize / 12f;
    }

    void init() throws SlickException {
    }

    void update(GameContainer gc, int delta) {
        if (!sorter.done()) {
            updateCounter += delta;
            timer += delta;

            if (updateCounter > SortDemo.updateInterval) {
                updateCounter -= SortDemo.updateInterval;
                sorter.step();
            }
        }
    }

    private Color toColor(ColorMarking marking) {
        switch (marking) {
            case DONE: return colourDone;
            case EVAL: return colourEval;
            case PIVOT: return colourPivot;
            case PLAIN: return colourPlain;
            default: throw new IllegalArgumentException("Unknown colour");
        }
    }

    void render(GameContainer gc, Graphics g, int x, int y) throws SlickException {
        for (int i = 0; i < dataSize; i++) {
            g.setColor(toColor(sorter.getMarking()[i]));

            float barHeight = height / dataSize * (data[i] + 1);

            g.fillRect(width / dataSize * i + gutter + x, height - barHeight + y,
                    width / dataSize - 2*gutter, barHeight);
        }
    }

    public int getTimer() {
        return timer;
    }
}
