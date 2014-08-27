package tterrag.gametest.element;

import static tterrag.gametest.GameTest.*;
import static tterrag.gametest.element.ElementBall.Axis.*;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import tterrag.gametest.GameTest;
import tterrag.gametest.api.IElement;
import tterrag.gametest.util.Box;
import tterrag.gametest.util.Pos;

public class ElementBall extends ElementBase
{
    public enum Axis
    {
        X, Y
    }

    private static final Random rand = new Random();
    int startX;
    static final int size = 10;
    static final int startY = ElementBounceBoard.startY + size;

    public float speed = 3f;
    float angle;
    float speedX, speedY;
    private float rot = 0f;
    private float rotChange = 5f;

    private boolean isDead;

    boolean collidedThisTick = false;

    public ElementBall()
    {
        this.bb = getNewBB();
        reset();
    }

    private Box getNewBB()
    {
        startX = rand.nextInt(GameTest.WIDTH);
        while (this.getBB().intersects(GameTest.bounceBoard.getBB()))
        {
            startX = rand.nextInt(GameTest.WIDTH);
        }

        return new Box(new Pos(startX - (size / 2), startY - (size / 2)), new Pos(startX + (size / 2), startY + (size / 2)));
    }

    @Override
    public void tick()
    {
        collidedThisTick = false;
        if (!isDead)
        {
            if (bb.getMax().getX() == screen.getMax().getX())
            {
                flipAngle(X);
            }

            if (bb.getMin().getX() == screen.getMin().getX())
            {
                flipAngle(X);
            }

            if (bb.getMax().getY() == screen.getMax().getY())
            {
                flipAngle(Y);
            }

            if (bb.getMin().getY() == screen.getMin().getY())
            {
                setDead();
            }

            angle %= 360;
            setSpeedFromAngle(angle);
            bb.translate(speedX, speedY);
        }
    }

    void flipAngle(Axis axis)
    {
        switch (axis)
        {
        case X:
            angle = 180 - angle;
            break;
        case Y:
            angle = 180 - (angle - 180);
            break;
        }

        rotChange = -rotChange;
    }

    @Override
    public void render()
    {
        if (!isDead)
        {
            GL11.glPushMatrix();
            Pos pos = bb.getCenter();
            GL11.glTranslatef(pos.getX(), pos.getY(), 0);
            GL11.glRotatef(rot, 0, 0, 1);
            rot += rotChange;
            GL11.glPushMatrix();
            bb.renderBox();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    public void setDead()
    {
        GameTest.instance().lives--;
        isDead = true;
    }

    public boolean isDead()
    {
        return isDead;
    }

    public void reset()
    {
        this.bb = getNewBB();
        angle = rand.nextInt(140) + 180 + 20;
        setSpeedFromAngle(angle);
        this.isDead = false;
    }

    private void setSpeedFromAngle(float angle)
    {
        this.speedX = (float) (speed * Math.cos(Math.toRadians(angle)));
        this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angle)));
    }

    private static final float maxAngleChange = 45f;

    @Override   
    public void onCollide(IElement other)
    {
        if (!collidedThisTick)
        {
            collidedThisTick = true;
            if (other == GameTest.bounceBoard)
            {
                if (this.bb.getMin().getY() > other.getBB().getMin().getY())
                {
                    float thisCenterX = ((float) (this.bb.getMax().getX() + this.bb.getMin().getX())) / 2f;
                    float boardMinX = other.getBB().getMin().getX();
                    float pointAcross = (thisCenterX - boardMinX) / other.getBB().getWidth();
                    float speedChange = (pointAcross - 0.5f) * maxAngleChange * 2;
                    flipAngle(Y);
                    angle += speedChange;
                }
                else if (this.bb.getMax().getX() > other.getBB().getMax().getX())
                {
                    flipAngle(X);
                }
                else if (this.bb.getMin().getX() < other.getBB().getMin().getX())
                {
                    flipAngle(X);
                }
            }
        }
    }
}
