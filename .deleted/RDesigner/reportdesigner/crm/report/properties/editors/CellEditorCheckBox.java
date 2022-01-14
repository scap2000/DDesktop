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

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 12:08:48
 */
public class CellEditorCheckBox extends JCheckBox
{
    public CellEditorCheckBox()
    {
    }


    public boolean isOpaque()
    {
        Color back = getBackground();
        Component p = getParent();
        if (p != null)
        {
            p = p.getParent();
        }
        // p should now be the JTable.
        boolean colorMatch = (back != null) && (p != null) &&
                             back.equals(p.getBackground()) &&
                             p.isOpaque();
        return !colorMatch && super.isOpaque();
    }
}
