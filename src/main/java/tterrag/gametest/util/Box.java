package tterrag.gametest.util;

import static org.lwjgl.opengl.GL11.*;
import static tterrag.gametest.GameTest.*;
import tterrag.gametest.GameTest;

public class Box
{
    private Pos min, max;
    
    public Box()
    {
        min = new Pos(0, 0);
        max = new Pos(GameTest.WIDTH, GameTest.HEIGHT);
    }

    public Box(Pos min, Pos max)
    {
        this.min = min.copy();
        this.max = max.copy();
        pushInBounds();
    }

    private void pushInBounds()
    {
        float transX = 0, transY = 0;
        if (this.min.getX() < screen.min.getX())
        {
            transX = screen.min.getX() - this.min.getX();
        }
        if (this.min.getY() < screen.min.getY())
        {
            transY = screen.min.getY() - this.min.getY();
        }
        if (this.max.getX() > screen.max.getX())
        {
            transX = screen.max.getX() - this.max.getX();
        }
        if (this.max.getY() > screen.max.getY())
        {
            transY = screen.max.getY() - this.max.getY();
        }

        if (transX != 0 || transY != 0)
        {
            translate(transX, transY);
        }
    }

    public float getWidth()
    {
        return max.getX() - min.getX();
    }

    public float getHeight()
    {
        return max.getY() - min.getY();
    }

    public Pos getMin()
    {
        return min.copy();
    }

    public Pos getMax()
    {
        return max.copy();
    }

    public boolean intersects(Box other)
    {
        return (this.getMax().getX() > other.getMin().getX() && this.getMax().getY() > other.getMin().getY())
            && (this.getMin().getX() < other.getMax().getX() && this.getMin().getY() < other.getMax().getY());
    }

    public void translate(float x, float y)
    {
        min.addX(x);
        max.addX(x);
        min.addY(y);
        max.addY(y);
        pushInBounds();
    }

    public void renderBox()
    {
        float width = getWidth() / 2, height = getHeight() / 2;
        glBegin(GL_QUADS);
        glVertex2f(-width, -height);
        glVertex2f(width, -height);
        glVertex2f(width, height);
        glVertex2f(-width, height);
        glEnd();
    }
    
    @Override
    public String toString()
    {
        return "Min: " + min.toString() + "  Max : " + max.toString();
    }

    public Pos getCenter()
    {
        return new Pos((min.getX() + max.getX()) / 2, (min.getY() + max.getY()) / 2);
    }
}
