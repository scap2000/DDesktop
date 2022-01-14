package jsolitaire.shared;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;

/** Represenation and display routines for cards <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) 
*/
public class Card 
{
/** First two bits of value indicate suit, remainder represents card rank */
    private int value; 

    private static final int N_SUITS = 4;
/** Height of card */
    public static final int HEIGHT = 100;
/** Width of card */ 
    public static final int WIDTH  = 60;

/** Number of horizontal pixels visible in partially covered cards in
 *  vertical stacks */ 
    public static final int TOP_HEIGHT = 18;
/** Number of vertical pixels visible in partially covered cards in 
 *  horizontal stacks */ 
    public static final int SIDE_WIDTH = 20;
/** Number of vertical pixels visible for partially covered face down cards in 
 *  vertical stacks */ 
    public static final int DOWN_TOP_HEIGHT = 10; 
/** Rank of ace */ 
    public static final int ACE = 0;
/** Rank of jack */
    public static final int JACK = 10;
/** Rank of queen */ 
    public static final int QUEEN = 11; 
/** Rank of king */ 
    public static final int KING = 12;
/** Circumference of round corners of card */ 
    public static int CIRC = 16; // circumference of round corners 
/** Radius of round corners of cards */ 
    public static int RADIUS = CIRC / 2; 
    
/** Color used for symbols on red cards */ 
    public static final Color redColor = new Color(240, 0, 0); 
/** Color of front of card */ 
    public static final Color frontColor = Color.white;
    public static final Color backColor = null; 
/** Background color of selected card */ 
    public static final Color selectColor = Color.gray;
/** Color of card back */
    private static final Color backCol = new Color(0, 128, 192); 
    
    private static Font font = new Font("Helvetica", Font.BOLD, 16);
    private static FontMetrics FM; 
    private static Font bigFont;
    private static FontMetrics bigFM;

    private static final int[] cardRows =
    {   0x0001000, 0x0100010, 0x0101010, 0x0200020, 0x0201020,
        0x0202020, 0x0202120, 0x0212120, 0x2021202, 0x2120212, 0, 0, 0
    };
    private static final int [][] symbolBitmaps = {
        {   0x80, 0x1c0, 0x3e0, 0x7f0, 0xff8, 0x1ffc, 0x3ffe, 0x7fff, 
            0x7fff, 0x7fff, 0x7fff, 0x3ebe, 0x1c9c, 0x1c0, 0x3e0, 0x0 },
        {   0x1c0, 0x7f0, 0xff8, 0xff8, 0xff8, 0x3e0, 0x80, 0x1ebc, 
            0x3ffe, 0x7fff, 0x7fff, 0x3ebe, 0x1c9c, 0x80, 0x1c0, 0x0, }, 
        {   0x0, 0x808, 0x3e3e, 0x7f7f, 0x7fff, 0x7fff, 0x7fff, 0x3ffe, 
            0x3ffe, 0x1ffc, 0xff8, 0x7f0, 0x3e0, 0x1c0, 0x80, 0x0 },
        {   0x80, 0x1c0, 0x3e0, 0x3e0, 0x7f0, 0xff8, 0x1ffc, 0x3ffe, 
            0x1ffc, 0xff8, 0x7f0, 0x3e0, 0x3e0, 0x1c0, 0x80, 0x0 },
    };
    private static final int SYMBOL_HEIGHT = symbolBitmaps[0].length;
    private static final int SYMBOL_WIDTH  = 15;
    
    private static final String[] numChars  = 
    { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
    
/** construct a card with the value */ 
    public Card(int value)           { this.value = value; }

/** Construct a card with the desired rank and suit */ 
    public Card(int rank, int suit)  { value = rank * N_SUITS + suit; }

/** Return rank of card */ 
    public int rank()       { return value / N_SUITS; }

/** Return suit of card */ 
    public int suit()         { return value % N_SUITS; }

/** Return true if card is black, false if card red */ 
    public boolean isBlack()  { return ((value & 2) == 0) ? true : false; }

/** Return the font used to print the text at the top of all cards */   
    public static Font getFont() { return font; }

/** Return true if obj is card whose value is the same as the card's */ 
    public boolean equals(Object obj) 
    {
        return (obj instanceof Card && ((Card)obj).value == value);
    }

/** Return true if a card has the same color as the card */
    public boolean sameColor(Card card)
    {
     // If two cards are both red or black, XORing their values should result
     //   in the high bit of the result being zero
        return ((this.value ^ card.value) & 2) == 0;
    }
    
/** Draw a card with a white background color
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    public void draw(Graphics g, int x, int y) { draw(g, x, y, frontColor); }
    
/** Draw a card with a variable background color
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color of card, if null draw card back */ 
    public void draw(Graphics g, int x, int y, Color bkgColor)
    {
        drawEmpty(g, x, y, bkgColor);

        if(bkgColor != null)
            print(g, x, y, isBlack() ? Color.black : redColor);
    }
    
/** Print the symbols and text of a card 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param c color of symbols and text */ 
    public void print(Graphics g, int x, int y, Color c)
    {
        g.setColor(c); 
     // If face card, print big letter indicating rank in center of card    
        if(rank() >= 10)
        {   drawTopChars(g, x, y);
            if(bigFont == null)
            {   bigFont = new Font("TimesRoman", Font.BOLD, 60);
                bigFM = g.getFontMetrics(bigFont);
            }
            g.setFont(bigFont);
            String ch = numChars[rank()];
            int chX = x + (WIDTH - bigFM.stringWidth(ch))/2;
            int asc = bigFM.getAscent(); 
            int chY = y + asc + (HEIGHT - asc)/2; 
            g.drawString(ch, chX, chY);
            g.setFont(font);
        }
     // Otherwise print n suit symbols where n is card rank
        else
        {   writeText(g, x, y);

            int rowI = 0, rowY = (rank() < 8) ? 20 : 25; 
            for(int cardRow = cardRows[rank()]; cardRow != 0;
                cardRow /= 16, rowY += 10, ++rowI)
            {   boolean flip = rowI > 3;
                
                if((cardRow & 3) == 1)
                    drawSymbol(g, x + WIDTH/2, y + rowY, flip);
                else if((cardRow & 3) == 2)
                {   drawSymbol(g, x + 16, y + rowY, flip);
                    drawSymbol(g, x + 44, y + rowY, flip);
                }
            }
        }
    }
/** Clear outline of card to background color */ 
    
/** Draw outline of card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */
    public static void drawOutline(Graphics g, int x, int y)
    {   
        g.setColor(Color.black);
        g.drawRoundRect(x, y, Card.WIDTH - 1, Card.HEIGHT - 1, CIRC, CIRC);
    }
/** Draw card with no symbols or text 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor color to fill empty card with (if null use card back color)*/
    public static void drawEmpty(Graphics g, int x, int y, Color bkgColor)
    {
        g.setColor(bkgColor == null ? backCol : bkgColor);
        g.fillRoundRect(x, y, Card.WIDTH - 1, Card.HEIGHT - 1, CIRC, CIRC);
        drawEdge(g, x, y);
    }

/** Draw a black line around the top and left side of a card    
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    private static void drawEdge(Graphics g, int x, int y)
    {
        drawEdge(g, x, y, 0);
    }

/** Draw a black line around the top and left side of a card    
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param corners, if 1 don't draw bottom corner, if -1 don't draw top corner
 */
    private static void drawEdge(Graphics g, int x, int y, int corners)
    {
        g.setColor(Color.black);
        if(corners != -1)
            g.drawArc(x + WIDTH - CIRC - 1, y, CIRC, CIRC, 30, 60); 
        g.drawLine(x + RADIUS, y, x + WIDTH - 1 - RADIUS, y);
        g.drawArc(x, y, CIRC, CIRC, 90, 90); 
        g.drawLine(x, y + RADIUS, x, y + HEIGHT - RADIUS - 1);
        if(corners != 1) 
            g.drawArc(x, y + HEIGHT - CIRC - 1, CIRC, CIRC, 180, 60); 
    }

/** Draw the appropriate suit symbol for a card
 *  @param g graphics context
 *  @param x horizontal coordinate of center of symbol
 *  @param y vertical coordinate of center of symbol 
 *  @param flip if set, draw symbol upside down */ 
    private void drawSymbol(Graphics g, int x, int y, boolean flip)
    {   
        int suit = suit();
        int rowI = 0, inc = 1;
        
        if(flip)
        {   rowI = SYMBOL_HEIGHT - 1;
            inc  = -1;
        }
        
        for(int rowNum = 0; rowNum < SYMBOL_HEIGHT; ++rowNum, rowI += inc)
        {   int rowBMP = symbolBitmaps[suit][rowI];
            int line_start = x - 7;
            int lineY = y - 8 + rowNum;
            while(rowBMP != 0)
            {   while((rowBMP & 1) == 0)
                {   rowBMP /= 2;
                    ++line_start;
                }
                int line_end = line_start;
                rowBMP /= 2;
                while((rowBMP & 1) != 0)
                {   rowBMP /= 2;
                    ++line_end;
                }
                g.drawLine(line_start, lineY, line_end, lineY);
                line_start = line_end + 1;
            }
        }
    }

/** Draw the top of a card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color, if null draw facedown */ 
    public void drawTop(Graphics g, int x, int y, Color bkgColor)
    {   
        g.setColor(bkgColor != null ? bkgColor : backCol);
        g.fillRoundRect(x, y, WIDTH - 1, TOP_HEIGHT + CIRC/2, CIRC, CIRC);
        g.setColor(Color.black);
        drawEdge(g, x, y, 1);
        if(bkgColor != null)
        {   if(!isBlack())
                g.setColor(redColor);
            drawTopChars(g, x, y);
        }
    }

/** Draw the left side of a card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color, if null draw facedown */ 
    public void drawSide(Graphics g, int x, int y, Color bkgColor)
    { 
        g.setColor(bkgColor != null ? bkgColor : backCol);
        g.fillRoundRect(x, y, WIDTH/2 + CIRC/2, HEIGHT - 1, CIRC, CIRC);
        drawEdge(g, x, y, -1);

        if(bkgColor != null)
        {   if(!isBlack())
                g.setColor(redColor);
            writeText(g, x, y);
            drawSymbol(g, x + 10, y + 30, false);
        }
    } 
    
/** Print the text and symbol for the top of a card. The color must be set
 *  before calling this function. 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    public void drawTopChars(Graphics g, int x, int y)
    {
        writeText(g, x, y);
        if(FM == null)
            FM = g.getFontMetrics(font);
        drawSymbol(g, FM.stringWidth(numChars[rank()]) + x + 14,
                   y + 10, false);
    }
    
/** Print the text and indicating a card's rank. The color must be set
 *  before calling this function. 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    private void writeText(Graphics g, int x, int y)
    {   
        g.drawString(numChars[rank()], 5 + x, 16 + y);
    }

/** Find the largest font whose ascent is less than or equal to pixHeight
 *  @param name font name
 *  @param style font style
 *  @param pixHeight desired pixel height */ 
    public static Font findFont(String name, int style, int pixHeight)
    {
        Label l = new Label(); // need component for getFontMetrics 
        int size = pixHeight * 2;
        Font f; 
        for( ; ; )
        {   f = new Font(name, style, size);
            if(l.getFontMetrics(f).getAscent() <= pixHeight)
                break;
            
            size -= (size > 10) ? 2 : 1;
        }
        l = null; 
        return f;
    }

/** Print textual representation of card */     
    public String toString()
    {   
        char[] suitChars   = {'s', 'c', 'h', 'd' };

        return numChars[rank()] + suitChars[suit()];
    }
}
