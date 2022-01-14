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
package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisElementBorderDefinition extends CellEditorJLabelWithEllipsis<ElementBorderDefinition>
{

    public CellEditorJLabelWithEllipsisElementBorderDefinition()
    {
        remove(label);//remove old label (see super)
        label = new JLabelBorderDefinition();
        add(label, BorderLayout.CENTER);
    }


    public void setValue(@Nullable ElementBorderDefinition borderDefinition, boolean commit)
    {
        super.setValue(borderDefinition, commit);
        ((JLabelBorderDefinition) label).setElementBorderDefinition(borderDefinition);
    }


    @Nullable
    public String convertToText(@Nullable ElementBorderDefinition borderDefinition)
    {
        return null;
    }


    private static class JLabelBorderDefinition extends JLabel
    {
        @NotNull
        private ElementBorderDefinition borderDefinition;


        private JLabelBorderDefinition()
        {
            borderDefinition = new ElementBorderDefinition();
            setText(" ");
        }


        public void setText(@Nullable String text)
        {
            super.setText(" ");
        }


        public void setElementBorderDefinition(@Nullable ElementBorderDefinition borderDefinition)
        {
            if (borderDefinition != null)
            {
                this.borderDefinition = borderDefinition;
            }
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            Stroke origStroke = g2d.getStroke();
            Color origColor = g2d.getColor();

            if (borderDefinition.getTopSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(borderDefinition.getTopSide().getColor());
                g2d.setStroke(borderDefinition.getTopSide().getStroke());

                g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }

            g2d.setStroke(origStroke);
            g2d.setColor(origColor);
        }
    }
}