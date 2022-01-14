package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 01.01.2007
 * Time: 17:44:57
 */
public interface ReportDesignerWindowsListener
{
    void reportDialogOpened(@NotNull ReportDialog reportDialog);


    void reportDialogClosed(@NotNull ReportDialog reportDialog);
}
