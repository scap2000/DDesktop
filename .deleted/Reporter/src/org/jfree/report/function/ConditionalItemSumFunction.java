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
 * ConditionalItemSumFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.event.ReportEvent;
import org.jfree.util.ObjectUtilities;

/**
 * A item sum function that only sums up the current value, if the value read from the conditionField is the same as the
 * value from the conditionValue property.
 *
 * @author Thomas Morgner
 */
public class ConditionalItemSumFunction extends ItemSumFunction
{
  /**
     * The name of the data-row destColumn from where to read the comparison value for the condition.
     */
  private String conditionField;
  /**
   * The static comparison value for the condition.
   */
  private Object conditionValue;

  /**
   * Default Constructor.
   */
  public ConditionalItemSumFunction()
  {
  }

  /**
     * Returns the name of the data-row destColumn from where to read the comparison value for the condition.
     *
     * @return a field name.
     */
  public String getConditionField()
  {
    return conditionField;
  }

  /**
     * Defines the name of the data-row destColumn from where to read the comparison value for the condition.
     *
     * @param conditionField a field name.
     */
  public void setConditionField(final String conditionField)
  {
    this.conditionField = conditionField;
  }

  /**
   * Returns the static comparison value for the condition.
   *
   * @return the static value.
   */
  public Object getConditionValue()
  {
    return conditionValue;
  }

  /**
   * Defines the static comparison value for the condition.
   *
   * @param conditionValue the static value.
   */
  public void setConditionValue(final Object conditionValue)
  {
    this.conditionValue = conditionValue;
  }

  /**
   * Receives notification that a row of data is being processed. 
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (getConditionField() == null)
    {
      return;
    }

    final Object currentValue = getDataRow().get(getConditionField());
    // ObjectUtils-equal does not crash if both values are 'null'.
    // You could use the ordinary equals as well, but thats more code to write
    if (ObjectUtilities.equal(currentValue, getConditionValue()))
    {
      // this does the addition of the values.
      super.itemsAdvanced(event);
    }
  }
}
