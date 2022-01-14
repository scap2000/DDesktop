package jsolitaire.castle;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;

/** Implementation of Beleagured Castle 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class Castle extends Solitaire
{
    int LEFT_WALLS_I = 4, RIGHT_WALLS_I = 8;
/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL # 
 *  <TR><TD> 0-3 <TD> SuitStack <TD> 1
 *  <TR><TD> 4-7 <TD> Left CastleStack <TD> 6
 *  <TR><TD> 8-11 <TD> Right CastleStack <TD> 6 </TABLE> */
    public void gameInit()
    {
        stacks   = new CardStack[12];
        helpFile = "castle-help.html";
        nsCards  = new int[12];

        int stackI = SUITS_I = 0;
        for(; stackI < 4; ++stackI)
        {   stacks[stackI]     = new SuitStack(new Card(stackI));
            stacks[LEFT_WALLS_I + stackI] = new LeftWallStack();
            stacks[RIGHT_WALLS_I + stackI] = new RightWallStack();
        }
        startRankChoice.select(1);
        startRankI = 1;
        reallot(); 
    }
    
/** Layout screen so that suit stacks appear between the stacks representing
 *  the "walls" of the castle */ 
    public void layoutScreen()
    {
        int row1y = 35, gap = 8;
        Button[] bs = { newGame, restart, undo, redo, options, help };
        buttonPanel = makeButtonPanel(bs, new GridLayout(1, 6, 10, 10),
                                      bpWidth * 6 - 10, 20);
        place(buttonPanel, 88, 5);
        layoutCol(5, row1y, 4, 4, gap); 
        layoutCol(290, row1y, 0, 4, gap); 
        layoutCol(360, row1y, 8, 4, gap); 
        setOverlaps(4, 8, Card.SIDE_WIDTH, 0);
    }
    
/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        int rightN = (13 - startRankI) / 2, leftN = rightN;
        if((startRankI & 1) == 0)
            ++leftN;
        
        for(int stackI = 0; stackI < 4; ++stackI)
        {   nsCards[LEFT_WALLS_I + stackI] = leftN;
            nsCards[RIGHT_WALLS_I + stackI] = rightN;
        }
    }

 // Must have an empty space for every card moved except the top card,
 //  if moving to an empty stack, top must have empty space also
    public int maxNMove(CardStack stk)
    {
        int max = 1;
        for(int i = LEFT_WALLS_I; i < stacks.length; ++i)
        {   if(stacks[i].isEmpty() && stk != stacks[i])
                ++max;
        }
        return max;
    }

 // Right clicking will move cards to empty stacks 
    public void rightClick(CardStack clicked) { removeSelected(clicked); }
    
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
    
    public String optsTitle() { return "Castle Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        p.add(dlgRow("Starting rank:", startRankChoice));
        addCommonOpts(p);
        return p; 
    }
}
/** The stacks on the right side of the layout */
class RightWallStack extends SpreadStack
{
    RightWallStack()
    {   
        super(20);
        setOrder(-1, CardStack.ANY_SUIT);
        selection = SELECT_RANGE; 
        lookBelow = true;
        doubleClickSelect = true; 
    }   
    public int maxWidth() { return 275; } 
/** If a card below the top of the stack is clicked on, select from that
 *  card to the top of the stack if the card is in an ordered sequence */
    public void select(int n, int selI) 
    { 
        super.select(Math.min(n, nOrdered()), selI); 
    }

/** If stack empty, accept all selected cards */    
    public int canStart(CardStack source) { return source.nSelected(); }
}

/** The stacks on the left side of the layout, will be drawn so that 
 *  they are right-justified */ 
class LeftWallStack extends RightWallStack
{
    LeftWallStack()
    {   super();
        repaintAll = true;
    }
    public int leftX() { return this.size().width - width(); }
}
