package jsolitaire.flowergarden;

import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of Flower Garden Solitaire
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class FlowerGarden extends Solitaire
{
    private final int BOUQUET_I = 4, GARDEN_START_I = 5, NGARDEN_COLS = 6;
/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL #
 *  <TR><TD> 0-3 <TD> SuitStack <TD> 0
 *  <TR><TD> 4   <TD> Bouquet <TD> 16
 *  <TR><TD> 5-10 <TD> Garden Columns <TD> 7 </TABLE> */ 
    public void gameInit()
    {   
        stacks   = new CardStack[11];
        helpFile = "flowergarden-help.html";

        int stackI;
        SUITS_I = 0; 
        for(stackI = 0; stackI < 4; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI));

        SpreadStack bouquet = new SpreadStack(16);
        bouquet.setOrder(0, CardStack.NO_ACCEPT);
        bouquet.setSelection(CardStack.SELECT_CLICKED);
        stacks[BOUQUET_I] = bouquet;

        for(stackI = GARDEN_START_I; stackI < stacks.length; ++stackI)
            stacks[stackI] = new GardenStack();
        
        int[] ns = { 0, 0, 0, 0, 16, 6, 6, 6, 6, 6, 6 };
        nsCards  = ns;
    }

/** Layout will consist of two rows, first will have the suit stacks,
  *   button panel and reserve, second will contain the building stacks */ 
    public void layoutScreen()
    {
        int hGap = 12, vGap = 10;
        int row1y = 5, row2y = row1y + Card.HEIGHT + vGap;
                
     // First row will have the button panel & bouquet  
        int col1End = place(buttonPanel, hGap, row1y).x;
        layoutRow(col1End + hGap, row1y, BOUQUET_I, 1, 0);
        setOverlaps(BOUQUET_I, 1, Card.SIDE_WIDTH, 0);
        
     // Bottom have will have the black suit stacks, garden columns and
     //   red suit stacks   
        layoutCol(col1End - Card.WIDTH, row2y, 0, 2, vGap); // black suits
        int endGardenX =layoutRow(col1End + hGap, row2y, 
                                  GARDEN_START_I, NGARDEN_COLS, hGap);
        setOverlaps(GARDEN_START_I, NGARDEN_COLS, 0, Card.TOP_HEIGHT);
        layoutCol(endGardenX, row2y, 2, 2, vGap); // red suits
    }

/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        for(int i = GARDEN_START_I; i < stacks.length; ++i) // Reset
            nsCards[i] = 6;
        
        int lastColI = GARDEN_START_I + NGARDEN_COLS - 1;
        for(int i = 0, stackI = lastColI; i < 4 * startRankI;
            stackI = (stackI == GARDEN_START_I) ? lastColI : stackI - 1)
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
        for(int i = GARDEN_START_I; i < stacks.length; ++i)
        {   if(stacks[i].isEmpty() && stk != stacks[i])
                ++max;
        }
        return max;
    }
    public boolean isBuildingStackI(int stackI) 
    {
        return stackI >= GARDEN_START_I;
    }
    
 // Right clicking will move cards to empty stacks 
    public void rightClick(CardStack clicked) { removeSelected(clicked); }

    public String optsTitle() { return "Flower Garden Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        p.add(dlgRow("Starting rank:", startRankChoice));
        addCommonOpts(p);
        return p; 
    }
}

class GardenStack extends SpreadStack
{
    GardenStack()
    {
        super(20);
        setOrder(-1, CardStack.ANY_SUIT);
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

    
    
