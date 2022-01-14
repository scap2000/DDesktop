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
package org.pentaho.reportdesigner.crm.report;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExceptionModelListener;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 10:40:31
 */
public class StatusBar extends JPanel implements UncaughtExceptionModelListener
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(StatusBar.class.getName());

    @NotNull
    private JLabel statusLabel;
    @NotNull
    private ExceptionStatusGadget exceptionStatusGadget;


    public StatusBar()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("2dlu, fill:1dlu:grow, 4dlu, pref, 4dlu, pref, 4dlu, pref, 15dlu, fill:max(60dlu;pref), 0dlu", "0dlu, fill:pref, 0dlu");
        setLayout(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(statusLabel, cc.xy(2, 2));
        add(new JSeparator(JSeparator.VERTICAL), cc.xy(4, 2));
        exceptionStatusGadget = new ExceptionStatusGadget();
        add(exceptionStatusGadget, cc.xy(6, 2));
        add(new JSeparator(JSeparator.VERTICAL), cc.xy(8, 2));
        MemoryStatusGadget memoryStatusGadget = new MemoryStatusGadget();
        add(memoryStatusGadget, cc.xy(10, 2));

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
    }


    public void setGeneralInfoText(@Nullable String text)
    {
        if (text == null || text.length() == 0)
        {
            statusLabel.setText(" ");
        }
        else
        {
            statusLabel.setText(text);
        }
    }


    public void exceptionCaught(@NotNull Throwable throwable)
    {
        if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "StatusBar.exceptionCaught ", throwable);
        exceptionStatusGadget.exceptionCaught(throwable);
    }


    private static class ExceptionStatusGadget extends JLabel
    {
        @NotNull
        private Timer timer;
        private boolean firstTime;


        private ExceptionStatusGadget()
        {
            firstTime = true;

            setHorizontalAlignment(JLabel.CENTER);
            setIcon(IconLoader.getInstance().getNoErrorIcon());
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            timer = new Timer(500, new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    //noinspection ObjectEquality
                    if (getIcon() == IconLoader.getInstance().getNoErrorIcon())
                    {
                        setIcon(IconLoader.getInstance().getErrorIcon());
                    }
                    else
                    {
                        setIcon(IconLoader.getInstance().getNoErrorIcon());
                    }
                }
            });

            timer.setRepeats(true);
            timer.stop();

            addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    timer.stop();
                    setIcon(IconLoader.getInstance().getNoErrorIcon());

                    final CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(ExceptionStatusGadget.this, TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.Title"), true);
                    ArrayList<UncaughtExcpetionsModel.ThrowableInfo> throwableInfos = new ArrayList<UncaughtExcpetionsModel.ThrowableInfo>(UncaughtExcpetionsModel.getInstance().getThrowables());

                    final ChangeableListModel defaultListModel = new ChangeableListModel();
                    for (UncaughtExcpetionsModel.ThrowableInfo throwableInfo : throwableInfos)
                    {
                        defaultListModel.addElement(throwableInfo);
                    }

                    final JList list = new JList(defaultListModel);
                    list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    final JTextArea stacktraceTextArea = new JTextArea();
                    stacktraceTextArea.setEditable(false);
                    TextComponentHelper.installDefaultPopupMenu(stacktraceTextArea);

                    final JTextArea descriptionTextArea = new JTextArea();
                    TextComponentHelper.installDefaultPopupMenu(descriptionTextArea);
                    UndoHelper.installUndoSupport(descriptionTextArea);

                    final JButton submitButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.sumbit"));
                    JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.close"));
                    JButton clearButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.clear"));

                    submitButton.setEnabled(false);

                    submitButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            OutputStreamWriter wr = null;
                            BufferedReader rd = null;
                            try
                            {
                                UncaughtExcpetionsModel.ThrowableInfo throwableInfo = (UncaughtExcpetionsModel.ThrowableInfo) list.getSelectedValue();
                                // Construct data
                                String title = throwableInfo.getThrowable() + " occured at " + new Date(throwableInfo.getMillis());//NON-NLS
                                String description = (descriptionTextArea.getText() + "\n\n" +
                                                      getStacktraceText(throwableInfo));

                                String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");//NON-NLS
                                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");//NON-NLS

                                // Send data
                                //TODO change to pentaho tracker
                                URL url = new URL("http://www.gridvision.ch/ReportDesigner/SubmitBug.php");//NON-NLS
                                URLConnection conn = url.openConnection();
                                conn.setDoOutput(true);
                                //noinspection IOResourceOpenedButNotSafelyClosed
                                wr = new OutputStreamWriter(conn.getOutputStream());
                                wr.write(data);
                                wr.flush();

                                // Get the response
                                //noinspection IOResourceOpenedButNotSafelyClosed
                                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String line;
                                StringBuilder sb = new StringBuilder(100);
                                boolean submitted = false;
                                while ((line = rd.readLine()) != null)
                                {
                                    if (line.contains("The Bug has been submitted"))//NON-NLS
                                    {
                                        submitted = true;
                                    }
                                    sb.append(line);
                                    sb.append('\n');
                                }
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StatusBar$ExceptionStatusGadget.actionPerformed sb = " + sb);

                                if (submitted)
                                {
                                    JOptionPane.showMessageDialog(centerPanelDialog,
                                                                  TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.Submitted.Message"),
                                                                  TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.Submitted.Title"),
                                                                  JOptionPane.INFORMATION_MESSAGE);

                                    throwableInfo.setSubmitted(true);
                                    defaultListModel.fireElementChanged(list.getSelectedIndex());
                                    submitButton.setEnabled(false);
                                }
                            }
                            catch (Exception ex)
                            {
                                if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "StatusBar$ExceptionStatusGadget.actionPerformed ", ex);

                                JOptionPane.showMessageDialog(centerPanelDialog,
                                                              TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.Error.Message"),
                                                              TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.Error.Title"),
                                                              JOptionPane.ERROR_MESSAGE);
                            }
                            finally
                            {
                                IOUtil.closeStream(wr);
                                IOUtil.closeStream(rd);
                            }
                        }
                    });

                    okButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            centerPanelDialog.dispose();
                        }
                    });

                    clearButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            UncaughtExcpetionsModel.getInstance().clearExceptions();
                            list.setModel(new DefaultListModel());
                            stacktraceTextArea.setText("");
                        }
                    });

                    list.addListSelectionListener(new ListSelectionListener()
                    {
                        public void valueChanged(@NotNull ListSelectionEvent e)
                        {
                            if (!e.getValueIsAdjusting())
                            {
                                Object selectedValue = list.getSelectedValue();
                                if (selectedValue instanceof UncaughtExcpetionsModel.ThrowableInfo)
                                {
                                    UncaughtExcpetionsModel.ThrowableInfo throwableInfo = (UncaughtExcpetionsModel.ThrowableInfo) selectedValue;
                                    String text = getStacktraceText(throwableInfo);
                                    stacktraceTextArea.setText(text);
                                    stacktraceTextArea.setCaretPosition(0);
                                }
                                else
                                {
                                    stacktraceTextArea.setText("");
                                }

                                Object sv = list.getSelectedValue();
                                if (sv instanceof UncaughtExcpetionsModel.ThrowableInfo)
                                {
                                    UncaughtExcpetionsModel.ThrowableInfo throwableInfo = (UncaughtExcpetionsModel.ThrowableInfo) sv;
                                    submitButton.setEnabled(!throwableInfo.isSubmitted());
                                }
                                else
                                {
                                    submitButton.setEnabled(false);
                                }
                            }
                        }
                    });

                    if (!throwableInfos.isEmpty())
                    {
                        list.getSelectionModel().addSelectionInterval(throwableInfos.size() - 1, throwableInfos.size() - 1);
                        centerPanelDialog.executeAfterMakingVisible(new Runnable()
                        {
                            public void run()
                            {
                                list.ensureIndexIsVisible(list.getMaxSelectionIndex());
                            }
                        });
                    }

                    @NonNls
                    FormLayout layout = new FormLayout("0dlu, fill:10dlu:grow, 0dlu", "4dlu, pref, 4dlu, fill:50dlu, 2dlu, fill:10dlu:grow, 0dlu");
                    @NonNls
                    CellConstraints cc = new CellConstraints();

                    JPanel lowerPanel = new JPanel(layout);
                    //lowerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "ExceptionStatusGadget.DescriptionLabel")), cc.xy(2, 2));
                    lowerPanel.add(new JScrollPane(descriptionTextArea), cc.xy(2, 4));
                    lowerPanel.add(new JScrollPane(stacktraceTextArea), cc.xy(2, 6));

                    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(list), lowerPanel);
                    splitPane.setDividerLocation(150);
                    JPanel centerPanel = new JPanel(new BorderLayout());
                    centerPanel.add(splitPane, BorderLayout.CENTER);

                    centerPanelDialog.setCenterPanel(centerPanel);
                    //centerPanelDialog.setButtons(submitButton, okButton, submitButton, okButton, clearButton);
                    centerPanelDialog.setButtons(okButton, okButton, okButton, clearButton);

                    centerPanelDialog.pack();
                    GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 600, 600);

                    WindowUtils.setLocationRelativeTo(centerPanelDialog, SwingUtilities.getWindowAncestor(ExceptionStatusGadget.this));
                    centerPanelDialog.setVisible(true);
                }
            });
        }


        @NotNull
        private String getStacktraceText(@NotNull UncaughtExcpetionsModel.ThrowableInfo throwableInfo)
        {
            StringWriter sw = new StringWriter();
            String text;
            PrintWriter pw = null;
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                pw = new PrintWriter(sw);
                throwableInfo.getThrowable().printStackTrace(pw);
                text = sw.getBuffer().toString();
            }
            finally
            {
                IOUtil.closeStream(pw);
            }
            return text;
        }


        public void exceptionCaught(@NotNull Throwable throwable)
        {
            if (firstTime)
            {
                firstTime = false;

                Component rootPane = getParent();
                while (rootPane != null && !(rootPane instanceof JRootPane))
                {
                    rootPane = rootPane.getParent();
                }
                if (rootPane != null)
                {
                    final JRootPane jRootPane = (JRootPane) rootPane;


                    final JLabel label = new JLabel(TranslationManager.getInstance().getTranslation("R", "StatusBar.InternalError.Message"));
                    label.setForeground(new Color(120, 0, 0));
                    label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
                    label.setBackground(new Color(255, 0, 0, 125));
                    label.setOpaque(true);

                    jRootPane.getLayeredPane().add(label);
                    jRootPane.getLayeredPane().setLayer(label, JLayeredPane.POPUP_LAYER.intValue());
                    Rectangle rectangle = SwingUtilities.convertRectangle(getParent(), getBounds(), jRootPane.getLayeredPane());
                    label.setBounds((int) (rectangle.getX() - label.getPreferredSize().width), (int) (rectangle.getY() - label.getPreferredSize().height), label.getPreferredSize().width, label.getPreferredSize().height);
                    jRootPane.getLayeredPane().revalidate();
                    jRootPane.getLayeredPane().repaint();

                    Timer t = new Timer(15000, new ActionListener()
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            jRootPane.getLayeredPane().remove(label);
                            jRootPane.getLayeredPane().repaint();
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
            }
            timer.restart();
        }


    }

    private static class ChangeableListModel extends DefaultListModel
    {
        private ChangeableListModel()
        {
        }


        public void fireElementChanged(int index)
        {
            fireContentsChanged(this, index, index);
        }
    }

    private static class MemoryStatusGadget extends JLabel
    {
        @NotNull
        private static final Color BG = new Color(220, 220, 220);

        private double tm;
        private double fm;


        private MemoryStatusGadget()
        {
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            setHorizontalAlignment(JLabel.CENTER);

            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    //noinspection InfiniteLoopStatement
                    while (true)
                    {
                        final long totalMemory = Runtime.getRuntime().totalMemory();
                        final long freeMemory = Runtime.getRuntime().freeMemory();

                        try
                        {
                            EventQueue.invokeAndWait(new Runnable()
                            {
                                public void run()
                                {
                                    tm = (totalMemory / (1024. * 1024));
                                    fm = (freeMemory / (1024. * 1024));

                                    setText(TranslationManager.getInstance().getTranslation("R", "MemoryStatusGadget.Text", Double.valueOf(tm - fm), Double.valueOf(tm)));
                                }
                            });

                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            //ignore
                        }
                        catch (InvocationTargetException e)
                        {
                            UncaughtExcpetionsModel.getInstance().addException(e);
                        }
                    }
                }
            });

            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();

            addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    System.gc();
                }
            });
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            Color origColor = g.getColor();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            int w = (int) (getWidth() * ((tm - fm) / tm));
            g.setColor(BG);
            g.fillRect(0, 0, w, getHeight());

            g.setColor(origColor);
            super.paintComponent(g);
        }
    }

}
