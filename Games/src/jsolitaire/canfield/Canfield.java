package jsolitaire.canfield;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Event;
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

/** Implementation of Canfield solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/

public class Canfield extends Solitaire
{
    Talon talon;
    Pile pile;
    CardStack reserve;

    private int nDraw = 3, NSUITS = 4, BUILDS_I = 6, NBUILDS = 4;
    boolean deckListed = false; 
    CardList deckList;
    Choice nDrawChoice = new Choice(), nRedealsChoice = new Choice();
    Checkbox deckCB  = new Checkbox("Display deck contents");

/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0-3     <TD> SuitStack <TD> 0 <TD> 0
 * <TR><TD> 4       <TD> Pile <TD> 34 - nDraw <TD> nDraw
 * <TR><TD> 5       <TD> Talon <TD> nDraw <TD> 0 
 * <TR><TD> 6-9     <TD> CfBuild <TD> 1 <TD> 0
 * <TR><TD> 10      <TD> Reserve <TD> 13 <TD> 12 </TABLE> */ 
    public void gameInit()
    {
        stacks = new CardStack[11];

        helpFile = "canfield-help.html";
        
        int stackI = SUITS_I = 0;
        for(; stackI < SUITS_I + NSUITS; ++stackI)
            stacks[stackI] = new SuitStack(null);
        
        stacks[4] = pile = new Pile(40);
        stacks[5] = talon = new Talon(3);
        
        for(stackI = 6; stackI < 10; ++stackI)
            stacks[stackI] = new CfBuild();
        stacks[10] = reserve = new SpreadStack(13);
        deckList = new CardList(talon, pile, 34);

        int[] nsC = { 0, 0, 0, 0, DECK_SIZE - nDraw - 18, nDraw, 
            1, 1, 1, 1, 13 };
        int[] nsT = { 0, 0, 0, 0, 0, nDraw, 0, 0, 0, 0, 12};
        nsCards  = nsC;
        nsTurned = nsT;
    }

/** The layout will have two row. The first row will contain the reserve,
 * button panel and suit stacks. The second will have the pile/talon and
 * building stacks */ 
    public void layoutScreen()
    {
        int row1y = 10,row2y = 20 + Card.HEIGHT, row3y = 30 + 2 * Card.HEIGHT;
        
     // reserve & button panel  
        setOverlaps(10, 1, 6, 0);
        int col2x = layoutRow(10, row1y, 10, 1, 16) + bpWidth + 16;
        place(buttonPanel, col2x - bpWidth - 16, row1y);
        
        layoutRow(10, row2y, 4, 2, 40); // pile & talon

        layoutRow(col2x, 10, 0, 4, 10); // suit stacks 
        setOverlaps(6, 4, 0, Card.TOP_HEIGHT); 
        int listX = layoutRow(col2x, row2y, 6, 4, 10); // build stacks

        place(deckList, listX, row1y);
        if(!deckListed)
            remove(deckList); 
    }

    public void startGame(boolean shuffle)
    {
        super.startGame(shuffle);
        deckList.setPrintTop(false); 
        if(deckListed)
            deckList.repaint();
    }

/** After dealing the base rank of the suit stacks needs to be set to the 
 *  rank of the last card in the deck. Put that card in its appropriate
 *  suit stack */
    public void deal()
    {
        super.deal();
        Card baseCard = new Card(deck[51]);
        baseRank = baseCard.rank(); 
        for(int i = 0; i < 4; ++i)
        {   ((SuitStack)stacks[SUITS_I + i]).setBase(new Card(baseRank, i));
            ((CfBuild)stacks[6 + i]).setTopRank(baseRank);
        }
        stacks[SUITS_I + baseCard.suit()].add(baseCard);
    }

/** If the pile is clicked, move nCards from the pile to the talon. 
 *  Otherwise select the clicked stack */ 
    public void select(CardStack clicked, int x, int y)
    {
        if(clicked.canRemove())
        {   clicked.select(1);
            selected = clicked;
        }
        else if(clicked instanceof Pile)
        {   if(pile.isEmpty())
                deckList.setPrintTop(true);
            talonAdd(talon, pile, nDraw);
        }
    }
/** Draw another set of cards if right click occurs */
    public void rightClick(CardStack stk)
    { 
        if(pile.isEmpty())
            deckList.setPrintTop(true);
        talonAdd(talon, pile, nDraw);
    }
    
/** Cannot move cards from the talon to an empty building stack if
  * the reserve is not empty */ 
    public void tryMove(CardStack clicked, Event evt, int nAccept)
    {
        if(   (clicked instanceof CfBuild) && clicked.isEmpty() 
           && (selected instanceof Talon) && !reserve.isEmpty())
            showMsg("Invalid move");
            
        else    
        {   moveCard(clicked, selected, nAccept, null);
            if(!(selected instanceof SuitStack))
                autoMove();
        }
    }

/** Update the deck list if the source or destination is the talon */ 
    public void moveCard(CardStack dest, CardStack src,
                         int nMove, Button btn)
    {   if((src == talon || dest == talon) && deckListed)
            deckList.repaint(); 
        super.moveCard(dest, src, nMove, btn);
    }
    
/** autoMove will move cards from the reserve to empty building stacks */ 
    public boolean autoMove()
    {
        boolean moved = false; 
        if(!makeAutoMoves)
            return false; 
        do 
        {   moved = super.autoMove();
            if(!reserve.isEmpty())
            {   for(int i = 6; i < 10; ++i)
                {   if(stacks[i].isEmpty())
                    {   moveCard(stacks[i], reserve, 1, null);
                        moved = true;
                    }
                }
            }
        }while(moved);
        return moved; 
    }
    public boolean isBuildingStackI(int stackI) 
    { 
        return (BUILDS_I <= stackI && stackI < BUILDS_I + NBUILDS);
    }
    public String statistics() 
    {
        int nBuild = (52 - talon.nCards() - pile.nCards() - nSuitStacks()
                      - reserve.nCards());
        return ((talon.nCards() + pile.nCards()) + "/" + reserve.nCards() 
                + "/" + nBuild + "/" + nSuitStacks() + " ");
    }
    public String optsTitle() { return "Canfield Options"; }
     
/** Canfield has 4 options, # drawn, # of redeals, deck listing and
 *  automatic movement */ 
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        nRedealsChoice.addItem("Unlimited");
        for(int i = 0; i <= 4; ++i)
            nRedealsChoice.addItem(Integer.toString(i));
        
        for(int i = 1; i <= 5 ; ++i)
            nDrawChoice.addItem(Integer.toString(i));

        p.add(dlgRow("Number of cards drawn:", nDrawChoice));
        p.add(dlgRow("Number of redeals:", nRedealsChoice));
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

     // Update initial # of cards in pile & talon if nDraw is changed   
        if(oldNDraw != nDraw)
        {   ((Talon)talon).setNDraw(nDraw);
            nsCards[4] = DECK_SIZE - nDraw - 18;
            nsCards[5] = nDraw;
            nsTurned[5] = nDraw;
            deckList.setListWidth(nDraw);
            if(oldNDraw > 3 || nDraw > 3 || deckListed)
                layoutScreen(); 
        }
        if(deckCB.getState() != deckListed)
        {   if(!deckListed)
                add(deckList);
            else
                remove(deckList);
            deckListed = deckCB.getState();
        }
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
        deckCB.setState(deckListed); 
    }
}

/** Canfield building stack */ 
class CfBuild extends SpreadStack
{
/** An empty stack can accept a stack which is not another building stack*/
    public int canStart(CardStack source)
    {
        return !(source instanceof CfBuild) ? 1 : 0;
    }

/** Moving part of a stack to another is not allowed. Only whole stacks
 *  can be merged */ 
    public int canAdd(CardStack source)
    {
        if(source instanceof CfBuild)
        {   if(orderedPair(top(), source.cardAt(0)))
                return source.nCards();
        }
        else
            return orderedPair(top(), source.top()) ? 1 : 0;

        return 0; 
    }
}
