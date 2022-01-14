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
 * ItemSumFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.math.BigDecimal;

import org.jfree.report.event.ReportEvent;
import org.jfree.util.Log;

/**
 * A report function that calculates the sum of one field (destColumn) from the data-row. This function produces a running
 * total, no global total. For a global sum, use the TotalGroupSumFunction function. The function can be used in two
 * ways: <ul> <li>to calculate a sum for the entire report;</li> <li>to calculate a sum within a particular group;</li>
 * </ul> This function expects its input values to be either java.lang.Number instances or Strings that can be parsed to
 * java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands two parameters, the <code>field</code> parameter is required and denotes the name of an
 * ItemBand-field which gets summed up.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is started, the counter gets reseted to
 * null.
 *
 * @author Thomas Morgner
 */
public class ItemSumFunction extends AbstractFunction
{
  /**
   * A useful constant representing zero.
   */
  protected static final BigDecimal ZERO = new BigDecimal(0.0);

  /**
   * The item sum.
   */
  private transient BigDecimal sum;
  /**
   * The name of the group on which to reset the count. This can be set to null to compute the sum for the whole
   * report.
   */
  private String group;
  /**
   * The name of the field from where to read the values.
   */
  private String field;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation will fail.
   */
  public ItemSumFunction()
  {
    sum = ZERO;
  }

  /**
   * Constructs a named function. <P> The field must be defined before using the function.
   *
   * @param name The function name.
   */
  public ItemSumFunction(final String name)
  {
    this();
    setName(name);
  }

  /**
   * Receives notification that a new report is about to start. <P> Does nothing.
   *
   * @param event Information about the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    this.sum = ZERO;
  }

  /**
   * Receives notification that a new group is about to start.  If this is the group defined for the function, then the
   * running total is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event))
    {
      this.sum = ZERO;
    }
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Sets the group name. <P> If a group is defined, the running total is reset to zero at the start of every instance
   * of this group.
   *
   * @param name the group name (null permitted).
   */
  public void setGroup(final String name)
  {
    this.group = name;
  }

  /**
     * Returns the field used by the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @return The field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Sets the field name for the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
     * Receives notification that a row of data is being processed.  Reads the data from the field defined for this
     * function and adds it to the running total. <P> This function assumes that it will find an instance of the Number
     * class in the destColumn of the data-row specified by the field name.
     *
     * @param event Information about the event.
     */
  public void itemsAdvanced(final ReportEvent event)
  {
    final Object fieldValue = getDataRow().get(getField());
    if (fieldValue == null)
    {
      return;
    }
    if (fieldValue instanceof Number == false)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
      return;
    }

    final Number n = (Number) fieldValue;
    sum = sum.add(new BigDecimal(n.toString()));
  }

  /**
     * Returns the function value, in this case the running total of a specific destColumn in the report's data-row.
     *
     * @return The function value.
     */
  public Object getValue()
  {
    return sum;
  }

  /**
   * Returns the current sum.
   *
   * @return the current sum.
   */
  protected BigDecimal getSum()
  {
    return sum;
  }

  /**
   * Defines the current sum.
   *
   * @param sum the current sum.
   */
  protected void setSum(final BigDecimal sum)
  {
    if (sum == null)
    {
      throw new NullPointerException("Sum must not be null");
    }
    this.sum = sum;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final ItemSumFunction function = (ItemSumFunction) super.getInstance();
    function.sum = ZERO;
    return function;
  }

}
