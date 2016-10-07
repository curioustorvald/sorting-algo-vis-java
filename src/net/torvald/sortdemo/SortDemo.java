package net.torvald.sortdemo;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by minjaesong on 16-10-06.
 */
public class SortDemo extends StateBasedGame {

    public static AppGameContainer appgc;

    public static final int STATE_SPLASH = 0;
    public static final int STATE_MAIN = 1;


    // if config file cannot be loaded, these values will be used.
    static int WIDTH = 960;
    static int HEIGHT = 720;
    static int updateInterval = 80;
    static int dataSize = 50;
    private static boolean nointro = false;
    static boolean doAA = true;

    static int[] initial;


    public SortDemo() throws SlickException {
        super("Sorter Demo");


        // read config file
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("./config.txt"));

            dataSize = new Integer(prop.getProperty("dataSize"));
            WIDTH = new Integer(prop.getProperty("width"));
            HEIGHT = new Integer(prop.getProperty("height"));
            updateInterval = new Integer(prop.getProperty("interval"));
            nointro = new Boolean(prop.getProperty("nointro"));
            doAA = new Boolean(prop.getProperty("smoothGraphics"));
        }
        catch (IOException e) {
            System.err.println("WARNING - config file cannot be read; default value will be used.");
        }

        // initialise array
        initial = new int[dataSize];
        for (int i = 0; i < dataSize; i++)
            initial[i] = i;
        // shuffle
        Random rnd = new Random();
        for (int i = dataSize - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = initial[index];
            initial[index] = initial[i];
            initial[i] = a;
        }
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        if (!nointro)
            addState(new StateFancyIntro());
        addState(new StateDemoMain());
    }

    public static void main(String[] args) {
        try {
            appgc = new AppGameContainer(new SortDemo());
            appgc.setShowFPS(false);
            appgc.setAlwaysRender(true);
            appgc.setUpdateOnlyWhenVisible(false);
            appgc.setDisplayMode(WIDTH, HEIGHT, false);

            // to make demo look bit more pretty :)
            if (doAA) appgc.setMultiSample(4);
            appgc.setVSync(updateInterval > (1000f / 60f));

            appgc.start();
        }
        catch (SlickException ex) {
            Logger.getLogger(StateDemoMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
