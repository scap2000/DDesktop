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
package org.pentaho.reportdesigner.lib.client.commands;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;


/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 13:21:43
 */
public class CommandDropDownToolBarButton extends CommandButton implements PropertyChangeListener
{
    @NotNull
    private String iconKey;

    @NotNull
    private JLabel label1;
    @NotNull
    private JLabel label2;
    private boolean actionDropdown;

    @Nullable
    private Presentation lastExecutedPresentation;

    @NotNull
    private ArrayList<Presentation> presentations;


    public CommandDropDownToolBarButton(@NotNull final CommandGroup commandGroup, @NotNull final Presentation presentation, @NotNull String iconKey)
    {
        super(presentation);

        presentations = new ArrayList<Presentation>();

        setHorizontalTextPosition(CommandButton.CENTER);
        setVerticalTextPosition(CommandButton.BOTTOM);
        setRolloverEnabled(true);
        setMargin(new Insets(4, 4, 4, 4));


        label1 = new JLabel();
        label2 = new JLabel(new ImageIcon(CommandDropDownToolBarButton.class.getResource("DropDownIcon.png")));//NON-NLS
        label2.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        setLayout(new ButtonLayout());
        add(label1);
        add(label2);

        this.iconKey = iconKey;

        addListener(commandGroup, presentation);

        setFocusable(false);

        addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                Presentation lastExecutedPresentation = CommandDropDownToolBarButton.this.lastExecutedPresentation;
                if (lastExecutedPresentation != null)
                {
                    lastExecutedPresentation.getCommandApplicationRoot().setStatusText(lastExecutedPresentation.getDescription());
                }
                else
                {
                    presentation.getCommandApplicationRoot().setStatusText(presentation.getDescription());
                }
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                presentation.getCommandApplicationRoot().clearStatusText();
            }
        });

        label1.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }
        });
        label1.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseClicked(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label1, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }
        });


        label2.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }
        });
        label2.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                actionDropdown = true;
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
                label2.setBorder(new DividerBorder());
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                actionDropdown = false;
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
                label2.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
            }


            public void mouseClicked(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                MouseEvent me = SwingUtilities.convertMouseEvent(label2, e, CommandDropDownToolBarButton.this);
                CommandDropDownToolBarButton.this.dispatchEvent(me);
            }
        });

        //noinspection ConstantConditions
        if (commandGroup != null)
        {
            addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Presentation lastExecutedPresentation = CommandDropDownToolBarButton.this.lastExecutedPresentation;
                    if (actionDropdown || lastExecutedPresentation == null || !lastExecutedPresentation.isEnabled())
                    {
                        CommandDropDownPopupMenu commandPopupMenu = new CommandDropDownPopupMenu(presentation.getCommandApplicationRoot(), commandGroup, CommandSettings.getInstance().getToolbarIconKey(), CommandDropDownToolBarButton.this);
                        JPopupMenu popupMenu = commandPopupMenu.getJPopupMenu();
                        CommandDropDownToolBarButton button = CommandDropDownToolBarButton.this;
                        popupMenu.show(button, 0, button.getHeight());
                    }
                    else
                    {
                        //execute seleted command
                        if (lastExecutedPresentation.isEnabled())
                        {
                            DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
                            presentation.getCommandApplicationRoot().clearStatusText();
                            lastExecutedPresentation.getCommand().execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                            CommandManager.dataProviderChanged();
                        }
                    }
                }

            });
        }

        update();
    }


    private void addListener(@NotNull final CommandGroup commandGroup, @NotNull final Presentation presentation)
    {
        Command[] children = commandGroup.getChildren();
        for (Command command : children)
        {
            Presentation pres = command.getTemplatePresentation().getCopy(presentation.getCommandApplicationRoot());
            presentations.add(pres);

            if (pres.isEnabled() && lastExecutedPresentation == null)
            {
                setLastExecutedPresentation(pres);
            }
            else if (pres.isEnabled() && lastExecutedPresentation != null && !lastExecutedPresentation.isEnabled())
            {
                setLastExecutedPresentation(pres);
            }
            pres.addPropertyChangeListener(this);

            if (command instanceof CommandGroup)
            {
                CommandGroup group = (CommandGroup) command;
                addListener(group, presentation);
            }
        }
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {

        Presentation lastExecutedPresentation1 = lastExecutedPresentation;
        if (lastExecutedPresentation1 == null)
        {
            //search an enable presentation
            Presentation p = (Presentation) evt.getSource();
            if (p.isEnabled())
            {
                lastExecutedPresentation = p;
                update();
            }
        }
        else
        {
            Presentation p = (Presentation) evt.getSource();
            if (!lastExecutedPresentation1.isEnabled() && p.isEnabled())
            {
                lastExecutedPresentation = p;
                update();
            }
        }
    }


    public void setLastExecutedPresentation(@Nullable Presentation lastExecutedPresentation)
    {
        this.lastExecutedPresentation = lastExecutedPresentation;
        if (lastExecutedPresentation != null)
        {
            lastExecutedPresentation.addPropertyChangeListener(this);
            update();
        }
    }


    protected void update()
    {

        Presentation presentation = getPresentation();

        setEnabled(presentation.isEnabled());
        setVisible(presentation.isVisible());

        if (!presentation.isEnabled() || !presentation.isVisible())
        {
            label1.setVisible(presentation.isVisible());
            label1.setEnabled(presentation.isEnabled());

            label2.setEnabled(presentation.isEnabled());
            label2.setVisible(presentation.isVisible());
        }

        Presentation lastExecutedPresentation = this.lastExecutedPresentation;
        if (lastExecutedPresentation != null)
        {
            label1.setIcon(lastExecutedPresentation.getIcon(iconKey));
            label1.setEnabled(lastExecutedPresentation.isEnabled());
            label1.setToolTipText(lastExecutedPresentation.getText());
        }
        else
        {
            label1.setIcon(presentation.getIcon(iconKey));
            label1.setEnabled(presentation.isEnabled());
            label1.setToolTipText(presentation.getText());
        }


    }


    @NotNull
    public Collection<? extends Presentation> getPresentations()
    {
        return presentations;
    }


    private static class DividerBorder implements Border
    {
        @NotNull
        private Insets insets;


        private DividerBorder()
        {
            insets = new Insets(0, 2, 0, 0);
        }


        public void paintBorder(@NotNull Component c, @NotNull Graphics g, int x, int y, int width, int height)
        {
            Color c1 = c.getBackground().darker();
            Color c2 = c.getBackground().brighter();

            g.translate(x, y);
            g.setColor(c1);
            g.drawLine(0, 2, 0, height - 4);

            g.setColor(c2);
            g.drawLine(1, 2, 1, height - 4);

            g.translate(-x, -y);
        }


        @NotNull
        public Insets getBorderInsets(@NotNull Component c)
        {
            return insets;
        }


        public boolean isBorderOpaque()
        {
            return false;
        }
    }

    private class ButtonLayout implements LayoutManager2
    {
        public void addLayoutComponent(@NotNull Component comp, @Nullable Object constraints)
        {
        }


        @NotNull
        public Dimension maximumLayoutSize(@NotNull Container target)
        {
            JButton button = (JButton) target;
            return getPreferredButtonSize(button, button.getIconTextGap());
        }


        public float getLayoutAlignmentX(@NotNull Container target)
        {
            return 0;
        }


        public float getLayoutAlignmentY(@NotNull Container target)
        {
            return 0;
        }


        public void invalidateLayout(@NotNull Container target)
        {
        }


        public void addLayoutComponent(@NotNull String name, @NotNull Component comp)
        {
        }


        public void removeLayoutComponent(@NotNull Component comp)
        {
        }


        @NotNull
        public Dimension preferredLayoutSize(@NotNull Container parent)
        {
            JButton button = (JButton) parent;
            return getPreferredButtonSize(button, button.getIconTextGap());
        }


        @NotNull
        public Dimension minimumLayoutSize(@NotNull Container parent)
        {
            JButton button = (JButton) parent;
            return getPreferredButtonSize(button, button.getIconTextGap());
        }


        public void layoutContainer(@NotNull Container parent)
        {
            int count = parent.getComponentCount();
            if (count == 2)
            {
                JButton button = (JButton) parent;
                Insets insets = button.getInsets();
                label1.setBounds(insets.left, insets.top, button.getWidth() - (insets.left + label2.getPreferredSize().width), button.getHeight() - (insets.top + insets.bottom));
                label2.setBounds(button.getWidth() - (label2.getPreferredSize().width), 0, label2.getPreferredSize().width, button.getHeight());
            }
        }


        @NotNull
        public Dimension getPreferredButtonSize(@NotNull AbstractButton b, int textIconGap)
        {

            Rectangle iconRL1 = new Rectangle();
            Rectangle textRL1 = new Rectangle();
            Rectangle viewRL1 = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

            SwingUtilities.layoutCompoundLabel(label1, label1.getFontMetrics(label1.getFont()), label1.getText(), label1.getIcon(),
                                               label1.getVerticalAlignment(), label1.getHorizontalAlignment(),
                                               label1.getVerticalTextPosition(), label1.getHorizontalTextPosition(),
                                               viewRL1, iconRL1, textRL1, (label1.getText() == null ? 0 : textIconGap));


            Rectangle iconRL2 = new Rectangle();
            Rectangle textRL2 = new Rectangle();
            Rectangle viewRL2 = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

            SwingUtilities.layoutCompoundLabel(label2, label2.getFontMetrics(label2.getFont()), label2.getText(), label2.getIcon(),
                                               label2.getVerticalAlignment(), label2.getHorizontalAlignment(),
                                               label2.getVerticalTextPosition(), label2.getHorizontalTextPosition(),
                                               viewRL2, iconRL2, textRL2, (label2.getText() == null ? 0 : textIconGap));

            /* The preferred size of the button is the size of
             * the text and icon rectangles plus the buttons insets.
             */

            Rectangle r1 = iconRL1.union(textRL1);
            Rectangle r2 = iconRL2.union(textRL2);

            Insets insets = b.getInsets();
            r1.width += r2.width + insets.left + insets.right;
            r1.height += insets.top + insets.bottom;

            return r1.getSize();
        }

    }

}
