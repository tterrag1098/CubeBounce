package tterrag.gametest.element;

import tterrag.gametest.GameTest;
import tterrag.gametest.api.IElement;
import tterrag.gametest.element.ElementBall.Axis;
import tterrag.gametest.util.Box;
import tterrag.gametest.util.Pos;

public class Block implements IElement
{
    private Box bb;

    public Block(float x, float y)
    {
        bb = new Box(new Pos(x, y), new Pos(x + BlockGrid.blockWidth, y + BlockGrid.blockHeight));
    }

    @Override
    public void onCollide(IElement other)
    {
        if (other == GameTest.ball)
        {
            if (!GameTest.ball.collidedThisTick)
            {
                if ((int) other.getBB().getMin().getX() >= (int) this.getBB().getMax().getX() || (int) other.getBB().getMax().getX() <= (int) this.getBB().getMin().getX())
                {
                    GameTest.ball.flipAngle(Axis.X);
                }
                else
                {
                    GameTest.ball.flipAngle(Axis.Y);
                }
            }

            GameTest.grid.delete(this);
            GameTest.instance().addScore();
        }
    }

    @Override
    public Box getBB()
    {
        return bb;
    }
}
