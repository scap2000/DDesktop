package jsolitaire.shared;

import java.applet.Applet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/** Base class for all card stacks <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) 
*/
public class CardStack extends Canvas
{
/** Array of cards in stack */  
    protected Card[] cards;
/** Number of cards in stack */ 
    protected int nCards;
/** Number of selected cards in stack */ 
    protected int nSelected;
/** Number of face down cards in stack */
    protected int nTurned;

/** Difference in rank between upper & lower ordered cards in stack */
    protected int rankDiff = 12;
/** The ordering of suits in the stack */
    public static final int ANY_SUIT = 0, DIFF_SUIT = 1;
    public static final int ALT_COLOR = 2, SAME_SUIT = 3, SAME_COLOR = 4;
    public static final int NO_ACCEPT = -1;
    protected int suitOrder = ALT_COLOR;
/** The rank at the top of the stack on which no card can be placed */ 
    protected int topRank = Card.ACE;
    
/** # of cards between upper selected card and top of stack, (usually 0) */
    protected int selectI;
/** Horizontal change in position between cards */
    protected int dX;
/** Vertical change in position between cards */ 
    protected int dY;
/** Horizontal change in position between face down cards */ 
    protected int downDX;
/** Vertical change in position between face down cards */ 
    protected int downDY;
/** Desired change in horizontal position between cards */ 
    protected int wantedDX;
/** Desired change in vertical position between cards */ 
    protected int wantedDY;

    protected boolean repaintAll = false;
/** If set, cards can be automatically removed from set */ 
    protected boolean autoMoveSrc = false;
/** If set, cards below the top card will be examined to see if they can be
  * moved to another stack */
    protected boolean lookBelow = false;
/** If set, clicking twice on a stack will select all ordered cards */
    protected boolean doubleClickSelect = false;
    
/** Selection protocol for stack */ 
    protected int selection = SELECT_TOP; 
/** If selection SELECT_TOP, select only top card when clicked */ 
    public static final int SELECT_TOP = 1;
/** If selection SELECT_RANGE, select from top card to clicked card */
    public static final int SELECT_RANGE = 2;
/** If selection SELECT_CLICKED, select only clicked card */ 
    public static final int SELECT_CLICKED = 3; 
        
/** Image buffer used to draw card */
    protected Image imageBuf; 
/** Size of image buffer */
    protected Dimension imageSize; 
/** Graphics context for image buffer */ 
    protected Graphics graphicsBuf; 
     
/** Initialize stack that can contain a maximum of maxCards */ 
    public CardStack(int maxCards) 
    {   
        nCards    = nSelected = 0; 
        cards     = new Card[maxCards];
    }
/** Remove nRemove cards from top of stack */ 
    public void remove(int nRemove) { nCards -= nRemove; }

/** Remove nRemove cards beginning at remI */
    public void remove(int remI, int nRemove)
    {
        System.arraycopy(cards, remI + nRemove,
                         cards, remI, nCards - remI - nRemove);
        nCards -= nRemove;
    }
    
/** Return true if card can be removed from top of stack */ 
    public boolean canRemove()     { return !isEmpty(); }
/** Return reference to card at top of stack */ 
    public Card top()              { return cards[nCards - 1]; }
/** Return reference to uppermost selected card */ 
    public Card chosen()           { return cards[nCards - 1 - selectI]; }
    
/** Return reference to card at specified index */ 
    public Card cardAt(int i)      { return cards[i]; }
/** Return reference to ith card from top */ 
    public Card cardFromTop(int i) { return cards[nCards - i - 1]; }
/** Remove all cards from stack */ 
    public void clear()            { nCards = 0; }
/** Return true if stack contains no cards */ 
    public boolean isEmpty()       { return nCards == 0; }
/** Return number of cards in stack */ 
    public int nCards()            { return nCards; }
/** Return leftmost x coordinate of card */ 
    public int leftX()              { return 0; }
/** Return topmost y coordinate of card */ 
    public int topY()             { return 0;   }
/** Return height of cards in stack */ 
    public int height()            { return Card.HEIGHT; }
/** Return width of cards in stack */ 
    public int width()             { return Card.WIDTH; }
/** Return number of selected cards */ 
    public int nSelected()         { return nSelected;  }
/** Return number of face down cards */ 
    public int getNTurned()        { return nTurned; }
/** Set number of face down cards */ 
    public void setNTurned(int n)  { nTurned = n; }
/** Return # of cards between upper selected card and top of stack */
    public int getSelectedI()     { return selectI; }
/** Return the selection protocol */ 
    public int getSelection()     { return selection; }
    
/** Set the ordering of the stack */
    public void setOrder(int diff, int order)
    { 
        if(diff < 0)
            diff += 13; 
        rankDiff  = diff;
        suitOrder = order;
    }
    public void setSelection(int sel) { selection = sel; }

/** Set the top rank of the stack */ 
    public void setTopRank(int r) { topRank = r; }
    
/** Return true if card can be automatically removed from stack */ 
    public boolean isAutoMoveSrc() { return autoMoveSrc; }
/** Set ability to automatically remove card from stack */ 
    public void setAutoMove(boolean b) { autoMoveSrc = b; }

/** Add a card to the top of the stack 
 *   @param card card to be added */ 
    public void add(Card card)     
    { 
        cards[nCards++] = card; 
    }

/** Add cards from array to top of stack 
 *   @param card array
 *   @offset starting index of card to add from array 
 *   @nAdd number of cards to add */ 
    public void add(Card [] src, int srcI, int nAdd)
    {
        System.arraycopy(src, srcI, cards, nCards, nAdd);
        nCards += nAdd;
    }

/** Add cards converted from integer values to top of stack
 *   @param cardValues array of integer values to convert to Card class
 *   @param offset starting index of integer to convert in array
 *   @nAdd number of cards to add */ 
    public void add(int [] cardValues, int offset, int nAdd)
    {   
        for(int i = offset; i < offset + nAdd; ++i)
            cards[nCards++] = new Card(cardValues[i]);
    }
/** Put the card c at the index insertI, shifting the cards at & after 
 *  insertI */ 
    public void insert(Card c, int insertI)
    {
        System.arraycopy(cards, insertI,
                         cards, insertI + 1, nCards - insertI);
        cards[insertI] = c;
        ++nCards;
    }

/** Insert cards from another stack
  *  @param src the source array of cards 
  *  @param srcI index of first card to insert 
  *  @param insI index in this stack at which insertion begins
  *  @param nIns number of cards to insert */
    public void insert(Card[] src, int srcI, int insI,  int nIns)
    {
        System.arraycopy(cards, insI, cards, insI + nIns, nCards - insI);
        System.arraycopy(src, srcI, cards, insI, nIns);
        nCards += nIns; 
    }
/** Given a click location, return the index to insert the new card.
 *  Default behavior will insert cards at top regardless of click location*/
    public int insertI(Point p) { return 0; }
    
/** Exchange cards at two positions in stack 
  * @param i1 first index to swap
  * @param i2 second index to swap */
    public void swap(int i1, int i2)
    {
        Card temp = cards[i1];
        cards[i1] = cards[i2];
        cards[i2] = temp;
    }
/** Shift cards in stack 
 *  @param srcI starting index of cards to shift
 *  @param destI new index of cards to shift 
 *  @param nMove number of cards to shift */
    public void shift(int srcI, int destI, int nMove)
    {
        Card[] temp = new Card[nMove];
        System.arraycopy(cards, srcI, temp, 0, nMove);
        if(srcI > destI)
            System.arraycopy(cards, destI, cards, destI + nMove, srcI - destI);
        else
            System.arraycopy(cards, srcI + nMove, cards, srcI, destI - srcI);
        System.arraycopy(temp, 0, cards, destI, nMove);
    }
    
/** Set the difference in position between cards in the stack 
 *   @param dx change in horizontal position
 *   @param dy change in vertical position */ 
    public void setdXdY(int dx, int dy) { setdXdY(dx, dy, dx, dy); }

/** Set the difference in position between cards in the stack 
 *   @param dx change in horizontal position
 *   @param dy change in vertical position 
 *   @param downdx change in horizontal position for face down cards 
 *   @param downdx change in vertical position for face down cards */ 
    public void setdXdY(int dx, int dy, int downdx, int downdy)
    {
        dX = wantedDX = dx;
        dY = wantedDY = dy;
        downDX = downdx;
        downDY = downdy; 
    }
/** Return the maximum height of the card stack */ 
    public int maxHeight()  { return Card.HEIGHT + (cards.length - 1) * dY; }
/** Return the maximum width of the card stack */
    public int maxWidth()   { return Card.WIDTH + (cards.length - 1) * dX; }
/** Set the size of the canvas to the maximum area occupied by the stack */
    public void fixSize()   { setSize(maxWidth(), maxHeight()); }
    
/** See if stack can accept cards from other stack
 *  @param src source of card to be moved to stack
 *  @return number of cards stack can accept from src */ 
    public int canAccept(CardStack src) 
    {
        if(src.canRemove())
        {   if(this.isEmpty())
                return canStart(src);
            else if(this.top().rank() != topRank)
                return canAdd(src);
        }
        return 0; 
    }
/** Default is to ignore location of click in destination and add 
  * cards to top of stack. Override this if location matters */
    public int canAccept(CardStack src, Point clickLoc)
    {
        return canAccept(src);
    }

/** Return the # of card(s) accepted from src when the stack is empty.
 *  Default will be to accept any card unless stack does not accept anything*/
    public int canStart(CardStack src) 
    { 
        return suitOrder == NO_ACCEPT ? 0 : 1; 
    }
    
/** Return the # of card(s) accepted from src when the stack is full */ 
    public int canAdd(CardStack src) 
    {
        if(orderedPair(this.top(), src.chosen()))
            return 1;

     // If stack clicked twice on & doubleclicking set to select all
     //   ordered cards, select them    
        else if(src == this)
        {   if(doubleClickSelect && nSelected < nOrdered() && nSelected > 0)
            {   select( nOrdered() );
                return -1;
            }
        }
     // If multiple cards can be removed from source stack, look at
     //   lowest ordered card, the move up until a match found  
        else if(src.lookBelow)
        {   int nOrd = src.nOrdered();
            int startI = src.nCards() - nOrd;
            for(int srcI = startI; srcI < src.nCards(); ++srcI)
            {   if(orderedPair(top(), src.cardAt(srcI)))
                    return src.nCards() - srcI;
            }
        }
        return 0; 
    }

/** See if two cards meet the ordering criteria for the stack. Default ordering
 *   will be descending rank with alternating suit colors. 
 *  @param below card at the bottom of the sequence
 *  @param above card at the top of the sequence
 *  @param baseRank if the lower card has the rank baseRank, the pair is 
 *   not ordered 
 *  @return true if pair is ordered */
    public boolean orderedPair(Card below, Card above)
    {
        int suitXOR = below.suit() ^ above.suit(); 
        if(   (suitOrder == SAME_SUIT && suitXOR != 0)
           || (suitOrder == ALT_COLOR && suitXOR < 2)
           || (suitOrder == DIFF_SUIT && suitXOR == 0)
           || (suitOrder == SAME_COLOR && suitXOR >= 2)
           || (suitOrder == NO_ACCEPT))
            return false;

        else
            return (   ((above.rank() - below.rank() + 13) % 13 == rankDiff)
                    && ((below.rank() != topRank)));
    }

/** Return the number of ordered cards in a building stack. If atTop is
  * true, ordered cards must occur in sequence from top. */
    public int nOrdered(boolean atTop)
    {
        int nOrdered = 1;
        if(nCards < 2)
            return nCards;

        for(int stackI = nCards - 2; stackI >= nTurned; --stackI)
        {   if(orderedPair(cards[stackI], cards[stackI + 1]))
                ++nOrdered;
            else if(atTop)
                break;
        }
        return nOrdered; 
    }
/** Default is to count ordered cards from top of stack */
    public int nOrdered() { return nOrdered(true); }

/** Return how many cards are ordered with the given suit order */
    public int nOrdered(int order)
    {
        int orig = suitOrder;
        suitOrder = order;
        int n = nOrdered();
//      System.out.println(suitOrder);
        suitOrder = orig; 
        return n; 
    }
        
    
/** Return the first card in the ordered sequence at the stack top */   
    public Card firstOrdered()
    {   
        return (nCards == 0) ? null : cards[nCards - nOrdered()];
    }

/** Reduce the distance between stacks if needed for the stack to fit in the
 *   applet panel 
 *  @param maxX applet panel width
 *  @param maxY applet panel height */ 
    public boolean compress(int maxX, int maxY)
    {
        boolean needRepaint = false;
        for( ; ; )
        {   Rectangle r = cardBounds();
            if(r.width > maxWidth() || r.x + r.width >= maxX && dX > 5)
            {   --dX;
                needRepaint = true;
            }
            else if(r.height > maxHeight() || r.y + r.height >= maxY && dY > 5)
            {   --dY;
                needRepaint = true;
            }
            else
                break;
        }
        if(needRepaint)
            repaint();
        return needRepaint; 
    }

/** Increase the distance between cards possible if the distance has been 
 *   decreased 
 *  @param maxX applet panel width
 *  @param maxY applet panel height */ 
    public void expand(int maxX, int maxY)
    {
        if(dX == wantedDX && dY == wantedDY)
            return;
        else
        {   dX = wantedDX;
            dY = wantedDY;
            if(!compress(maxX, maxY))
                repaint();
        }
    }
    
/** Move cards from another stack 
 *  @param src source stack for cards
 *  @param nMove number of cards to move
 *  @param insI # of cards between top of stack & insertion location 
 *    will always be 0 except for undos
 *  @parem delI # of cards between top of stack & card removal location
 *  @param repaintSrc if true repaint entire source, if false repaint
 *   changed portion 
 *  @param repaintDest if true repaint entire destination otherwise 
 *   repaint changed portion */ 
    public void move(CardStack src, int nMove, int insI, int delI,
                     boolean repaintSrc, boolean repaintDest)
    {
        int y = height() - Card.HEIGHT, x = width() - Card.WIDTH;

        if(src == this)
            shift(nCards - delI - nMove, nCards - insI - 1, nMove);
        else if(insI != 0)
            insert(src.cards, src.nCards - delI - nMove, nCards - insI, nMove);
        else
            add(src.cards, src.nCards() - delI - nMove, nMove);

        if(!repaintDest && insI == 0)
            repaint(x, y, width() - x, height() - y);
        else
            repaint(); 

        int h = src.height(), w = src.width();
            
        if(src != this)
            src.remove(src.nCards - delI - nMove, nMove);
    
        x = src.width()  - Card.WIDTH;
        y = src.height() - Card.HEIGHT;
        if(!repaintSrc && delI == 0)
            src.repaint(x, y, w - x, h - y);
        else
        {   src.repaint();
            src.selectI = 0;
        }
    }

/** Move selected card(s) from another stack to top of this stack, repainting
 *  both stacks in their entirity.
 *  @param src source stack for cards
 *  @param n number of cards to move 
 *  @param insI index to add cards at
 *  @param delI index to remove cards from */       
    public void move(CardStack src, int n, int insI, int delI)
    {
        move(src, n, insI, delI, src.repaintAll, repaintAll);
    }
/** Move selected card(s) from another stack to top of this stack, repainting
 *  both stacks in their entirity.
 *  @param src source stack for cards
 *  @param n number of cards to move */
    public void move(CardStack src, int n)
    {
        move(src, n, 0, src.selectI, src.repaintAll, repaintAll);
    }
    
/** Move the selected card(s) in a stack to the top of this stack
 *  @param src source stack for cards
 *  @param n number of cards to move
 *  @param repaintSrc if true repaint entire source, if false repaint
 *   changed portion 
 *  @param repaintDest if true repaint entire destination otherwise 
 *   repaint changed portion */ 
    public void move(CardStack src, int nMove, 
                     boolean repaintSrc, boolean repaintDest)
    {
        move(src, nMove, 0, src.selectI, repaintSrc, repaintDest);
    }

/** Repaint stack using image buffer */
    public synchronized void update (Graphics g) 
    {
        Dimension d = getSize(); 
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
    
/** Default drawing routine, if empty just draw outline otherwise draw top 
 * card */ 
    public void paint(Graphics g)  
    { 
        if(nCards != 0)
            drawTop(g, leftX(), topY());
        else
            Card.drawOutline(g, leftX(), topY());
    }
 /* Draw the top card in the stack starting at (x, y) */    
    public void drawTop(Graphics g, int x, int y)
    {
        if(nCards > 0)
            top().draw(g, x, y, 
                       nSelected != 0 ? Card.selectColor : Card.frontColor);
    }
    
/** Return a rectangle indicating the location of the cards in the canvas
 *  the x and y value are absolute values in the applet panel */ 
    public Rectangle cardBounds()  
    {
        Component c = this;
        int x = 0, y = 0;
        
        while(!(c instanceof Applet))
        {   Rectangle r = c.bounds();
            x += r.x;
            y += r.y;
            c = c.getParent();
            if(c == null)
                return(new Rectangle(-1, -1, 0, 0));
        }
        return new Rectangle(x + leftX(), y + topY(), width(), height());
    }
/** Return true if the point can befound inside the cards in the stack */  
    public boolean inCards(int x, int y) {  return cardBounds().inside(x, y);}
    
/** Method for drawing stack with multiple background colors
 *  @param g graphics context
 *  @param x left coordinate of stack
 *  @param y top coordinate of stack
 *  @param dx horizontal change in position between cards
 *  @param dy vertical change in position between cards
 *  @param bkgs array of colors for backgrounds colors
 *  @param lenArr array containing number of cards which should be drawn 
 *   with each background color */ 
    public void draw(Graphics g, int x, int y, int dx, int dy, 
                     Color[] bkgs, int[] lenArr)
    {
     // Determine first card to print
        int cardI = nCards, bkgI = -1, bkgLeft = 0; 
        for(int i = 0; i < lenArr.length; ++i)
            cardI -= lenArr[i];
        
        if(cardI == nCards)
        {   if(suitOrder != NO_ACCEPT)
                Card.drawOutline(g, leftX(), topY());
            return;
        }
        for(int i = 0; !draw(g, x, y, dx, dy, bkgs[i], lenArr[i], cardI); ++i)
        {   int n  = lenArr[i];
            cardI += n;
            x     += dx * n;
            y     += dy * n;
        }
    }

/** Method for drawing part of stack 
 *  @param g graphics context
 *  @param x left coordinate of stack
 *  @param y top coordinate of stack
 *  @param dx horizontal change in position between cards
 *  @param dy vertical change in position between cards
 *  @param bkgColor background color of substack, if null draw card back
 *  @param nDraw number of cards to draw
 *  @param cardI index of first card to draw */ 
    public boolean draw(Graphics g, int x, int y, int dx, int dy,
                        Color bkgColor, int nDraw, int cardI)
    {
        x += leftX();
        y += topY();
        boolean atTop = false;

        if(cardI + nDraw == nCards && nDraw >= 1)
        {   --nDraw;
            atTop = true;
        }
        
        for( ; nDraw > 0; --nDraw, ++cardI, x += dx, y += dy)
            drawCovered(g, cardI, x, y, dx, dy, bkgColor);

        if(atTop)
            cards[cardI].draw(g, x, y, bkgColor);

        return atTop;
    }

/** Draw partially covered card *   
 *  @param g graphics context
 *  @param i index of card
 *  @param x left coordinate of card
 *  @param y top coordinate of card
 *  @param dx horizontal change in position between cards
 *  @param dy vertical change in position between cards */ 
    public void drawCovered(Graphics g, int i, int x, int y, int dx, int dy,
                            Color bkgColor)
    {
        if(dy > 5)
            cards[i].drawTop(g, x, y, bkgColor);
        else if(dx > 5)
            cards[i].drawSide(g, x, y, bkgColor);
     // Don't draw anything if less than 5 pixels visible   
        else
            Card.drawEmpty(g, x, y, bkgColor);
    }
    
/** Highlight selected cards at top of stack 
 *  @param n number of cards to select 
 *  @param selI number of cards between top of stack & upper selected card*/
    public void select(int n, int selI)
    {
        if(nSelected != n || selI != selectI)
        {   int nRepaint = Math.max(n + selI, nSelected + selectI);
            nSelected = n;
            selectI = selI;
            if(!repaintAll)
            {   int x = leftX() + width() - Card.WIDTH - (nRepaint - 1) * dX;
                int y = topY() + height() - Card.HEIGHT - (nRepaint - 1) * dY; 
                repaint(x, y, width() - x, height() - y);
            }
            else
                repaint();
        }
    }
    public void select(int n) { select(n, 0); }
/** Select the appropriate number of cards after a 
 *   click inside the stack
 *   @param p point inside stack where click occured */ 
    public void select(Point p)
    { 
        int nSel = 1, selI = 0;
        if(getSelection() != SELECT_TOP)
        {   nSel += clickedI(p);
            if(getSelection() == SELECT_RANGE)
                nSel = Math.min(nSel, nCards - nTurned);
            else
            {   selI = nSel - 1;
                nSel = 1;
            }
        }
        select(nSel, selI);
    }
/** Return index of card corresponding to clicked point. Index is from top 
 *  @param cardI index of card */
    public int clickedI(Point p)
    {   
        if(dX != 0 && (p.x = width() - p.x - Card.WIDTH) > 0)
            return  1 + p.x / dX;
        else if(dY != 0 && (p.y = height() - p.y - Card.HEIGHT) > 0)
            return  1 + p.y / dY;
        else return 0;
    }
        
/** Return true if card at index cardI is selected, false if not 
 *  @param cardI  index of card */ 
    public boolean isSelected(int cardI)
    {
        return (nCards - selectI - nSelected <= cardI
                && cardI < nCards - selectI);
    }
    
/** Print a textual representation of the stack */ 
    public String toString()
    {
        String str = ""; 
        for(int i = 0; i < nCards; ++i)
            str += " [" + new Integer(i).toString() +"]:" +cards[i].toString();
        return str;
    }
}
