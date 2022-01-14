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
package org.pentaho.reportdesigner.crm.report.wizard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * User: Martin
 * Date: 10.02.2006
 * Time: 14:58:15
 */
public class WizardHeaderComponent extends JPanel
{
    @NotNull
    private JLabel iconLabel;
    @NotNull
    private JLabel titleLabel;
    @NotNull
    private JLabel descriptionLabel;


    public WizardHeaderComponent()
    {
        setOpaque(true);
        setBackground(Color.WHITE);

        setLayout(new BorderLayout(10, 20));
        iconLabel = new JLabel();
        iconLabel.setBackground(Color.WHITE);
        add(iconLabel, BorderLayout.WEST);

        JPanel helperPanel = new JPanel(new BorderLayout(0, 10));
        helperPanel.setBackground(Color.WHITE);
        helperPanel.setOpaque(true);

        titleLabel = new JLabel("TitleLabel");//NON-NLS
        titleLabel.setFont(FontUtils.getDerivedFont(titleLabel.getFont(), Font.BOLD, titleLabel.getFont().getSize() + 2));
        titleLabel.setBackground(Color.WHITE);
        helperPanel.add(titleLabel, BorderLayout.NORTH);
        descriptionLabel = new JLabel("descriptionLabel");//NON-NLS
        descriptionLabel.setBackground(Color.WHITE);
        helperPanel.add(descriptionLabel, BorderLayout.CENTER);

        add(helperPanel, BorderLayout.CENTER);

        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBorder(border);
    }


    public void setIcon(@Nullable ImageIcon imageIcon)
    {
        iconLabel.setIcon(imageIcon);
    }


    public void setTitle(@Nullable String title)
    {
        if (title != null && title.length() > 0)
        {
            titleLabel.setText(title);
        }
        else
        {
            titleLabel.setText(" ");
        }
    }


    public void setDescription(@Nullable String description)
    {
        if (description != null && description.length() > 0)
        {
            descriptionLabel.setText(description);
        }
        else
        {
            descriptionLabel.setText(" ");
        }
    }


}
