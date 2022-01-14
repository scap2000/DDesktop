
package jsolitaire.shared;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;

import java.util.Random;
import java.util.Stack;

import org.digitall.lib.components.basic.BasicPanel;

/** This is the abstract base class for all solitaire games. <BR> 
 * The following methods must be overriden: gameInit, LayoutScreen <BR> 
 * The following methods must be overriden if the game has options:
 *   optsTitle, setOptions, resetOptions, optsPanel <BR>
 * The following methods might need to be overridden:
 *   startGame, shuffle, action, tryMove, rightClick, select, gameWon, 
 *   autoMove and optAction <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.09 (2000/07/19)
*/

public abstract class Solitaire extends BasicPanel
{
/** Array containing all card stacks used in the game */
    protected CardStack[] stacks;
/** Reference to currently selected stack, null if no selection */ 
    protected CardStack selected;
    protected final int DECK_SIZE = 52;
/** Random array of integers reflecting initial deck state */ 
    protected int[] deck = new int[DECK_SIZE];
/** Number of decks of cards used in game */ 
    protected int NDECKS = 1; 
    
/** Array containing the number of cards which initially go in each stack */
    protected int[] nsCards;
/** Array containing the number of facedown cards in each stack */ 
    protected int[] nsTurned;  
    
/** Record of what cards have been moved between what stacks */ 
    protected Stack moveLog;

/** Recording of the undone moves which can be reversed with redo */
    protected Stack undoLog;
    
/** Starting rank for suit stacks, Ace is default */ 
    protected int baseRank = Card.ACE; 
    protected int nRedeals, PrevNDeck, maxRedeals, nCheats;
    protected String errMsg;
/** Name of file continaing on-line help for game */ 
    protected String helpFile;
    
    protected Button restart, newGame, undo, help, options, redo;
    protected Button[] buttons = { newGame, restart, undo, options, help };
    protected Panel buttonPanel;

/** Dialog window for game options */ 
    protected OptsDlg optsDlg;
    
/** Checkbox indicating if automatic moves to suit stacks should be made */ 
    protected Checkbox autoMoveCB = new Checkbox("Automatic card moving");
    protected boolean makeAutoMoves = true;

/** Checkbox for displaying timer at bottom of screen */    
    protected Checkbox timerCB = new Checkbox("Display Timer"); 
    protected boolean displayTimer = true;
    
 // Components for setting random seed
    protected long randomSeed, seed;
    protected boolean userSeed   = false; // if set, user sets seed
    protected Checkbox seedCB  = new Checkbox("Seed #:");
    protected TextField seedTF = new TextField(8);

    protected Choice startRankChoice = new Choice();
    protected int startRankI;

/** If true # ordered cards in statistics are counted from top, if false
  * ordered cards counted anywhere in face up cards */
    protected boolean nOrderedTop = true;
    
    public final static int AUTOMOVE_OPT = 2, TIMER_OPT = 1;
    public final static int START_RANK_OPT = 4;
    
/** Height of button panel */
    protected final int bpHeight = Card.HEIGHT;
/** Width of button panel */ 
    protected final int bpWidth = 80;
    protected Insets insets;
/** Index of first suit stack in stacks array, -1 if no suit stacks */ 
    protected int SUITS_I = -1;
    protected int gamesPlayed = 0, gamesWon = 0; 

/** Writes elapsed time & message in status bar */ 
    protected StatusBar statusBar;
    
/** Do general initialization and call game-specific initialization function*/
    public void init()
    {
        setBackground(new Color(0, 0xCC, 0));
        setLayout(null); 
        moveLog    = new Stack();
        undoLog    = new Stack();
        restart    = makeButton("Restart");
        newGame    = makeButton("New Game");
        undo       = makeButton("Undo");
        redo       = makeButton("Redo"); 
        help       = makeButton("Help"); 
        options    = makeButton("Options"); 
        Button[] btns = { newGame, restart, options, undo, help };
        buttons     = btns;
        buttonPanel = makeButtonPanel(buttons, new GridLayout(5, 1, 0, 0),
                                      bpWidth, Card.HEIGHT);
        Panel undoRedo = new Panel();
        undoRedo.setLayout(new GridLayout(1, 2, 0, 0));
        undoRedo.add(undo);
        undoRedo.add(redo);
        buttonPanel.add(undoRedo, 2); 
        
        String[] rankNames = {"None", "A", "2", "3", "4", "5"};
        for(int i = 0; i < rankNames.length; ++i)
            startRankChoice.addItem(rankNames[i]);

        statusBar = new StatusBar(/*new getAppletContext()*/);
        Thread timerThread = new Thread(statusBar);
        timerThread.start(); 
        
        gameInit(); 
        makeScreen(); 
        deck       = new int[DECK_SIZE * NDECKS];
        optsDlg    = new OptsDlg(this);
        resetOptions();
        startGame(true);
    }
    public void stop() { statusBar.stopTimer(); }
    public void destroy() { statusBar.stopTimer(); }
    
/** Do game-specific initialization. Must initialize stacks array and fill
 *  nsCards and nsTurned with the appropriate values */
    public abstract void gameInit(); 

/** Place the game's card stacks in their appropriate location */ 
    public abstract void layoutScreen();

/** Set up the game screen, calling the game-specific layout function */
    public void makeScreen()
    {
        insets = getInsets(); 
        removeAll();
        for(int i = 0; i < stacks.length; ++i)
        {   stacks[i].fixSize();
            stacks[i].setFont(Card.getFont());
        }
        layoutScreen();
    }
    
/** Make a light gray button
 *  @param text button text
 *  @return the button */ 
    public Button makeButton(String text)
    {
        Button button = new Button(text);
        button.setBackground(Color.lightGray);
        return button;
    }

/** Place a component in the applet panel. Expand the panel if there is
 *  not enough room. 
 *  @param c component to be placed
 *  @param x x coordinate of left of component
 *  @param y y coordinate of top of component
 *  @return coordinates of lower right of component */ 
    public Point place(Component c, int x, int y) 
    {
        add(c); 
        c.setLocation(x + insets.left, y + insets.top);
        Dimension appletSize = this.getSize(); 
        Point p; 
        if(!(c instanceof CardStack))
        {   Dimension d = c.getSize(); 
            p = new Point(x + d.width, y + d.height);
        }
        else
        {   CardStack s = (CardStack)c; 
            p = new Point(x + s.width(), y + s.height());
        }
        if(p.x + 5 > appletSize.width)
        {   this.setSize(p.x + 5, appletSize.height);
            appletSize = this.getSize();
        }
        if(p.y + 5 > appletSize.height)
            this.setSize(appletSize.width, p.y + 5); 
        return p; 
    }

/** Lay out a row of card stacks 
 *  @param x x coordinate of start of row
 *  @param y y coordinate of top of row
 *  @param startI starting index in stacks 
 *  @param n number of stacks in column 
 *  @param gap number of horizontal pixels between stacks 
 *  @return x coordinate of right side of last card in column + gap */
    public int layoutRow(int x, int y, int startI, int n, int gap)
    {
        for(int i = startI; i < startI + n; ++i)
        {   place(stacks[i], x, y);
            x += stacks[i].getSize().width + gap;
        }
        return x;
    }

/** Lay out a column of card stacks 
 *  @param x x coordinate of left side of column
 *  @param y y coordinate of top of column
 *  @param startI starting index in stacks 
 *  @param n number of stacks in column 
 *  @param gap number of vertical pixels between stacks 
 *  @return y coordinate of bottom of last card in column + gap */
    public int layoutCol(int x, int y, int startI, int n, int gap)
    {
        for(int i = startI; i < startI + n; ++i)
        {   place(stacks[i], x, y);
            y += stacks[i].getSize().height + gap;
        }
        return y; 
    }

/** Change the distance between overlapping cards in stacks 
 *    @param startI starting index in stacks 
 *    @param startI number of stacks to resize 
 *    @param dx horizontal distance between faceup cards
 *    @param dy vertical distance between faceup cards
 *    @param downDx horizontal distance between facedown cards
 *    @param downDy vertical distance between facedown cards */ 
    public void setOverlaps(int startI, int n, int dx, int dy,
                           int downDx, int downDy)
    {
        for(int i = startI; i < startI + n; ++i)
        {   stacks[i].setdXdY(dx, dy, downDx, downDy);
            stacks[i].fixSize();
        }
    }

/** Change the distance between overlapping cards in stacks
 *    @param startI starting index in stacks 
 *    @param startI number of stacks to resize 
 *    @param dx horizontal distance between faceup & facedown cards
 *    @param dy vertical distance between faceup & facedown cards */
    public void setOverlaps(int startI, int n, int dx, int dy)
    {
        setOverlaps(startI, n, dx, dy, dx, dy);
    }
    
/** Create a button panel 
 *    @param buttons array of buttons to place in panel
 *    @param gl layout for panel, should be set to desired # of rows & cols
 *    @param width width of panel
 *    @param height height of panel
 *    @return the button panel */ 
    public Panel makeButtonPanel(Button[] buttons, GridLayout gl, 
                                 int width, int height)
    {
        Panel p = new Panel();
        p.setSize(width, height);
        p.setLayout(gl);
        for(int i = 0; i < buttons.length; ++i)
            p.add(buttons[i]);
        return p;
    }

/** Create a row to be placed in a dialog box consisting of a component
 *   and a label for the component 
 *   @param text text for label
 *   @param c component 
 *   @return a panel containing the row */ 
    public Panel dlgRow(String text, Component c)
    {
        return dlgRow(new Label(text), c);
    }

/** Create a row containing two components 
 *   @param left left component
 *   @param right right component
 *   @return a panel containing the row */ 
    public Panel dlgRow(Component left, Component right)
    {
        Panel p = new Panel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        p.add(left);
        p.add(right);
        return p; 
    }
    
/** Begin a game, shuffling if desired. 
 *   @param doShuffle if true, shuffle deck otherwise reuse deck */
    public void startGame(boolean doShuffle)
    {
        if(doShuffle)
        {// Set unshuffled deck up with all card values in order    
            for(int deckI = 0; deckI < NDECKS; ++deckI)
            {   for(int i = 0; i < DECK_SIZE; ++i)
                    deck[deckI * DECK_SIZE + i] = i;
            }

            if(startRankI != startRankChoice.getSelectedIndex())
            {   startRankI = startRankChoice.getSelectedIndex();
                reallot();
            }
            shuffle(deck, 4 * startRankI, 
                    (DECK_SIZE * NDECKS) - 4 * startRankI);
        }
        deal();
        deselect();
        undo.setEnabled(false);
        redo.setEnabled(false);
        moveLog.removeAllElements(); 
        undoLog.removeAllElements();
        statusBar.resetTimer(); 
        showMsg("");
        statusBar.updateStats(statistics());
        nCheats = nRedeals = 0;
        autoMove(); 
    }

/** Randomize an array of integers 
 *   @param array to shuffle
 *   @param shufI first index to randomize
 *   @param nShuf number of integers to randomize */
    public void shuffle (int[] array, int shufI, int nShuf)
    {   
     // Base random seed on current time if random seed not fixed   
        if(!userSeed)
            randomSeed = System.currentTimeMillis() % 1000000;
        else
            randomSeed = seed;
        Random rand = new Random(randomSeed);
        seedTF.setText(Long.toString(randomSeed));
        
     // Go through deck and swap card with randomly selected following card 
        int endI = shufI + nShuf; 
        for(; shufI < endI; ++shufI)
        {   int swapI = shufI + (Math.abs(rand.nextInt()) % (endI - shufI));
            int temp    = array[swapI];
            array[swapI] = array[shufI];
            array[shufI] = temp;
        }
    }
    
/** Move the desired number of cards from the deck (from nsCards) into each 
 *   stack after clearing the original stack contents, turning facedown the 
 *   appropriate number of cards (from nsTurned).  This function should be 
 *   overridden if there are any additional things a game must do before 
 *   starting. */
    public void deal()
    {
        resetStacks();
        
     // If the suit stacks are not initially empty, fill them to the
     //   desired starting rank 
        int deckI = 0;
        for(int rank = 0; rank < startRankI; ++rank, deckI += 4)
        {   for(int suitI = 0; suitI < 4; ++suitI)
                stacks[SUITS_I + suitI].add(deck, deckI + suitI, 1);
        }
            
     // Now fill the remainder of the stacks    
        for(int stackI = 0; stackI < stacks.length; ++stackI)
        {   CardStack stk = stacks[stackI];
            stk.add(deck, deckI, nsCards[stackI]);
            if(nsTurned != null)
                stk.setNTurned(nsTurned[stackI]);
            stk.repaint();
            deckI += nsCards[stackI];
        }
    }
/** Clear all the stacks, deselect their cards and restore the desired
  * overlap distance */ 
    public void resetStacks()
    {
        Dimension d = this.getSize(); 
        for(int i = 0; i < stacks.length; ++i)
        {   stacks[i].clear();
            stacks[i].select(0); 
            stacks[i].expand(d.width, d.height); 
        }
    }
/** When the starting rank of the suit stacks is changed, this function
 *  will change the initial number of cards in the other stacks. This
 *  must be overridden if the game allows the starting rank to be altered */
    public void reallot() { }

/** Handle the action events generated by the buttons on the game layout.
 *    If a game has buttons other than the default, this function must
 *    be subclassed, with the new function calling super.action */
    public boolean action(Event evt, Object arg)
    {
        Object source = evt.target;
        if(source == restart)
            startGame(false);
            
        else if(source == newGame)
            startGame(true);
        
        else if(source == undo)
        {   undo(moveLog, undoLog, undo);
            redo.setEnabled(true);
        }
        else if(source == redo)
            undo(undoLog, null, redo);

     // Put options window in center of screen  
        else if(source == options)
        {   if(!optsDlg.isVisible())
            {   Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension d2 = optsDlg.getSize();
                optsDlg.setLocation((d1.width - d2.width) / 2, 
                             (d1.height - d2.height) / 2);
                optsDlg.show();
            }
            else
            {   resetOptions();
                optsDlg.hide();
            }
        }
        /*else if(source == help)
            showURL(helpFile);
        else
            return false;
        */
        return true;
    }
 /** Display a new browser window contain a document. This function is
  *   used to display a game's on-line help 
  *   @param url the document's URL */
/*    public void showURL(String url)
    {   
        {   try 
            {   URL absURL = new URL(getCodeBase(), url);
                getAppletContext().showDocument(absURL, "_blank");
            }
            catch(java.net.MalformedURLException e) { }
        }
    }*/
/** return the name of the game's help file */ 
    public String helpFName() { return helpFile; }

/** return the title of the options dialog. (Should be overridden). */
    public String optsTitle() { return ""; }

/** Modify the game settings to reflect the state of the controls in
 *    the options dialog. (This function should be overridden) */ 
    public void setOptions() 
    {
        makeAutoMoves = autoMoveCB.getState();
        displayTimer  = timerCB.getState();
        statusBar.displayTimer(displayTimer);

        if(seedCB.getState())
        {   long gameNum = -1;
            try 
            {   gameNum = Long.parseLong(seedTF.getText());
            }
            catch (NumberFormatException e) { e.printStackTrace();}

            if(gameNum == -1 || gameNum >= 1000000)
            {   showMsg("Game # must be between 0 and 999999");
                seedCB.setState(false);
            }
            else
                seed = gameNum;
        }
        if(!seedCB.getState())
        {   seedTF.setText(Long.toString(randomSeed));
            seedTF.setEnabled(false);
        }
        userSeed = seedCB.getState();
    }

/** Set the controls in the options dialog to reflect the current game
 *   settings. (This function should be overridden) */
    public void resetOptions() 
    {
        autoMoveCB.setState(makeAutoMoves);
        startRankChoice.select(startRankI);
        timerCB.setState(displayTimer);
        statusBar.displayTimer(displayTimer);
        seedCB.setState(userSeed);
        seedTF.setText(Long.toString(userSeed ? seed :
                                           randomSeed));
        seedTF.setEnabled(userSeed);
    }
/** This function must be overridden if it is necessary to know when
 *   the state of the option dialog controls change, instead of waiting
 *   for the OK button to be pressed */ 
    public boolean optAction(Event evt, Object arg) { return true; }
    
/** If a game has options, this function must be overridden to return a
 *    containing the game-specific controls 
 *  @return the panel containing the options */ 
    public Panel optsPanel() { return new Panel();  }
 
/** Add the common options to an options panel corresponding to the
 *    bits set in opts_bitmap*/
    public void addCommonOpts(Panel p, int opts_bitmap)
    {
        p.add(dlgRow(seedCB, seedTF));
        if((opts_bitmap & 2) != 0)
            p.add(autoMoveCB);
        if((opts_bitmap & 1) != 0)
            p.add(timerCB);
    }
/** Add all common options to an options panel */
    public void addCommonOpts(Panel p) { addCommonOpts(p, 0xffff); }
    
/** Handle action events occuring in the options windows. The default
 *   action is to just dis/enable the game number textField based on
 *   the state of the corresponding checkbox */
    public boolean optsAction(Event evt)
    {
        if(evt.target == seedCB)
        {   seedTF.setEnabled(seedCB.getState());
            return true;
        }
        return false;
    }

/** Handle mouse clicks. First see if click occurs inside a card stack. 
 *   If it does and no stack has been selected, select the stack. If a 
 *   stack has been selected, try to move cards from the selected stack
 *   to the clicked stack 
 *   @param evt mouse click event
 *   @param x x-coordinate of click in applet panel
 *   @param y y-coordinate of click in applet panel
 *   @return true if click occured inside card stack */
    public boolean mouseDown(Event evt, int x, int y)
    {	
        showMsg("");
        int i = 0; 

        Component c = this.getComponentAt(x, y);
        CardStack clicked;

        if(c == null || !(c instanceof CardStack)
           || !((CardStack)c).inCards(x, y))
            clicked = null;
        else
            clicked = (CardStack)c;

    // If both Alt & Ctrl down, print a dump of the card stacks
        boolean alt = (evt.modifiers & evt.ALT_MASK) != 0;
        if(alt && evt.controlDown())
        {   for(i = 0; i < stacks.length; ++i)
            {   CardStack stk = stacks[i]; 
                System.out.print(i + ": " + stk.nCards() + "/" +
                                   stk.getNTurned() + "/" +
                                   stk.nSelected() + "  ");
                System.out.println(stacks[i]);
            }
            return true;
        }
     // If right mouse button clicked, perform appropriate action   
        if(alt || evt.metaDown())
            rightClick(clicked);
            
        else if(clicked == null)
            return false; 
        
     // If no selected cards, select clicked stack  
        else if(selected == null)
            select(clicked, x, y);
        
        else
        {// If any suit stack clicked, move automatically to appropriate
         //   suit stack 
            if(clicked instanceof SuitStack && !selected.isEmpty())
                clicked = stacks[selected.chosen().suit() + SUITS_I];
            
            Point p = translateXY(clicked, x, y); 
            int nAccept = clicked.canAccept(selected, p);

         // nAccept less than 0 means multiple cards have been selected 
            if(nAccept < 0)
                return true;

         // nAccept of 0 means move is not allowed 
            else if(nAccept == 0)
            {   if(clicked != selected)
                    showMsg("Invalid move");
            }
            else
                tryMove(clicked, clicked.insertI(p), evt, nAccept);

            deselect(); 
        }
        return true;
    }

/** Move card from selected stack to clicked stack, then make possible
 *   automatic moves. This function should be overridden if there are
 *   game conditions which might prevent the move from being made 
 *   @param clicked clicked stack
 *   @param evt mouse click event
 *   @param nMove number of cards to move */ 
    public void tryMove(CardStack clicked, int insI, Event evt, int nMove)
    {
        int max = maxNMove(clicked); 
        if(nMove > max)
        {   // "Cheating" will ignore lack of empty stacks 
            if(evt.shiftDown()) 
                ++nCheats;
         // If destination is empty, move as many cards as possible 
         //   to the destination stack  
            else if(clicked.isEmpty())
                nMove = max;
            
         // Otherwise report error
            else
            {   String err = ("Need " + (nMove - 1) 
                              + " empty spaces to make move. Only "
                              + (max - 1) + ((max < 3) ? " is" : " are")
                              + " available.");
                showMsg(err);
                return;
            }
        }
        moveCard(clicked, selected, insI, nMove, null);
        if(!(selected instanceof SuitStack))
            autoMove();
    }
    
/** Return the maximum # of cards that can be moved to a stack,
  *  default is unlimited */
    public int maxNMove(CardStack stk) { return Integer.MAX_VALUE;  }

/** Default action for right click is to do nothing. Games which use
 *   right mouse clicks must override this. */ 
    public void rightClick(CardStack clicked) { }

/* Translate a global pair of coordinates into a its location in a
 * card stack */ 
    private Point translateXY(CardStack stk, int x, int y)
    {   
        Rectangle b = stk.cardBounds();
        return new Point(x - b.x, y - b.y);
    }
    
/** Have the selected stack highlight the appropriate part of itself 
 *   for a mouse click at the coordinates given and set selected to 
 *   the clicked stack. Games with an implicit source or destination
 *   stack will need to override this function to handle card movement 
 *   @param clicked stack inside which mouse button has been pressed
 *   @param x x-coordinate of mouse click
 *   @param y y-coordinate of mouse click */ 
    public void select(CardStack clicked, int x, int y) 
    {   if(clicked.canRemove())
        {   clicked.select(translateXY(clicked, x, y));
            selected = clicked;
        }
    }
    public void select(CardStack clicked) { select(clicked, 0, 0);  }

/** Removes highlighting from selected stack */ 
    public void deselect() 
    {
        if(selected != null)
        {   selected.select(0);
            selected = null;
        }
    }

/** Move the clicked card to an empty stack, if present. If more than one 
  * card in the stack has been selected move cards until there are no more 
  * empty spaces or selected cards. This will be the action for right
  * clicking for some games */
    public void removeSelected(CardStack clicked)
    {
        if(clicked != null && clicked.canRemove())
        {   int nMove = Math.max(1, clicked.nSelected());
            int nMoved = 0;
            deselect();
                    
            for(int i = 0 ; i < stacks.length && nMoved < nMove ; ++i)
            {   if(isBuildingStackI(i) && stacks[i].isEmpty())
                {   moveCard(stacks[i], clicked, 1, null);
                    ++nMoved;
                }
            }
            if(nMoved == 0)
                showMsg("No empty stacks");
            else
                autoMove(); 
        }
    }

/** Return true if the stack index refers to a building stack, false if
 *  not */
    public boolean isBuildingStackI(int stackI)
    {   
        return (stackI < SUITS_I || SUITS_I + 4 <= stackI);
    }
/** Return number of cards in suit stacks */ 
    public int nSuitStacks()
    {
        if(SUITS_I == -1)
            return -1; 
        else
        {   int nSuitStacks = 0;
            for(int i = SUITS_I; i < SUITS_I + 4; ++i)
                nSuitStacks += stacks[i].nCards();
            return nSuitStacks;
        }
    }

/** Default victory condition is all cards in the deck being in the suit
 *   stacks. Solitaire games with different victory conditions must override
 *   this function. 
 *  @return true if victory condition has been attained */ 
    public boolean gameWon() { return nSuitStacks() == DECK_SIZE; }

/** End the game when the game can no longer be won */ 
    public void endGame()
    {
        showMsg("Game over");
        statusBar.stopTimer();
    }
/** Check and see if no more moves are possible. Default action will not 
 *  check for dead end. If function is overridden, it should return 
 *  appropriate message */
    public boolean gameLost() { return false; }

/*  Move nMove cards from src to dest, recording the move if desired.
 *    change the distance between cards in the stacks if necessary.
 *    See if the victory condition has been met after making the move 
 *   @param dest destination stack for card move
 *   @param src source stack for card move
 *   @param nMove number of cards moved 
 *   @param insI index to insert cards at
 *   @param delI index to remove cards from 
 *   @param btn button which made move, null if cards moved normally */
    public void moveCard(CardStack dest, CardStack src, int nMove, 
                         int insI, int delI, Button btn)
    {
     // Do not record move if move an undoing of previous move 
        if(btn != undo)
        {   if(moveLog.size() == 0)
                undo.setEnabled(true);
            recordMove(moveLog, new CardMove(dest, src, nMove, insI, delI));
        }
     // Remove undolog log to prevent redo if move not undo or redo 
        if(btn == null && undoLog.size() != 0)
        {   undoLog.removeAllElements();
            redo.setEnabled(false);
        }
            
        Dimension d = this.getSize(); 
        dest.move(src, nMove, insI, delI);
        dest.compress(d.width, d.height);
        src.expand(d.width, d.height);
        statusBar.updateStats(statistics());

        if(gameWon())
        {   showMsg("Victory!");
            statusBar.stopTimer();
        }
        else if(gameLost())
        {   showMsg("Game Over. No more possible moves");
            statusBar.stopTimer();
        }
    }
    
/*  Move nMove cards from src to dest, recording the move if desired.
 *    change the distance between cards in the stacks if necessary.
 *    See if the victory condition has been met after making the move 
 *   @param dest destination stack for card move
 *   @param src source stack for card move
 *   @param nMove number of cards moved 
 *   @param record if set, add card move to moveLog */
    public void moveCard(CardStack dest, CardStack src, int insI,
                         int nMove, Button btn)
    {
        moveCard(dest, src, nMove, insI, src.getSelectedI(), btn);
    }
    public void moveCard(CardStack dest, CardStack src, int nMove, Button btn)
    {
        moveCard(dest, src, nMove, 0, src.getSelectedI(), btn);
    }

    public void recordMove(Stack stack, Object move) { stack.push(move); }
    public Object retrieveMove(Stack stack) { return stack.pop(); }
    public void copyLogEntry(Stack src, Stack dest) { dest.push(src.peek()); }
    public void undoExtra(Stack stk) {}
    public void redoExtra(Stack stk) {}
    
/** If moves made must be undo as a group, make an array for the group and
  * put it in the move log stack 
  * @param nPrev number of previous moves which must be undone */ 
    public void makeUndoGroup(int nPrev)
    {
        CardMove[] undoGroup = new CardMove[nPrev];
        for(int grpI = nPrev - 1; grpI >= 0; --grpI)
            undoGroup[grpI] = (CardMove)retrieveMove(moveLog);
        
        recordMove(moveLog, undoGroup);
    }
    
/** Automatically move all possible cards from the tops of the building stacks
 *    to the suit stacks if there are no cards outside the suit stacks which 
 *    can be placed on the cards. Keep scanning the stacks until all 
 *    possible stacks have been made */ 
    public boolean autoMove()
    {
        boolean moved;
        int nMoved = 0; 

        if(SUITS_I == -1 || !makeAutoMoves)
            return false;
        
        do 
        {// Look through stacks to see which stacks top cards can be moved
         //   to the appropriate suit stack
            moved = false; 
            for(int stackI = 0; stackI < stacks.length; ++stackI)
            {   CardStack src = stacks[stackI];
                if(   src.canRemove() && src.isAutoMoveSrc()
                   && (stackI < SUITS_I || SUITS_I + 4 <= stackI))

                {// If any card in stack is accessible, need to look at
                 //   entire stack, otherwise just look at top  
                    int n = (src.getSelection() != CardStack.SELECT_CLICKED
                             ? 1 : src.nCards());
                    for(int cardI = src.nCards() - 1; n > 0; --cardI, --n)
                    {   Card card = src.cardAt(cardI);
                        CardStack dest = stacks[SUITS_I + card.suit()];
                        int nAbove = src.nCards() - cardI - 1;
                        src.select(1, nAbove);
                        if(dest.canAccept(src) != 0 && noBuilds(card))
                        {   moveCard(dest, src, 1, 0, nAbove, null);
                            moved = true;
                            ++nMoved;
                        }
                    }
                    src.select(0); 
                }
            }
        }while(moved);

        statusBar.setnAutoMoves(nMoved);
        return nMoved != 0; 
    }

/** Move cards from the deck to the talon. If the deck is empty and a
 *    redeal is possible, do another cycle through the deck 
 *  @param talon stack containing cards drawn from deck
 *  @param pile stack containing remainder of deck 
 *  @param nDraw number of cards to attempt to move from pile to talon */ 
    public boolean talonAdd(CardStack talon, CardStack pile, int nDraw)
    {
        if(pile.isEmpty())
        {   if(nRedeals + 1 == maxRedeals)
            {   showMsg("No more redeals");
                return false;
            }
            
            if(++nRedeals > 1 && PrevNDeck == talon.nCards())
            {   String noRem ="No cards removed in previous pass through deck";
                showMsg(noRem);
            }
            PrevNDeck = talon.nCards();
            moveCard(pile, talon, talon.nCards(), null);
            if(maxRedeals != 0)
                showMsg("Redeal #" + nRedeals + " of " + (maxRedeals - 1));
        }
        moveCard(talon, pile, Math.min(nDraw, pile.nCards()), null);
        autoMove();
        return true; 
    }
    
/** See if there are no cards outside the suit stacks which can be placed
 *   on top of a card 
 *   @param card the tested card 
 *   @returns true if no builds can be made on the card */ 
    public boolean noBuilds(Card card)
    {
        int i = SUITS_I + (card.isBlack() ? 2 : 0);
        int d = (card.rank() + 13 - baseRank) % 13;
        
        return (   d == 0 || d == 1 
                || (stacks[i + 1].nCards() >= d && stacks[i].nCards() >= d));
    }
/** For the last card move in srcLog, move the cards back to the
 *   source stack from the destination stack and set the number of facedown
 *   cards to what they were before the move. If destLog is not null, 
 *   push the undone move to it. If srcLog has no more moves, disable btn */
    public void undo(Stack srcLog, Stack destLog, Button btn)
    {
        deselect(); 
        statusBar.setnAutoMoves(0); 
        if(!srcLog.empty())
        {   if(destLog != null)
                copyLogEntry(srcLog, destLog);
            
            Object popped = retrieveMove(srcLog);
            CardMove[] moveGroup;
            if(popped instanceof CardMove)
            {   moveGroup = new CardMove[1];
                moveGroup[0] = (CardMove)popped;
            }
            else
                moveGroup = (CardMove[])popped;

            int grpI = (btn == undo) ? moveGroup.length - 1 : 0;
                
            do {
                CardMove move = moveGroup[grpI];

                if(btn == undo)
                {   moveCard(move.src, move.dest, move.nMoved,
                             move.srcSelectI, move.destSelectI, undo);
                    move.src.setNTurned(move.srcNTurned);
                    move.dest.setNTurned(move.destNTurned);
                    undoExtra(srcLog);
                    --grpI;
                }
                else if(btn == redo)
                {   moveCard(move.dest, move.src, move.nMoved,
                             move.destSelectI, move.srcSelectI, redo);
                    redoExtra(srcLog);
                    ++grpI;
                }

            }while(grpI >= 0 && grpI < moveGroup.length);
            if(btn == redo && moveGroup.length > 1)
                makeUndoGroup(moveGroup.length);
        }
        if(srcLog.empty())
            btn.setEnabled(false);
    }
/** Display a message in the browsers status line 
 *   @param msg the message text */
    public void showMsg(String msg) { statusBar.showMsg(msg); }

    public String statistics()
    {
        if(nSuitStacks() == -1)
            return " ";
        else
            return (DECK_SIZE - nSuitStacks()) + "/" + nSuitStacks() + " ";
    }
    public int nDisordered()
    {
        return -1;
    }
    public int nOrdered()
    {
        int nOrd = 0; 
        for(int stkI = 0; stkI < stacks.length; ++stkI)
        {   if(isBuildingStackI(stkI))
                nOrd += stacks[stkI].nOrdered(nOrderedTop);
        }
        return nOrd;
    }
    
/** Get the enclosing frame for the game applet panel. If the enclosing frame
 *   is not found, create a frame and return it 
 *   @return the enclosing frame */
    public Frame getFrame()
    {
        Container c = this;
        while(!(c instanceof Frame))
        {   c = c.getParent();
            if(c == null || c == c.getParent())
                return new Frame(); 
        }
        return (Frame)c;
    }
}

/** CardMove records the movement one cards from one stack to another 
 *    and also records the number of face down cards in the source and
 *    destination stack before the movement */
class CardMove
{
    CardStack src, dest;
    int nMoved, srcNTurned, destNTurned, srcSelectI, destSelectI;
    
/** Create a class containing information about card movement 
  * @param dest destination stack of cards
  * @param src source stack of cards 
  * @param nMoved number of cards moved */
    public CardMove(CardStack dest, CardStack src, int nMoved)
    {
        this.dest        = dest;
        this.src         = src;
        this.nMoved      = nMoved;
        this.srcNTurned  = src.getNTurned();
        this.destNTurned = dest.getNTurned();
        this.srcSelectI  = src.getSelectedI();
        this.destSelectI = dest.getSelectedI(); 
    }
    public CardMove(CardStack dest, CardStack src, int nMoved, 
                    int insI, int delI)
    {
        this.dest        = dest;
        this.src         = src;
        this.nMoved      = nMoved;
        this.srcNTurned  = src.getNTurned();
        this.destNTurned = dest.getNTurned();
        this.srcSelectI  = delI;
        this.destSelectI = insI;
    }

}


/** StatusBar writes the time elapsed along with the current message and the
 *  game statistics in the bar at the bottom of the browser */
class StatusBar implements Runnable 
{
    long startTime = System.currentTimeMillis();
    int nSec = 0, next_update;
    int nAutoMoves;
    String message = "", stats = "";
    //java.applet.AppletContext apCont;
    boolean visible = true, running = true;
    
    StatusBar(/*java.applet.AppletContext ac*/) 
    {
        //apCont = ac; 
        resetTimer();
    }
/** Change the message, then write it and the current time to the status bar */
    void showMsg(String msg)
    {
        message = msg;
        update();
    }
/** Update the statistics in the status bar */
    void updateStats(String st)
    {
        stats = st;
        update();
    }
    void setnAutoMoves(int n) { nAutoMoves = n; }
    
/** Write the current time and message to the status bar */ 
    void update()
    {   String msg;
        String am = "";
        if(nAutoMoves == 1)
            am = " (1 automove made)";
        else if(nAutoMoves > 1)
            am = " (" + nAutoMoves + " automoves made)";
        if(visible)
        {   int min = nSec / 60, sec = nSec % 60;
            msg = (stats + "Time: " + min + (sec >= 10 ? ":" : ":0")
                   + sec + " " + message + am);
        }
        else 
            msg = stats + " " + message + am;

        //apCont.showStatus(msg); 
    }
                
 /* Update the displayed time every second */   
    public void run() 
    {   
        for( ; ; )
        {   long elapsed = System.currentTimeMillis() - startTime;
            long sleepMSec = (nSec + 1) * 1000 - elapsed;
            try
            {   Thread.sleep(Math.max(sleepMSec, 0));
            } catch (InterruptedException e) { e.printStackTrace(); }
            ++nSec;

            if(running)
                update();
        }
    }
    public void resetTimer()
    {
        nSec = 0; 
        startTime = System.currentTimeMillis();
        running   = visible;
    }
    public void displayTimer(boolean vis)
    {   
        if(vis != visible)
        {   visible = vis;
            update();
        }
    }
    public void stopTimer() { running = false; }
}


/** OptsDlg generates a free-floating dialog window containing controls
 *    for a game's options. The default dialog will only have OK, Cancel
 *    and help buttons. OptsDlg will use the solitaire game's optPanel
 *    method to add the game-specific controls to the window */
class OptsDlg extends Dialog
{
    Solitaire game;
    Button OK, cancel, help;
    boolean visible = false;

/** Create the options dialog for a solitaire game and attach it to the
 *    enclosing frame of the applet. The window title will come from the
 *    optsTitle method of the game 
    @param sol instance of solitaire game */   
    public OptsDlg(Solitaire sol)
    {
        super(sol.getFrame(), sol.optsTitle(), false);
        game = sol;
        OK = new Button("OK");
        cancel = new Button("Cancel");
        help = new Button("Help");
        Panel buttonRow = new Panel();
        buttonRow.setLayout(new GridLayout(1, 3));
        buttonRow.add(OK);
        buttonRow.add(cancel); 
        buttonRow.add(help); 
        add("South", buttonRow);
        add("North", game.optsPanel()); 
        pack();
    }
/** Handle window closing for the dialog. */
    public boolean handleEvent(Event evt)
    {
        if(evt.id == Event.WINDOW_DESTROY)
        {   game.resetOptions();
            hide();
            return true; 
        }
        else if(evt.id == Event.ACTION_EVENT && game.optsAction(evt))
            return true;
        else
            return super.handleEvent(evt); 
    }
/** Handle button presses for the buttons at the bottom of a row. */ 
    public boolean action(Event evt, Object arg)
    {   
        Object source = evt.target;
        if(source == OK)
        {   game.setOptions();
            hide(); 
        }
        /*else if(source == help)
            game.showURL(game.helpFName() + "#options");
            
        else if(source == cancel)
        {   game.resetOptions();
            hide(); 
        }*/
        else
            return game.optAction(evt, arg);

        return true; 
    }
    public void show()
    {   
        visible = true;
        super.show();
    }
    public void hide()
    {
        visible = false;
        super.hide();
    }
    public boolean isVisible() { return visible; }
}
