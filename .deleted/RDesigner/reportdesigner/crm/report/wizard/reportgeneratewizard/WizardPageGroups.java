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
package org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageGroups extends AbstractWizardPage
{
    @NotNull
    private JPanel centerPanel;

    @NotNull
    private JButton addButton;
    @NotNull
    private JButton removeButton;

    @NotNull
    private JButton upButton;
    @NotNull
    private JButton downButton;

    @NotNull
    private GroupsPanel groupsPanel;
    @NotNull
    private JList columnsList;
    @NotNull
    private DefaultListModel columnListModel;

    @NotNull
    private GroupsModel groupsModel;


    public WizardPageGroups()
    {
        addButton = ComponentFactory.createButton("R", "WizardPageGroup.ButtonAdd");
        removeButton = ComponentFactory.createButton("R", "WizardPageGroup.ButtonRemove");

        upButton = ComponentFactory.createButton("R", "WizardPageGroup.ButtonUp");
        downButton = ComponentFactory.createButton("R", "WizardPageGroup.ButtonDown");

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:10dlu:grow, 4dlu, default, 4dlu, fill:10dlu:grow, 4dlu, default, 4dlu",
                                               "4dlu, " +
                                               "fill:pref:grow, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:pref:grow, " +
                                               "4dlu");

        centerPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        columnListModel = new DefaultListModel();
        columnsList = new JList(columnListModel);
        columnsList.setCellRenderer(new ColumnInfoListCellRenderer());
        centerPanel.add(new JScrollPane(columnsList), cc.xywh(2, 2, 1, 7, "fill, fill"));

        centerPanel.add(addButton, cc.xy(4, 4));
        centerPanel.add(removeButton, cc.xy(4, 6));

        groupsModel = new GroupsModel();
        groupsPanel = new GroupsPanel(groupsModel, new ArrayList<ColumnInfo>(), columnsList.getBackground(), columnsList.getForeground(), columnsList.getSelectionBackground(), columnsList.getSelectionForeground());
        centerPanel.add(new JScrollPane(groupsPanel), cc.xywh(6, 2, 1, 7, "fill, fill"));

        centerPanel.add(upButton, cc.xy(8, 4));
        centerPanel.add(downButton, cc.xy(8, 6));

        addButton.setEnabled(false);
        removeButton.setEnabled(false);

        upButton.setEnabled(false);
        downButton.setEnabled(false);

        columnsList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    addButton.setEnabled(columnsList.getSelectedValues().length > 0);
                }
            }
        });

        groupsPanel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                boolean selected = false;
                ArrayList<Rectangle> groupLocations = groupsPanel.getGroupLocations();
                for (int i = 0; i < groupLocations.size(); i++)
                {
                    Rectangle rectangle = groupLocations.get(i);
                    if (rectangle.contains(e.getPoint()))
                    {
                        groupsPanel.setSelectedGroup(groupsModel.getGroupInfos().get(i));
                        removeButton.setEnabled(true);
                        selected = true;
                    }
                }

                if (!selected)
                {
                    groupsPanel.setSelectedGroup(null);
                    removeButton.setEnabled(false);
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
                else
                {
                    int index = groupsModel.getGroupInfos().indexOf(groupsPanel.getSelectedGroup());
                    upButton.setEnabled(index > 0);
                    downButton.setEnabled(index < groupsModel.getGroupInfos().size() - 1);
                }
            }
        });

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int[] selectedIndices = columnsList.getSelectedIndices();
                ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
                for (int i = selectedIndices.length - 1; i >= 0; i--)
                {
                    int selectedIndice = selectedIndices[i];
                    ColumnInfo element = (ColumnInfo) columnListModel.getElementAt(selectedIndice);
                    columnListModel.removeElementAt(selectedIndice);
                    columnInfos.add(element);
                }

                Collections.reverse(columnInfos);

                groupsModel.addGroup(new GroupInfo(columnInfos));
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                GroupInfo selectedGroup = groupsPanel.getSelectedGroup();

                if (selectedGroup != null)
                {
                    groupsModel.removeGroupInfo(selectedGroup);
                    groupsPanel.setSelectedGroup(null);

                    //noinspection unchecked
                    @Nullable
                    ArrayList<ColumnInfo> columnInfos = (ArrayList<ColumnInfo>) getWizardDialog().getWizardDatas().get(WizardData.COLUMN_INFOS).getValue();
                    columnListModel.clear();
                    if (columnInfos != null)
                    {
                        for (ColumnInfo columnInfo : columnInfos)
                        {
                            columnListModel.addElement(columnInfo);
                        }
                    }
                }

                removeButton.setEnabled(false);
                upButton.setEnabled(false);
                downButton.setEnabled(false);
            }
        });

        upButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (groupsPanel.getSelectedGroup() != null)
                {
                    int index = groupsModel.getGroupInfos().indexOf(groupsPanel.getSelectedGroup());
                    if (index > 0)
                    {
                        groupsModel.moveGroup(index, index - 1);
                    }
                    index = groupsModel.getGroupInfos().indexOf(groupsPanel.getSelectedGroup());
                    upButton.setEnabled(index > 0);
                    downButton.setEnabled(index < groupsModel.getGroupInfos().size() - 1);
                }
            }
        });

        downButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (groupsPanel.getSelectedGroup() != null)
                {
                    int index = groupsModel.getGroupInfos().indexOf(groupsPanel.getSelectedGroup());
                    if (index < groupsModel.getGroupInfos().size() - 1)
                    {
                        groupsModel.moveGroup(index, index + 1);
                    }
                    index = groupsModel.getGroupInfos().indexOf(groupsPanel.getSelectedGroup());
                    upButton.setEnabled(index > 0);
                    downButton.setEnabled(index < groupsModel.getGroupInfos().size() - 1);
                }
            }
        });
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        @Nullable
        WizardData wizardData = getWizardDialog().getWizardDatas().get(WizardData.COLUMN_INFOS);
        if (wizardData != null)
        {
            Object ci = wizardData.getValue();
            if (ci instanceof ArrayList)
            {
                //noinspection unchecked
                ArrayList<ColumnInfo> columnInfos = (ArrayList<ColumnInfo>) ci;
                columnListModel.clear();
                for (ColumnInfo columnInfo : columnInfos)
                {
                    columnListModel.addElement(columnInfo);
                }
                groupsPanel.setSelectedGroup(null);
                groupsPanel.setPossibleColumnInfos(columnInfos);
            }
        }
        return centerPanel;
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageGroup.Title");
    }


    @Nullable
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageGroupIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageGroup.Description");
    }


    public void dispose()
    {
    }


    public boolean canNext()
    {
        return true;
    }


    public boolean canPrevious()
    {
        return true;
    }


    public boolean canCancel()
    {
        return true;
    }


    public boolean canFinish()
    {
        return true;
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        return new WizardData[]{
                new WizardData(WizardData.COLUMN_GROUPS, new ArrayList<GroupInfo>(groupsModel.getGroupInfos()))
        };
    }


    private static class GroupsPanel extends JPanel implements GroupsModelListener
    {
        @NotNull
        private GroupsModel groupsModel;
        @NotNull
        private Color background;
        @NotNull
        private Color foreground;
        @NotNull
        private Color selectionBackgrouond;
        @NotNull
        private Color selectionForeground;
        @Nullable
        private GroupInfo selectedGroup;

        @NotNull
        private ArrayList<Rectangle> groupLocations;
        @NotNull
        private ArrayList<ColumnInfo> possibleColumnInfos;


        private GroupsPanel(@NotNull GroupsModel groupsModel, @NotNull ArrayList<ColumnInfo> possibleColumnInfos, @NotNull Color background, @NotNull Color foreground, @NotNull Color selectionBackgrouond, @NotNull Color selectionForeground)
        {
            this.groupsModel = groupsModel;
            this.background = background;
            this.foreground = foreground;
            this.selectionBackgrouond = selectionBackgrouond;
            this.selectionForeground = selectionForeground;

            groupLocations = new ArrayList<Rectangle>();
            groupsModel.addGroupsModelListener(this);
            this.possibleColumnInfos = possibleColumnInfos;
        }


        public void setPossibleColumnInfos(@NotNull ArrayList<ColumnInfo> possibleColumnInfos)
        {
            this.possibleColumnInfos = possibleColumnInfos;
        }


        @Nullable
        public GroupInfo getSelectedGroup()
        {
            return selectedGroup;
        }


        public void setSelectedGroup(@Nullable GroupInfo groupInfo)
        {
            this.selectedGroup = groupInfo;
            repaint();
        }


        @NotNull
        public ArrayList<Rectangle> getGroupLocations()
        {
            return new ArrayList<Rectangle>(groupLocations);
        }


        public void modelChanged()
        {
            if (!groupsModel.getGroupInfos().contains(selectedGroup))
            {
                selectedGroup = null;
            }
            repaint();
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            int borderInset = 5;
            int paperInset = 10;
            int inset = 5;
            int insetPerGroup = 20;
            int currentY = paperInset + borderInset;

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            groupLocations.clear();

            ArrayList<ColumnInfo> pci = new ArrayList<ColumnInfo>(possibleColumnInfos);

            ArrayList<GroupInfo> groupInfos = groupsModel.getGroupInfos();
            for (int i = 0; i < groupInfos.size(); i++)
            {
                GroupInfo groupInfo = groupInfos.get(i);
                StringBuilder sb = new StringBuilder(100);
                ArrayList<ColumnInfo> columnInfos = groupInfo.getColumnInfos();
                for (int j = 0; j < columnInfos.size(); j++)
                {
                    ColumnInfo columnInfo = columnInfos.get(j);
                    sb.append(columnInfo.getColumnName());
                    if (j < columnInfos.size() - 1)
                    {
                        sb.append(", ");
                    }
                    pci.remove(columnInfo);
                }

                Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(sb.toString(), g);
                Rectangle rectangle = new Rectangle(i * insetPerGroup + paperInset + borderInset, currentY, getWidth() - (i * insetPerGroup) - 2 * paperInset - 2 * borderInset - 1, (int) (stringBounds.getHeight() + 0.5) + 2 * inset);
                groupLocations.add(rectangle);

                //noinspection ObjectEquality
                if (selectedGroup == groupInfo)
                {
                    g.setColor(selectionBackgrouond);
                }
                else
                {
                    g.setColor(background);
                }
                g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

                g.setColor(Color.DARK_GRAY);
                g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

                //noinspection ObjectEquality
                if (selectedGroup == groupInfo)
                {
                    g.setColor(selectionForeground);
                }
                else
                {
                    g.setColor(foreground);
                }

                Shape clip = g.getClip();
                g.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                g.drawString(sb.toString(), i * insetPerGroup + inset + paperInset + borderInset, (int) (currentY + -stringBounds.getY() + inset));
                g.setClip(clip);

                currentY += (int) (stringBounds.getHeight() + 0.5) + 2 * inset + 5;
            }


            StringBuilder sb2 = new StringBuilder(100);
            for (int j = 0; j < pci.size(); j++)
            {
                ColumnInfo columnInfo = pci.get(j);
                sb2.append(columnInfo.getColumnName());
                if (j < pci.size() - 1)
                {
                    sb2.append(", ");
                }
            }

            int i = groupInfos.size() - 1;
            if (i == -1)
            {
                i = 0;
                insetPerGroup = 0;
            }
            Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(sb2.toString(), g);
            Rectangle rectangle = new Rectangle(i * insetPerGroup + (insetPerGroup / 2) + paperInset + borderInset,
                                                currentY,
                                                getWidth() - (i * insetPerGroup) - insetPerGroup / 2 - 1 - 2 * paperInset - 2 * borderInset,
                                                getHeight() - currentY - paperInset - borderInset - 1);

            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

            g.setColor(foreground);
            Shape clip = g.getClip();
            g.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g.drawString(sb2.toString(), i * insetPerGroup + insetPerGroup / 2 + inset + paperInset + borderInset, (int) (currentY + -stringBounds.getY() + inset));
            g.setClip(clip);

            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), paperInset);
            g.fillRect(getWidth() - paperInset, 0, paperInset, getHeight());
            g.fillRect(0, getHeight() - paperInset, getWidth(), paperInset);
            g.fillRect(0, 0, paperInset, getHeight());

            g.setColor(Color.DARK_GRAY);
            g.fillRect(getWidth() - paperInset, paperInset + paperInset / 2, paperInset / 2, getHeight() - (paperInset + paperInset));
            g.fillRect(paperInset + paperInset / 2, getHeight() - paperInset, getWidth() - (2 * paperInset), paperInset / 2);

        }
    }

    private static interface GroupsModelListener
    {
        void modelChanged();
    }

    private static class GroupsModel
    {
        @NotNull
        private LinkedHashSet<GroupsModelListener> groupsModelListeners;
        @NotNull
        private ArrayList<GroupInfo> groupInfos;


        private GroupsModel()
        {
            groupsModelListeners = new LinkedHashSet<GroupsModelListener>();
            groupInfos = new ArrayList<GroupInfo>();
        }


        public void addGroup(@NotNull GroupInfo groupInfo)
        {
            groupInfos.add(groupInfo);
            fireModelChanged();
        }


        public void removeGroupInfo(@NotNull GroupInfo groupInfo)
        {
            groupInfos.remove(groupInfo);
            fireModelChanged();
        }


        public void moveGroup(int index, int destIndex)
        {
            GroupInfo groupInfo = groupInfos.remove(index);
            groupInfos.add(destIndex, groupInfo);
            fireModelChanged();
        }


        @NotNull
        public ArrayList<GroupInfo> getGroupInfos()
        {
            return groupInfos;
        }


        public void addGroupsModelListener(@NotNull GroupsModelListener groupsModelListener)
        {
            groupsModelListeners.add(groupsModelListener);
        }


        public void fireModelChanged()
        {
            LinkedHashSet<GroupsModelListener> listeners = new LinkedHashSet<GroupsModelListener>(groupsModelListeners);
            for (GroupsModelListener groupsModelListener : listeners)
            {
                groupsModelListener.modelChanged();
            }
        }
    }


}
