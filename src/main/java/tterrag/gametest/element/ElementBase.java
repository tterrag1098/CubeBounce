package tterrag.gametest.element;

import static org.lwjgl.opengl.GL11.*;
import tterrag.gametest.api.IElement;
import tterrag.gametest.api.ITicker;
import tterrag.gametest.util.Box;
import tterrag.gametest.util.Pos;

public abstract class ElementBase implements IElement, ITicker
{
    protected Box bb;
    
    public ElementBase()
    {
        bb = new Box(new Pos(0, 0), new Pos(20, 20));
    }
    
    @Override
    public void render()
    {
        Pos pos = bb.getCenter();
        glPushMatrix();
        glColor3f(1, 1, 1);
        glTranslatef(pos.getX(), pos.getY(), 0);
        bb.renderBox();
        glPopMatrix();
    }
    
    @Override
    public Box getBB()
    {
        return bb;
    }
    
    @Override
    public void onCollide(IElement other)
    {
        ;
    }
}
