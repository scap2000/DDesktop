package jsolitaire.calculation;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import jsolitaire.shared.Card;
import jsolitaire.shared.CardStack;
import jsolitaire.shared.Solitaire;
import jsolitaire.shared.SpreadStack;

/** Implementation of Calculation solitaire. <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 <BR>
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.00 (2000/3/15)
*/
public class Calculation extends Solitaire
{
    CardStack reserve;
    protected Choice nCardsChoice = new Choice();
    int nCardsStart = 1;  

/** gameInit sets up the stacks array. The array will be initialized to 
 * be: <BR> <TABLE> 
 * <TR><TH> INDICES <TH> TYPE <TH> INITIAL # <TH> # TURNED 
 * <TR><TD> 0       <TD> SpreadStack (reserve) <TD> 48 <TD> 47
 * <TR><TD> 1-5     <TD> CalcStack <TD> 1 <TD> 0
 * <TR><TD> 6-9     <TD> CalcWaste <TD> 0 <TD> 0
 * </TABLE> */
    public void gameInit()
    {
        stacks  = new CardStack[9];
        helpFile = "calculation-help.html";
        
        stacks[0] = reserve = new SpreadStack(52);
        reserve.setOrder(1, CardStack.NO_ACCEPT);

        for(int stkI = 1; stkI < 5; ++stkI)
        {   stacks[stkI] = new CalcStack();
            stacks[stkI].setOrder(stkI, CardStack.ANY_SUIT);
        }
        for(int stkI = 5; stkI < stacks.length; ++stkI)
            stacks[stkI] = new CalcWaste();
    }
/** The first column contains the button panel and the reserve, the 
  * second the destination stacks, the third the waste piles */ 
    public void layoutScreen()
    {
        int hGap = 10, vGap = 10; 
        int col1X = hGap;
        int row1Y = 10, row2Y = place(buttonPanel, col1X, row1Y).y + vGap;
        setOverlaps(0, 1, 0, 4);
        place(reserve, col1X + (bpWidth - Card.WIDTH)/2, row2Y);

        int col2X = col1X + bpWidth + hGap;
        layoutCol(col2X, row1Y, 1, 4, vGap);

        setOverlaps(5, 4, Card.SIDE_WIDTH, 0);
        int col3X = col2X + stacks[1].maxWidth() + hGap;
        layoutCol(col3X, row1Y, 5, 4, vGap);
    }
    
 // Move entire contents of deck to reserve, then move cards to 
 //  destination stacks which meet criteria 
    public void deal()
    {
        resetStacks();
        
        reserve.add(deck, 0, DECK_SIZE);

        for(int resI = 0; reserve.nCards() > DECK_SIZE - 4 * nCardsStart;
            resI = (resI < reserve.nCards()) ? resI + 1 : 0)
        {   Card card = reserve.cardAt(resI);

            for(int destI = 1; destI < 5; ++destI)
            {   CalcStack dest = (CalcStack)stacks[destI];
                
                if(dest.nCards() < nCardsStart && dest.canAccept(card))
                {   dest.add(card);
                    reserve.remove(resI, 1);
                    --resI;
                    break;
                }
            }
        }
        reserve.setNTurned(reserve.nCards() - 1);
        for(int stackI = 0; stackI < stacks.length; ++stackI)
            stacks[stackI].repaint();
    }

 // Right-clicking will move a card to a destination stack if possible  
    public void rightClick(CardStack clicked) 
    {
        if(clicked != null && clicked.canRemove())
        {   for(int stkI = 1; stkI < 5; ++stkI)
            {   if(stacks[stkI].canAccept(clicked) > 0)
                {   moveCard(stacks[stkI], clicked, 1, null);
                    return; 
                }
            }
            showMsg("No destination found for card");
        }
    }
    
 // Game will be won if all cards are in the destination stacks */
    public boolean gameWon() 
    {   for(int stkI = 1; stkI < 5; ++stkI)
        {   if(stacks[stkI].nCards() < 13)
                return false;
        }
        return true; 
    }
    public String optsTitle() { return "Calculation Options";  }
    
// The only options determine what ranks will be accpeted when a king
//  and 2 is at the top of the pile */ 
    public Panel optsPanel() 
    {
        Panel p = new Panel();
        p.setLayout(new GridLayout(0, 1));
        for(int i = 0; i < 8; ++i)
            nCardsChoice.addItem(Integer.toString(i));

        p.add(dlgRow("Initial # of cards:", nCardsChoice));
        addCommonOpts(p, TIMER_OPT);
        return p; 
    }
    public void setOptions()
    {
        super.setOptions();
        nCardsStart = nCardsChoice.getSelectedIndex();
    }
    public void resetOptions()
    {
        super.resetOptions();
        nCardsChoice.select(nCardsStart);
    }
}

class CalcStack extends SpreadStack
{
    static String[] rankText = { "A", "2", "3", "4", "5", "6", "7", "8", "9", 
        "10", "J", "Q", "K"
    };
    static Font sideFont = Card.findFont("Helvetica", Font.PLAIN, 
                                         (Card.HEIGHT / 7) - 2);
    static FontMetrics fm = (new Label()).getFontMetrics(sideFont);
    static int sideGap    = 3; // gap between text and sides
    static int sideWidth  = fm.stringWidth("10") + 2 * sideGap;
    static int ascent     = fm.getAscent() + 2;
    static Color sideColor = new Color(0, 176, 0);
    static int strWidths[] = getStrWidths();
    
    public CalcStack() 
    { 
        super(13);  
        setTopRank(Card.KING);
        repaintAll = true; 
    }
    public int width() { return 2 * sideWidth + Card.WIDTH; }
    public int maxWidth() { return width(); }
    public int leftX() { return sideWidth; }
    private static int[] getStrWidths() 
    {
        strWidths = new int[13];
        for(int i = 0; i < 13; ++i)
            strWidths[i] = fm.stringWidth(rankText[i]);
        return strWidths;
    }
    
    public boolean canAccept(Card card)
    {
        return (   (isEmpty() && card.rank() == rankDiff - 1)
                || (!isEmpty() && orderedPair(top(), card)));
    }
    public int canAccept(CardStack src) 
    {
        return (src.canRemove() && canAccept(src.top())) ? 1 : 0; 
    }
    public boolean canRemove() { return false; }
    public void paint(Graphics g)  
    {
     // Print nothing along sides if stack full 
        if(nCards() < 13)
        {   g.setColor(sideColor);
            g.fillRoundRect(0, 0, width(), Card.HEIGHT, 10, 10);
         // Highlight rank of next card accepted by stack   
            g.setColor(Color.yellow);
            int boxX = sideGap, boxRow = nCards + 1;
            if(nCards > 5)
            {   boxX   += Card.WIDTH + sideWidth;
                boxRow -= 6; 
            }
            int y = Card.HEIGHT - sideGap;
            g.fillRect(boxX, y - boxRow * ascent + 2,
                       sideWidth - 2 * sideGap, ascent);

         // Now print ranks in order of acceptance along sides
            g.setColor(Color.black);
            g.setFont(sideFont);
            for(int rowI = 1; rowI < 7; ++rowI, y -= ascent)
            {   centerText(g, (rowI * rankDiff - 1) % 13, 0, y);
                centerText(g, ((rowI + 6) * rankDiff - 1) % 13,
                           sideWidth + Card.WIDTH, y); 
            }
            centerText(g, 12, sideWidth + Card.WIDTH, y); // print K on right 
            g.setFont(Card.getFont()); 
        }
        super.paint(g);
    }
    private void centerText(Graphics g, int rank, int x, int y)
    {
        g.drawString(rankText[rank], x + (sideWidth - strWidths[rank])/2, y);
    }
}

// Waste stacks in calculation accepts all cards regardless of suit or rank
class CalcWaste extends SpreadStack
{
    public CalcWaste() 
    {
        super(52); 
        setTopRank(-1);
    }
 // Cannot transfer cards between waste piles 
    public int canAccept(CardStack src) 
    { 
        return !(src instanceof CalcWaste) ? 1 : 0; 
    }
}
