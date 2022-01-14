package jsolitaire.shared;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Card stack with variable distances between cards 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2001/05/28) */

public class VarStack extends CardStack
{
/** Horizontal increments between cards */ 
    protected int [] dXs;
/** Vertical increments between cards */ 
    protected int [] dYs;

    public VarStack(int maxCards)
    {   
        super(maxCards);
        dXs = new int[maxCards];
        dYs = new int[maxCards];
    }
    public int diffX(int i) { return dXs[i]; }
    public int diffY(int i) { return dYs[i]; }
    
    public int width()
    {
        int w = Card.WIDTH;
        for(int i = 0; i < nCards() - 1 && dXs != null; ++i)
            w += diffX(i);
        return w;
    }

    public int height()
    {
        int h = Card.HEIGHT;
        for(int i = 0; i < nCards() - 1 && dYs != null; ++i)
            h += diffY(i);
        return h;
    }
/** Need to use intervals to determine card that was clicked on */ 
    public int clickedI(Point p)
    {
        for(int i = 0, x = 0, y = 0; i < nCards();
            x += diffX(i), y += diffY(i), ++i)
        {
            if(   ((x <= p.x && p.x < x + diffX(i)) || diffX(i) == 0)
               && ((y <= p.x && p.y < y + diffY(i)) || diffY(i) == 0))
                return nCards() - i - 1;
        }
        return 0; 
    }
/** Move intervals when cards shifted */
    public void shift(int srcI, int destI, int nMove)
    {
        super.shift(srcI, destI, nMove);
        int xTemp[] = new int[nMove], yTemp[] = new int[nMove];

        System.arraycopy(dXs, srcI, xTemp, 0, nMove);
        System.arraycopy(dYs, srcI, yTemp, 0, nMove);

        if(srcI > destI)
        {   System.arraycopy(dXs, destI, dXs, destI + nMove, srcI - destI);
            System.arraycopy(dYs, destI, dYs, destI + nMove, srcI - destI);
        }
        else
        {   System.arraycopy(dXs, srcI + nMove, dXs, srcI, destI - srcI);
            System.arraycopy(dYs, srcI + nMove, dYs, srcI, destI - srcI);
        }
        System.arraycopy(xTemp, 0, dXs, destI, nMove);
        System.arraycopy(yTemp, 0, dYs, destI, nMove);
    }

    public Color cardColor(int cardI)
    {
        return isSelected(cardI) ? Card.selectColor : Card.frontColor;
    }

    public void paint(Graphics g)
    {
        int x = 0, y = 0, cardI = 0;
        for( ; cardI < nCards() - 1; 
            x += diffX(cardI), y += diffY(cardI), ++cardI)
            drawCovered(g, cardI, x, y, diffX(cardI), diffY(cardI), 
                        cardColor(cardI));

     /* Draw entire top card */     
        if(nCards() != 0)
            cards[cardI].draw(g, x, y, cardColor(nCards() - 1));
    }
}
