package jsolitaire.pyramid;

import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;

/** Implementation of Pyramid solitaire. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2000/03/15)
*/

public class Pyramid extends Solitaire
{
    final int PYR_START = 3;
    Pile stock, waste, discard;

 // Variables for options 
    Choice nRedealsChoice = new Choice();
    Checkbox revLayerCB   = new Checkbox("Reverse Layering");
    boolean layersReversed;
    
/** gameInit sets up the stack array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0       <TD> PyramidPile (stock) <TD> 28 <TD> 1
 * <TR><TD> 1       <TD> PyramidPile (waste) <TD> 0 <TD> nDraw
 * <TR><TD> 2       <TD> Pile (discard) <TD> 0 <TD> 0
 * <TR><TD> 3-30    <TD> PyramidCards <TD> 1 <TD> 0 </TABLE> */
    public void gameInit()
    {
        stacks = new CardStack[31];

        helpFile = "pyramid-help.html";
        
        int[] nsC = { 24, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,
            1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1 };
        int[] nsT = { 1, 1, 1,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, 
                0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0 };
        nsCards  = nsC;
        nsTurned = nsT;
        maxRedeals = 1; 
        
        stacks[0] = stock = new PyramidPile(28);
        stacks[1] = waste = new PyramidPile(28);
        stacks[2] = discard = new Pile(52); 
        for(int stkI = PYR_START; stkI < stacks.length; ++stkI)
            stacks[stkI] = new PyramidCard(); 

     // Indicate the cards which lie on top of the upper 6 rows
        for(int rowI = 1, stkI = PYR_START;  rowI <= 6; ++rowI)
        {   for(int colI = 0; colI < rowI; ++colI, ++stkI)
            {   PyramidCard pyr = (PyramidCard)stacks[stkI];
                pyr.setBelow(stacks[stkI + rowI], stacks[stkI + rowI + 1]);
            }
        }
     // Now indicate which cards are covered by the lower 6 rows
     //   Cards on the sides will only cover one card, cards in the
     //   middle will cover two.    
        for(int rowI = 7, stkI = stacks.length - 1; rowI >= 2; --rowI)
        {   setAbove(stkI, stacks[stkI - rowI], null);
            --stkI; 
            for(int midI = 0; midI < rowI - 2; ++midI, --stkI)
                setAbove(stkI, stacks[stkI - rowI], stacks[stkI - rowI + 1]);

            setAbove(stkI, null, stacks[stkI - rowI + 1]);
            --stkI; 
        }
    }
 // Indicate that a1 and a2 are the two stacks which the stack at stkI
 //   lies on top of    
    private void setAbove(int stkI, CardStack a1, CardStack a2)
    {
        ((PyramidCard)stacks[stkI]).setAbove(a1, a2);
    }
    
 /* Set the screen up in two rows. The first contains the pile/talon,
  * button panel and suit stacks. The second contains the building stacks.
  * The gaps between stacks is set so the rows will have equal length */ 
    public void layoutScreen()
    {
        int rowY = 10, hGap = 10, rowX = 7 * Card.WIDTH/2 + hGap * 2;
        int endX = 2 * hGap + 7 * Card.WIDTH + 6 * PyramidCard.H_GAP;
        int dX   = (Card.WIDTH + PyramidCard.H_GAP)/2;

     // Put stock & waste pile in upper left corner 
        layoutCol(hGap, rowY, 0, 2, 10);

     // Put button panel & discard pile in upper right corner   
        place(buttonPanel, endX - bpWidth, rowY);
        place(discard, endX - Card.WIDTH, rowY * 2 + bpHeight);
        
        for(int rowI = 1, stkI = 3; rowI <= 7;
            ++rowI, rowX -= dX, rowY += PyramidCard.V_DIFF)
        {   for(int colI = 0, x = rowX; colI < rowI; 
                ++colI, x += Card.WIDTH + PyramidCard.H_GAP)
                place(stacks[stkI++], x, rowY);
        }
        layerRows();
    }

 // Add rows to screen so that bottom rows are layered on top
    void layerRows()
    {
        for(int stkI = 3; stkI < stacks.length; ++stkI)
            remove(stacks[stkI]);
        if(!layersReversed)
        {   for(int stkI = stacks.length - 1; stkI >= 3; --stkI)
                add(stacks[stkI]);
        }
        else
        {   for(int stkI = 3; stkI < stacks.length; ++stkI)
                add(stacks[stkI]);
        }
    }
    
 // Need to resize all stacks before starting because stacks are resized
 //  to pixel size when card is removed 
    public void startGame(boolean shuffle)
    {
        for(int i = PYR_START; i < stacks.length; ++i)
            stacks[i].fixSize();
        super.startGame(shuffle);
    }
    
/** If the pile is clicked, move cards from it to the talon. Otherwise 
 *  select the clicked stack. */ 
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked.canRemove())
        {   if(   (clicked instanceof PyramidCard) 
               && ((PyramidCard)clicked).covered())
                return; 
            
            if(clicked.top().rank() == Card.KING)
            {   moveCard(discard, clicked, 1, null);
                return; 
            }
            clicked.select(1);
            selected = clicked;
        }
     // If stock empty and stock clicked on, try a redeal
        else if(clicked == stock)
            talonAdd(waste, stock, 1); 
    }
    public void tryMove(CardStack clicked, int insI, Event evt, int nMove)
    {   
     // do nothing if card covered  
        if(   clicked.isEmpty()
           || (   (clicked instanceof PyramidCard) 
               && ((PyramidCard)clicked).covered()))
            ;
        else if(clicked.top().rank() + selected.top().rank() == 11)
        {   moveCard(discard, selected, nMove, null);
            moveCard(discard, clicked, nMove, null);
            makeUndoGroup(2);
        }
        else if(clicked != selected)
            showMsg("Sum of cards ranks must equal 13");
        else if(clicked == stock)
            talonAdd(waste, stock, 1);
        
        deselect(); 
    }
    public void rightClick(CardStack clicked) 
    {   
        talonAdd(waste, stock, 1);
        deselect(); 
    }
    
    public boolean gameWon() { return discard.nCards() == DECK_SIZE; }
    public String statistics()
    {
        int nDis = discard.nCards(), n = stock.nCards() + waste.nCards();
        return n + "/" + (52 - nDis - n) + "/" + nDis + " ";
    }
    
    public String optsTitle() { return "Pyramid Options"; }
     
/** The only option for Pyramid is the number of redeals */ 
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        nRedealsChoice.addItem("Unlimited");
        for(int i = 0; i <= 4; ++i)
            nRedealsChoice.addItem(Integer.toString(i));
        
        p.add(dlgRow("Number of redeals:", nRedealsChoice));
        addCommonOpts(p, TIMER_OPT);
        p.add(revLayerCB);

        resetOptions(); 
        return p;   
    }
    
    public void setOptions()
    {
        super.setOptions();
        maxRedeals = nRedealsChoice.getSelectedIndex(); 
        if(revLayerCB.getState() != layersReversed)
        {   layersReversed = revLayerCB.getState();
            layerRows();
        }
    }
    public void resetOptions()
    {
        super.resetOptions();
        nRedealsChoice.select(maxRedeals);
        revLayerCB.setState(layersReversed);
    }
}

/** Alternating color stack for Klondike */ 
class PyramidCard extends CardStack
{
    public static final int H_GAP = 10, V_DIFF = 55;
    CardStack above1, above2; // 2 cards card lies above
    CardStack below1, below2; // 2 cards card lies below 

    PyramidCard() { super(1); }

 // Resizing to pixel after removing card will show cards under removed card
    public void remove(int nRemove) 
    { 
        super.remove(nRemove);
        resize(1, 1);
    }
    public void remove(int remI, int nRemove)
    {
        super.remove(remI, nRemove);
        resize(1, 1);
    }

 // In case removal undone, need to resize to normal size before adding    
    public void add(Card [] src, int srcI, int nAdd)
    {   
        super.add(src, srcI, nAdd);
        fixSize();
    }
    
    void setAbove(CardStack a1, CardStack a2)
    {
        above1 = a1;
        above2 = a2;
    }
    void setBelow(CardStack b1, CardStack b2)
    {
        below1 = b1;
        below2 = b2;
    }
    
 /* Return true if there are cards on top of the card */    
    boolean covered() 
    {   
        return (   (below1 != null && !below1.isEmpty())
                || (below2 != null && !below2.isEmpty()));
    }

    public void paint(Graphics g)  
    { 
     // Need to draw under cards to fill in corners
        if(above1 != null && !above1.isEmpty())
            above1.drawTop(g, -(Card.WIDTH + H_GAP)/2, -V_DIFF);
            
        if(above2 != null && !above2.isEmpty())
            above2.drawTop(g, (Card.WIDTH + H_GAP)/2, -V_DIFF);

        if(nCards != 0)
        {   top().draw(g, 0, 0, 
                       nSelected != 0 ? Card.selectColor : Card.frontColor);
            Card.drawOutline(g, 0, 0);
            if(below1 != null)
                below1.repaint();
            if(below2 != null)
                below2.repaint(); 
        }
        else
        {   if(above1 != null && !above1.isEmpty())
                Card.drawOutline(g, -(Card.WIDTH + H_GAP)/2, -V_DIFF); 
            if(above2 != null && !above2.isEmpty())
                Card.drawOutline(g, (Card.WIDTH + H_GAP)/2,-V_DIFF);
        }
    }
    public int canAccept(CardStack source) { return 1;  }
}

class PyramidPile extends Pile
{
    public PyramidPile(int maxCards) { super(maxCards); }
    public boolean canRemove() { return (nCards() > 0); }
    public int canAccept(CardStack source) { return 1;  }
}
