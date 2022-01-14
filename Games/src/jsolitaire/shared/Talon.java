package jsolitaire.shared;

import java.awt.Color;
import java.awt.Graphics;

/** A Talon is a stack which receives cards from the deck in batches. Only
 *  the most recent batch is visible, and only the topmost card is accessible.
 *  In this stack, nTurned is the number of visible cards instead of the
 *  number of cards face down <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR> 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) */

public class Talon extends CardStack
{
    private int nDraw;

/*# @param nDraw number of cards in batch */ 
    public Talon(int nDraw)
    { 
        super(52); 
        this.nDraw = nDraw; 
        repaintAll = true; 
    }
    public int canAccept(CardStack src) { return 0; }

/** Modify batch size */  
    public void setNDraw(int n) 
    {
        nDraw = n; 
        fixSize(); 
    }
    
 // Remove cards from stack, have at least one card turned up regardless
 //  of how many removed unless deck is empty   
    public void remove(int remI, int nRemove)
    { 
        nCards -= nRemove;
        nTurned = Math.max((nCards > 0) ? 1 : 0, nTurned - nRemove);
    }

 // If adding multiple cards, add in reverse order
    public void add(Card [] newCards, int offset, int nAdd)
    {
        for(int i = offset + nAdd - 1; i >= offset; --i)
            cards[nCards++] = newCards[i];
        nTurned = Math.min(nDraw, nAdd);
    }
    public void add(Card [] cards, int nAdd) { add(cards, 0, nAdd); }
    public int maxWidth() 
    {   
        return Card.WIDTH + Math.max(2, nDraw - 1) * Card.SIDE_WIDTH;
    }
    public int width() 
    {   
        return Card.WIDTH + Card.SIDE_WIDTH * Math.max(0, nTurned - 1); 
    }
    static Color bkgs[] = { Card.frontColor, Card.selectColor };
    public void paint(Graphics g)  
    { 
        int[] nBkgs = { nTurned - nSelected, nSelected };
        draw(g, 0, 0, Card.SIDE_WIDTH, 0, bkgs, nBkgs);
    }
}
