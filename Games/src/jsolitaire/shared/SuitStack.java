package jsolitaire.shared;

import java.awt.Color;
import java.awt.Graphics;

/** A Suit Stack will accept cards of a single suit in ascending rank <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR> 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) */
public class SuitStack extends CardStack
{
    static private Color emptyBg = new Color (0, 175, 0);
    static private Color emptyFg = new Color (176, 176, 176);
    private Card base;
    private boolean removal;

/** Create a Suit Stack whose initial card must be base
 *  @param base the required initial card */ 
    public SuitStack(Card c)
    { 
        super(13); 
        setOrder(1, CardStack.SAME_SUIT);
        setBase(c);
        this.removal = false; 
    }
/** Set if removing cards from the suit stack is allowed
 *  @param r if true, removal will be allowed, if false no removal */ 
    public void allowRemoval(boolean r) { this.removal = r; }

/** Change the base of the stack
 *  @param c the new base card */ 
    public void setBase(Card c) 
    { 
        base = c; 
        if(c != null)
            setTopRank(c.rank() != Card.ACE ? c.rank() - 1 : Card.KING);
    }
    
    public boolean canRemove() 
    {
        return removal ? super.canRemove() : false; 
    }

/** A card can be added to an empty stack only if it is the same as the
 *   base card */ 
    public int canStart(CardStack src) 
    { 
        return src.chosen().equals(base) ? 1 : 0; 
    }
    
    public void paint(Graphics g)  
    { 
        if(nCards != 0)
            super.paint(g);
     // If stack empty, print grayed desired base card 
        else
        {   g.setColor(emptyBg);
            g.fillRoundRect(0, 0, Card.WIDTH - 1, Card.HEIGHT - 1, 
                            Card.CIRC, Card.CIRC);
            Card.drawOutline(g, 0, 0);
            if(base != null)
                base.print(g, 0, 0, emptyFg); 
        }
    }
}
