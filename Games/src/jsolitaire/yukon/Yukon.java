package jsolitaire.yukon;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of Klondike solitaire. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/
public class Yukon extends Solitaire
{
    static final int NSUITS = 4, BUILDS_I = 4;
    Choice emptyChoice = new Choice();
    int emptyChoiceI;
    Choice nCardsChoice = new Choice();
    int nCardsI = 0;

/** gameInit sets up the stack array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0-3     <TD> SuitStack <TD> 0 <TD> 0
 * <TR><TD> 4       <TD> YukonBuild <TD> 1 <TD> 0
 * <TR><TD> 5-10    <TD> YukonBuild 6-11 <TD> 1-6 </TABLE> */ 
    public void gameInit()
    {
        stacks = new CardStack[11];

        helpFile = "yukon-help.html";
        nOrderedTop = false; 

        int stackI = SUITS_I = 0;
        for(; stackI < SUITS_I + NSUITS; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI - SUITS_I));

        for(; stackI < stacks.length; ++stackI)
            stacks[stackI] = new YukonBuild();

        nsCards = new int[11];
        nsTurned = new int[11]; 
    }

    public void deal()
    {
        setLayout();
        super.deal();
    }

    void setLayout()
    {
        nCardsI = nCardsChoice.getSelectedIndex(); 
        int[][] nsCs = 
        {   { 0, 0, 0, 0, 1, 6, 7, 8, 9, 10, 11 },
            { 0, 0, 0, 0, 8, 8, 8, 7, 7, 7, 7} 
        };
        int[][] nsTs = 
        {   { 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6 },
            { 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6 }
        };
        System.arraycopy(nsCs[nCardsI], 0, nsCards, 0, nsCs[nCardsI].length);
        System.arraycopy(nsTs[nCardsI], 0, nsTurned, 0, nsTs[nCardsI].length);

        for(int i = 0, stackI = stacks.length - 1; i < 4 * startRankI;
            stackI = (stackI == BUILDS_I) ? stacks.length - 1: stackI - 1)
        {   if(nsCards[stackI] > 1)
            {   --nsCards[stackI];
                ++i;
                if(nsTurned[stackI] > 0)
                    --nsTurned[stackI];
            }
        }
    }

/** layoutScreen sets up the screen in 9 columns. The first column has
 *    the button panel and black suit stacks. The second through eighth 
 *    columns have the building stacks and the ninth column has the red
 *    suit stacks. */ 
    public void layoutScreen()
    {
        int hGap = 8, vGap = 10; 
        int x2 = place(buttonPanel, hGap, vGap).x;
        setOverlaps(4, 7, 0, Card.TOP_HEIGHT, 0, Card.DOWN_TOP_HEIGHT);
        int redX = layoutRow(x2 + hGap, vGap, 4, 7, hGap);

        int y2 = Card.HEIGHT + 2 * vGap; 
        layoutCol(x2 - Card.WIDTH, y2, 0, 2, vGap);
        layoutCol(redX, y2, 2, 2, vGap);
    }
/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        setLayout(); 

    }

    public boolean noBuilds(Card card)
    {
        int i = SUITS_I + (card.isBlack() ? 2 : 0);
        int d = (card.rank() + 13 - baseRank) % 13;
        
        return (   d == 0 
                || (stacks[i + 1].nCards() >= d && stacks[i].nCards() >= d));
    }
    
    public String optsTitle() { return "Yukon Options"; }

/** There are two options in yukon. The first is the minimum rank that
 *    and empty suit stack will accept. The second is moving cards to suit
 *    stacks automatically */
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        nCardsChoice.addItem("1, 6-11");
        nCardsChoice.addItem("8, 7");
        p.add(dlgRow("# of cards in stacks:", nCardsChoice));
        nCardsChoice.select(0);
        
        p.add(dlgRow("Starting rank:", startRankChoice));

        emptyChoice.addItem("K only");
        emptyChoice.addItem("K, Q or J");
        emptyChoice.addItem("Any card"); 
        p.add(dlgRow("Empty spaces accept:", emptyChoice));

        addCommonOpts(p);
        return p;
    }

    public void setOptions()
    {
        super.setOptions();
        int[] emptyMins = { Card.KING, Card.JACK, Card.ACE };
        emptyChoiceI = emptyChoice.getSelectedIndex();
        YukonBuild.setEmptyMin(emptyMins[emptyChoiceI]);
    }
    
    public void resetOptions()
    {
        super.resetOptions();
        emptyChoice.select(emptyChoiceI); 
        nCardsChoice.select(nCardsI); 
    }
}

/** Alternating color stack for yukon */ 
class YukonBuild extends SpreadStack
{
    static int emptyMin = Card.KING;
    public YukonBuild() 
    {   super(52); 
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
