package jsolitaire.spider;

import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Spider solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2000/2/24) 
*/

public class Spider extends Solitaire
{
    private final int NSUITS = 4, BUILDS_I = 2, NBUILDS = 10;
    Pile pile, discard;
    Choice nCardsChoice = new Choice();
    int nCardsI = 0;

    int nFreeCells = 0, MAX_FREE_CELLS = 4, FREE_CELLS_I = 12;
    Choice nfcChoice = new Choice();

    boolean emptyDraw, kingOnAce;
    Checkbox emptyDrawCB = new Checkbox("Allow unfilled spaces before draw");
    Checkbox kingOnAceCB = new Checkbox("Allow kings on aces");
    
    
/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0       <TD> Pile <TD> 50 <TD> 0
 * <TR><TD> 1       <TD> Discard <TD> 0 <TD> 0
 * <TR><TD> 2-11    <TD> SpiderStack <TD> 6/5 <TD> 5/4
 * <TR><TD> 12-15    <TD> TempStack <TD> 0 <TD> 0 </TABLE> */ 
    public void gameInit()
    {
        NDECKS = 2; 
        stacks = new CardStack[16];

        helpFile = "spider-help.html";

        stacks[0] = pile    = new Pile(2 * DECK_SIZE);
        stacks[1] = discard = new Discard(2 * DECK_SIZE);

        for(int stackI = 0; stackI < NBUILDS; ++stackI)
            stacks[BUILDS_I + stackI] = new SpiderStack();

        for(int stackI = 0; stackI < MAX_FREE_CELLS; ++stackI)
            stacks[FREE_CELLS_I + stackI] = new TempStack(); 
    }

 /* Set the screen up in two rows. The first contains the pile, button
  *  panel and discard pile. The second contains the building stacks. */ 
    public void layoutScreen()
    {
        int row1y = 10, hGap = 5;
        
     // lay out first row so that button panel aligns with middle column
        setOverlaps(0, 2, 3, 0); 
        int bpX = hGap + pile.maxWidth();
        place(pile, hGap, row1y);
        place(buttonPanel, bpX, row1y);
        place(discard, bpX + bpWidth + hGap, row1y);

     // Align free cells with 7th column    
        int fcX = 6 * Card.WIDTH + 7 * hGap;
        layoutRow(fcX, row1y, FREE_CELLS_I, nFreeCells, hGap);
        
     // lay out second row containing building stacks   
        int row2y = (row1y * 2) + pile.maxHeight() + 5;
        setOverlaps(BUILDS_I, NBUILDS,
                    0, Card.TOP_HEIGHT, 0, Card.DOWN_TOP_HEIGHT);
        layoutRow(hGap, row2y, BUILDS_I, NBUILDS, hGap);
    }

/** Override deal so the number of cards in each stack can be changed
  * if desired */ 
    public void deal()
    {
        nCardsI = nCardsChoice.getSelectedIndex(); 
        int[] nsCs = { 50, 0, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0};
        int[] nsTs = {  0, 0, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0};
        if(nCardsI != 0)
        {   int n = nCardsI + 1; 
            nsCs[0] = (2 * DECK_SIZE) - (NBUILDS * n);
            for(int stkI = BUILDS_I; stkI < BUILDS_I + NBUILDS; ++stkI)
            {   nsCs[stkI] = n;
                nsTs[stkI] = n - 1; 
            }
        }
        nsCards = nsCs;
        nsTurned = nsTs;
        super.deal(); 
    }
    
/** If the pile is clicked, move cards from it the building stack */
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked.canRemove())
            super.select(clicked, x, y); 

        else if(clicked == pile && !pile.isEmpty())
        {   int stkI;
            if(emptyDraw == false)
            {   for(stkI = BUILDS_I; stkI < BUILDS_I + NBUILDS; ++stkI)
                {   if(stacks[stkI].isEmpty())
                    {   showMsg("All stacks must be filled before moving cards"
                                + " from the pile.");
                        return;
                    }
                }
            }
            stkI = BUILDS_I;    
            for(; stkI < BUILDS_I + NBUILDS && !pile.isEmpty(); ++stkI)
                moveCard(stacks[stkI], pile, 1, null);

            makeUndoGroup(stkI - BUILDS_I);
            autoMove(); 
        }
    }

/** Destination for all automatic moves in Spider is discard pile and
  * not individual suit stacks as in most games */ 
    public boolean autoMove()
    {
        boolean moveMade = false; 
        if(makeAutoMoves)
        {   for(int stkI = BUILDS_I; stkI < BUILDS_I + NBUILDS; ++stkI)
            {   CardStack src = stacks[stkI];
                if(discard.canAccept(stacks[stkI]) == 13)
                {   moveCard(discard, src, 13, null);
                    moveMade = true;
                }
            }
        }
        return moveMade;
    }
    
/** Game is won when all cards in the deck have been moved to the discard
  * pile */ 
    public boolean gameWon() { return discard.nCards() == DECK_SIZE * 2; }
    public String statistics() 
    {
        int nStk = 52 * 2 - pile.nCards() - discard.nCards();
        return pile.nCards() + "/" + nStk + "/" + discard.nCards() + " ";
    }

    public String optsTitle() { return "Spider Options"; }
     
    public Panel optsPanel() 
    {
        Panel p = new Panel(); 
        p.setLayout(new GridLayout(0, 1));

        nCardsChoice.addItem("6, 5");
        for(int i = 2; i < 8; ++i)
            nCardsChoice.addItem(Integer.toString(i));

        p.add(dlgRow("# of cards in stacks:", nCardsChoice));

        for(int i = 0; i <= 4; ++i)
            nfcChoice.addItem(Integer.toString(i));
        
        p.add(dlgRow("# of free cells:", nfcChoice));
        p.add(emptyDrawCB);
        p.add(kingOnAceCB);
        nCardsChoice.select(0);
        addCommonOpts(p); 
        return p; 
    }
    public void setOptions()
    {
        super.setOptions();
        emptyDraw = emptyDrawCB.getState();
        kingOnAce = kingOnAceCB.getState();
        
        for(int stkI = BUILDS_I; stkI < BUILDS_I + NBUILDS; ++stkI)
            stacks[stkI].setTopRank(kingOnAce ? -1 : Card.ACE);
        
        int newN = nfcChoice.getSelectedIndex(); 
        if(newN == nFreeCells)
            return; 

     // Determine last occupied free cell   
        int nFull = 0, lastFullI = FREE_CELLS_I;
        for(int i = FREE_CELLS_I; i < FREE_CELLS_I + MAX_FREE_CELLS; ++i)
        {   if(!stacks[i].isEmpty())
            {   ++nFull;
                lastFullI = i;
            }
        }
        if(lastFullI - FREE_CELLS_I >= newN)
        {// Report error if desired number smaller than occupied number 
            if(newN < nFull)
            {   resetOptions();
                showMsg("Unable to remove occupied free cell");
                return;
            }
         // Compress free cells if necessary 
            for(int srcI = FREE_CELLS_I, destI = FREE_CELLS_I; 
                srcI < FREE_CELLS_I + nFreeCells; ++srcI)
            {   if(!stacks[srcI].isEmpty())
                {   if(srcI != destI)
                        stacks[destI].move(stacks[srcI], 1);
                    ++destI;
                }
            }
        }
        nFreeCells = newN;
        makeScreen();
    }
    
    public void resetOptions() 
    {
        super.resetOptions();
        nCardsChoice.select(nCardsI); 
        emptyDrawCB.setState(emptyDraw);
        kingOnAceCB.setState(kingOnAce);
        nfcChoice.select(nFreeCells);
    }
}

/** Building stack for Spider */ 
class SpiderStack extends SpreadStack
{
    SpiderStack()
    {   super(104);
        setOrder(-1, CardStack.ANY_SUIT);
        selection = SELECT_RANGE; 
        lookBelow = true;
        doubleClickSelect = true;       
    }
/** nOrdered returns number of cards with the same suit at the stack top. */
    public int nOrdered()
    {   
        suitOrder = CardStack.SAME_SUIT;
        int nOrd  = super.nOrdered();
        suitOrder = CardStack.ANY_SUIT;
        return nOrd;
    }
    
/** If a card below the top of the stack is clicked on, select from that
 *  card to the top of the stack if the card is in an ordered sequence */
    public void select(int n, int selI) 
    { 
        super.select(Math.min(n, nOrdered()), selI); 
    }

/** If stack empty, accept all selected cards */    
    public int canStart(CardStack source) { return source.nSelected(); }
}

/** Discard pile, can only accept 13 cards of same suit in descending order */ 
class Discard extends Pile
{
    public Discard(int maxCards) { super(maxCards); }
    public Discard(int maxCards, int cardsLine) { super(maxCards, cardsLine);}
    
    public int canAccept(CardStack source)
    {
        return source.nOrdered() == 13 ? 13 : 0;
    }
 // Override adding function so that cards are not added in reverse order
    public void add(Card [] src, int srcI, int nAdd)
    {
        System.arraycopy(src, srcI, cards, nCards, nAdd);
        nCards += nAdd;
    }
}

/** TempStack implements the stacks which can contain only a single card
 *   and are used for temporarily holding cards */ 
class TempStack extends CardStack
{
    TempStack() 
    {
        super(1); 
        autoMoveSrc = true; 
    }
    public int canAccept(CardStack src)
    {   
        return (src.canRemove() && isEmpty()) ? 1 : 0; 
    }
}
