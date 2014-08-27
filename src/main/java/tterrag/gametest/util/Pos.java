package tterrag.gametest.util;


public class Pos
{
    private float x, y;
        
    public Pos(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public void setX(float newX)
    {
        x = newX;
    }
    
    public void setY(float newY)
    {
        y = newY;
    }
    
    public void addX(float add)
    {
        setX(x + add);
    }
    
    public void addY(float add)
    {
        setY(y + add);
    }
    
    public Pos copy()
    {
        return new Pos(x, y);
    }
    
    @Override
    public String toString()
    {
        return String.format("(%s, %s)", x, y);
    }
}
