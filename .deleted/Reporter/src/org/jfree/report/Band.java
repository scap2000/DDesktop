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
 * Band.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementDefaultStyleSheet;

/**
 * A report band is a collection of other elements and bands, similiar to an AWT-Container.
 * <p/>
 * This implementation is not synchronized, to take care that you externally synchronize it when using multiple threads
 * to modify instances of this class.
 * <p/>
 * Trying to add a parent of an band as child to the band, will result in an exception.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class Band extends Element
{
  /**
   * An empty array to prevent object creation.
   */
  private static final Element[] EMPTY_ARRAY = new Element[0];

  /**
   * The defined content type for the band.
   *
   * @deprecated The content-type is no longer used.
   */
  public static final String CONTENT_TYPE = "X-container";

  /**
   * All the elements for this band.
   */
  private ArrayList allElements;

  /**
   * Cached elements.
   */
  private transient Element[] allElementsCached;

  /**
   * The prefix for anonymous bands, bands without an userdefined name.
   */
  public static final String ANONYMOUS_BAND_PREFIX = "anonymousBand@";

  /**
   * Constructs a new band (initially empty).
   */
  public Band()
  {
    setName(Band.ANONYMOUS_BAND_PREFIX + System.identityHashCode(this));
    allElements = new ArrayList();
  }

  /**
   * Constructs a new band with the given pagebreak attributes. Pagebreak attributes have no effect on subbands.
   *
   * @param pagebreakAfter  defines, whether a pagebreak should be done after that band was printed.
   * @param pagebreakBefore defines, whether a pagebreak should be done before that band gets printed.
   */
  public Band(final boolean pagebreakBefore, final boolean pagebreakAfter)
  {
    this();
    if (pagebreakBefore)
    {
      setPagebreakBeforePrint(pagebreakBefore);
    }
    if (pagebreakAfter)
    {
      setPagebreakAfterPrint(pagebreakAfter);
    }
  }

  /**
   * Returns the global stylesheet for all bands. This stylesheet provides the predefined default values for some of the
   * stylekeys.
   *
   * @return the global default stylesheet.
   */
  protected ElementDefaultStyleSheet createGlobalDefaultStyle()
  {
    return BandDefaultStyleSheet.getBandDefaultStyle();
  }

  /**
   * Adds a report element to the band.
   *
   * @param element the element that should be added
   * @throws NullPointerException     if the given element is null
   * @throws IllegalArgumentException if the position is invalid, either negative or greater than the number of elements
   *                                  in this band or if the given element is a parent of this element.
   */
  public void addElement(final Element element)
  {
    addElement(allElements.size(), element);
  }

  /**
   * Adds a report element to the band. The element will be inserted at the specified position.
   *
   * @param position the position where to insert the element
   * @param element  the element that should be added
   * @throws NullPointerException     if the given element is null
   * @throws IllegalArgumentException if the position is invalid, either negative or greater than the number of elements
   *                                  in this band or if the given element is a parent of this element.
   */
  public void addElement(final int position, final Element element)
  {
    if (position < 0)
    {
      throw new IllegalArgumentException("Position < 0");
    }
    if (position > allElements.size())
    {
      throw new IllegalArgumentException("Position < 0");
    }
    if (element == null)
    {
      throw new NullPointerException("Band.addElement(...): element is null.");
    }

    // check for component loops ...
    if (element instanceof Band)
    {
      Band band = this;
      while (band != null)
      {
        if (band == element)
        {
          throw new IllegalArgumentException("adding container's parent to itself");
        }
        band = band.getParent();
      }
    }

    // remove the element from its old parent ..
    // this is the default AWT behaviour when adding Components to Container
    if (element.getParent() != null)
    {
      if (element.getParent() == this)
      {
        // already a child, wont add twice ...
        return;
      }

      element.getParent().removeElement(element);
    }

    // add the element, update the childs Parent and the childs stylesheet.
    allElements.add(position, element);
    allElementsCached = null;

    // then add the parents, or the band's parent will be unregistered ..
    element.setParent(this);
  }

  /**
   * Adds a collection of elements to the band.
   *
   * @param elements the element collection.
   * @throws NullPointerException     if one of the given elements is null
   * @throws IllegalArgumentException if one of the given element is a parent of this element.
   */
  public void addElements(final Collection elements)
  {
    if (elements == null)
    {
      throw new NullPointerException("Band.addElements(...): collection is null.");
    }

    final Iterator iterator = elements.iterator();
    while (iterator.hasNext())
    {
      final Element element = (Element) iterator.next();
      addElement(element);
    }
  }

  /**
   * Returns the first element in the list that is known by the given name. Functions should use
   * {@link org.jfree.report.function.FunctionUtilities#findAllElements(Band, String)} or
   * {@link org.jfree.report.function.FunctionUtilities#findElement(Band, String)} instead. 
   *
   * @param name the element name.
   * @return the first element with the specified name, or <code>null</code> if there is no such element.
   * @throws NullPointerException if the given name is null.
   */
  public Element getElement(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Band.getElement(...): name is null.");
    }

    final Element[] elements = getElementArray();
    final int elementsSize = elements.length;
    for (int i = 0; i < elementsSize; i++)
    {
      final Element e = elements[i];
      final String elementName = e.getName();
      if (elementName != null)
      {
        if (elementName.equals(name))
        {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Removes an element from the band.
   *
   * @param e the element to be removed.
   * @throws NullPointerException if the given element is null.
   */
  public void removeElement(final Element e)
  {
    if (e == null)
    {
      throw new NullPointerException();
    }
    if (e.getParent() != this)
    {
      // this is none of my childs, ignore the request ...
      return;
    }

    e.setParent(null);
    allElements.remove(e);
    allElementsCached = null;
  }

  /**
   * Returns all child-elements of this band as immutable list.
   *
   * @return an immutable list of all registered elements for this band.
   * @deprecated use <code>getElementArray()</code> instead.
   */
  public List getElements()
  {
    return Collections.unmodifiableList(allElements);
  }

  /**
   * Returns the number of elements in this band.
   *
   * @return the number of elements of this band.
   */
  public int getElementCount()
  {
    return allElements.size();
  }

  /**
   * Returns an array of the elements in the band. If the band is empty, an empty array is returned.
   * <p/>
   * For performance reasons, a shared cached instance is returned. Do not modify the returned array or live with the
   * consquences.
   *
   * @return the elements.
   */
  public Element[] getElementArray()
  {
    if (allElementsCached == null)
    {
      if (allElements.isEmpty())
      {
        allElementsCached = Band.EMPTY_ARRAY;
      }
      else
      {
        Element[] elements = new Element[allElements.size()];
        elements = (Element[]) allElements.toArray(elements);
        allElementsCached = elements;
      }
    }
    return allElementsCached;
  }

  /**
   * Returns the element stored add the given index.
   *
   * @param index the element position within this band
   * @return the element
   * @throws IndexOutOfBoundsException if the index is invalid.
   */
  public Element getElement(final int index)
  {
    if (allElementsCached == null)
    {
      if (allElements.isEmpty())
      {
        allElementsCached = Band.EMPTY_ARRAY;
      }
      else
      {
        Element[] elements = new Element[allElements.size()];
        elements = (Element[]) allElements.toArray(elements);
        allElementsCached = elements;
      }
    }
    return allElementsCached[index];
  }

  /**
   * Returns a string representation of the band, useful mainly for debugging
   * purposes.
   *
   * @return a string representation of this band.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    b.append("={name=\"");
    b.append(getName());
    b.append("\", size=\"");
    b.append(allElements.size());
    b.append("\"}");
    return b.toString();
  }

  /**
   * Clones this band and all elements contained in this band. After the cloning the band is no longer connected to a
   * report definition.
   *
   * @return the clone of this band.
   * @throws CloneNotSupportedException if this band or an element contained in this band does not support cloning.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final Band b = (Band) super.clone();

    final int elementSize = allElements.size();
    b.allElements = new ArrayList(elementSize);
    b.allElementsCached = new Element[elementSize];

    if (allElementsCached != null)
    {
      for (int i = 0; i < allElementsCached.length; i++)
      {
        final Element eC = (Element) allElementsCached[i].clone();
        b.allElements.add(eC);
        b.allElementsCached[i] = eC;
        eC.setParent(b);
      }
    }
    else
    {
      for (int i = 0; i < elementSize; i++)
      {
        final Element e = (Element) allElements.get(i);
        final Element eC = (Element) e.clone();
        b.allElements.add(eC);
        b.allElementsCached[i] = eC;
        eC.setParent(b);
      }
    }
    return b;
  }

  /**
   * Returns the content type of the element. For bands, the content type is by default &quot;X-container&quot;.
   *
   * @return the content type
   * @deprecated we no longer use the content-type.
   */
  public String getContentType()
  {
    return Band.CONTENT_TYPE;
  }

  /**
   * Returns, whether the page layout manager should perform a pagebreak before this page is printed. This will have no
   * effect on empty pages or if the band is no root-level band.
   *
   * @return true, if to force a pagebreak before this band is printed, false otherwise
   */
  public boolean isPagebreakBeforePrint()
  {
    return getStyle().getBooleanStyleProperty (BandStyleKeys.PAGEBREAK_BEFORE);
  }

  /**
   * Defines, whether the page layout manager should perform a pagebreak before this page is printed. This will have no
   * effect on empty pages or if the band is no root-level band.
   *
   * @param pagebreakBeforePrint set to true, if to force a pagebreak before this band is printed, false otherwise
   */
  public void setPagebreakBeforePrint(final boolean pagebreakBeforePrint)
  {
    getStyle().setBooleanStyleProperty (BandStyleKeys.PAGEBREAK_BEFORE, pagebreakBeforePrint);
  }

  /**
   * Returns, whether the page layout manager should perform a pagebreak before this page is printed. This will have no
   * effect on empty pages or if the band is no root-level band.
   *
   * @return true, if to force a pagebreak before this band is printed, false otherwise
   */
  public boolean isPagebreakAfterPrint()
  {
    return getStyle().getBooleanStyleProperty (BandStyleKeys.PAGEBREAK_AFTER);
  }

  /**
   * Defines, whether the page layout manager should perform a pagebreak before this page is printed. This will have no
   * effect on empty pages or if the band is no root-level band.
   *
   * @param pagebreakAfterPrint set to true, if to force a pagebreak before this band is printed, false otherwise
   */
  public void setPagebreakAfterPrint(final boolean pagebreakAfterPrint)
  {
    getStyle().setBooleanStyleProperty (BandStyleKeys.PAGEBREAK_AFTER, pagebreakAfterPrint);
  }

  /**
   * Assigns the report definition to this band.
   *
   * @param reportDefinition the report definition or null, if the band is not part of a valid report definition.
   */
  protected void setReportDefinition(final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
    final Element[] elements = getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].setReportDefinition(reportDefinition);
    }
  }
}
