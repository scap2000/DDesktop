/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * RowBandingFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.awt.Color;

import org.jfree.report.Element;
import org.jfree.report.ItemBand;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.util.Log;

/**
 * A function that alternates the background-color for each item-band within a group. If the function evaluates to true,
 * then the background of the named element will be set to the visible-color, else it will be set to the
 * invisible-color.
 * <p/>
 * The ElementVisibilitySwitchFunction defines two parameters: <ul> <li>element <p>The name of the element in the
 * itemband that should be modified. The element(s) must be named using the "name" attribute, if no name is given here,
 * the ItemBand's background color is defined instead.</p> <li>initial-state <p>The initial state of the function. (true
 * or false) defaults to false. This is the reverse of the element's visiblity (set to false to start with an visible
 * element, set to true to hide the element in the first itemrow).</p> </ul>
 *
 * @author Thomas Morgner
 * @author Michael D'Amour
 */
public class RowBandingFunction extends AbstractFunction implements PageEventListener
{
  /**
   * The computed visibility value.
   */
  private transient boolean trigger;
  /**
   * An internal counter that counts the number of rows processed since the last visibility switch.
   */
  private transient int count;
  /**
   * A internal flag indicating whether a warning has been printed.
   */
  private transient boolean warned;
  /**
   * If not null, this boolean flag defines the function state that should be used after page breaks. If not
   * defined, the initial state is used instead.
   */
  private Boolean newPageState;
  /**
   * A field defining the number of rows that must be processed before the visibility can switch again.
   */
  private int numberOfElements;
  /**
   * The name of the element that should be formatted.
   */
  private String element;
  /**
   * The initial visibility that is used on the start of a new report, a new group or a new page.
   */
  private boolean initialState;

  /**
   * A flag indicating whether the visiblity should be reset to its initial state when a group starts.
   */
  private boolean resetOnGroupStart;
  /**
   * A flag indicating whether the visiblity should be reset to its initial state when a page starts.
   */
  private boolean resetOnPageStart;

  /**
   * The background color that is used if the row-banding background should be visible.
   */
  private Color visibleBackground;
  /**
   * The background color that is used if the row-banding background should be invisible. 
   */
  private Color invisibleBackground;

  /**
   * Default constructor.
   */
  public RowBandingFunction()
  {
    warned = false;
    numberOfElements = 1;
    resetOnGroupStart = true;
    resetOnPageStart = true;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    if (resetOnPageStart)
    {
      //pagebreak = false;
      if (newPageState == null)
      {
        trigger = getInitialState();
      }
      else
      {
        trigger = newPageState.booleanValue();
      }
      count = 0;
      triggerVisibleState(event);
    }
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The event carries a
   * ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    //pagebreak = false;
    trigger = !getInitialState();
    count = 0;
  }

  /**
   * Receives notification that the items are being processed.  Sets the function value to false. <P> Following this
   * event, there will be a sequence of itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event Information about the event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    if (resetOnGroupStart)
    {
      // pagebreak = false;
      trigger = !getInitialState();
      count = 0;
    }
  }

  /**
   * Triggers the visibility of an element. If the named element was visible at the last itemsAdvanced call, it gets now
   * invisible and vice versa. This creates the effect, that an element is printed every other line.
   *
   * @param event the report event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    triggerVisibleState(event);
  }

  /**
   * Triggers the visible state of the specified itemband element. If the named element was visible at the last call, it
   * gets now invisible and vice versa. This creates the effect, that an element is printed every other line.
   *
   * @param event the current report event.
   */
  private void triggerVisibleState(final ReportEvent event)
  {
    if ((count % numberOfElements) == 0)
    {
      trigger = (!trigger);
    }
    count += 1;

    final ItemBand itemBand = event.getReport().getItemBand();
    if (element == null)
    {
      if (trigger)
      {
        itemBand.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, visibleBackground);
      }
      else
      {
        itemBand.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, invisibleBackground);
      }
    }
    else
    {
      final Element[] e = FunctionUtilities.findAllElements(itemBand, getElement());
      if (e.length > 0)
      {
        for (int i = 0; i < e.length; i++)
        {
          if (trigger)
          {
            e[i].getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, visibleBackground);
          }
          else
          {
            e[i].getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, invisibleBackground);
          }
        }
      }
      else
      {
        if (warned == false)
        {
          Log.warn("The Band does not contain an element named " + getElement());
          //Log.warn(new Log.SimpleMessage(Messages.getString("ElementVisibilitySwitchFunction.0"), getElement(), Messages.getString("ElementVisibilitySwitchFunction.1"))); //$NON-NLS-1$ //$NON-NLS-2$
          warned = true;
        }
      }
    }
  }

  /**
   * Returns the background color that is used if the row-banding background should be invisible.
   *
   * @return a color.
   */
  public Color getInvisibleBackground()
  {
    return invisibleBackground;
  }

  /**
   * Defines the background color that is used if the row-banding background should be invisible.
   *
   * @param invisibleBackground a color.
   */
  public void setInvisibleBackground(final Color invisibleBackground)
  {
    this.invisibleBackground = invisibleBackground;
  }

  /**
   * Returns the background color that is used if the row-banding background should be visible.
   *
   * @return a color.
   */
  public Color getVisibleBackground()
  {
    return visibleBackground;
  }

  /**
   * Defines the background color that is used if the row-banding background should be visible.
   *
   * @param visibleBackground a color.
   */
  public void setVisibleBackground(final Color visibleBackground)
  {
    this.visibleBackground = visibleBackground;
  }

  /**
   * Returns the number of rows that must be processed before the visibility can switch again.
   *
   * @return a row count.
   */
  public int getNumberOfElements()
  {
    return numberOfElements;
  }

  /**
   * Defines the number of rows that must be processed before the visibility can switch again.
   *
   * @param numberOfElements a row count.
   */
  public void setNumberOfElements(final int numberOfElements)
  {
    this.numberOfElements = numberOfElements;
  }

  /**
   * Gets the initial value for the visible trigger, either "true" or "false".
   *
   * @return the initial value for the trigger.
   * @deprecated use getInitialState instead.
   */
  public boolean getInitialTriggerValue()
  {
    return initialState;
  }

  /**
   * Returns the initial visibility that is used on the start of a new report, a new group or a new page.
   *
   * @return the initial value for the trigger.
   */
  public boolean getInitialState()
  {
    return initialState;
  }

  /**
   * Defines the initial visibility that is used on the start of a new report, a new group or a new page.
   *
   * @param initialState the initial value for the trigger.
   */
  public void setInitialState(final boolean initialState)
  {
    this.initialState = initialState;
  }

  /**
   * Sets the element name. The name denotes an element or band within the root-band or the root-band itself. It is
   * possible to define multiple elements with the same name to apply the modification to all of these elements.
   *
   * @param name The element name.
   * @see org.jfree.report.function.FunctionUtilities#findAllElements(org.jfree.report.Band,String)
   */
  public void setElement(final String name)
  {
    this.element = name;
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   * @see #setElement(String)
   */
  public String getElement()
  {
    return element;
  }

  /**
   * Returns the visibility state that should be used on new pages. This is only used if resetOnPageStart is set
   * to true. If this value is not defined, the initialState is used.
   *
   * @return the state on new pages.
   */
  public Boolean getNewPageState()
  {
    return newPageState;
  }

  /**
   * Defines the visibility state that should be used on new pages. This is only used if resetOnPageStart is set
   * to true. If this value is not defined, the initialState is used.
   *
   * @param newPageState the state on new pages or null to use the initialState.
   */
  public void setNewPageState(final Boolean newPageState)
  {
    this.newPageState = newPageState;
  }

  /**
   * Returns the defined visibility of the element. Returns either true or false as java.lang.Boolean.
   *
   * @return the visibility of the element, either Boolean.TRUE or Boolean.FALSE.
   */
  public Object getValue()
  {
    if (trigger)
    {
      return Boolean.TRUE;
    }
    else
    {
      return Boolean.FALSE;
    }
  }
}
