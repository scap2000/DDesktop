package jsolitaire.clock;

import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of clock solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) */
public class Clock extends Solitaire
{
    CardStack src;
    Button move = makeButton("Move"), finish = makeButton("Finish");

/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0-12    <TD> ClockStack <TD> 4 <TD> 4 </TABLE> */ 
    public void gameInit()
    {
        stacks  = new CardStack[13];
        helpFile = "clock-help.html";
        int[] ns = { 4,4,4,4, 4,4,4,4 ,4,4,4,4, 4};
        nsCards  = nsTurned = ns;

        String[] labels = { "A", "2", "3", "4", "5", "6", "7", 
            "8", "9", "10", "J", "Q", "K" };
//      String[] labels = { "A", "II", "III", "IV", "V", "VI", "VII", "VIII",
//          "IX", "X", "J", "Q", "K" };
        
        for(int i = 0; i < stacks.length; ++i)
            stacks[i] = new ClockStack(i, labels[i]);
    }

/** Layout cards in hexagon except for King stack which will go in center. 
 *  Button panels will flank the king stack. */ 
    public void layoutScreen()
    {
        setOverlaps(0, stacks.length, 12, 0);
        int[] rowYs = new int[7];
        int[] colXs = new int[7];
        int mh = stacks[0].maxHeight(), mw = stacks[0].maxWidth();
        int hGap = 4, vGap = 5;
        
        colXs[0] = hGap;
        colXs[1] =  hGap;
        for(int i = 2; i < 6; ++i)
            colXs[i] = colXs[1] + (i - 1) * (mw + hGap);
        colXs[6] = colXs[5] + colXs[1];
        
        rowYs[0] = vGap;
        rowYs[1] = vGap + mh / 6;
        rowYs[2] = vGap + mh / 3; 
        for(int i = 3; i < 5; ++i)
            rowYs[i] = rowYs[2] + (i - 2) * (mh + vGap);
        for(int i = 2; i < 4; ++i)
            rowYs[3 + i] = rowYs[4] + (rowYs[2] - rowYs[3 - i]);

        place(stacks[8], colXs[0], rowYs[3]);
        place(stacks[9], colXs[1], rowYs[2]);
        place(stacks[7], colXs[1], rowYs[4]);
        place(stacks[10], colXs[2], rowYs[1]);
        place(stacks[6], colXs[2], rowYs[5]);
        place(stacks[11], colXs[3], rowYs[0]);
        place(stacks[12], colXs[3], rowYs[3]);
        place(stacks[5], colXs[3], rowYs[6]);
        place(stacks[0], colXs[4], rowYs[1]);
        place(stacks[4], colXs[4], rowYs[5]);
        place(stacks[1], colXs[5], rowYs[2]);
        place(stacks[3], colXs[5], rowYs[4]);
        place(stacks[2], colXs[6], rowYs[3]); 

        Button[] b1 = { newGame, finish };
        Button[] b2 = { move, help };
        int bw = 70, bh = 48, by = rowYs[3] + (mh - bh) / 2;
        place(makeButtonPanel(b1, new GridLayout(2, 1), bw, bh),
              colXs[2] + (mw - bw) / 2, by);
        place(makeButtonPanel(b2, new GridLayout(2, 1), bw, bh),
              colXs[4] + (mw - bw) / 2, by);
    }  
    
/** Being game by selecting card at top of king stack */    
    public void startGame(boolean shuffle)
    {
        super.startGame(shuffle);
        src = stacks[12];
        src.select(1);
        move.enable();
        finish.enable(); 
    }
    
/** Handle events for move & finish buttons */ 
    public boolean action(Event evt, Object arg)
    {
        if(!super.action(evt, arg))
        {   Object source = evt.target;

         // Move button makes move automatically    
            if(source == move)
            {   if(src != null)
                    select(stacks[src.top().rank()]);
            }

         // Finish makes all moves until game won or over   
            else if(source == finish)
            {   while(src != null)
                    select(stacks[src.top().rank()]);
            }
            else
                return false;
        }
        return true; 
    }

/** This function will handle card movement because the source stack is
 *   automatically selected */  
    public void select(CardStack dest, int x, int y)
    {
        if(src == null)
            return; 
        else if(dest.canAccept(src) == 0)
            showMsg("Invalid move");
     // Move card to clicked stack, then select card at top of clicked stack
        else
        {   
            if(dest != src)
            {   moveCard(dest, src, 0, 1, null);
                src.select(0);
            }
            else
            {   Card c = src.top();
                src.insert(c, 0);
                src.remove(0, 1);
                src.select(0);
                src.repaint();
            }
            src = null;
            
         // If no turned down cards in stack, move clockwise until a stack
         //   with turned down cards is found   
            if(!gameWon())
            {   if(dest.getNTurned() == 0)
                {   int rank = ((ClockStack)dest).rank(); 
                    dest = null; 
                
                    if(rank != Card.KING)
                    {   for(int r = rank + 1; dest == null; ++r)
                        {   if(r == Card.KING)
                                r = Card.ACE;
                            if(r == rank)
                                r = Card.KING;
                            if(stacks[r].getNTurned() != 0)
                               dest = stacks[r];
                        }
                    }
                }
                if(dest != null)
                {   src = dest;
                    src.select(1);
                }
                else
                    showMsg("Game Over"); 
            }
            if(src == null)
            {   move.disable();
                finish.disable();
            }
        }
    }
 // Game is won when all cards are face up 
    public boolean gameWon()
    {
        int i;
        for(i = 0; i < stacks.length && stacks[i].getNTurned() == 0; ++i)
            ;
        return i == stacks.length;
    }
}

/** Stack for clock solitaire. Stack will have label indicating accepting
 *  rank at top of stack. */
class ClockStack extends SpreadStack
{
    int rank;
    String label;
    static int LABEL_HEIGHT = 15;
    static Font labelFont = Card.findFont("TimesRoman", Font.PLAIN, 13);
    static FontMetrics fm = (new Label()).getFontMetrics(labelFont);

/** Set the clock stack's rank to r and label to l */ 
    public ClockStack(int r, String l)
    {
        super(6);
        rank  = r;
        label = l;
        repaintAll = true; 
    }
    public int rank() { return rank; }
    public int topY() { return LABEL_HEIGHT; }
    public int maxHeight() { return Card.HEIGHT + LABEL_HEIGHT; }
    public int maxWidth() { return Card.WIDTH + 4 * dX; }
    public int canAccept(CardStack src) 
    {
        return (src.top().rank() == rank) ? 1 : 0;
    }

/** Put accepted card at bottom of stack */
    public void add(Card card[], int srcI, int nAdd) { insert(card[srcI], 0); }

    public void remove(int remI, int nRemove)
    {
        nCards  -= nRemove;
        nTurned -= nRemove;
    }
    static Color bkgs[] = { Card.frontColor, Card.backColor, Card.selectColor};
    
/** Draw face up cards, face down cards, the selected card if present. 
  * Draw label at top of card */ 
    public void paint(Graphics g)  
    { 
        g.setFont(labelFont);
        g.setColor(Color.black); 
        int labelX = dX * 3 + (Card.WIDTH - fm.stringWidth(label)) / 2;  
        g.drawString(label, labelX, LABEL_HEIGHT - 2);
        g.setFont(Card.getFont());
        int[] nBkgs = { nCards - nTurned, nTurned  - nSelected, nSelected };
        draw(g, 0, 0, dX, dY, bkgs, nBkgs);
    }
}
