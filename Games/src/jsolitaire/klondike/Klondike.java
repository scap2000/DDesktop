package jsolitaire.klondike;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardList;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;
import jsolitaire.shared.SuitStack;
import jsolitaire.shared.Talon;

/** Implementation of Klondike solitaire. <BR> 
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.08 (2000/3/15)
*/

public class Klondike extends Solitaire
{
    private int nDraw = 3, NSUITS = 4, emptyChoiceI;
    final int BUILDS_I = 6;
    Pile pile;
    Talon talon;

 // Variables for options 
    boolean deckListed = false; 
    CardList deckList;
    Choice nDrawChoice = new Choice(), nRedealsChoice = new Choice();
    Choice emptyChoice = new Choice();
    Checkbox deckCB  = new Checkbox("Display deck contents");
    
/** gameInit sets up the stack array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0       <TD> Pile <TD> 24 - nDraw <TD> 0
 * <TR><TD> 1       <TD> Talon <TD> nDraw <TD> nDraw
 * <TR><TD> 2-5     <TD> SuitStack <TD> 0 <TD> 0 
 * <TR><TD> 6-12    <TD> Klondike Build <TD> 1-7 <TD> 0-6 </TABLE> */
    public void gameInit()
    {
        stacks = new CardStack[13];

        helpFile = "klondike-help.html";

        int[] nsC = { DECK_SIZE - nDraw - 28, nDraw, 0, 0, 0, 0, 
            1, 2, 3, 4, 5, 6, 7 };
        int[] nsT = { 0, nDraw, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6 };
        nsCards  = nsC;
        nsTurned = nsT;
        
        stacks[0] = pile  = new Pile(27);
        stacks[1] = talon = new Talon(3);

        int stackI = SUITS_I = 2;
        for(; stackI < SUITS_I + NSUITS; ++stackI)
        {   stacks[stackI] = new SuitStack(new Card(stackI - SUITS_I));
            ((SuitStack)stacks[stackI]).allowRemoval(true);
        }
        for(; stackI < stacks.length; ++stackI)
            stacks[stackI] = new KlondikeBuild();
        deckList = new CardList(stacks[1], stacks[0], 24);
    }

 /* Set the screen up in two rows. The first contains the pile/talon,
  * button panel and suit stacks. The second contains the building stacks.
  * The gaps between stacks is set so the rows will have equal length */ 
    public void layoutScreen()
    {
        int row1y = 10, row2y = row1y + pile.maxHeight() + 5;
        int x = layoutRow(10, row1y, 0, 2, 5);

        x = (place(buttonPanel, x + 2, row1y)).x;
        layoutRow(x + 7, row1y, 2, 4, 4);
        setOverlaps(6, 7, 0, Card.TOP_HEIGHT, 0, Card.DOWN_TOP_HEIGHT);
        x = layoutRow(10, row2y, 6, 7, 18);

        place(deckList, x, row2y); 
        
        if(!deckListed)
            remove(deckList); 
    }

/** When a game is started, do not list the cards which have not been seen */
    public void startGame(boolean shuffle)
    {
        super.startGame(shuffle);
        deckList.setPrintTop(false); 
        if(deckListed)
            deckList.repaint();
    }

/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        for(int i = 0; i <= 6; ++i) // Reset to original values
        {   nsCards[BUILDS_I + i] = i + 1;
            nsTurned[BUILDS_I + i] = i;
        }
            
        for(int i = 0, stkI = stacks.length - 1; i < 4 * startRankI;
            stkI = (stkI == BUILDS_I) ? stacks.length - 1 : stkI - 1)
        {   if(nsCards[stkI] != 0)
            {   --nsCards[stkI];
                ++i;
                if(nsTurned[stkI] > 0)
                    --nsTurned[stkI];
            }
        }
    }
    
/** If the pile is clicked, move cards from it to the talon. Otherwise 
 *  select the clicked stack. */ 
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked.canRemove())
        {   clicked.select(1);
            selected = clicked;
        }
        else if(clicked instanceof Pile)
        {   if(stacks[0].isEmpty())
                deckList.setPrintTop(true);
            talonAdd(talon, pile, nDraw);
        }
    }
/** Draw another set of cards if right click occurs */
    public void rightClick(CardStack stk)
    { 
        if(stacks[0].isEmpty())
            deckList.setPrintTop(true);
        talonAdd(talon, pile, nDraw);
    }
    
/** If deck listed, update the deck list approriately to reflect the move */
    public void moveCard(CardStack dest, CardStack src,
                         int nMove, Button btn)
    {   if((src == talon || dest == talon) && deckListed)
            deckList.repaint(); 
        super.moveCard(dest, src, nMove, btn);
    }
    public String statistics() 
    {
        int nBuild = 52 - talon.nCards() - pile.nCards() - nSuitStacks();
        return ((talon.nCards() + pile.nCards()) + "/" +
                nBuild + "/" + nSuitStacks() + " ");
    }

    public boolean isBuildingStackI(int stackI) { return stackI >= BUILDS_I; }

    public String optsTitle() { return "Klondike Options"; }
     
/** Klondike has 5 settings, # of cards drawn, # of redeals, ranks 
 *  accepted in empty spaces, deck display and automatic movement to suit
 *  stacks */ 
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        nRedealsChoice.addItem("Unlimited");
        for(int i = 0; i <= 4; ++i)
            nRedealsChoice.addItem(Integer.toString(i));
        
        for(int i = 1; i <= 5 ; ++i)
            nDrawChoice.addItem(Integer.toString(i));

        emptyChoice.addItem("K only");
        emptyChoice.addItem("K, Q or J");
        emptyChoice.addItem("Any card"); 
        p.add(dlgRow("Number of cards drawn:", nDrawChoice));
        p.add(dlgRow("Number of redeals:", nRedealsChoice));
        p.add(dlgRow("Starting rank:", startRankChoice));
        p.add(dlgRow("Empty spaces accept:", emptyChoice));
        p.add(deckCB);
        addCommonOpts(p);
        resetOptions(); 
        return p;   
    }
    
    public void setOptions()
    {
        super.setOptions();
        maxRedeals = nRedealsChoice.getSelectedIndex(); 
        int oldNDraw = nDraw;
        nDraw = nDrawChoice.getSelectedIndex() + 1;

    /*  When the number of cards drawn is changed, nsCards and nsTurned
     *    need to be changed appropriately. */ 
        if(oldNDraw != nDraw)
        {   talon.setNDraw(nDraw);
            nsCards[0] = DECK_SIZE - nDraw - 28;
            nsCards[1] = nDraw;
            nsTurned[1] = nDraw;
            deckList.setListWidth(nDraw);
            if(oldNDraw > 3 || nDraw > 3 || deckListed)
                layoutScreen(); 
        }
        emptyChoiceI = emptyChoice.getSelectedIndex();
        int[] emptyMins = { Card.KING, Card.JACK, Card.ACE };
        KlondikeBuild.setEmptyMin(emptyMins[emptyChoiceI]);
        
     // Remove or add deck list if checkbox changed     
        if(deckCB.getState() != deckListed)
        {   if(!deckListed)
                add(deckList);
            else
                remove(deckList);
            deckListed = deckCB.getState();
        }

     // Drawing one card will result in the deck list being too long 
        if(nDraw == 1 && deckListed)
        {   deckListed = false;
            deckCB.setState(false);
            remove(deckList);
            showMsg("Cannot list cards if one card drawn"); 
        }
    }
    public void resetOptions()
    {
        super.resetOptions();
        nDrawChoice.select(nDraw - 1);
        nRedealsChoice.select(maxRedeals);
        emptyChoice.select(emptyChoiceI); 
        deckCB.setState(deckListed); 
    }
}

/** Alternating color stack for Klondike */ 
class KlondikeBuild extends SpreadStack
{
    static int emptyMin = Card.KING;
    KlondikeBuild()
    {   super(20);
        lookBelow = true;
    }
    
    public static void setEmptyMin(int n) { emptyMin = n;   }

/** If stack is empty, card must have rank of at least emptyMin */ 
    public int canStart(CardStack source)
    {
        if(!(source instanceof KlondikeBuild))
            return (source.top().rank() >= emptyMin) ? 1 : 0;
        else if(source.firstOrdered().rank() >= emptyMin)
            return source.nOrdered();

        return 0; 
    }
}
