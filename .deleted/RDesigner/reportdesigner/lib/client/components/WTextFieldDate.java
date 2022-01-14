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
package org.pentaho.reportdesigner.lib.client.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.util.UIConstants;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 08:46:57
 */
public class WTextFieldDate extends JFormattedTextField
{
    @NotNull
    private static final String SHORT_DATE_FORMAT = "dd.MM.yyyy";
    @NotNull
    private static final String INCREMENT = "increment";
    @NotNull
    private static final String DECREMENT = "decrement";


    public static void main(@NotNull String[] args) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException
    {
        UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[0].getClassName());
        UIManager.put(UIConstants.SWING_BOLD_METAL, Boolean.FALSE);

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        WTextFieldDate wTextFieldDate = new WTextFieldDate(new Date());

        frame.getContentPane().add(wTextFieldDate);
        frame.add(new JButton("adfds"));//NON-NLS
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private static final int OK = 1;
    private static final int WARNING = 2;
    private static final int ERROR = 3;

    @NotNull
    private static final Color COLOR_OK = new Color(208, 255, 202);
    @NotNull
    private static final Color COLOR_WARNING = new Color(255, 254, 202);
    @NotNull
    private static final Color COLOR_ERROR = new Color(255, 202, 202);


    @NotNull
    private SimpleDateFormat simpleDateFormat;
    @NotNull
    private GregorianCalendar gregorianCalendar;

    @NotNull
    private DateChooserWindow dateChooserWindow;


    public WTextFieldDate(@NotNull Date date)
    {
        final int[] fields = new int[]{Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH,
                                       Calendar.MONTH, Calendar.MONTH, Calendar.MONTH,
                                       Calendar.YEAR, Calendar.YEAR, Calendar.YEAR, Calendar.YEAR, Calendar.YEAR};


        try
        {
            simpleDateFormat = new SimpleDateFormat(SHORT_DATE_FORMAT);
            gregorianCalendar = new GregorianCalendar();

            MaskFormatter formatter = new MaskFormatter("##.##.####")
            {
                @NotNull
                protected Action[] getActions()
                {
                    return new Action[]
                            {
                                    new IncrementAction(INCREMENT, 1),
                                    new IncrementAction(DECREMENT, -1)
                            };
                }


                protected void setEditValid(boolean valid)
                {
                    super.setEditValid(valid);
                    higlightState();
                }


                class IncrementAction extends AbstractAction
                {
                    private int direction;


                    private IncrementAction(@NotNull String name, int direction)
                    {
                        super(name);
                        this.direction = direction;
                    }


                    public void actionPerformed(@NotNull ActionEvent ae)
                    {
                        try
                        {
                            if (isEditValid())
                            {
                                int p = getCaretPosition();
                                int field = fields[p];
                                if (field != -1)
                                {
                                    Date s = simpleDateFormat.parse(WTextFieldDate.this.getText());
                                    gregorianCalendar.setTime(s);

                                    gregorianCalendar.add(field, direction);

                                    Date newDate = gregorianCalendar.getTime();
                                    setValue(simpleDateFormat.format(newDate));
                                }
                                setCaretPosition(p);
                            }
                        }
                        catch (ParseException e)
                        {
                            //noinspection CallToPrintStackTrace
                            e.printStackTrace();
                        }

                    }
                }
            };
            formatter.setPlaceholderCharacter('0');
            setFormatterFactory(new DefaultFormatterFactory(formatter));
        }
        catch (ParseException e)
        {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        addKeyListener(new KeyListener()
        {
            public void keyTyped(@NotNull KeyEvent e)
            {
            }


            public void keyPressed(@NotNull KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    showDateChooserWindow();
                }
            }


            public void keyReleased(@NotNull KeyEvent e)
            {
            }
        });

        setValue(simpleDateFormat.format(date));

        higlightState();
    }


    private void showDateChooserWindow()
    {
        if (dateChooserWindow == null)
        {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof Dialog)
            {
                dateChooserWindow = new DateChooserWindow((Dialog) w);
            }
            else if (w instanceof Frame)
            {
                dateChooserWindow = new DateChooserWindow((Frame) w);
            }
        }
        Point p = getLocationOnScreen();
        try
        {
            dateChooserWindow.setSelectedDate(simpleDateFormat.parse(getText()));
        }
        catch (ParseException e1)
        {
            //that's ok
            dateChooserWindow.setSelectedDate(new Date());
        }
        dateChooserWindow.pack();
        dateChooserWindow.setLocation(p.x, p.y + getHeight());

        dateChooserWindow.setVisible(true);
    }


    private void higlightState()
    {
        int s = getDateState();
        if (s == OK)
        {
            setBackground(COLOR_OK);
        }
        else if (s == WARNING)
        {
            setBackground(COLOR_WARNING);
        }
        else
        {
            setBackground(COLOR_ERROR);
        }
    }


    private int getDateState()
    {
        if (isEditValid())
        {
            //noinspection UnusedCatchParameter
            try
            {
                String orig = getText();
                Date d = simpleDateFormat.parse(orig);
                String corrected = simpleDateFormat.format(d);
                setToolTipText(corrected);

                if (orig.equals(corrected))
                {
                    return OK;
                }
                return WARNING;
            }
            catch (ParseException e)
            {
                setToolTipText(null);
                return ERROR;
            }
        }
        setToolTipText(null);
        return ERROR;
    }


    public void setValue(@NotNull Object value)
    {
        super.setValue(value);
        higlightState();
    }


    public void commitEdit() throws ParseException
    {
        super.commitEdit();

        Date s = simpleDateFormat.parse(getText());
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(s);
        Date newDate = g.getTime();
        setValue(simpleDateFormat.format(newDate));
    }


    private class DateChooserWindow extends JDialog
    {
        @NotNull
        private WDateChooserPanel WDateChooserPanel;
        @NotNull
        private static final String VALUE_SELECTED = "valueSelected";


        private DateChooserWindow(@NotNull Frame owner)
        {
            super(owner, false);
            init();
        }


        private DateChooserWindow(@NotNull Dialog owner)
        {
            super(owner, false);
            init();
        }


        private void init()
        {
            setUndecorated(true);

            WDateChooserPanel = new WDateChooserPanel(new Date());
            Color background = getBackground();
            if (background == null)
            {
                background = Color.LIGHT_GRAY;
            }
            WDateChooserPanel.setBorder(BorderFactory.createLineBorder(background.darker().darker()));

            getContentPane().add(WDateChooserPanel, BorderLayout.CENTER);

            WDateChooserPanel.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    if (VALUE_SELECTED.equals(e.getActionCommand()))
                    {
                        int p = WTextFieldDate.this.getCaretPosition();
                        WTextFieldDate.this.setText(simpleDateFormat.format(WDateChooserPanel.getSelectedDate()));
                        WTextFieldDate.this.setCaretPosition(p);
                    }

                    setVisible(false);
                }
            });

            addWindowListener(new WindowAdapter()
            {
                public void windowDeactivated(@NotNull WindowEvent e)
                {
                    setVisible(false);
                }
            });

        }


        public void setSelectedDate(@NotNull Date date)
        {
            WDateChooserPanel.setSelectedDate(date);
        }


        @Nullable
        public Date getSelectedDate()
        {
            return WDateChooserPanel.getSelectedDate();
        }
    }
}
