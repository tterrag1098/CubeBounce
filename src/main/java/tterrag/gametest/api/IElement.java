package tterrag.gametest.api;

import tterrag.gametest.util.Box;

public interface IElement
{
    public void onCollide(IElement other);
    
    public Box getBB();
}
