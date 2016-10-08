package net.torvald.sortdemo;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
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
    static int updateInterval = 100;
    static int dataSize = 50;
    private static boolean nointro = false;
    static boolean doAA = true;
    static String mode = "random";
    static HashMap<String, String> modeDesc = new HashMap<>();

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
            mode = prop.getProperty("arrangement").toLowerCase();
        }
        catch (IOException e) {
            System.err.println("WARNING - config file cannot be read; default value will be used.");
        }

        Random rnd = new Random();

        switch (mode) {
            case "random":
                initArray();
                break;
            case "reverse":
                initial = new int[dataSize];
                for (int i = 0; i < dataSize; i++)
                    initial[i] = dataSize - i - 1;
                break;
            case "sorted":
                initial = new int[dataSize];
                for (int i = 0; i < dataSize; i++)
                    initial[i] = i;
                break;
            case "almost":
                int victim = rnd.nextInt(dataSize / 3); // deliberate move

                initial = new int[dataSize];
                for (int i = 0; i < dataSize - 1; i++) {
                    initial[i] = (i < victim) ? i : i + 1;
                }

                initial[dataSize - 1] = victim;
                break;
            default:
                initArray();
        }

        // write mode description
        modeDesc.put("random", "random numbers");
        modeDesc.put("reverse", "reverse sorted numbers");
        modeDesc.put("sorted", "already sorted numbers");
        modeDesc.put("almost", "almost sorted numbers");
    }

    private void initArray() {
        Random rnd = new Random();
        // initialise array
        initial = new int[dataSize];
        for (int i = 0; i < dataSize; i++)
            initial[i] = i;
        // shuffle
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
        System.setProperty("java.library.path", "lib");
        System.setProperty("org.lwjgl.librarypath", new File("lib").getAbsolutePath());

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
