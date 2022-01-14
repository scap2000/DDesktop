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
 * FunctionUtilities.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;

/**
 * A collection of utility methods relating to functions.
 *
 * @author Thomas Morgner.
 */
public final class FunctionUtilities
{

  /**
   * Default Constructor.
   */
  private FunctionUtilities ()
  {
  }

  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band    the band that is suspected to contain the element.
   * @param element the element name.
   * @return the found element or null, if no element could be found.
   */
  public static Element findElement (final Band band, final String element)
  {
    if (element == null)
    {
      throw new NullPointerException("Element name must not be null");
    }

    final Element[] elements = band.getElementArray();
    if (band.getName().equals(element))
    {
      return band;
    }

    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      if (e.getName().equals(element))
      {
        return e;
      }
      if (e instanceof Band)
      {
        final Element retval = findElement((Band) e, element);
        if (retval != null)
        {
          return retval;
        }
      }
    }
    return null;
  }


  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band    the band that is suspected to contain the element.
   * @param element the element name.
   * @return the found element or null, if no element could be found.
   */
  public static Element[] findAllElements (final Band band, final String element)
  {
    if (element == null)
    {
      throw new NullPointerException("Element name must not be null");
    }
    final ArrayList collector = new ArrayList();
    if (band.getName().equals(element))
    {
      collector.add(band);
    }
    performFindElement(band, element, collector);
    return (Element[]) collector.toArray(new Element[collector.size()]);
  }

  /**
   * Internal function that collects all elements of a given band with a given name.
   *
   * @param band the band from which elements should be collected.
   * @param element the name of the element to collect.
   * @param collector the list of results.
   */
  private static void performFindElement (final Band band, final String element,
                                          final ArrayList collector)
  {
    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      if (e.getName().equals(element))
      {
        collector.add(e);
      }
      if (e instanceof Band)
      {
        performFindElement((Band) e, element, collector);
      }
    }
  }

  /**
   * Returns true if the events current groupname is equal to the group name.
   *
   * @param groupName the group name.
   * @param event     the report event.
   * @return A boolean.
   */
  public static boolean isDefinedGroup (final String groupName, final ReportEvent event)
  {
    if (groupName == null)
    {
      return false;
    }

    final Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    return groupName.equals(group.getName());
  }

  /**
   * Returns true, if the current run level is defined for the given function and this is
   * a prepare run. The prepare run is used to compute the function values.
   *
   * @param f     the function.
   * @param event the event.
   * @return A boolean.
   */
  public static boolean isDefinedPrepareRunLevel (final Function f,
                                                  final ReportEvent event)
  {
    if (f == null)
    {
      throw new NullPointerException("Function is null");
    }

    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }

    if (event.getState().isPrepareRun() == false)
    {
      return false;
    }
    return (event.getState().getLevel() == f.getDependencyLevel());
  }

  /**
   * Returns true or false.
   *
   * @param event the report event.
   * @return A boolean.
   */
  public static boolean isLayoutLevel (final ReportEvent event)
  {
    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }
    return (event.getState().getLevel() < 0);
  }

  /**
   * Returns the current group instance, based on the given report event.
   *
   * @param event the event which is base for the action.
   * @return the current group of the event, never null.
   */
  public static Group getCurrentGroup (final ReportEvent event)
  {
    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }
    return event.getReport().getGroup(event.getState().getCurrentGroupIndex());
  }
}
