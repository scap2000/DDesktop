package jsolitaire.spiderette;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Spiderette solitaire. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2000/2/14) 
*/

public class Spiderette extends Solitaire
{
    private final int NSUITS = 4, BUILDS_I = 2, NBUILDS = 7;
    Pile pile, discard;
    Choice moveOrderChoice = new Choice();
    int moveChoiceI = 0; 
    Choice nCardsChoice = new Choice();
    int nCardsI = 0;
    
/** gameInit sets up the stack array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0       <TD> Pile <TD> 24 <TD> 1
 * <TR><TD> 1       <TD> Pile <TD> 0 <TD> 0
 * <TR><TD> 2-8     <TD> SpideretteBuild <TD> 1-7 <TD> 0-6 </TABLE> */
    public void gameInit()
    {
        stacks = new CardStack[9];

        helpFile = "spiderette-help.html";

        stacks[0] = pile    = new Pile(38);
        stacks[1] = discard = new Discard(52);

        int stackI = 2;
        for(; stackI < stacks.length; ++stackI)
            stacks[stackI] = new SpideretteBuild();
    }

 /* Set the screen up in two rows. The first contains the pile, button
  *  panel and discard pile. The second contains the building stacks. */ 
    public void layoutScreen()
    {
        int row1y   = 10, row2y = (row1y * 3) + Card.HEIGHT, hGap = 10;
        int midColX = (hGap * 4 + Card.WIDTH * 3); 
        
     // lay out first row so that button panel aligns with middle column
        place(pile, midColX - pile.maxWidth() - hGap, row1y);
        int x = (place(buttonPanel, midColX, row1y)).x + hGap;
        place(discard, x, row1y);

     // lay out second row containing building stacks   
        setOverlaps(BUILDS_I, 7, 0, Card.TOP_HEIGHT, 0, Card.DOWN_TOP_HEIGHT);
        layoutRow(10, row2y, BUILDS_I, 7, hGap);
    }

/** Override deal so the number of cards in each stack can be changed
  * if desired */ 
    public void deal()
    {
        nCardsI = nCardsChoice.getSelectedIndex(); 
        int[][] nsCs = 
        {   { 24, 0, 1, 2, 3, 4, 5, 6, 7 }, { 38, 0, 2, 2, 2, 2, 2, 2, 2},
            { 31, 0, 3, 3, 3, 3, 3, 3, 3 }, { 24, 0, 4, 4, 4, 4, 4, 4, 4}
        };
        int[][] nsTs = 
        {   { 0, 0, 0, 1, 2, 3, 4, 5, 6 },  { 0, 0, 1, 1, 1, 1, 1, 1, 1},
            { 0, 0, 2, 2, 2, 2, 2, 2, 2 },  { 0, 0, 3, 3, 3, 3, 3, 3, 3}
        };
        nsCards = nsCs[nCardsI];
        nsTurned = nsTs[nCardsI];
        super.deal(); 
    }
    
/** If the pile is clicked, move cards from it the building stack */
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked.canRemove())
            super.select(clicked, x, y); 

        else if(clicked == pile && !pile.isEmpty())
        {   int stkI;
            for(stkI = BUILDS_I; stkI < stacks.length; ++stkI)
            {   if(stacks[stkI].isEmpty())
                {   showMsg("All stacks must be filled before moving cards"
                            + " from the pile.");
                    return;
                }
            }
            stkI = BUILDS_I;    
            for(; stkI < stacks.length && !pile.isEmpty(); ++stkI)
                moveCard(stacks[stkI], pile, 1, null);

            makeUndoGroup(stkI - BUILDS_I);
            autoMove();
        }
    }
/** Game is won when all cards in the deck have been moved to the discard
  * pile */ 
    public boolean gameWon() { return discard.nCards() == DECK_SIZE; }

    public String statistics() 
    {
        int nStk = 52 - pile.nCards() - discard.nCards();
        return pile.nCards() + "/" + nStk + "/" + discard.nCards() + " ";
    }
    
/** Destination for all automatic moves in Spider is discard pile and
 *   * not individual suit stacks as in most games */
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
 
    public String optsTitle() { return "Spiderette Options"; }
     
    public Panel optsPanel() 
    {
        Panel p = new Panel(); 
        p.setLayout(new GridLayout(0, 1));

        moveOrderChoice.addItem("same suit");
        moveOrderChoice.addItem("same color");
        moveOrderChoice.addItem("any suit");
        p.add(dlgRow("Can Move:", moveOrderChoice));
        moveOrderChoice.select(moveChoiceI);

        nCardsChoice.addItem("1-7");
        nCardsChoice.addItem("2");
        nCardsChoice.addItem("3"); 
        nCardsChoice.addItem("4"); 
        p.add(dlgRow("# of cards in stack:", nCardsChoice));
        nCardsChoice.select(0);

        addCommonOpts(p); 
        return p; 
    }

    public void setOptions()
    {
        super.setOptions();
        moveChoiceI = moveOrderChoice.getSelectedIndex();
        int ord = moveChoiceI == 0 ? CardStack.SAME_SUIT :CardStack.SAME_COLOR;
        if(moveChoiceI == 2)
            ord = CardStack.ANY_SUIT;   
        SpideretteBuild.setMoveOrder(ord);
    }
    
    public void resetOptions() 
    { 
        super.resetOptions();
        moveOrderChoice.select(moveChoiceI);
        nCardsChoice.select(nCardsI); 
    }
}

/** Alternating color stack for Klondike */ 
class SpideretteBuild extends SpreadStack
{
    static int moveOrder = CardStack.SAME_SUIT;
    SpideretteBuild()
    {   super(52);
        setOrder(-1, CardStack.ANY_SUIT);
        selection = SELECT_RANGE; 
        lookBelow = true;
        doubleClickSelect = true;       
    }
    public static void setMoveOrder(int order)  { moveOrder = order; }
    
/** nOrdered returns number of cards with the same suit at the stack top. */
    public int nOrdered()
    {   
        suitOrder = moveOrder;
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
        if(source.nOrdered() == 13)
        {   int suit = source.cardFromTop(0).suit();
            for(int i = 1; i < 13; ++i)
            {   if(source.cardFromTop(i).suit() != suit)
                    return 0;
            }
            return 13; 
        }
        else 
            return 0; 
    }
 // Override adding function so that cards are not added in reverse order
    public void add(Card [] src, int srcI, int nAdd)
    {
        System.arraycopy(src, srcI, cards, nCards, nAdd);
        nCards += nAdd;
    }
}
