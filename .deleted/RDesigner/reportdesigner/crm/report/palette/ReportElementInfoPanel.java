/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.palette;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 11:59:07
 */
public class ReportElementInfoPanel extends JLabel
{
    @NotNull
    private ReportElementInfo reportElementInfo;
    @NotNull
    private Color selectionForeground;
    @NotNull
    private Color selectionBackground;
    @NotNull
    private Color normalForeground;
    @NotNull
    private Color normalBackground;
    @NotNull
    private Border focusBorder;
    @NotNull
    private Border noFocusBorder;


    public ReportElementInfoPanel(@NotNull ReportElementInfo reportElementInfo,
                                  @NotNull Color selectionForeground,
                                  @NotNull Color selectionBackground,
                                  @NotNull Color normalForeground,
                                  @NotNull Color normalBackground,
                                  @NotNull Border focusBorder,
                                  @NotNull Border noFocusBorder)
    {
        this.reportElementInfo = reportElementInfo;
        this.selectionForeground = selectionForeground;
        this.selectionBackground = selectionBackground;
        this.normalForeground = normalForeground;
        this.normalBackground = normalBackground;
        this.focusBorder = focusBorder;
        this.noFocusBorder = noFocusBorder;

        setOpaque(true);

        setIcon(reportElementInfo.getIcon());
        setText(reportElementInfo.getTitle());

        setHighlighted(false, false);

        addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                setHighlighted(true, false);
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                setHighlighted(true, true);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                setHighlighted(true, false);
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                setHighlighted(false, false);
            }
        });
    }


    @NotNull
    public ReportElementInfo getReportElementInfo()
    {
        return reportElementInfo;
    }


    private void setHighlighted(boolean highlighted, boolean pressed)
    {
        if (highlighted)
        {
            setBorder(focusBorder);
            if (pressed)
            {
                setForeground(selectionForeground);
                setBackground(selectionBackground);
            }
            else
            {
                setForeground(normalForeground);
                setBackground(normalBackground);
            }
        }
        else
        {
            setBorder(noFocusBorder);
            setForeground(normalForeground);
            setBackground(normalBackground);
        }
    }


    public void dropCompleted()
    {
        setHighlighted(false, false);
    }
}
