package jsolitaire.shared;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/** CardList is used to display a listing of the cards in the deck which
 *  have been moved to the talon and show the upcoming cards on subsequent
 *  passes through the deck. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) */

public class CardList extends Canvas
{
    private CardStack bottom, top;
    private int listWidth = 3, maxCards;
    private boolean printTop;
    private static final int BLOCK_WIDTH = 36, BLOCK_HEIGHT = 24;
    Color backColor = new Color(0, 216, 0), redHue = new Color(200, 0, 0);

    private Image imageBuf; 
    private Dimension imageSize; 
    private Graphics graphicsBuf; 
    
/** Create a CardList
 *  @param b bottom stack in listing, will be the talon
 *  @param t top stack in listing, will be the deck
 *  @param mc maximum number of cards which can appear in listing */ 
    public CardList(CardStack b, CardStack t, int mc)
    {
        bottom = b;
        top = t;
        setFont(Card.getFont());
        maxCards = mc;
        setListWidth(listWidth);
    }
/** Set if top part of listing should be printed. This part will only be 
 *  displayed after the first pass through the deck */ 
    public void setPrintTop(boolean b) { printTop = b; }

/** Set how many cards will appear in a row of the listing */ 
    public void setListWidth(int w) 
    {
        int h = (maxCards + w - 1)/ w * BLOCK_HEIGHT;
        resize(w * BLOCK_WIDTH + 8, h + 20);
        listWidth = w;
    }
    public void paint(Graphics g)
    {
        Dimension d = size(); 
        g.setColor(backColor);
        g.fillRoundRect(0, 0, d.width, d.height, 24, 24);
        int y = listStack(g, bottom, size().height - 25, true, false);
        if(printTop)
            listStack(g, top, y - BLOCK_HEIGHT - 10, false, true); 
    }
    private int listStack(Graphics g, CardStack stack, 
                          int y, boolean highlightTop, boolean backwards)
    {
        int i   = backwards ? (stack.nCards() - 1) : 0;
        int dir = backwards ? -1 : 1;
        for(int x =0, n =0; n < stack.nCards();++n, i += dir, x +=BLOCK_WIDTH)
        {   if(n % listWidth == 0 && n != 0)
            {   x = 0; 
                y -= BLOCK_HEIGHT;
            }
            if(highlightTop && n == stack.nCards - 1)
            {   g.setColor(Color.lightGray);
                g.fillRect(x + 2, y, BLOCK_WIDTH, BLOCK_HEIGHT);
            }
            Card c = stack.cardAt(i);
            g.setColor(c.isBlack() ? Color.black : redHue);
            c.drawTopChars(g, x, y);
        }
        return y; 
    }
    public final synchronized void update (Graphics g) 
    {
        Dimension d = size(); 
        if(   imageBuf == null || d.width != imageSize.width
           || d.height != imageSize.height) 
        {   imageBuf  = createImage(d.width, d.height); 
            imageSize = d; 
            graphicsBuf = imageBuf.getGraphics(); 
        } 
        graphicsBuf.clearRect(0, 0, d.width, d.height); 
        paint(graphicsBuf); 
        g.drawImage(imageBuf, 0, 0, null); 
    } 
}
