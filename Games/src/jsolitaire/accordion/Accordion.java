package jsolitaire.accordion;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.VarStack;

/** Implementation of Accordion solitaire. <BR>
 * Released under the GNU public license. Copyright (C) 1999-2001 <BR>
 * @author Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net$* @version 1.00 (2000/3/15)
 * @version 1.00 (2001/05/30)
 */
public class Accordion extends Solitaire
{
    Checkbox moveCBs[];
    Choice resChoice = new Choice();
    int resI = 2;

/** gameInit sets up the stacks array. Only one stack is present in this game
 * which is the accordion stack */
    public void gameInit()
    {
        stacks    = new CardStack[1];
        helpFile  = "accordion-help.html";

        moveCBs = new Checkbox[6];
        moveCBs[0] = new Checkbox("next card to left");
        for(int cbI = 1; cbI < 6; ++cbI)
            moveCBs[cbI] = new Checkbox(cbI + 1 +  " cards to left");
        moveCBs[0].setState(true);
        moveCBs[2].setState(true);

        stacks[0] = new AccStack(14, moveCBs);
        int ns[]  = { 52 };
        nsCards   = ns; 
    }
        
/** Layout is extremely simple. First row has button panel, second has cards. */
    public void layoutScreen()
    {
        int vGap = 10, dXs[] = { 11, 14, 17 }, horX[] = { 640, 800, 1024 };
        place(buttonPanel, (horX[resI] - bpWidth) / 2, vGap);
        ((AccStack)stacks[0]).setdX(dXs[resI]);
        layoutRow(8, bpHeight + 2 * vGap, 0, 1, 0); 
    }
    public void startGame(boolean shuffle)
    {
        ((AccStack)stacks[0]).resetdXs();
        super.startGame(shuffle);
    }
    public boolean gameWon()
    {   
        return ((AccStack)stacks[0]).nUnder(0) == 51; 
    }
    public boolean gameLost()
    {
        return ((AccStack)stacks[0]).deadEnd();
    }
    public String statistics()
    {
        int nCov = 1;
        for(int i = 0; i < 51; ++i)
        {   if(((AccStack)stacks[0]).buried(i))
                ++nCov;
        }
        return 52 - nCov + "/" + nCov + " ";
    }
    
    public String optsTitle() { return "Accordion Options"; }
    public Panel optsPanel() 
    { 
        Panel p = new Panel(), cbPanel = new Panel(), optsPanel = new Panel();
        p.setLayout(new BorderLayout()); 
        optsPanel.setLayout(new GridLayout(0, 1));
        cbPanel.setLayout(new GridLayout(3, 2));
        
        for(int i = 0; i < 6; ++i)
            cbPanel.add(moveCBs[i]);
        p.add(cbPanel);
        
        resChoice.addItem("640 x 480");
        resChoice.addItem("800 x 600");
        resChoice.addItem("1024 x 768 (or more)");
        optsPanel.add(dlgRow("Screen Size: ", resChoice));

        addCommonOpts(optsPanel, 1);
        p.add("North", new Label("Allowed Moves", Label.CENTER));
        p.add("South", optsPanel);
        p.add("Center", cbPanel);
        return p; 
    }
    public void setOptions()
    {
        super.setOptions();
        int i = resChoice.getSelectedIndex();
        if(resI != i)
        {   resI = i;
            layoutScreen();
        }
    }
    public void resetOptions()
    {
        super.resetOptions();
        resChoice.select(resI);
    }
}

class AccStack extends VarStack
{
    int defaultdX = Card.SIDE_WIDTH;
    Checkbox[] moveCBs; 
    AccStack(int ddx, Checkbox[] cbs)
    {   
        super(52);
        setSelection(CardStack.SELECT_CLICKED);
        repaintAll = true;
        autoMoveSrc = false; 
        defaultdX = ddx; 
        moveCBs = cbs;
        resetdXs();
        
    }
    public int maxWidth() { return Card.SIDE_WIDTH * 51 + Card.WIDTH; }

    void setdX(int dx) 
    {
        for(int i = 0; i < 52; ++i)
        {   if(dXs[i] > 5)
                dXs[i] = dx; 
        }
        defaultdX = dx; 
    }
    void resetdXs()
    {
        for(int i = 0; i < 52; ++i)
            dXs[i] = defaultdX;
    }

/** If card in bottom of subpile clicked on, use top of subpile  */
    public int clickedI(Point p)
    {
        int clkI = super.clickedI(p);
        while(clkI > 0 && dXs[nCards() - clkI - 1] <= 5)
            --clkI;
        return clkI;
    }
/** Try to shift card to location clicked on */
    public int insertI(Point p) { return clickedI(p); }

/*  Return number of cards under a subpile */
    int nUnder(int pileI)
    {
        int n = 0;
        for(int i = nCards() - pileI - 1; i > 0 && dXs[i - 1] <= 5; --i)
            ++n;
        return n;
    }
/*  Do not remove cards from stack */ 
    public void remove(int remI, int nRemove) { }
/*  Return number of subpiles between src and dest */
    private int nSubPiles(int srcI, int destI)
    {
        int n = 0;
        if(destI < srcI)
            return -1;
        
        for( ; srcI != destI; srcI += nUnder(srcI) + 1)
            ++n;
        return n;
    }
    public boolean buried(int i) {  return dXs[i] < 5; }

/* Return true if there are no possible moves in the stack */   
    public boolean deadEnd()
    {
        for(int srcI = 51; srcI >= 0; --srcI)
        {   if(!buried(52 - srcI - 1))
            {   for(int destI = srcI, gap = 0; gap < 5 ; ++gap)
                {   destI += (nUnder(destI) + 1);
                    if(destI >= 52)
                        break; 
                    if(canMove(srcI, destI) > 0)
                        return false;
                }
            }
        }
        return true; 
    }

    public int canAccept(CardStack src, Point clickLoc)
    {
        return canMove(selectI, clickedI(clickLoc));
    }

    public int canMove(int srcI, int destI)
    {
        if(destI == srcI)
            return 0; 

        int n = nSubPiles(srcI, destI) - 1;

        Card upper = cardFromTop(srcI), lower = cardFromTop(destI);
        if(upper.suit() != lower.suit() && upper.rank() != lower.rank())
            return 0; 
        else
            return moveCBs[n].getState() ? (nUnder(srcI)  + 1) : 0;
    }
    
    public void shift(int srcI, int destI, int nMove)
    {
//      System.out.println(srcI + " " + destI + " " + nMove);
        if(srcI > destI)
        {   dXs[destI] = 4;
            super.shift(srcI, destI + 1, nMove);
        }
        else 
        {   dXs[srcI + nMove - 1] = defaultdX;
            super.shift(srcI + nMove, destI - nMove + 1, nMove);
        }
    }
}

