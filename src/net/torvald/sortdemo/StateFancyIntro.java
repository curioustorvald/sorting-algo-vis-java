package net.torvald.sortdemo;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

import static net.torvald.sortdemo.SortDemo.*;
import static net.torvald.sortdemo.StateDemoMain.*;

/**
 * Created by minjaesong on 16-10-07.
 */
public class StateFancyIntro extends BasicGameState {

    private int timer = 0;

    private ArrayList<DrawActionDef> drawQueue = new ArrayList<>();

    @Override
    public int getID() {
        return SortDemo.STATE_SPLASH;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        // draw queue setup
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                wait            (g, 0, timer, execTime), 300)
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                drawDivider     (g, 1, timer, execTime), 600)
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                layOutBars      (g, 2, timer, execTime),
                                               SortDemo.dataSize / 2 * Math.max(SortDemo.updateInterval, 1))
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                wait            (g, 3, timer, execTime), 600)
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                drawAlgoName    (g, 4, timer, execTime), 1200)
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                drawTitle       (g, 5, timer, execTime), 1200)
        );
        drawQueue.add(new DrawActionDef((Graphics g, int order, int timer, float execTime) ->
                                                drawSubtitle    (g, 6, timer, execTime), 1100)
        );
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);

        gc.setClearEachFrame(true);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        timer += delta;

        String title = "Sorter Demo — FPS: " + appgc.getFPS();
        appgc.setTitle(title);

        if (queueCnt == drawQueue.size()) {
            gc.setClearEachFrame(false); // for smooth transition
            sbg.enterState(STATE_MAIN);
        }
    }

    private int queueCnt = 0;

    private void executeQueue(Graphics g) {
        DrawActionDef qobj = drawQueue.get(queueCnt);

        if (timer <= qobj.length)
            drawQueue.get(queueCnt).action.draw(g, queueCnt, timer, qobj.length);
        else {
            // make sure each drawing is finished properly
            timer = jfloatToInt(qobj.length);
            drawQueue.get(queueCnt).action.draw(g, queueCnt, jfloatToInt(qobj.length), qobj.length);

            queueCnt++;
            timer = 0;
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setAntiAlias(SortDemo.doAA);


        if (queueCnt < drawQueue.size()) {
            executeQueue(g);
        }
    }


    static int MARGIN_Y = 4;
    static int MARGIN_X = 8;

    void drawDivider(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);


        float drawH = (timer / execTime) * WIDTH;
        float drawV = (timer / execTime) * HEIGHT;

        g.setColor(dividerCol);
        g.setLineWidth(2);
        g.drawLine(0, HEIGHT / 2, drawH, HEIGHT / 2);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, drawV);
    }

    void drawAlgoName(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);


        g.setColor(labelBoxCol);
        g.fillRect(WIDTH / 2 + MARGIN_X, MARGIN_Y, g.getFont().getWidth("Quick"), g.getFont().getLineHeight());
        g.fillRect(MARGIN_X, HEIGHT / 2 + MARGIN_Y, g.getFont().getWidth("Bubble"), g.getFont().getLineHeight());
        g.fillRect(WIDTH / 2 + MARGIN_X, HEIGHT / 2 + MARGIN_Y, g.getFont().getWidth("Insertion"), g.getFont().getLineHeight());

        g.setColor(algoLabelCol);
        g.drawString("Quick ", WIDTH / 2 + MARGIN_X, MARGIN_Y);
        g.drawString("Bubble ", MARGIN_X, HEIGHT / 2 + MARGIN_Y);
        g.drawString("Insertion ", WIDTH / 2 + MARGIN_X, HEIGHT / 2 + MARGIN_Y);
    }

    void wait(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);
    }

    void layOutBars(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);

        int bars = Math.round(dataSize / execTime * timer);



        for (int i = 0; i < bars; i++) {
            g.setColor(Color.white);

            float barHeight = HEIGHT / 2f / dataSize * (SortDemo.initial[i] + 1);

            g.fillRect(WIDTH / 2f / dataSize * i + SorterWindow.gutter + (WIDTH / 2f),
                    HEIGHT / 2f - barHeight + (HEIGHT / 2f),
                    WIDTH / 2f / dataSize - 2*SorterWindow.gutter,
                    barHeight
            );
            g.fillRect(WIDTH / 2f / dataSize * i + SorterWindow.gutter,
                    HEIGHT / 2f - barHeight + (HEIGHT / 2f),
                    WIDTH / 2f / dataSize - 2*SorterWindow.gutter,
                    barHeight
            );
            g.fillRect(WIDTH / 2f / dataSize * i + SorterWindow.gutter + (WIDTH / 2f),
                    HEIGHT / 2f - barHeight,
                    WIDTH / 2f / dataSize - 2*SorterWindow.gutter,
                    barHeight
            );
        }
    }

    void drawTitle(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);

        g.setColor(Color.white);
        g.drawString(title,
                (WIDTH / 2 - g.getFont().getWidth(title)) / 2,
                (HEIGHT / 4) - 14 - g.getFont().getLineHeight() / 2
        );
    }

    void drawSubtitle(Graphics g, int order, int timer, float execTime) {
        drawPrev(g, order);

        g.setColor(Color.white);
        g.drawString(subtitle,
                (WIDTH / 2 - g.getFont().getWidth(subtitle)) / 2,
                (HEIGHT / 4) + 14 - g.getFont().getLineHeight() / 2
        );
    }




    void drawPrev(Graphics g, int order) {
        // draw prev
        for (int i = 0; i < order; i++) {
            DrawActionDef qpast = drawQueue.get(i);
            qpast.action.draw(g, i, jfloatToInt(qpast.length), qpast.length);
        }
    }



    interface DrawAction {
        void draw(Graphics g, int order, int timer, float execTime);
    }

    private int jfloatToInt(Float f) {
        float f1 = f;
        return (int) f1;
    }

    class DrawActionDef {
        StateFancyIntro.DrawAction action;
        Float length;

        DrawActionDef(StateFancyIntro.DrawAction lhs, Integer rhs) {
            action = lhs;
            length = (float) rhs;
        }
    }
}
