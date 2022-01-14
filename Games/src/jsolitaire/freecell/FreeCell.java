package jsolitaire.freecell;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of FreeCell solitaire, allows for variable number of 
 *  free cells (between 1-8) <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class FreeCell extends Solitaire
{
    int nFreeCells = 4, MAX_FREE_CELLS = 8;
    final int BUILDS_I = 12;
    Choice nfcChoice = new Choice();

/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL # 
 *  <TR><TD> 0-7 <TD> TempStack <TD> 0 
 *  <TR><TD> 8-11 <TD> SuitStack <TD> 0
 *  <TR><TD> 12-15 <TD> FreeCellBuild <TD> 7
 *  <TR><TD> 16-19 <TD> FreeCellBuild <TD> 6 </TABLE> */
    public void gameInit()
    {
        stacks  = new CardStack[MAX_FREE_CELLS + 12];
        helpFile = "freecell-help.html";
        int[] ns = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            7, 7, 7, 7, 6, 6, 6, 6};
        nsCards = ns;

        int stackI = 0;
        for(; stackI < MAX_FREE_CELLS; ++stackI)
            stacks[stackI] = new TempStack();

        SUITS_I = MAX_FREE_CELLS; 
        for(; stackI < BUILDS_I; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI - SUITS_I));

        for(; stackI < stacks.length; ++stackI)
            stacks[stackI] = new FreeCellBuild();
    }

/** layoutScreen arranges the display in two rows. The first row has
 *  the first 4 free cells, the button panel and the suit stacks. The
 *  second row has the suit stacks. The interval between stacks is set
 *  so the rows are equal length. Additional free cells are placed on
 *  the right side of the layout */
    public void layoutScreen()
    {
        int row1Gap = 4, row2Gap = 16;
        int row1y = 10, row2y = (2 * row1y) + Card.HEIGHT;
        layoutRow(10, row1y, 0, Math.min(nFreeCells, 4), row1Gap);
        int x = 10 + (row1Gap + Card.WIDTH) * 4;

        x = place(buttonPanel, x, row1y + (Card.HEIGHT - Card.HEIGHT) / 2).x;
        layoutRow(x + 4, row1y, 8, 4, row1Gap);

        setOverlaps(12, 8, 0, Card.TOP_HEIGHT);
        x = layoutRow(10, row2y, 12, 8, row2Gap);
        
        layoutCol(x, row1y, 4, Math.max(0, nFreeCells - 4), row1y);
    }

/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        int rightN = (13 - startRankI) / 2, leftN = rightN;
        if((startRankI & 1) == 0)
            ++leftN;
        
        for(int stackI = 0; stackI < 4; ++stackI)
        {   nsCards[stackI + BUILDS_I] = leftN;
            nsCards[stackI + BUILDS_I + 4] = rightN;
        }
    }
    
 // Must have an empty space for every card moved except the top card,
 //  if moving to an empty stack, top must have emtpy space also
    public int maxNMove(CardStack stk)
    {
        int max = 1;
        for(int i = 0; i < stacks.length; ++i)
        {   if(   (i < nFreeCells || i >= BUILDS_I) 
               && stacks[i].isEmpty() && stk != stacks[i])
                ++max;
        }
        return max;
    }

 // Right clicking will send the selected card(s) to empty stacks   
    public void rightClick(CardStack clicked) { removeSelected(clicked); }
    
 // Make sure that right clicking does not sent cards to free cells 
 //   which are not displayed   
    public boolean isBuildingStackI(int stackI)
    {
        return (stackI < nFreeCells || stackI >= BUILDS_I);
    }
    
    public String optsTitle() { return "FreeCell Options"; }
     
/** FreeCell options are the number of free cells and moving cards to the
 *  suit stacks automatically */ 
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        for(int i = 1; i <= MAX_FREE_CELLS; ++i)
            nfcChoice.addItem(Integer.toString(i));

        p.add(dlgRow("Number of free cells:", nfcChoice));
        p.add(dlgRow("Starting rank:", startRankChoice));
        addCommonOpts(p);
        
        return p;   
    }
    
/** If number of freecells decreased, make sure occupied free cell is not
 *  removed. Compress free cells if necessary before removal. */
    public void setOptions() 
    { 
        int newN = nfcChoice.getSelectedIndex() + 1; 
        int lastFullI = 0, nFull = 0; 
        super.setOptions(); 
        if(newN == nFreeCells)
            return; 

     // Determine last occupied free cell   
        for(int i = 0; i < MAX_FREE_CELLS; ++i)
        {   if(!stacks[i].isEmpty())
            {   ++nFull;
                lastFullI = i;
            }
        }
        if(lastFullI >= newN)
        {// Report error if desired number smaller than occupied number 
            if(newN < nFull)
            {   resetOptions();
                showMsg("Unable to remove occupied free cell");
                return;
            }
         // Compress free cells if necessary 
            for(int srcI = 0, destI = 0; srcI < MAX_FREE_CELLS; ++srcI)
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
        nfcChoice.select(nFreeCells - 1); 
    }
}

/** Free cell building stacks accept alternating colors in descending
 *  rank */ 
class FreeCellBuild extends SpreadStack
{
    FreeCellBuild() 
    {   selection = SELECT_RANGE; 
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
