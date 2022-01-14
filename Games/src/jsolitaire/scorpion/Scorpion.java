package jsolitaire.scorpion;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Scorpion solitaire. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2000/2/17) 
*/
public class Scorpion extends Solitaire
{
    static final int NSUITS = 4, BUILDS_I = 0, NBUILDS = 7, TAIL_I = 7;
    Choice nDownChoice = new Choice();
    int nDown = 4; // number of stacks with face-down cards
    Choice emptyChoice = new Choice();
    int emptyChoiceI;
    CardStack tail; 

/** gameInit sets up the stack array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0-6     <TD> ScorpionStack <TD> 7 <TD> 3/0
 * <TR><TD> 7       <TD> SpreadStack (tail) <TD> 3 <TD> 3 */ 
    public void gameInit()
    {
        stacks = new CardStack[8];
        nOrderedTop = false;
        helpFile = "scorpion-help.html";
        
        for(int stackI = 0; stackI < NBUILDS; ++stackI)
            stacks[stackI] = new ScorpionStack();

        stacks[NBUILDS] = tail = new SpreadStack(3);
        tail.setOrder(0, CardStack.NO_ACCEPT);
        
        int[] nsC = { 7, 7, 7, 7, 7, 7, 7, 3};
        int[] nsT = { 3, 3, 3, 0, 0, 0, 0, 3};
        nsCards  = nsC;
        nsTurned = nsT;
    }

/** layoutScreen sets up the screen in 8 columns. The first column has
 *    the button panel and the "tail". The remaining 7 contain the
 *    building stacks */ 
    public void layoutScreen()
    {
        int hGap = 8, vGap = 10; 
        int x2 = place(buttonPanel, hGap, vGap).x;
        setOverlaps(TAIL_I, 1, 0, Card.TOP_HEIGHT);
        layoutRow(x2 - Card.WIDTH, vGap * 2 + bpHeight, TAIL_I, 1, 0);
        
        setOverlaps(BUILDS_I, NBUILDS, 0, Card.TOP_HEIGHT);
        layoutRow(x2 + hGap, vGap, BUILDS_I, NBUILDS, hGap);
    }

 /* Before dealing, set the number of stacks with face down cards
  * appropriately */ 
    public void deal()
    {
        for(int i = 0; i < NBUILDS; ++i)
            nsTurned[i] = (i < nDown) ? 3 : 0;
        super.deal();
    }

/** If the pile is clicked, move cards from it the building stack */
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked == tail)
        {   if(!tail.isEmpty())
            {   for(int stkI = 0; stkI < 3; ++stkI)
                    moveCard(stacks[BUILDS_I + stkI], tail, 1, null);
                makeUndoGroup(3);
            }
        }
        else if(clicked.canRemove())
            super.select(clicked, x, y); 
    }
 // Return the number of suits with all 13 cards in order   
    private int nSuitsOrdered()
    {
        int suitsOrdered = 0; 
        for(int stkI = BUILDS_I; stkI < BUILDS_I + NBUILDS; ++stkI)
        {   if(stacks[stkI].nOrdered() == 13)
                ++suitsOrdered;
        }
        return suitsOrdered;
    }
 /* Game is won if all suits are ordered */
    public boolean gameWon() { return (nSuitsOrdered() == 4); }
    public boolean isBuildingStackI(int stackI) { return stackI != TAIL_I; }

    public int nRemoved() { return 13 * nSuitsOrdered(); }
    public int nOrdered() { return super.nOrdered() - nRemoved(); }

    public String optsTitle() { return "Scorpion Options"; }

/** There are two options in yukon. The first is the minimum rank that
 *    and empty suit stack will accept. The second is moving cards to suit
 *    stacks automatically */
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        for(int i = 0; i <= NBUILDS; ++i)
            nDownChoice.addItem(Integer.toString(i));

        emptyChoice.addItem("K only");
        emptyChoice.addItem("K, Q or J");
        emptyChoice.addItem("Any card"); 
        p.add(dlgRow("# of face down stacks:", nDownChoice));
        p.add(dlgRow("Empty spaces accept:", emptyChoice));
        addCommonOpts(p, TIMER_OPT);
        return p;
    }

    public void setOptions()
    {
        nDown = nDownChoice.getSelectedIndex(); 
        
        int[] emptyMins = { Card.KING, Card.JACK, Card.ACE };
        emptyChoiceI = emptyChoice.getSelectedIndex();
        ScorpionStack.setEmptyMin(emptyMins[emptyChoiceI]);
    }
    
    public void resetOptions()
    {
        nDownChoice.select(nDown); 
        emptyChoice.select(emptyChoiceI); 
    }
}

/** Alternating color stack for yukon */ 
class ScorpionStack extends SpreadStack
{
    static int emptyMin = Card.KING;
    public ScorpionStack() 
    {   super(52);
        setOrder(-1, CardStack.SAME_SUIT); 
        selection = SELECT_RANGE;
    }
    public static void setEmptyMin(int n) { emptyMin = n;   }

    public int canAccept(CardStack src)
    {
        if(src.canRemove())
        {
         // If stack clicked twice on, select all face up cards if
         //  not already accepted    
            if(src == this)
            {   int nFaceUp = src.nCards() - src.getNTurned();
                if(nSelected < nFaceUp && nSelected > 0)
                {   select(nFaceUp);
                    return -1;
                }
                else
                    return 0; 
            }
         // Look through lowest to top selected card, then examine
         //   unselected cards from top to bottom if no match   
            int n = src.nCards(), nSel = src.nSelected();
            for(int cardI = n - nSel; cardI < n; ++cardI)
            {   if(canAccept(src.cardAt(cardI)))
                    return n - cardI;
            }
            for(int cardI = n - nSel - 1; cardI >= src.getNTurned(); --cardI)
            {   if(canAccept(src.cardAt(cardI)))
                    return n - cardI;
            }
        }
        return 0;
    }

 // Return true if card can be added to the stack top, false if not 
    private boolean canAccept(Card card)
    {               
        if(isEmpty())
        {   if(card.rank() >= emptyMin)
                return true;
        }
        else if(orderedPair(top(), card))
            return true;

        return false; 
    }
}
