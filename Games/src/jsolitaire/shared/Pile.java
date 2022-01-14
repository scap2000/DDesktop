package jsolitaire.shared;

import java.awt.Color;
import java.awt.Graphics;

/** Pile is a card stack of which only the top card (if turned up)
 *   will be visible. Lines will be drawn on the side of the pile to 
 *   provide an indication of the stack height. If nTurned is 0, the pile
 *   top card will be face down, otherwise it will be face up <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/03/15)
*/

public class Pile extends CardStack
{
/** Number of cards per line on the side */  
    private int CARDS_LINE = 5;

    public Pile(int maxCards) 
    {
        super(maxCards); 
        setdXdY(3, 2);
    }
    public Pile(int maxCards, int cardsLine)
    {
        this(maxCards);
        CARDS_LINE = cardsLine;
    }

 // If adding multiple cards, add in reverse order
    public void add(Card [] newCards, int offset, int nAdd)
    {
        for(int i = offset + nAdd - 1; i >= offset; --i)
            cards[nCards++] = newCards[i];
    }
    public int canAccept(CardStack src) { return 0; }
    public boolean canRemove()    { return false; }
    public int width()
    {
        return Card.WIDTH + (nCards / CARDS_LINE) * dX;
    }
    public int maxWidth()
    {
        return Card.WIDTH + (cards.length / CARDS_LINE) * dX;
    }
    public int height()
    {
        return Card.HEIGHT + (nCards / CARDS_LINE) * dY;
    }
    public int maxHeight()
    {
        return Card.HEIGHT + (cards.length / CARDS_LINE) * dY;
    }
    public void paint(Graphics g)
    {
        Color topColor = null;
        if(nTurned != 0)
            topColor = nSelected != 0 ? Card.selectColor : Card.frontColor;

        Color[] bkgs = { Color.white, topColor };
        int nBkgs[] = {nCards / CARDS_LINE, Math.min(1, nCards) };
        draw(g, 0, 0, dX, dY, bkgs, nBkgs);
    }
}
