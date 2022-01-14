package org.jfree.report.function.sys;

import org.jfree.report.Band;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractElementFormatFunction;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.states.LayoutProcess;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;

/**
 * This function is used to generate sheet names into destTable exports. Sheet names are generated on page breaks and have
 * different representations depending the export type.<br/>
 * To use this functionnality report configuration must set the property {@link #DECALRED_SHEETNAME_FUNCTION_KEY}to
 * point to an existing function or property accessible within the report.
 * <p/>
 * As for example using simple report definition:<br/>
 * <pre>
 * &lt;report&gt;
 * &lt;configuration&gt;
 * &lt;!-- where sheetNameExpression is pointing to a valid function declared in this report --&gt;
 * &lt;property name="org.jfree.report.targets.destTable.TableWriter.SheetNameFunction"&gtsheetNameExpression&lt;/property&gt;
 * &lt;/configuration&gt;
 * ...
 * &lt;/report&gt;
 * </pre>
 *
 * @author Cedric Pronzato
 */
public class SheetNameFunction extends AbstractElementFormatFunction implements StructureFunction
{
  /**
   * The configuration property declaring the function name to call in order to generate sheet names.<br/>
   */
  private static final String DECALRED_SHEETNAME_FUNCTION_KEY =
      "org.jfree.report.targets.table.TableWriter.SheetNameFunction";

  /** A property that holds the last computed value of the sheetname function. */
  private transient String lastValue;
  private transient String functionToCall;

  /**
   * Default constructor.
   */
  public SheetNameFunction()
  {
  }


  public void reportInitialized(final ReportEvent event)
  {
    super.reportInitialized(event);
    functionToCall = this.getReportConfiguration().getConfigProperty(DECALRED_SHEETNAME_FUNCTION_KEY);
  }

  /**
   * Overrides the dependency level to only execute this function on the pagination and content-generation level.
   * @return LayoutProcess.LEVEL_PAGINATE.
   */
  public int getDependencyLevel()
  {
    return LayoutProcess.LEVEL_PAGINATE;
  }

  /**
   * Sets the sheet name value to the current <code>Band</code>
   * {@link org.jfree.report.style.BandStyleKeys#COMPUTED_SHEETNAME} style key.
   *
   * @param b The current band element.
   */
  protected void processRootBand(final Band b)
  {
    lastValue = null;
    // if exporting to a table/* export
    if (getRuntime().getExportDescriptor().startsWith("table/"))
    {
      if (functionToCall != null)
      {
        // evaluating the declared sheetname function
        final Object value = this.getDataRow().get(functionToCall);
        if (value == null)
        {
          Log.debug("Cannot find the sheetname function/property referenced by '" + functionToCall + '\'');
        }
        else
        {
          // setting the value as a style property
          lastValue = value.toString();
          b.getStyle().setStyleProperty(BandStyleKeys.COMPUTED_SHEETNAME, lastValue);
        }
      }
    }
  }

  /**
   * Structure functions do not care of the result so this method should never be called.
   *
   * @return <code>null</code>
   */
  public Object getValue()
  {
    return lastValue;
  }
}
