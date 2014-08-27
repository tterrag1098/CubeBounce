package tterrag.gametest;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import tterrag.gametest.api.IElement;
import tterrag.gametest.api.ITicker;
import tterrag.gametest.element.BlockGrid;
import tterrag.gametest.element.ElementBall;
import tterrag.gametest.element.ElementBounceBoard;
import tterrag.gametest.util.Box;

public class GameTest implements Runnable
{
    private static final GameTest INSTANCE = new GameTest();

    public static GameTest instance()
    {
        return INSTANCE;
    }

    private GameTest()
    {
    }

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    public static final Box screen = new Box();

    private List<ITicker> tickers = new ArrayList<ITicker>();
    private List<IElement> elements = new ArrayList<IElement>();

    public static final ElementBounceBoard bounceBoard = new ElementBounceBoard();
    public static final ElementBall ball = new ElementBall();
    public static final BlockGrid grid = new BlockGrid();
    
    private static final int maxLives = 3;
    private static final int scorePerBlock = 1;
    
    public static final int startingRows = 1;
    
    public int score = 0;
    public int lives = maxLives;
    public int level = 0;

    @Override
    public void run()
    {
        setupDisplay();
        setupGL();
        setupElements();

        while (!Display.isCloseRequested())
        {
            Display.sync(Keyboard.isKeyDown(Keyboard.KEY_F1) ? 120: 60); // double-time if F1 is pressed
            tick();
            render();
            Display.update();
        }
    }

    private void setupDisplay()
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
        }
        catch (Exception e)
        {
            System.out.println("Error creating display.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void setupGL()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    private void setupElements()
    {
        addTicker(bounceBoard);
        addElement(bounceBoard);
        
        addTicker(ball);
        addElement(ball);
        
        addTicker(grid);
    }

    private void tick()
    {        
        // check collisions
        for (IElement ele : elements)
        {
            for (IElement other : elements)
            {
                if (ele != other && ele.getBB().intersects(other.getBB()))
                {
                    ele.onCollide(other);
                }
            }
        }
        
        // tick elements
        for (ITicker ticker : tickers)
        {
            ticker.tick();
        }
        
        // parse keyboard events
        while (Keyboard.next())
        {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_SPACE && ball.isDead())
            {
                if (lives <= 0)
                {
                    restart();
                }
                ball.reset();
            }
        }
        
        // update text
        Display.setTitle("Level: " + (level + 1) + "  Score: " + score + "  Lives: " + lives);
    }
    
    private void restart()
    {
        lives = maxLives;
        score = 0;
        grid.reset();
        ball.reset();
        bounceBoard.reset();
    }

    private void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for (ITicker ele : tickers)
        {
            ele.render();
        }
    }

    public void addTicker(ITicker ticker)
    {
        tickers.add(ticker);
    }

    public void addElement(IElement ele)
    {
        elements.add(ele);
    }
    
    public void addScore()
    {
        score += scorePerBlock;        
    }
    
    public void nextLevel()
    {
        level++;
        lives++;
        ball.speed += 0.1f;
        grid.reset();
        ball.reset();
        bounceBoard.reset();
    }

    // Main Class stuff

    public static void main(String[] args)
    {
        instance().run();
    }
}