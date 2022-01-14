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
 * CreateGroupAnchorsFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.Anchor;
import org.jfree.report.event.ReportEvent;

/**
 * Creates anchor objects for the current group. The anchors generated consist of the group's name and the current group
 * count.
 * <p/>
 * To use the CreateGroupAnchorsFunction set the group's name as the function's group-property value. Next, add a
 * AnchorElement to where the anchor should be generated (usually either the group-header or footer) and give the
 * function's name as fieldname in the anchor-field.
 *
 * @author Thomas Morgner
 * @deprecated It is easier to create anchors using a Style-expression. The Anchor-Field has been deprecated now.
 */
public class CreateGroupAnchorsFunction extends AbstractFunction
{
  /**
   * The name of the group for which anchors should be created.
   */
  private String group;
  /**
   * A prefix for the anchor name.
   */
  private String anchorPrefix;
  /**
   * A temporary variable holding the last anchor created by this function.
   */
  private Anchor anchor;
  /**
   * A counter to create unique anchor names.
   */
  private int count;

  /**
   * Default Constructor. Does nothing.
   */
  public CreateGroupAnchorsFunction()
  {
    anchorPrefix = "anchor";
  }

  /**
   * Returns the prefix for the generated anchor.
   *
   * @return the anchor prefix.
   */
  public String getAnchorPrefix()
  {
    return anchorPrefix;
  }

  /**
   * Defines the prefix for the generated anchor.
   *
   * @param anchorPrefix the prefix for the anchor.
   */
  public void setAnchorPrefix(final String anchorPrefix)
  {
    if (anchorPrefix == null)
    {
      throw new NullPointerException("The Anchor-Prefix must not be null.");
    }
    this.anchorPrefix = anchorPrefix;
  }

  /**
   * Returns the name of the group for which an anchor should be generated.
   *
   * @return the name of the group.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Defines the name of the group for which an anchor should be generated.
   *
   * @param group the name of the group.
   */
  public void setGroup(final String group)
  {
    this.group = group;
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The event carries a
   * ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    count = 0;

    final StringBuffer targetBuffer = new StringBuffer();
    final String prefix = getAnchorPrefix();
    targetBuffer.append(prefix);
    targetBuffer.append(getGroup());
    targetBuffer.append("%3D");
    targetBuffer.append(count);
    anchor = new Anchor(targetBuffer.toString());
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      return;
    }

    final StringBuffer targetBuffer = new StringBuffer();
    final String prefix = getAnchorPrefix();
    targetBuffer.append(prefix);
    targetBuffer.append(getGroup());
    targetBuffer.append("%3D");
    targetBuffer.append(count);
    anchor = new Anchor(targetBuffer.toString());
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return anchor;
  }
}
