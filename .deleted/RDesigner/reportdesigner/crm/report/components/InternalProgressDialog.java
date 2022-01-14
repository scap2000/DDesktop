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
package org.pentaho.reportdesigner.crm.report.components;

import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 10:30:20
 */
public class InternalProgressDialog extends JComponent
{
    @NotNull
    private static final String START_LWMODAL = "startLWModal";
    @NotNull
    private static final String STOP_LWMODAL = "stopLWModal";

    @SuppressWarnings({"UnusedDeclaration"})
    @NotNull
    private String title;
    @SuppressWarnings({"UnusedDeclaration"})
    @NotNull
    private String task;

    @NotNull
    private JRootPane rootPane;

    @NotNull
    private Timer timer;
    private int state;


    public InternalProgressDialog(@NotNull JFrame parentFrame, @NotNull String title, @NotNull String task)
    {
        this.title = title;
        this.task = task;

        rootPane = parentFrame.getRootPane();

        init();
    }


    public InternalProgressDialog(@NotNull JDialog parentFrame, @NotNull String title, @NotNull String task)
    {
        this.title = title;
        this.task = task;

        rootPane = parentFrame.getRootPane();

        init();
    }


    private void init()
    {
        setLayout(new BorderLayout());

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:100dlu:grow, 0dlu", "0dlu:grow, pref, 4dlu, pref, 0dlu:grow");
        JPanel centerPanel = new JPanel(formLayout);

        centerPanel.setPreferredSize(new Dimension(100, 100));

        add(centerPanel, BorderLayout.CENTER);

        centerPanel.setOpaque(false);
        setOpaque(false);

        rootPane.getLayeredPane().add(this);
        rootPane.getLayeredPane().setLayer(this, JLayeredPane.POPUP_LAYER.intValue() + 1);

        state = 0;

        timer = new Timer(0, null);
    }


    public void pack()
    {
        setSize(getPreferredSize());
    }


    public void setVisible(boolean visible)
    {
        super.setVisible(visible);

        if (visible)
        {
            if (isVisible() && !isShowing())
            {
                Container parent = getParent();
                while (parent != null)
                {
                    if (!parent.isVisible())
                    {
                        parent.setVisible(true);
                    }
                    parent = parent.getParent();
                }
            }

            timer = new Timer(100, new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    state++;
                    repaint();
                }
            });

            timer.setCoalesce(true);
            timer.setRepeats(true);
            timer.start();

            rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try
            {
                Object obj;
                //noinspection unchecked
                obj = AccessController.doPrivileged(new ModalPrivilegedAction(
                        Container.class, START_LWMODAL));
                if (obj != null)
                {
                    ((Method) obj).invoke(this);
                }
            }
            catch (IllegalAccessException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (IllegalArgumentException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (InvocationTargetException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }
    }


    public void dispose()
    {
        timer.stop();
        rootPane.setCursor(Cursor.getDefaultCursor());

        try
        {
            Object obj;
            //noinspection unchecked
            obj = AccessController.doPrivileged(new ModalPrivilegedAction(
                    Container.class, STOP_LWMODAL));
            if (obj != null)
            {
                ((Method) obj).invoke(this);
            }
        }
        catch (IllegalAccessException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        catch (IllegalArgumentException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        catch (InvocationTargetException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

        rootPane.getLayeredPane().remove(this);
        setVisible(false);
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);

        g.setColor(new Color(0, 0, 0, 40));
        g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
        g.setColor(new Color(0, 0, 0, 80));
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform origTransform = g2d.getTransform();

        int h = getHeight() / 10;
        Rectangle2D r = new Rectangle2D.Double(getWidth() / 5, -h / 2, getWidth() / 4, h);
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.rotate(Math.toRadians(360) / 8 * (state % 8));
        for (int i = 0; i < 8; i++)
        {
            g2d.setColor(new Color(255, 255, 255, 255 - (8 - i) * 30));
            g2d.fill(r);
            g2d.rotate(Math.toRadians(45));
        }

        g2d.setTransform(origTransform);
    }


    public boolean isOptimizedDrawingEnabled()
    {
        return true;
    }


    public void setLocationRelativeTo(@NotNull JComponent component)
    {
        setLocation((component.getWidth() - getWidth()) / 2,
                    (component.getHeight() - getHeight()) / 2);
    }


    private static class ModalPrivilegedAction implements PrivilegedAction
    {
        @NotNull
        private Class clazz;
        @NotNull
        private String methodName;


        private ModalPrivilegedAction(@NotNull Class clazz, @NotNull String methodName)
        {
            this.clazz = clazz;
            this.methodName = methodName;
        }


        @Nullable
        public Object run()
        {
            Method method = null;
            try
            {
                method = clazz.getDeclaredMethod(methodName);
            }
            catch (NoSuchMethodException ex)
            {
                //ok
            }
            if (method != null)
            {
                method.setAccessible(true);
            }
            return method;
        }
    }


}
