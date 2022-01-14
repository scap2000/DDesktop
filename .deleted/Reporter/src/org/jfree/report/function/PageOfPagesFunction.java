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
 * PageOfPagesFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.text.MessageFormat;

import java.util.Locale;

import org.jfree.report.event.ReportEvent;
import org.jfree.util.ObjectUtilities;

/**
 * A report function that combines {@link PageFunction}and {@link PageTotalFunction}. Restrictions for both classes
 * apply to this one also.
 *
 * @author J&ouml;rg Schaible
 */
public class PageOfPagesFunction extends PageFunction
{
  /**
   * A internal function delegate that computes the total number of pages.
   */
  private PageTotalFunction pageTotalFunction;
  /**
   * The message format pattern.
   */
  private String format;

  /**
   * An internal cached value holding the last locale used by the function.
   */
  private transient Locale lastLocale;
  /**
   * An internal cached value holding the last page seen by the function.
   */
  private transient Integer lastPage;
  /**
   * An internal cached value holding the last total-pages seen by the function.
   */
  private transient Integer lastTotalPage;
  /**
   * An internal cached value holding the message format object.
   */
  private transient MessageFormat messageFormat;
  /**
   * An internal cached value holding the last message that has been computed.
   */
  private transient String lastMessage;

  /**
   * Default Constructor.
   */
  public PageOfPagesFunction()
  {
    this.pageTotalFunction = new PageTotalFunction();
    this.format = "{0} / {1}";
  }

  /**
   * Constructs a named function.
   *
   * @param name the function name.
   */
  public PageOfPagesFunction(final String name)
  {
    this();
    setName(name);
  }

  /**
   * Returns the format used to print the value. The default format is &quot;{0} / {1}&quot;.
   *
   * @return the format string.
   * @see MessageFormat
   */
  public String getFormat()
  {
    return format;
  }

  /**
   * Set the format of the value. The format should follow the rules of {@link MessageFormat}. The first parameter is
   * filled with the current page, the second with the total number of pages.
   *
   * @param format the format string.
   */
  public void setFormat(final String format)
  {
    if (format == null)
    {
      throw new NullPointerException("Format must not be null.");
    }
    this.format = format;
    this.messageFormat = null;
  }

  /**
   * Forwards the report event to both the base class and the page-total function delegate.
   *
   * @param event the received report event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    super.reportInitialized(event);
    pageTotalFunction.reportInitialized(event);
  }

  /**
   * Forwards the report event to both the base class and the page-total function delegate.
   *
   * @param event the received report event.
   */
  public void pageStarted(final ReportEvent event)
  {
    super.pageStarted(event);
    pageTotalFunction.pageStarted(event);
  }

  /**
   * Forwards the report event to both the base class and the page-total function delegate.
   *
   * @param event the received report event.
   */
  public void pageFinished(final ReportEvent event)
  {
    super.pageFinished(event);
    pageTotalFunction.pageFinished(event);
  }

  /**
   * Forwards the report event to both the base class and the page-total function delegate.
   *
   * @param event the received report event.
   */
  public void groupStarted(final ReportEvent event)
  {
    super.groupStarted(event);
    pageTotalFunction.groupStarted(event);
  }

  public void groupFinished(final ReportEvent event)
  {
    super.groupFinished(event);
    pageTotalFunction.groupFinished(event);
  }

  /**
   * Return the value of this {@link Function}. The method uses the format definition from the properties and adds the
   * current page and the total number of pages as parameter.
   *
   * @return the formatted value with current page and total number of pages.
   */
  public Object getValue()
  {
    final Integer page = (Integer) super.getValue();
    final Integer pages = (Integer) pageTotalFunction.getValue();
    Locale locale = getResourceBundleFactory().getLocale();
    if (locale == null)
    {
      locale = Locale.getDefault();
    }

    if (messageFormat == null || ObjectUtilities.equal(locale, lastLocale) == false)
    {
      this.messageFormat = new MessageFormat(getFormat());
      this.messageFormat.setLocale(locale);
      this.messageFormat.applyPattern(getFormat());
      this.lastLocale = locale;
    }

    if (lastMessage == null ||
        ObjectUtilities.equal(page, this.lastPage) == false ||
        ObjectUtilities.equal(pages, this.lastTotalPage) == false)
    {
      this.lastMessage = messageFormat.format(new Object[]{page, pages});
      this.lastPage = page;
      this.lastTotalPage = pages;
    }
    return lastMessage;
  }

  /**
   * Sets the name of the group that the function acts upon.
   *
   * @param group the group name.
   */
  public void setGroup(final String group)
  {
    super.setGroup(group);
    pageTotalFunction.setGroup(group);
  }

  /**
   * Defines the page number where the counting starts.
   *
   * @param startPage the page number of the first page.
   */
  public void setStartPage(final int startPage)
  {
    super.setStartPage(startPage);
    pageTotalFunction.setStartPage(startPage);
  }

  /**
   * Defines the defined dependency level. For page functions, this level can be as low as the pagination level.
   *
   * @param level the dependency level.
   */
  public void setDependencyLevel(final int level)
  {
    super.setDependencyLevel(level);
    pageTotalFunction.setDependencyLevel(level);
  }

  /**
   * Returns the defined dependency level. For page functions, this level can be as low as the pagination level.
   *
   * @return the dependency level.
   */
  public int getDependencyLevel()
  {
    return pageTotalFunction.getDependencyLevel();
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function. Only the datarow may be shared.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final PageOfPagesFunction function = (PageOfPagesFunction) super.getInstance();
    function.pageTotalFunction = (PageTotalFunction) pageTotalFunction.getInstance();
    return function;
  }

  /**
   * Defines the ExpressionRune used in this expression. The ExpressionRuntime is set before the expression receives
   * events or gets evaluated and is unset afterwards. Do not hold references on the runtime or you will create
   * memory-leaks.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(final ExpressionRuntime runtime)
  {
    super.setRuntime(runtime);
    pageTotalFunction.setRuntime(runtime);
  }

  /**
   * Creates a copy of the function.
   *
   * @return the clone.
   * @throws CloneNotSupportedException if an error occurs.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final PageOfPagesFunction function = (PageOfPagesFunction) super.clone();
    function.pageTotalFunction = (PageTotalFunction) pageTotalFunction.clone();
    return function;
  }
}
