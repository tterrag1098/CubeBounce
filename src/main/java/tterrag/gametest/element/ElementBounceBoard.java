package tterrag.gametest.element;

import org.lwjgl.input.Keyboard;

import tterrag.gametest.GameTest;
import tterrag.gametest.util.Box;
import tterrag.gametest.util.Pos;

public class ElementBounceBoard extends ElementBase
{
    static final int width = 50;
    static final int height = 6;
    static final int startX = GameTest.WIDTH / 2;
    static final int startY = 50;
    static final int moveSpeed = 4;
    
    public ElementBounceBoard()
    {
        super();
        reset();
    }
    
    @Override
    public void tick()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            this.bb.translate(-moveSpeed, 0);
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            this.bb.translate(moveSpeed, 0);
        }
    }

    public void reset()
    {
        this.bb = new Box(new Pos(startX - (width / 2), startY - (height / 2)), new Pos(startX + (width / 2), startY + (height / 2)));
    }
}
