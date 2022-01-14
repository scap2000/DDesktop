package jsolitaire.blackhole;

import java.awt.Choice;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Black Hole solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2001 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.01 (2000/1/30)
*/

public class BlackHole extends Solitaire
{
    int kRank1 = Card.ACE, kRank2 = Card.QUEEN, aRank1 = 1, aRank2 = Card.KING;
    CardStack pile;
    Choice kingChoice = new Choice(), aceChoice = new Choice();
    int kingChoiceI = 1, aceChoiceI = 1;

/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0      <TD> pile  <TD> 1 ( Ace of spades ) <TD> 1
 * <TR><TD> 1-17   <TD> <TD> 3 <TD> 3
 * </TABLE> */
    public void gameInit()
    {
        stacks   = new CardStack[18];
        helpFile = "blackhole-help.html";
        int[] ns = { 1, 3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, 3, 3, 3, 3, 3};
        nsCards  = ns;
        
        pile = stacks[0] = new Pile(52); 
        pile.setNTurned(1);
        for(int i = 1; i < 18; ++i)
        {   stacks[i] = new BHStack();
            stacks[i].setOrder(0, CardStack.NO_ACCEPT);
        }
    }

/** The top part of the screen will contain the 7 stacks of five cards, 
 *  the bottom row will have the reserve, pile and button panel */ 
    public void layoutScreen()
    {
        setOverlaps(1, 17, Card.SIDE_WIDTH, 0); 
        int hGap = 10, vGap = 8, rowY = 8;
        int hInc = Card.WIDTH + 2 * Card.SIDE_WIDTH + hGap;
        int vInc = Card.HEIGHT + vGap;
        int bpX  = hGap + (9 * hInc)/4 + (hInc - bpWidth) / 2; 
        
     // First row has button panel in middle column 
        layoutRow(hGap + hInc/4, vGap, 1, 2, hGap);
        place(buttonPanel, bpX, vGap);
        layoutRow(hGap + (hInc * 13)/4, vGap, 3, 2, hGap);

     // Leave space in middle of rows 2 & 3 for black hole stack    
        layoutRow(hGap, vGap + vInc, 5, 2, hGap);
        layoutRow(hGap + (hInc * 7)/2, vGap + vInc, 7, 2, hGap);
        
        layoutRow(hGap, vGap + vInc * 2, 9, 2, hGap);
        layoutRow(hGap + (hInc * 7)/2, vGap + vInc * 2, 11, 2, hGap);
        
     // Bottom row has remaining 5 cards    
        layoutRow(hGap + hInc/4, vGap + vInc * 3, 13, 5, hGap);
        
        layoutRow(bpX, vGap + (vInc * 3)/2, 0, 1, 0);
    }
/** Do not shuffle the first card so it will always go in the central pile */
    public void shuffle (int[] array, int shufI, int nShuf)
    {
        super.shuffle(array, 1, 51);
    }
    
/** Attempt to move the card at the top of the clicked stack to the pile */
    public void select(CardStack clicked, int x, int y)
    {
        int nAccept = pileAccept(clicked);
        showMsg("");

        if(nAccept == 0)
            showMsg("Invalid move");
        else if(nAccept == 1)
            moveCard(pile, clicked, 1, null);
    }

/** See if the pile can accept the card at the top of the clicked stack */ 
    public int pileAccept(CardStack clicked)
    {
        if(clicked == pile || clicked.isEmpty())
            return -1;
        else
        {   int pileRank = pile.top().rank(), srcRank = clicked.top().rank();
            if(pileRank == Card.KING)
                return (srcRank == kRank1 || srcRank == kRank2) ? 1 : 0;
            else if(pileRank == Card.ACE)
                return (srcRank == aRank1 || srcRank == aRank2) ? 1 : 0;
            return (srcRank == pileRank + 1 || srcRank == pileRank - 1) ? 1 :0;
        }
    }
/** Game will be won if all cards are in the pile */ 
    public boolean gameWon() { return pile.nCards() == DECK_SIZE; }
    public boolean gameLost() 
    {
        for(int i = 1; i <= 17; ++i)
        {   if(pileAccept(stacks[i]) != 0)
                return false;
        }
        return true;
    }
    public String statistics() 
    {
        return 52 - pile.nCards() + "/" + pile.nCards() + " ";
    }
    public String optsTitle() { return "Black Hole Options"; }
     
/** The only options determine what ranks will be accpeted when a king
 *  and 2 is at the top of the pile */ 
    public Panel optsPanel() 
    {
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        addCommonOpts(p, TIMER_OPT);
        return p; 
    }
}

/** Class for 17 stacks surrounding black hole */ 
class BHStack extends SpreadStack
{
    BHStack() 
    { 
        super(3); 
    }
    public void paint(Graphics g)
    {
        if(nCards() != 0)  // don't draw outlines for empty stacks
            super.paint(g);
    }
}
