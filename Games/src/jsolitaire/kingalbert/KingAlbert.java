package jsolitaire.kingalbert;

import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of King Albert Solitaire
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class KingAlbert extends Solitaire
{
    final int BUILDS_I = 5; 
/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL #
 *  <TR><TD> 0-3 <TD> SuitStack <TD> 0
 *  <TR><TD> 4   <TD> Reserve <TD> 7
 *  <TR><TD> 5-13 <TD> Building Stacks <TD> 1-9 </TABLE> */ 
    public void gameInit()
    {   
        stacks   = new CardStack[14];
        helpFile = "kingalbert-help.html";

        int stackI;
        SUITS_I = 0; 
        for(stackI = 0; stackI < 4; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI));

        SpreadStack fan = new SpreadStack(7);
        fan.setOrder(0, CardStack.NO_ACCEPT);
        fan.setSelection(CardStack.SELECT_CLICKED);
        stacks[4] = fan; 

        for(stackI = BUILDS_I; stackI < stacks.length; ++stackI)
            stacks[stackI] = new AlbertStack();
        
        int[] ns = { 0, 0, 0, 0, 7, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        nsCards  = ns;
    }

/** Layout will consist of two rows, first will have the suit stacks,
  *   button panel and reserve, second will contain the building stacks */ 
    public void layoutScreen()
    {
        int gap = 10, row1y = 5, row2y = row1y + Card.HEIGHT + gap;

        int x = layoutRow(5, row1y, 0, 4, gap);
        x = place(buttonPanel, x, row1y).x + gap;
    
        layoutRow(x, row1y, 4, 1, gap);
        setOverlaps(4, 1, Card.SIDE_WIDTH, 0); 
        
        layoutRow(5, row2y, 5, 9, gap); 
        setOverlaps(5, 9, 0, Card.TOP_HEIGHT);
    }

/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        for(int i = 1; i <= 9; ++i) // Reset to original values
            nsCards[4 + i] = i;

        for(int i = 0, stackI = stacks.length - 1; i < 4 * startRankI;
            stackI = (stackI == BUILDS_I) ? stacks.length - 1: stackI - 1)
        {   if(nsCards[stackI] != 0)
            {   --nsCards[stackI];
                ++i;
            }
        }
    }
 // Must have an empty space for every card moved except the top card,
 //  if moving to an empty stack, top must have empty space also
    public int maxNMove(CardStack stk)
    {
        int max = 1;
        for(int i = BUILDS_I; i < stacks.length; ++i)
        {   if(stacks[i].isEmpty() && stk != stacks[i])
                ++max;
        }
        return max;
    }
    public boolean isBuildingStackI(int stackI) 
    {
        return stackI >= BUILDS_I;
    }
    
 // Right clicking will move cards to empty stacks 
    public void rightClick(CardStack clicked) { removeSelected(clicked); }

    public String optsTitle() { return "King Albert Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        p.add(dlgRow("Starting rank:", startRankChoice));
        addCommonOpts(p);
        return p; 
    }
}

class AlbertStack extends SpreadStack
{
    AlbertStack()
    {
        super(22);
        selection = SELECT_RANGE; 
        lookBelow = true;
        doubleClickSelect = true; 
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
