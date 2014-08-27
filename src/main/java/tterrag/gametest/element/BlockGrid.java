package tterrag.gametest.element;

import static org.lwjgl.opengl.GL11.*;
import tterrag.gametest.GameTest;
import tterrag.gametest.api.IElement;
import tterrag.gametest.api.ITicker;
import tterrag.gametest.util.Pos;
import tterrag.gametest.util.Utils;

public class BlockGrid implements ITicker
{
    static final int blockWidth = 29;
    static final int blockHeight = 6;
    static final float Xpadding = 4;
    static final float Ypadding = 10;
    private final int maxBlocksX = (int) (GameTest.WIDTH / (blockWidth + Xpadding));

    private int rows = GameTest.instance().level + GameTest.startingRows;

    private IElement[][] blocks = new IElement[rows][maxBlocksX];

    private static final int[] colors = { 0xFF0000, 0xFF8800, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0055FF, 0x6600FF, 0x990099 };
    
    public BlockGrid()
    {
        reset();
    }

    public void reset()
    {
        rows = GameTest.instance().level + GameTest.startingRows;
        blocks = new IElement[rows][maxBlocksX];
        float y = GameTest.HEIGHT - 2;
        for (int r = 0; r < blocks.length; r++)
        {
            IElement[] row = blocks[r];
            float x = 0;
            for (int c = 0; c < row.length; c++)
            {
                row[c] = new Block(x + Xpadding, y - Ypadding);
                x += blockWidth + Xpadding;
            }
            y -= blockHeight + Ypadding;
        }
    }

    @Override
    public void tick()
    {
        for (IElement[] arr : blocks)
        {
            for (IElement block : arr)
            {
                if (block != null && GameTest.ball.getBB().intersects(block.getBB()))
                {
                    block.onCollide(GameTest.ball);
                }
            }
        }
    }

    @Override
    public void render()
    {
        for (int r = 0; r < blocks.length; r++)
        {
            IElement[] row = blocks[r];
            for (int c = 0; c < row.length; c++)
            {
                IElement block = row[c];
                if (block != null)
                {
                    Pos center = block.getBB().getCenter();
                    glPushMatrix();
                    glTranslatef(center.getX(), center.getY(), 0);
                    Utils.setGLColorFromInt(colors[r]);
                    block.getBB().renderBox();
                    glPopMatrix();
                }
            }
        }
    }

    public void delete(Block block)
    {
        boolean anyLeft = false;
        for (int r = 0; r < blocks.length; r++)
        {
            IElement[] row = blocks[r];
            for (int c = 0; c < row.length; c++)
            {
                if (row[c] == block)
                {
                    row[c] = null;
                }
                if (row[c] != null)
                {
                    anyLeft = true;
                }
            }
        }
        
        if (!anyLeft)
        {
            GameTest.instance().nextLevel();
        }
    }
}
