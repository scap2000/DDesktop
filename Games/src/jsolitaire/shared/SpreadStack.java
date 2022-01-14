package jsolitaire.shared;

import java.awt.Color;
import java.awt.Graphics;

/** A SpreadStack implements a stack which is spread out so that all cards
*   all visible <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR> 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) */
public class SpreadStack extends CardStack
{
    public SpreadStack(int maxCards) 
    {   super(maxCards);    
        autoMoveSrc = true; 
    }
    public SpreadStack() { this(20); };
/** If all face up cards from a building stack are removed, the top face down
 *   card will be turned over */ 
    public void remove(int remI, int nRemove)
    {   
        if(nTurned > nCards - nRemove - 1)
            nTurned = Math.max(nCards - nRemove - 1, 0);

        super.remove(remI, nRemove);
    }

    public int width()
    {
        return (Card.WIDTH + downDX * nTurned 
                + Math.max(0, nCards - 1 - nTurned) * dX);
    }
    public int height() 
    {   
        return (Card.HEIGHT + downDY * nTurned
                + Math.max(0, nCards - 1 - nTurned) * dY);
    }

    static Color[] bkgs = { Card.frontColor, Card.selectColor, 
    Card.frontColor};

    public void paint(Graphics g)
    {
        int[] lenArr = { nCards - nTurned - nSelected - selectI, 
            nSelected, selectI };
        draw(g, 0, 0, downDX, downDY, null, nTurned, 0);
        draw(g, downDX * nTurned, downDY * nTurned, dX, dY, bkgs, lenArr); 
    } 
}
