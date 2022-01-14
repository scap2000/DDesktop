package jsolitaire.golf;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Pile;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Golf solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.07 (2000/1/30) 
*/
public class Golf extends Solitaire
{
    int kRank1 = -1, kRank2 = -1, aRank1 = 1, aRank2 = -1; 
    CardStack reserve, pile;
    Choice kingChoice = new Choice(), aceChoice = new Choice();
    int kingChoiceI = 0, aceChoiceI = 1; 

/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0-6     <TD> golf stack <TD> 7 <TD> 0 
 * <TR><TD> 7       <TD> reserve <TD> 16 <TD> 15
 * <TR><TD> 5       <TD> Pile <TD> 1 <TD> 1
 * </TABLE> */
    public void gameInit()
    {
        stacks  = new CardStack[9];
        helpFile = "golf-help.html";
        int[] ns = { 5, 5, 5, 5, 5, 5, 5, 16, 1 };
        nsCards = ns;
        int[] nst = { 0, 0, 0, 0, 0, 0, 0, 15, 1 };
        nsTurned = nst;
        
        for(int i = 0; i < 7; ++i)
        {   stacks[i] = new SpreadStack(5);
            stacks[i].setOrder(0, CardStack.NO_ACCEPT);
        }
        reserve = stacks[7] = new SpreadStack(16); 
        pile    = stacks[8] = new Pile(52); 
    }

/** The top part of the screen will contain the 7 stacks of five cards, 
 *  the bottom row will have the reserve, pile and button panel */ 
    public void layoutScreen()
    {
        setOverlaps(0, 7, 0, Card.TOP_HEIGHT);
        layoutRow(10, 10, 0, 7, 8);
        int y2 = 25 + stacks[0].maxHeight(); 

        setOverlaps(7, 1, 14, 0);
        int x = layoutRow(10, y2, 7, 2, 16);
        place(buttonPanel, x, y2);
    }

/** Attempt to move the card at the top of the clicked stack to the pile */
    public void select(CardStack clicked, int x, int y)
    {
        int nAccept = pileAccept(clicked);
        showMsg("");

        if(nAccept == 0)
            showMsg("Invalid move");
        else if(nAccept == 1)
        {   moveCard(pile, clicked, 1, null);
            if(reserve.isEmpty() && !gameWon())
            {   int i; 
                for(i = 0; i < 7 && pileAccept(stacks[i]) < 1; ++i)
                    ;
                if(i == 7)
                    endGame();
            }
        }
    }
/** Right click will move next card from reserve to pile */ 
    public void rightClick(CardStack stk) { select(reserve, 0, 0); }

/** See if the pile can accept the card at the top of the clicked stack */ 
    public int pileAccept(CardStack clicked)
    {
        if(clicked == pile || clicked.isEmpty())
            return -1;
        else if(clicked != reserve)
        {   int pileRank = pile.top().rank(), srcRank = clicked.top().rank();
            if(pileRank == Card.KING)
                return (srcRank == kRank1 || srcRank == kRank2) ? 1 : 0;
            else if(pileRank == Card.ACE)
                return (srcRank == aRank1 || srcRank == aRank2) ? 1 : 0;
            return (srcRank == pileRank + 1 || srcRank == pileRank - 1) ? 1 :0;
        }
        return 1;
    }
/** Game will be won if all cards are in the pile */ 
    public boolean gameWon() { return pile.nCards() == DECK_SIZE; }
    public String statistics()
    {   
        int nStk = 52 - pile.nCards() - reserve.nCards();
        return reserve.nCards() + "/" + nStk + "/" + pile.nCards() + " ";
    }
    public String optsTitle() { return "Golf Options"; }
     
/** The only options determine what ranks will be accpeted when a king
 *  and 2 is at the top of the pile */ 
    public Panel optsPanel() 
    {
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        kingChoice.addItem("Nothing");
        kingChoice.addItem("Q only");
        kingChoice.addItem("Q and A");
        aceChoice.addItem("Nothing");
        aceChoice.addItem("2 only");
        aceChoice.addItem("2 and K");
        
        p.add(dlgRow("Kings accept:", kingChoice));
        p.add(dlgRow("Aces accept:", aceChoice));
        addCommonOpts(p, TIMER_OPT);
        return p; 
    }
    public void setOptions()
    {
        kRank1 = kRank2 = aRank1 = aRank2 = -1;
        kingChoiceI = kingChoice.getSelectedIndex();
        if(kingChoiceI >= 1)
            kRank1 = Card.QUEEN;
        if(kingChoiceI == 2)
            kRank2 = Card.ACE;

        aceChoiceI = aceChoice.getSelectedIndex();
        if(aceChoiceI >= 1)
            aRank1 = 1;
        if(aceChoiceI == 2)
            aRank2 = Card.KING;
    }
    public void resetOptions()
    {
        super.resetOptions();
        kingChoice.select(kingChoiceI);
        aceChoice.select(aceChoiceI);
    }
}
