package net.torvald.sortdemo;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static net.torvald.sortdemo.SortDemo.WIDTH;
import static net.torvald.sortdemo.SortDemo.HEIGHT;
import static net.torvald.sortdemo.SortDemo.initial;
import static net.torvald.sortdemo.SortDemo.dataSize;
import static net.torvald.sortdemo.SortDemo.appgc;

/**
 * Created by minjaesong on 16-10-06.
 */
public class StateDemoMain extends BasicGameState {



    public StateDemoMain() throws SlickException {
    }

    @Override
    public int getID() {
        return SortDemo.STATE_MAIN;
    }

    private SorterWindow windowBubble;
    private SorterWindow windowInsertion;
    private SorterWindow windowQuick;


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        windowBubble = new SorterWindow(new SorterBubble(initial), WIDTH / 2, HEIGHT / 2);
        windowInsertion = new SorterWindow(new SorterInsertion(initial), WIDTH / 2, HEIGHT / 2);
        windowQuick = new SorterWindow(new SorterQuick(initial), WIDTH / 2, HEIGHT / 2);

        windowBubble.init();
        windowInsertion.init();
        windowQuick.init();
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);

        gc.setClearEachFrame(true);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        windowBubble.update(gc, delta);
        windowInsertion.update(gc, delta);
        windowQuick.update(gc, delta);


        String title = "Sorter Demo — FPS: " + appgc.getFPS();
        appgc.setTitle(title);
    }

    static String title = "Sorting Race";
    static String subtitle = "of " + dataSize + " " + SortDemo.modeDesc.get(SortDemo.mode);

    static Color labelBoxCol = new Color(0x77000000);
    static Color dividerCol = new Color(0x888888);
    static Color algoLabelCol = new Color(0x27d7ff);

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setAntiAlias(SortDemo.doAA);

        int MARGIN_Y = StateFancyIntro.MARGIN_Y;
        int MARGIN_X = StateFancyIntro.MARGIN_X;


        // sorters
        windowBubble.render(gc, g, 0, HEIGHT / 2);
        windowInsertion.render(gc, g, WIDTH / 2, HEIGHT / 2);
        windowQuick.render(gc, g, WIDTH / 2, 0);

        // divider
        g.setColor(dividerCol);
        g.setLineWidth(2);
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // title
        g.setColor(Color.white);

        g.drawString(title,
                (WIDTH / 2 - g.getFont().getWidth(title)) / 2,
                (HEIGHT / 4) - 14 - g.getFont().getLineHeight() / 2
        );
        g.drawString(subtitle,
                (WIDTH / 2 - g.getFont().getWidth(subtitle)) / 2,
                (HEIGHT / 4) + 14 - g.getFont().getLineHeight() / 2
        );

        // algorithm names and timers
        String quickTime = "Quick " + formatTime(windowQuick.getTimer());
        String bubbleTime = "Bubble " + formatTime(windowBubble.getTimer());
        String insertionTime = "Insertion " + formatTime(windowInsertion.getTimer());

        g.setColor(labelBoxCol);
        g.fillRect(WIDTH / 2 + MARGIN_X, MARGIN_Y, g.getFont().getWidth(quickTime), g.getFont().getLineHeight());
        g.fillRect(MARGIN_X, HEIGHT / 2 + MARGIN_Y, g.getFont().getWidth(bubbleTime), g.getFont().getLineHeight());
        g.fillRect(WIDTH / 2 + MARGIN_X, HEIGHT / 2 + MARGIN_Y, g.getFont().getWidth(insertionTime), g.getFont().getLineHeight());

        g.setColor(algoLabelCol);
        g.drawString(quickTime, WIDTH / 2 + MARGIN_X, MARGIN_Y);
        g.drawString(bubbleTime, MARGIN_X, HEIGHT / 2 + MARGIN_Y);
        g.drawString(insertionTime, WIDTH / 2 + MARGIN_X, HEIGHT / 2 + MARGIN_Y);
    }

    private String formatTime(int millisec) {
        int mins = millisec / 1000 / 60;
        int secs = (millisec / 1000) % 60;
        int mills = (millisec % 1000) / 10;

        return String.format("%02d:%02d.%02d", mins, secs, mills);
    }
}
