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
package org.pentaho.reportdesigner.lib.client.components.favoritespanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.Category;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 11.03.2005
 * Time: 16:31:21
 */
public class DefaultHeaderComponent extends AbstractHeaderComponent implements PropertyChangeListener
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(DefaultHeaderComponent.class.getName());

    @NotNull
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    @NotNull
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;

    private boolean focused;

    @NotNull
    private JPanel containerPanel;

    @NotNull
    private JLabel headerLabel;
    @NotNull
    private GradientPanel actionComponent;
    @NotNull
    private Category category;

    @NotNull
    private GradientPanel gradientPanel;

    @NotNull
    private Color origForegroundColor;

    @Nullable
    private AnimationThread animationThread;
    private double darkeningFactor;


    public DefaultHeaderComponent()
    {
        this(new Insets(1, 17, 1, 17), 0.7);
    }


    public DefaultHeaderComponent(@NotNull Insets insets, double darkeningFactor)
    {
        this.darkeningFactor = darkeningFactor;
        setLayout(new BorderLayout());

        containerPanel = new JPanel();

        focused = false;

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:10dlu:grow, 0dlu, pref, 0dlu",
                                               "0dlu, pref, " +
                                               "0dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        containerPanel.setLayout(formLayout);

        headerLabel = new JLabel(" ");

        headerLabel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        actionComponent = new GradientPanel();
        actionComponent.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionComponent.setDirection(GradientPanel.Direction.DIRECTION_LEFT);
        Color acbg = actionComponent.getBackground();
        if (acbg == null)
        {
            acbg = DEFAULT_BACKGROUND_COLOR;
        }
        actionComponent.setGradientColors(new Color[]{acbg, acbg});

        gradientPanel = new GradientPanel();
        gradientPanel.setGradientColors(new Color[]{getDarkerColor(getBackground()), getBackground()});
        gradientPanel.setDirection(GradientPanel.Direction.DIRECTION_LEFT);

        gradientPanel.add(headerLabel, BorderLayout.CENTER);

        containerPanel.add(gradientPanel, cc.xy(2, 2, "fill, fill"));
        containerPanel.add(actionComponent, cc.xy(4, 2));

        add(containerPanel, BorderLayout.CENTER);

        setOpaque(false);
        Color background = containerPanel.getBackground();
        if (background == null)
        {
            background = DEFAULT_BACKGROUND_COLOR;
        }
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getDarkerColor(background)));

        origForegroundColor = headerLabel.getForeground();
    }


    @NotNull
    private Color getDarkerColor(@NotNull Color color)
    {
        return new Color(Math.max((int) (color.getRed() * darkeningFactor), 0),
                         Math.max((int) (color.getGreen() * darkeningFactor), 0),
                         Math.max((int) (color.getBlue() * darkeningFactor), 0));
    }


    public void setFocused(boolean focused)
    {
        if (this.focused == focused)
        {
            return;
        }

        this.focused = focused;

        if (!focused)
        {
            Color background = getBackground();
            if (background == null)
            {
                background = DEFAULT_BACKGROUND_COLOR;
            }

            Color hihglight = UIManager.getColor("List.selectionBackground");//NON-NLS
            if (hihglight == null)
            {
                hihglight = Color.BLUE;
            }

            Color selectForegroundColor = UIManager.getColor("List.selectionForeground");//NON-NLS
            if (selectForegroundColor == null)
            {
                selectForegroundColor = Color.BLACK;
            }

            int startStep = 0;
            AnimationThread at = animationThread;
            if (at != null && !at.isInterrupted() && at.isAlive())
            {
                at.interrupt();
                startStep = AnimationThread.STEPS - at.getCurrentStep();
            }
            at = new AnimationThread(startStep, this, hihglight, getDarkerColor(background), selectForegroundColor, origForegroundColor);
            at.setDaemon(true);
            at.start();
            animationThread = at;
        }
        else
        {
            Color background = getBackground();
            if (background == null)
            {
                background = DEFAULT_BACKGROUND_COLOR;
            }

            Color hihglight = UIManager.getColor("List.selectionBackground");//NON-NLS

            if (hihglight == null)
            {
                hihglight = Color.BLUE;
            }

            Color selectForegroundColor = UIManager.getColor("List.selectionForeground");//NON-NLS
            if (selectForegroundColor == null)
            {
                selectForegroundColor = Color.BLACK;
            }

            int startStep = 0;
            AnimationThread at = animationThread;
            if (at != null && !at.isInterrupted() && at.isAlive())
            {
                at.interrupt();
                startStep = AnimationThread.STEPS - at.getCurrentStep();
            }
            at = new AnimationThread(startStep, this, getDarkerColor(background), hihglight, origForegroundColor, selectForegroundColor);
            at.setDaemon(true);
            at.start();
            animationThread = at;
        }
    }


    @NotNull
    public GradientPanel getGradientPanel()
    {
        return gradientPanel;
    }


    public void setCategory(@NotNull Category category)
    {
        //noinspection ConstantConditions
        if (this.category != null)
        {
            this.category.removePropertyChangeListener(this);
        }

        this.category = category;
        update(category);
    }


    private void update(@NotNull Category category)
    {
        headerLabel.setIcon(category.getIconBig());
        headerLabel.setText(category.getTitle());

        actionComponent.removeAll();
        JToolBar toolBar = category.getToolBar();
        if (toolBar != null)
        {
            toolBar.setBackground(actionComponent.getBackground());
            actionComponent.add(toolBar);
        }
        else
        {
            for (int i = 0; i < category.getActionComponents().length; i++)
            {
                JComponent component = category.getActionComponents()[i];
                actionComponent.add(component);
            }
        }

        revalidate();
        repaint();
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        update(category);
    }


    private void setCurrentColor(@NotNull Color backgroundColor, @NotNull Color textColor)
    {
        Color background = getBackground();
        if (background == null)
        {
            background = DEFAULT_BACKGROUND_COLOR;
        }

        Color[] gradientColors = new Color[]{backgroundColor, background};
        getGradientPanel().setGradientColors(gradientColors);
        headerLabel.setForeground(textColor);

        revalidate();
        repaint();
    }


    private static class AnimationThread extends Thread
    {
        public static final int STEPS = 5;

        @NotNull
        private DefaultHeaderComponent defaultHeaderComponent;
        @NotNull
        private Color startColor;
        @NotNull
        private Color endColor;

        @NotNull
        private Color startColor2;
        @NotNull
        private Color endColor2;

        private int step;


        private AnimationThread(int startStep, @NotNull DefaultHeaderComponent defaultHeaderComponent, @NotNull Color startColor, @NotNull Color endColor, @NotNull Color startColor2, @NotNull Color endColor2)
        {
            this.defaultHeaderComponent = defaultHeaderComponent;

            this.startColor = startColor;
            this.endColor = endColor;

            this.startColor2 = startColor2;
            this.endColor2 = endColor2;

            step = startStep;
        }


        public void run()
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DefaultHeaderComponent$AnimationThread.run ", e);
                return;
            }

            double v = (double) STEPS;
            for (; step < STEPS && !isInterrupted(); step++)
            {
                try
                {
                    Thread.sleep(20);

                    final Color currentColor = getIntermediateColor(v, step, startColor, endColor);
                    final Color textColor = getIntermediateColor(v, step, startColor2, endColor2);

                    EventQueue.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            defaultHeaderComponent.setCurrentColor(currentColor, textColor);
                        }
                    });
                }
                catch (InterruptedException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DefaultHeaderComponent$AnimationThread.run ", e);
                    return;
                }
                catch (InvocationTargetException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DefaultHeaderComponent$AnimationThread.run ", e);
                }
            }

            if (!isInterrupted())
            {
                try
                {
                    EventQueue.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            defaultHeaderComponent.setCurrentColor(endColor, endColor2);
                        }
                    });
                }
                catch (InterruptedException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DefaultHeaderComponent$AnimationThread.run ", e);
                }
                catch (InvocationTargetException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DefaultHeaderComponent$AnimationThread.run ", e);
                }
            }
        }


        public int getCurrentStep()
        {
            return step;
        }


        @NotNull
        private Color getIntermediateColor(double v, int i, @NotNull Color startColor, @NotNull Color endColor)
        {
            int rDiff = endColor.getRed() - startColor.getRed();
            int rOff = (int) (rDiff / v * i);
            int gDiff = endColor.getGreen() - startColor.getGreen();
            int gOff = (int) (gDiff / v * i);
            int bDiff = endColor.getBlue() - startColor.getBlue();
            int bOff = (int) (bDiff / v * i);

            int red = startColor.getRed() + rOff;
            int green = startColor.getGreen() + gOff;
            int blue = startColor.getBlue() + bOff;

            return new Color(red, green, blue);
        }
    }
}

