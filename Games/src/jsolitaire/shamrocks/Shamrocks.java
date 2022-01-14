package jsolitaire.shamrocks;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of Shamrocks Solitaire
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class Shamrocks extends Solitaire
{
/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL #
 *  <TR><TD> 0-3 <TD> SuitStack <TD> 0
 *  <TR><TD> 4-20 <TD> ShamRocksBuild <TD> 3
 *  <TR><TD> 21 <TD> ShamRocksBuild <TD> 1 </TABLE> */ 
    public void gameInit()
    {   
        stacks   = new CardStack[22];
        helpFile = "shamrocks-help.html";

        int stackI;
        SUITS_I = 0; 
        for(stackI = 0; stackI < 4; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI));

        for(stackI = 4; stackI < stacks.length; ++stackI)
            stacks[stackI] = new ShamrocksBuild();
        
        int[] ns = { 0, 0, 0, 0,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,
            3, 3, 3, 3, 3, 1};
        nsCards  = ns;
    }

 // The leftmost column will contain the suit stacks. The next 5 columns
 //   contain the building stacks. The first position in the building stacks
 //   grid has the button panel, the last is empty. 
    public void layoutScreen()
    {
        int hGap = 10, vGap = 8, rowY = 8;
        int stackWidth = Card.WIDTH + 2 * Card.SIDE_WIDTH;
        int col2X = 10 + Card.WIDTH + hGap + 5; 
            
        setOverlaps(4, 18, Card.SIDE_WIDTH, 0); 

        layoutCol(10, rowY, 0, 4, vGap); // suit stacks
        place(buttonPanel, col2X + (stackWidth - bpWidth) / 2, rowY);
        
        layoutRow(col2X + stackWidth + hGap, rowY, 4, 4, hGap);
        for(int rowI = 1; rowI < 4; ++rowI)
        {   rowY += Card.HEIGHT + vGap; 
            layoutRow(col2X, rowY, 3 + rowI * 5,
                      rowI < 3 ? 5 : 4, hGap);
        }
    }
/** If a king lies above a card of the same suit, put the king below
 *  that card */ 
    public void deal()
    {
        super.deal();
        for(int stackI = 4; stackI < stacks.length - 1; ++stackI)
        {   CardStack stk = stacks[stackI]; 
            for(int kingI = stk.nCards() - 1; kingI > 0; --kingI)
            {   Card c = stk.cardAt(kingI);
                if(c.rank() == Card.KING)
                {   for(int belowI = 0; belowI < kingI; ++belowI)
                    {   Card c2 = stk.cardAt(belowI);
                        if(c.suit() == c2.suit())
                        {   stk.swap(belowI, kingI);
                            break;
                        }
                    }
                }
            }
        }
    }

 // Return true if the card is an ace or one or the rank of all the tops
 //  of the suit stacks is at one least less than the cards rank 
    public boolean noBuilds(Card card)
    {
        int r = card.rank();

        if(r >= 2)
        {   for(int i = 0; i < 4; ++i)
            {   if(stacks[ SUITS_I + i].nCards() < r)
                    return false;
            }
        }
        return true;
    }
    public String optsTitle() { return "Shamrocks Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        addCommonOpts(p); 
        return p; 
    }
}
class ShamrocksBuild extends SpreadStack
{
    ShamrocksBuild() 
    { 
        super(3); 
        setTopRank(-1); // allow building on Aces 
    }
    public int canStart(CardStack src) { return 0;  } // empty stacks unusable
    public int canAdd(CardStack src) 
    {
        if(nCards() == 3)
            return 0; 
        int destRank = this.top().rank(), srcRank = src.top().rank();
        return (destRank == srcRank + 1 || destRank == srcRank - 1) ? 1 : 0;
    }
    public void paint(Graphics g)
    {
        if(nCards() != 0)  // don't draw outlines for empty stacks
            super.paint(g);
    }

 // Never automatically remove cards if stack has one card because that
 //  will make the stack unusable   
    public boolean isAutoMoveSrc() 
    {
        return (autoMoveSrc && nCards() > 1);
    }
}
