package jsolitaire.labellelucie;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import java.util.Stack;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SuitStack;
import jsolitaire.shared.VarStack;

/** Implementation of La Belle Lucie Solitaire
* Released under the GNU public license. Copyright (C) 1999-2001 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.01 (2001/7/19)
*/

public class LaBelleLucie extends Solitaire
{
    Button redeal = makeButton("Redeal");
    Label dealNum = new Label();
    Choice nRedealsChoice = new Choice();
    Choice nMercisChoice = new Choice();
    Checkbox emptyCB  = new Checkbox("Allow moving kings to empty spaces");
    int nMerci = 0, maxMerci = 1;
    
/** gameInit initializes the stacks array in the following manner: <BR> 
 *  <TABLE> 
 *  <TR><TH> INDICES <TH> TYPE <TH> INITIAL #
 *  <TR><TD> 0-3 <TD> SuitStack <TD> 0
 *  <TR><TD> 4-20 <TD> LucieBuild <TD> 3
 *  <TR><TD> 21 <TD> LucieBuild <TD> 1 </TABLE> */ 
    public void gameInit()
    {   
        stacks   = new CardStack[22];
        helpFile = "labellelucie-help.html";
        maxRedeals = 2; 
        
        int stackI;
        SUITS_I = 0; 
        for(stackI = 0; stackI < 4; ++stackI)
            stacks[stackI] = new SuitStack(new Card(stackI));

     // Maximum heights for 2 rows of fans  
        int h2 = 2 * Card.HEIGHT + 8;
        int h1 = h2 - 20;
        for(int col = 1; col <= 9; ++col)
        {   stacks[3 + col] = new LucieBuild(h1);
            stacks[12 + col] = new LucieBuild(h2);
        }
        startRankChoice.select(0);
        startRankI = 0;
        nsCards  = new int[22];
        reallot(); 
    }

 // The leftmost column will contain the suit stacks. The next 5 columns
 //   contain the building stacks. The first position in the building stacks
 //   grid has the button panel, the last is empty. 
    public void layoutScreen()
    {
        int hGap = 6, vGap = 8, rowY = 8;
        int stackWidth = Card.WIDTH + 2 * Card.SIDE_WIDTH;
        int col2X = 10 + Card.WIDTH + hGap + 5; 
        
        setOverlaps(4, 18, 0, Card.TOP_HEIGHT); 

        layoutCol(10, rowY, 0, 4, vGap); // suit stacks
     // Use horizontal button panel 
        Button[] bs = { newGame, restart, redeal, undo, options, help };
        buttonPanel = makeButtonPanel(bs, new GridLayout(1, 7, 10, 10),
                                      bpWidth * 7 + 20, 20);
        Panel undoRedo = new Panel();
        undoRedo.setLayout(new GridLayout(1, 2, 0, 0));
        undoRedo.add(undo);
        undoRedo.add(redo);
        buttonPanel.add(dealNum, 3);
        buttonPanel.add(undoRedo, 4);
        place(buttonPanel, col2X, vGap);
        
        layoutRow(col2X, vGap + buttonPanel.size().height + 5, 4, 9, hGap);
        layoutRow(col2X, 2 * Card.HEIGHT + 3 * vGap, 13, 9, hGap);
    }

/** Change the number of cards originally in the stacks when the starting
 *   rank of the suit stacks is changed */ 
    public void reallot()
    {
        int fanCards = 52 -  (4 * startRankI), stkI = 4;
        for( ; fanCards > 0 ; fanCards -= 3, ++stkI)
            nsCards[stkI] = Math.min(3, fanCards);

        for( ; stkI < 22; ++stkI)
            nsCards[stkI] = 0; 
    }

    public void startGame(boolean shuffle)
    {
        nMerci = 0;
        super.startGame(shuffle);
        setLabel();
        redeal.enable();
    }
    
    /** Handle events for move & finish buttons */ 
    public boolean action(Event evt, Object arg)
    {
        if(!super.action(evt, arg))
        {   Object source = evt.target;

            if(source == redeal)
                shuffleFans();
            else
                return false;
        }
        return true; 
    }

 /* Reshuffle cards remaining in fans */
    private void shuffleFans()
    {   
        int[] fanCards = new int[52];
        int nFan = 0;
        for(int suit = 0; suit < 4; ++suit)
        {   for(int rank = 0; rank < 13; ++rank)
            {   if(rank >= stacks[suit].nCards())
                    fanCards[nFan++] = rank * 4 + suit;
            }
        }
        shuffle(fanCards, 0, nFan);

        for(int fanI = 0; fanI < 18 ; ++fanI)
        {   int stkI = fanI + 4; 
            stacks[stkI].clear();
            if(fanI * 3 < nFan)
                stacks[stkI].add(fanCards, fanI * 3, 
                                     Math.min(3, nFan - fanI * 3));
            stacks[stkI].repaint();
        }
        undo.disable();
        redo.disable();
        moveLog.removeAllElements(); 
        undoLog.removeAllElements();

        autoMove();
        
        if(++nRedeals == maxRedeals && maxRedeals != 5)
        {   LucieBuild.setSelectAny(true);
            redeal.disable();
        }
        setLabel();
    }
 // This will move cards to the suit stacks whenever possible 
    public boolean noBuilds(Card card) { return true; }

    private void setLabel()
    {
        if(maxRedeals < 5 && nRedeals == maxRedeals && nMerci < maxMerci)
        {   
            String l; 
            if(maxMerci - nMerci == 1)
                l = "1 Merci";
            else
                l = (maxMerci - nMerci) + " Mercis";
            dealNum.setText(l);
        }
        else
        {   String l = "Deal " + (nRedeals + 1);
            if(maxRedeals < 5)
                l += " of " + (maxRedeals + 1);
            dealNum.setText(l);
        }
    }
    
    public boolean mouseDown(Event evt, int x, int y)
    {
        LucieBuild.setSelectAny(nRedeals == maxRedeals && nMerci < maxMerci);
           
        return super.mouseDown(evt, x, y);
    }

    public void moveCard(CardStack dest, CardStack src, int nMove,
                         int insI, int delI, Button btn)
    {
        super.moveCard(dest, src, nMove, insI, delI, btn);
        if((LucieBuild.madeMerci() || delI > 0) && btn != undo)
        {   ++nMerci;
            moveLog.setElementAt(new Boolean(true), moveLog.size() - 2);
        }
        setLabel();
    }

 // Cards below top of stacks cannot be moved in automove. Set selectAny to
 //   false to prevent them from being chosen regardless of the # of mercis 
    public boolean autoMove() 
    {
        LucieBuild.setSelectAny(false);
        return super.autoMove();
    }

    public boolean gameLost()
    {
        if(nRedeals == maxRedeals && nMerci == maxMerci)
        {   for(int srcI = 4; srcI < 22; ++srcI)
            {   for(int destI = 0; destI < 22; ++destI)
                {   if(   srcI != destI 
                       && stacks[destI].canAccept(stacks[srcI]) > 0)
                        return false; 
                }
            }
            return true;
        }
        return false;
    }
    
    public void recordMove(Stack stack, Object move) 
    { 
        stack.push(new Boolean(false));
        super.recordMove(stack, move);
    }

    public void undoExtra(Stack stack)
    {
        if(((Boolean)stack.pop()).booleanValue())
        {   --nMerci;
            setLabel();
        }
    }

    public void redoExtra(Stack stack)
    {
        stack.pop();
    }

    public void copyLogEntry(Stack src, Stack dest)
    {
        dest.push(src.elementAt(src.size() - 2));
        dest.push(src.peek());
    }
    
    public String statistics()
    {
        int nSuit = 0;
        for(int i = 0; i < 4; ++i)
            nSuit += stacks[i].nCards();
        return 52 - nSuit + "/" + nSuit + " "; 
    }
    public String optsTitle() { return "La Belle Lucie Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        for(int i = 0; i <= 4; ++i)
            nRedealsChoice.addItem(Integer.toString(i));
        nRedealsChoice.addItem("Unlimited");
        p.add(dlgRow("Number of redeals:", nRedealsChoice));
        for(int i = 1; i <= 5; ++i)
            nMercisChoice.addItem(Integer.toString(i));
        p.add(dlgRow("Number of mercis:", nMercisChoice));
            
        p.add(emptyCB);
        p.add(dlgRow("Starting rank:", startRankChoice));
        addCommonOpts(p); 
        return p; 
    }
    public void setOptions()
    {
        super.setOptions();
        maxRedeals = nRedealsChoice.getSelectedIndex();
        maxMerci = nMercisChoice.getSelectedIndex() + 1;
        boolean doRepaint = (emptyCB.getState() != LucieBuild.getEmptyAccept());
        LucieBuild.setEmptyAccept(emptyCB.getState());

     // If empty acceptance has changed, draw or remove borders around empty
     //   stacks    
        if(doRepaint)
        {   for(int stkI = 4; stkI < 22; ++stkI)
            {   if(stacks[stkI].nCards() == 0)
                    stacks[stkI].repaint();
            }
        }
        setLabel();
    }
    public void resetOptions()
    {
        super.resetOptions();
        nRedealsChoice.select(maxRedeals);
        nMercisChoice.select(maxMerci - 1);
        emptyCB.setState(LucieBuild.getEmptyAccept()); 
    }
}

/** Vertical stacks representing fans. Bottom 2 cards will always have height 
 * of Card.TOP_HEIGHT. Other buried cards will have Card.TOP_HEIGHT visible
 * also if possible, otherwise remaining pixels will be distributed evenly */ 

class LucieBuild extends VarStack
{
    int maxheight;
    static int nMerci = -1; 
    static int maxMerci = 1;
    static boolean emptyAccept = false; 
    static boolean selectAny = false;  
    static boolean lastMoveMerci = false;
    
    LucieBuild(int max)
    { 
        super(15);
        setOrder(-1, CardStack.SAME_SUIT);
        autoMoveSrc = true;
        repaintAll = true; 
        maxheight = max;
    }

 // First three card tops get normal height, remaining height is alloted 
 //  equally for remaining tops     
    public int diffY(int i)
    {   
        if(i < 3)
            return Card.TOP_HEIGHT;
        else
            return Math.min(Card.TOP_HEIGHT, 
                              (maxheight - 3 * Card.TOP_HEIGHT - Card.HEIGHT) 
                            / Math.max(1, nCards() - 4));
    }
        
    static boolean getSelectAny() { return selectAny; }
    static void setSelectAny(boolean s) { selectAny = s; }

    static boolean madeMerci() 
    {
        boolean didMerci = lastMoveMerci;
        lastMoveMerci = false;
        return didMerci;
    }
    
    static void setEmptyAccept(boolean b) { emptyAccept = b; }
    static boolean getEmptyAccept() { return emptyAccept; }
    public int maxHeight() { return maxheight; }
    public int canStart(CardStack src) 
    {
        if(!emptyAccept) // empty stacks normally unusable
            return 0; 
        else return (src.top().rank() == Card.KING) ? 1 : 0; 
    } 

 // In merci, any card can be selected otherwise only top can be selected 
    public int getSelection()
    {
        return (selectAny ? CardStack.SELECT_CLICKED : CardStack.SELECT_TOP);
    }
    public int canAdd(CardStack src)
    {

        if(super.canAdd(src) > 0)
            return 1;
        else if(selectAny && src.chosen() != top())
        {   lastMoveMerci = true;
            return 1;
        }
        
        return 0;
    }
    public void paint(Graphics g)
    {
        if(nCards == 0 && emptyAccept)
            Card.drawOutline(g, 0, 0);
        else
            super.paint(g);
    }
}
