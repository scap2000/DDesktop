package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataFactory;

/**
 * User: Martin
 * Date: 06.03.2007
 * Time: 17:46:22
 */
public interface ExtendedDataFactory extends DataFactory
{
    boolean canExecuteQuery(@Nullable String query);
}
