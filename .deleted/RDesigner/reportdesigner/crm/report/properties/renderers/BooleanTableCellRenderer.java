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
package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UIConstants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class BooleanTableCellRenderer extends JCheckBox implements TableCellRenderer
{

    @NotNull
    protected static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    // We need a place to store the color the JLabel should be returned
    // to after its foreground and background colors have been set
    // to the selection background color.
    // These ivars will be made protected when their names are finalized.
    @Nullable
    private Color unselectedForeground;
    @Nullable
    private Color unselectedBackground;


    /**
     * Creates a default table cell renderer.
     */
    public BooleanTableCellRenderer()
    {
        super();
        setOpaque(true);
        setBorder(noFocusBorder);
    }


    /**
     * Overrides <code>JComponent.setForeground</code> to assign
     * the unselected-foreground color to the specified color.
     *
     * @param c set the foreground color to this value
     */
    public void setForeground(@Nullable Color c)
    {
        super.setForeground(c);
        unselectedForeground = c;
    }


    /**
     * Overrides <code>JComponent.setBackground</code> to assign
     * the unselected-background color to the specified color.
     *
     * @param c set the background color to this value
     */
    public void setBackground(@Nullable Color c)
    {
        super.setBackground(c);
        unselectedBackground = c;
    }


    /**
     * Notification from the <code>UIManager</code> that the look and feel
     * [L&F] has changed.
     * Replaces the current UI object with the latest version from the
     * <code>UIManager</code>.
     *
     * @see JComponent#updateUI
     */
    public void updateUI()
    {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    // implements javax.swing.table.TableCellRenderer


    /**
     * Returns the default table cell renderer.
     *
     * @param table      the <code>JTable</code>
     * @param value      the value to assign to the cell at
     *                   <code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus   true if cell has focus
     * @param row        the row of the cell to render
     * @param column     the column of the cell to render
     * @return the default table cell renderer
     */
    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column)
    {

        if (isSelected)
        {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }
        else
        {
            super.setForeground((unselectedForeground != null) ? unselectedForeground
                                : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground
                                : table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus)
        {
            Border border = null;
            if (isSelected)
            {
                border = UIManager.getBorder(UIConstants.TABLE_FOCUS_SELECTED_CELL_HIGHLIGHT_BORDER);
            }
            if (border == null)
            {
                border = UIManager.getBorder(UIConstants.TABLE_FOCUS_CELL_HIGHLIGHT_BORDER);
            }
            setBorder(border);

            if (!isSelected && table.isCellEditable(row, column))
            {
                Color col;
                col = UIManager.getColor(UIConstants.TABLE_FOCUS_CELL_FOREGROUND);
                if (col != null)
                {
                    super.setForeground(col);
                }
                col = UIManager.getColor(UIConstants.TABLE_FOCUS_CELL_BACKGROUND);
                if (col != null)
                {
                    super.setBackground(col);
                }
            }
        }
        else
        {
            setBorder(noFocusBorder);
        }

        setValue(value);

        return this;
    }

    /*
    * The following methods are overridden as a performance measure to
    * to prune code-paths are often called in the case of renders
    * but which we know are unnecessary.  Great care should be taken
    * when writing your own renderer to weigh the benefits and
    * drawbacks of overriding methods like these.
    */


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
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


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
    public void invalidate()
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    public void validate()
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    public void revalidate()
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    public void repaint(long tm, int x, int y, int width, int height)
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    public void repaint(@NotNull Rectangle r)
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
    public void repaint()
    {
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    protected void firePropertyChange(@NotNull String propertyName, @Nullable Object oldValue, @Nullable Object newValue)
    {
        // Strings get interned...
        if (PropertyKeys.TEXT.equals(propertyName))
        {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    public void firePropertyChange(@NotNull String propertyName, boolean oldValue, boolean newValue)
    {
    }


    /**
     * Sets the <code>String</code> object for the cell being rendered to
     * <code>value</code>.
     *
     * @param value the string value for this cell; if value is
     *              <code>null</code> it sets the text value to an empty string
     * @see JLabel#setText
     */
    protected void setValue(@Nullable Object value)
    {
        if (value instanceof HashSet)
        {
            HashSet hashSet = (HashSet) value;
            if (hashSet.size() > 1)
            {
                setText(TranslationManager.getInstance().getTranslation("R", "Property.MultipleValues"));
                setEnabled(false);
            }

            if (hashSet.size() == 1)
            {
                Boolean selected = (Boolean) hashSet.iterator().next();
                if (selected == null)
                {
                    selected = Boolean.FALSE;
                }
                setSelected(selected.booleanValue());
                setEnabled(true);
                if (selected.booleanValue())
                {
                    setText(TranslationManager.getInstance().getTranslation("R", "Property.Boolean.True"));
                }
                else
                {
                    setText(TranslationManager.getInstance().getTranslation("R", "Property.Boolean.False"));
                }
            }
        }
    }


    public static class UIResource extends BooleanTableCellRenderer
            implements javax.swing.plaf.UIResource
    {
    }


}
