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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.util.CalendarUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 08:44:51
 */
public class WDateChooserPanel extends JPanel
{
    @NotNull
    private static final String DEFAULT_SHORT_DATE_FORMAT = "EEE dd.MM.yyyy";
    @NotNull
    private static final String DEFAULT_LONG_DATE_FORMAT = "EEE dd.MM.yyyy HH:mm:ss";
    @NotNull
    private static final String VALUE_SELECTED = "valueSelected";
    @NotNull
    private static final String CANCEL = "cancel";


    public static void main(@NotNull String[] args) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException
    {
        UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[0].getClassName());

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());

        Date d = CalendarUtils.getDate(13, 4, 2005);
        frame.getContentPane().add(new WDateChooserPanel(d), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @NotNull
    private JLabel headerLabel;
    @NotNull
    private JLabel footerLabel;
    @NotNull
    private JPanel daysPanel;

    @NotNull
    private DayLabel[][] dayLabels = new DayLabel[6][7];
    @NotNull
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_SHORT_DATE_FORMAT);
    @NotNull
    private SimpleDateFormat longDateFormat = new SimpleDateFormat(DEFAULT_LONG_DATE_FORMAT);


    public WDateChooserPanel(@NotNull final Date initialValue)
    {
        super();

        @NonNls
        FormLayout formLayout = new FormLayout("2dlu, pref:grow, 2dlu",
                                               "2dlu," +
                                               "pref," +
                                               "4dlu," +
                                               "pref," +
                                               "0dlu," +
                                               "pref:grow," +
                                               "4dlu," +
                                               "pref," +
                                               "2dlu");

        setLayout(formLayout);

        @NonNls
        CellConstraints cc = new CellConstraints();

        headerLabel = new JLabel(dateFormat.format(initialValue));
        headerLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                notifyActionListeners(VALUE_SELECTED);
            }
        });

        footerLabel = new JLabel(longDateFormat.format(new Date()));

        add(headerLabel, cc.xy(2, 2, "center, center"));
        add(footerLabel, cc.xy(2, 8, "center, center"));

        footerLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                setupDaysPanel(new Date());
            }
        });

        Timer timer = new Timer(1000, new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                footerLabel.setText(longDateFormat.format(new Date()));
            }
        });

        timer.start();

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        JPanel weekdayPanel = new JPanel(new GridLayout(1, 0));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.MONDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.TUESDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.WEDNESDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.THURSDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.FRIDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.SATURDAY], JLabel.CENTER));
        weekdayPanel.add(new JLabel(dateFormatSymbols.getShortWeekdays()[Calendar.SUNDAY], JLabel.CENTER));

        weekdayPanel.setOpaque(true);
        weekdayPanel.setBackground(Color.LIGHT_GRAY);

        add(weekdayPanel, cc.xy(2, 4));

        daysPanel = new JPanel(new GridLayout(0, 7));
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setOpaque(true);
        daysPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));


        for (int y = 0; y < 6; y++)
        {
            for (int x = 0; x < 7; x++)
            {
                if (dayLabels[y][x] == null)
                {
                    final DayLabel dayLabel = new DayLabel(false);
                    dayLabels[y][x] = dayLabel;
                    dayLabel.addMouseListener(new MouseAdapter()
                    {
                        public void mouseClicked(@NotNull MouseEvent e)
                        {
                            Date date = dayLabel.getDate();
                            if (date != null)
                            {
                                setupDaysPanel(date);
                                notifyActionListeners(VALUE_SELECTED);
                            }
                        }
                    });
                    daysPanel.add(dayLabel);
                }
            }
        }

        setupDaysPanel(initialValue);

        add(daysPanel, cc.xy(2, 6, "fill, fill"));

        addKeyListener(new KeyAdapter()
        {
            public void keyTyped(@NotNull KeyEvent e)
            {
            }


            public void keyPressed(@NotNull KeyEvent e)
            {
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(activeLabel.getDate());
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    c.add(Calendar.DAY_OF_MONTH, -1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    c.add(Calendar.DAY_OF_MONTH, -7);
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    c.add(Calendar.DAY_OF_MONTH, 7);
                }
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
                {
                    c.add(Calendar.MONTH, -1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
                {
                    c.add(Calendar.MONTH, 1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    notifyActionListeners(VALUE_SELECTED);
                }
                else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    notifyActionListeners(VALUE_SELECTED);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    notifyActionListeners(CANCEL);
                }

                setupDaysPanel(c.getTime());
            }


            public void keyReleased(@NotNull KeyEvent e)
            {
            }
        });

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                requestFocusInWindow(false);
            }
        });

        setFocusable(true);

    }


    private void setupDaysPanel(@NotNull Date initialValue)
    {

        for (int y = 0; y < 6; y++)
        {
            for (int x = 0; x < 7; x++)
            {
                if (dayLabels[y][x] != null)
                {
                    dayLabels[y][x].setDate(null, false);
                }
            }
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(initialValue);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);

        if (weekDay == 1)
        {
            weekDay = 8;
        }

        weekDay -= 2;//monday=0, sunday=6

        int currentMonth = cal.get(Calendar.MONTH);

        //move to first visible day in calendarview
        cal.add(Calendar.DATE, -(weekDay + 1));

        for (int y = 0; y < 6; y++)
        {
            for (int x = 0; x < 7; x++)
            {
                cal.add(Calendar.DATE, 1);
                dayLabels[y][x].setDate(cal.getTime(), cal.get(Calendar.MONTH) == currentMonth);

                if (initialValue.compareTo(cal.getTime()) == 0)
                {
                    makeLabelActive(dayLabels[y][x]);
                }
            }
        }


    }


    @NotNull
    private DayLabel activeLabel;


    private void makeLabelActive(@NotNull DayLabel newLabel)
    {
        if (activeLabel != null)
        {
            activeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        headerLabel.setText(dateFormat.format(newLabel.getDate()));

        newLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
        activeLabel = newLabel;
    }


    @Nullable
    public Date getSelectedDate()
    {
        return activeLabel.getDate();
    }


    @NotNull
    private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();


    public void addActionListener(@NotNull ActionListener actionListener)
    {
        if (!actionListeners.contains(actionListener))
        {
            actionListeners.add(actionListener);
        }
    }


    public void removeActionListener(@NotNull ActionListener actionListener)
    {
        actionListeners.remove(actionListener);
    }


    private void notifyActionListeners(@NotNull String command)
    {
        ArrayList<ActionListener> als = new ArrayList<ActionListener>(actionListeners);
        for (ActionListener actionListener : als)
        {
            actionListener.actionPerformed(new ActionEvent(this, -1, command));
        }
    }


    public void setSelectedDate(@NotNull Date date)
    {
        setupDaysPanel(date);
    }


    private static class DayLabel extends JLabel
    {
        @NotNull
        private static final GregorianCalendar cal = new GregorianCalendar();

        @NotNull
        private static final String EEEE_DD_MM_YYYY = "EEEE dd.MM.yyyy";
        @NotNull
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat(EEEE_DD_MM_YYYY);

        @Nullable
        private Date date;
        private boolean active;


        private DayLabel(boolean active)
        {
            this.active = active;

            updatePanel();
        }


        public void setDate(@Nullable Date date, boolean active)
        {
            if (date == null)
            {
                this.date = null;
            }
            else
            {
                this.date = new Date(date.getTime());
            }
            this.active = active;

            updatePanel();
        }


        private void updatePanel()
        {
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

            if (date != null)
            {
                cal.setTime(date);
                setToolTipText(dateFormat.format(date));
                setText(String.valueOf(cal.get(Calendar.DATE)));
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
            else
            {
                setText(" ");
                setToolTipText(null);
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }

            if (active)
            {
                setForeground(Color.BLACK);
            }
            else
            {
                setForeground(Color.LIGHT_GRAY);
            }
        }


        @Nullable
        public Date getDate()
        {
            if (date == null)
            {
                return null;
            }
            else
            {
                return new Date(date.getTime());
            }
        }
    }
}
