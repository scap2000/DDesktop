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
package org.pentaho.reportdesigner.crm.report.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.client.util.UIConstants;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;


public class MultiLabelTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    @Nullable
    private JTree tree;
    protected boolean selected;
    protected boolean hasFocus;
    private boolean drawsFocusBorderAroundIcon;
    private boolean drawDashedFocusIndicator;
    @Nullable
    private Color treeBGColor;
    @Nullable
    private Color focusBGColor;
    @Nullable
    protected Color textSelectionColor;
    @Nullable
    protected Color textNonSelectionColor;
    @Nullable
    protected Color backgroundSelectionColor;
    @Nullable
    protected Color backgroundNonSelectionColor;
    @Nullable
    protected Color borderSelectionColor;

    @Nullable
    private JLabel[] labels;


    public MultiLabelTreeCellRenderer(@NotNull JLabel[] labels)
    {
        this.labels = labels;

        for (JLabel label : labels)
        {
            label.setHorizontalAlignment(JLabel.LEFT);
        }

        setLayout(new BorderLayout(5, 5));

        add(labels[0], BorderLayout.WEST);
        add(labels[1], BorderLayout.CENTER);
        add(labels[2], BorderLayout.EAST);

        setTextSelectionColor(UIManager.getColor(UIConstants.TREE_SELECTION_FOREGROUND));
        setTextNonSelectionColor(UIManager.getColor(UIConstants.TREE_TEXT_FOREGROUND));
        setBackgroundSelectionColor(UIManager.getColor(UIConstants.TREE_SELECTION_BACKGROUND));
        setBackgroundNonSelectionColor(UIManager.getColor(UIConstants.TREE_TEXT_BACKGROUND));
        setBorderSelectionColor(UIManager.getColor(UIConstants.TREE_SELECTION_BORDER_COLOR));
        Object value = UIManager.get(UIConstants.TREE_DRAWS_FOCUS_BORDER_AROUND_ICON);
        drawsFocusBorderAroundIcon = (value != null && ((Boolean) value).booleanValue());
        value = UIManager.get(UIConstants.TREE_DRAW_DASHED_FOCUS_INDICATOR);
        drawDashedFocusIndicator = (value != null && ((Boolean) value).booleanValue());

        setOpaque(false);
        for (JLabel label : labels)
        {
            label.setOpaque(false);
        }
    }


    @Nullable
    public JLabel[] getLabels()
    {
        return labels;
    }


    public void setTextSelectionColor(@Nullable Color newColor)
    {
        textSelectionColor = newColor;
    }


    @Nullable
    public Color getTextSelectionColor()
    {
        return textSelectionColor;
    }


    public void setTextNonSelectionColor(@Nullable Color newColor)
    {
        textNonSelectionColor = newColor;
    }


    @Nullable
    public Color getTextNonSelectionColor()
    {
        return textNonSelectionColor;
    }


    public void setBackgroundSelectionColor(@Nullable Color newColor)
    {
        backgroundSelectionColor = newColor;
    }


    @Nullable
    public Color getBackgroundSelectionColor()
    {
        return backgroundSelectionColor;
    }


    public void setBackgroundNonSelectionColor(@Nullable Color newColor)
    {
        backgroundNonSelectionColor = newColor;
    }


    @Nullable
    public Color getBackgroundNonSelectionColor()
    {
        return backgroundNonSelectionColor;
    }


    public void setBorderSelectionColor(@Nullable Color newColor)
    {
        borderSelectionColor = newColor;
    }


    @Nullable
    public Color getBorderSelectionColor()
    {
        return borderSelectionColor;
    }


    public void setFont(@Nullable Font font)
    {
        if (font instanceof FontUIResource)
            font = null;
        super.setFont(font);
    }


    @Nullable
    public Font getFont()
    {
        Font font = super.getFont();

        if (font == null && tree != null)
        {
            // Strive to return a non-null value, otherwise the html support
            // will typically pick up the wrong font in certain situations.
            font = tree.getFont();
        }
        return font;
    }


    public void setBackground(@Nullable Color color)
    {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
        if (labels != null)
        {
            for (JLabel label : labels)
            {
                label.setBackground(color);
            }
        }
    }


    @NotNull
    public Component getTreeCellRendererComponent(@NotNull JTree tree,
                                                  @Nullable Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus)
    {
        try
        {
            String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);

            this.tree = tree;
            this.hasFocus = hasFocus;
            JLabel[] labels = this.labels;
            if (labels == null)
            {
                return this;
            }
            labels[0].setText(stringValue);
            for (int i = 1; i < labels.length; i++)
            {
                JLabel label = labels[i];
                label.setText("");
            }
            if (sel)
            {
                setForeground(getTextSelectionColor());
                for (JLabel label : labels)
                {
                    label.setForeground(getTextSelectionColor());
                }
            }
            else
            {
                setForeground(getTextNonSelectionColor());
                for (JLabel label : labels)
                {
                    label.setForeground(getTextNonSelectionColor());
                }
            }
            // There needs to be a way to specify disabled icons.
            if (!tree.isEnabled())
            {
                setEnabled(false);
                for (JLabel label : labels)
                {
                    label.setEnabled(false);
                }
            }
            else
            {
                setEnabled(true);
                for (JLabel label : labels)
                {
                    label.setEnabled(true);
                }
            }
            setComponentOrientation(tree.getComponentOrientation());
            for (JLabel label : labels)
            {
                label.setComponentOrientation(tree.getComponentOrientation());
            }

            selected = sel;
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

        return this;
    }


    public void paint(@NotNull Graphics g)
    {
        Color bColor;

        if (selected)
        {
            bColor = getBackgroundSelectionColor();
        }
        else
        {
            bColor = getBackgroundNonSelectionColor();
            if (bColor == null)
                bColor = getBackground();
        }
        int imageOffset = -1;
        if (bColor != null)
        {
            imageOffset = getLabelStart();
            g.setColor(bColor);
            if (getComponentOrientation().isLeftToRight())
            {
                g.fillRect(imageOffset, 0, getWidth() - imageOffset,
                           getHeight());
            }
            else
            {
                g.fillRect(0, 0, getWidth() - imageOffset,
                           getHeight());
            }
        }

        if (hasFocus)
        {
            if (drawsFocusBorderAroundIcon)
            {
                imageOffset = 0;
            }
            else if (imageOffset == -1)
            {
                imageOffset = getLabelStart();
            }
            if (getComponentOrientation().isLeftToRight())
            {
                paintFocus(g, imageOffset, 0, getWidth() - imageOffset,
                           getHeight());
            }
            else
            {
                paintFocus(g, 0, 0, getWidth() - imageOffset, getHeight());
            }
        }
        super.paint(g);
    }


    private void paintFocus(@NotNull Graphics g, int x, int y, int w, int h)
    {
        Color bsColor = getBorderSelectionColor();

        if (bsColor != null && (selected || !drawDashedFocusIndicator))
        {
            g.setColor(bsColor);
            g.drawRect(x, y, w - 1, h - 1);
        }
        if (drawDashedFocusIndicator)
        {
            Color color;
            if (selected)
            {
                color = getBackgroundSelectionColor();
            }
            else
            {
                color = getBackgroundNonSelectionColor();
                if (color == null)
                {
                    color = getBackground();
                }
            }

            //noinspection ObjectEquality
            if (treeBGColor != color)
            {
                treeBGColor = color;
                if (color != null)
                {
                    focusBGColor = new Color(~color.getRGB());
                }
            }
            g.setColor(focusBGColor);
            BasicGraphicsUtils.drawDashedRect(g, x, y, w, h);
        }
    }


    private int getLabelStart()
    {
        if (labels != null)
        {
            JLabel label = labels[0];
            Icon currentI = label.getIcon();
            if (currentI != null && label.getText() != null)
            {
                return currentI.getIconWidth() + Math.max(0, label.getIconTextGap() - 1);
            }
        }

        return 0;
    }


    @Nullable
    public Dimension getPreferredSize()
    {
        Dimension retDimension = super.getPreferredSize();

        if (retDimension != null)
            retDimension = new Dimension(retDimension.width + 3,
                                         retDimension.height);
        return retDimension;
    }


    public void repaint(long tm, int x, int y, int width, int height)
    {
    }


    public void repaint(@NotNull Rectangle r)
    {
    }


    public void repaint()
    {
    }


    protected void firePropertyChange(@NotNull String propertyName, @Nullable Object oldValue, @Nullable Object newValue)
    {
        //noinspection StringEquality
        if (propertyName == PropertyKeys.TEXT)
            super.firePropertyChange(propertyName, oldValue, newValue);
    }


    public void firePropertyChange(@NotNull String propertyName, byte oldValue, byte newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, char oldValue, char newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, short oldValue, short newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, int oldValue, int newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, long oldValue, long newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, float oldValue, float newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, double oldValue, double newValue)
    {
    }


    public void firePropertyChange(@NotNull String propertyName, boolean oldValue, boolean newValue)
    {
    }

}
