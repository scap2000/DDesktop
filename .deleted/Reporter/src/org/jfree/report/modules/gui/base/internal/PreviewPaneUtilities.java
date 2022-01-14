/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PreviewPaneUtilities.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.base.internal;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.base.actions.ControlAction;
import org.jfree.report.modules.gui.base.actions.ControlActionPlugin;
import org.jfree.report.modules.gui.base.actions.ExportAction;
import org.jfree.report.modules.gui.base.actions.ZoomAction;
import org.jfree.report.modules.gui.base.actions.ZoomListActionPlugin;
import org.jfree.report.modules.gui.common.DefaultIconTheme;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.commonswing.ActionFactory;
import org.jfree.report.modules.gui.commonswing.ActionPlugin;
import org.jfree.report.modules.gui.commonswing.ActionPluginMenuComparator;
import org.jfree.report.modules.gui.commonswing.DefaultActionFactory;
import org.jfree.report.modules.gui.commonswing.ExportActionPlugin;
import org.jfree.report.modules.gui.commonswing.SwingCommonModule;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.FloatingButtonEnabler;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 17.11.2006, 15:06:51
 *
 * @author Thomas Morgner
 */
public class PreviewPaneUtilities
{
  private static class ReportJobListener implements PropertyChangeListener
  {
    private JComponent component;
    private PreviewPane previewPane;

    protected ReportJobListener(final JComponent component,
                                final PreviewPane previewPane)
    {
      this.previewPane = previewPane;
      this.component = component;
      this.component.setEnabled(previewPane.getReportJob() != null);
    }

    public void propertyChange(final PropertyChangeEvent evt)
    {
      component.setEnabled(previewPane.getReportJob() != null);
    }
  }

  /**
   * A zoom select action.
   */
  private static class ZoomSelectAction extends AbstractAction
  {
    private KeyedComboBoxModel source;
    private PreviewPane pane;

    /**
     * Creates a new action.
     */
    protected ZoomSelectAction(final KeyedComboBoxModel source,
                            final PreviewPane pane)
    {
      this.source = source;
      this.pane = pane;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      final Double selected = (Double) source.getSelectedKey();
      if (selected != null)
      {
        pane.setZoom(selected.doubleValue());
      }
//      else
//      {
//        Log.warn ("No selected key! : " + pane.getZoom());
//      }
    }
  }

  private static final String ICON_THEME_CONFIG_KEY = "org.jfree.report.modules.gui.common.IconTheme"; //$NON-NLS-1$
  private static final String ACTION_FACTORY_CONFIG_KEY = "org.jfree.report.modules.gui.base.ActionFactory"; //$NON-NLS-1$
  private static final String CATEGORY_PREFIX = "org.jfree.report.modules.gui.swing.category."; //$NON-NLS-1$

  private PreviewPaneUtilities()
  {
  }

  public static JMenu createMenu(final ActionCategory cat)
  {
    final JMenu menu = new JMenu();
    menu.setText(cat.getDisplayName());
    final Integer mnemonicKey = cat.getMnemonicKey();
    if (mnemonicKey != null)
    {
      menu.setMnemonic(mnemonicKey.intValue());
    }
    final String toolTip = cat.getShortDescription();
    if (toolTip != null && toolTip.length() > 0)
    {
      menu.setToolTipText(toolTip);
    }
    return menu;
  }


  public static int buildMenu(final JMenu menu,
                              final ActionPlugin[] actions,
                              final PreviewPane pane)
  {
    if (actions.length == 0)
    {
      return 0;
    }

    Arrays.sort(actions, new ActionPluginMenuComparator());
    boolean separatorPending = false;
    int count = 0;
    for (int i = 0; i < actions.length; i++)
    {
      final ActionPlugin actionPlugin = actions[i];
      if (actionPlugin.isAddToMenu() == false)
      {
        continue;
      }

      if (count > 0 && separatorPending)
      {
        menu.addSeparator();
        separatorPending = false;
      }

      if (actionPlugin instanceof ExportActionPlugin)
      {
        final ExportActionPlugin exportPlugin = (ExportActionPlugin) actionPlugin;
        final ExportAction action = new ExportAction(exportPlugin, pane);
        menu.add(new ActionMenuItem(action));
        count += 1;
      }
      else if (actionPlugin instanceof ControlActionPlugin)
      {
        final ControlActionPlugin controlPlugin = (ControlActionPlugin) actionPlugin;
        final ControlAction action = new ControlAction(controlPlugin, pane);
        menu.add(new ActionMenuItem(action));
        count += 1;
      }
      else if (actionPlugin instanceof ZoomListActionPlugin)
      {
        buildViewMenu(menu, pane);
      }

      if (actionPlugin.isSeparated())
      {
        separatorPending = true;
      }

    }
    return count;
  }

  private static void buildViewMenu(final JMenu zoom, final PreviewPane pane)
  {
    final double[] zoomFactors = pane.getZoomFactors();
    for (int i = 0; i < zoomFactors.length; i++)
    {
      final double factor = zoomFactors[i];
      zoom.add(new ActionMenuItem(new ZoomAction(factor, pane)));
    }
  }

  public static void addActionsToToolBar(final JToolBar toolBar,
                                         final ActionPlugin[] reportActions,
                                         final PreviewPane pane)
  {
    if (reportActions == null)
    {
      return;
    }

    boolean separatorPending = false;
    int count = 0;

    for (int i = 0; i < reportActions.length; i++)
    {
      final ActionPlugin actionPlugin = reportActions[i];
      if (actionPlugin.isAddToToolbar() == false)
      {
        continue;
      }

      if (count > 0 && separatorPending)
      {
        toolBar.addSeparator();
        separatorPending = false;
      }

      if (actionPlugin instanceof ExportActionPlugin)
      {
        final ExportActionPlugin exportPlugin = (ExportActionPlugin) actionPlugin;
        final ExportAction action = new ExportAction(exportPlugin, pane);
        toolBar.add(createButton(action, pane.getSwingGuiContext()));
        count += 1;
      }
      else if (actionPlugin instanceof ControlActionPlugin)
      {
        final ControlActionPlugin controlPlugin = (ControlActionPlugin) actionPlugin;
        final ControlAction action = new ControlAction(controlPlugin, pane);
        toolBar.add(createButton(action, pane.getSwingGuiContext()));
        count += 1;
      }
      else if (actionPlugin instanceof ZoomListActionPlugin)
      {
        final JPanel zoomPane = new JPanel();
        zoomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        zoomPane.add(createZoomSelector(pane));
        toolBar.add(zoomPane);
        count += 1;
      }

      if (actionPlugin.isSeparated())
      {
        separatorPending = true;
      }
    }
  }

  private static JComboBox createZoomSelector(final PreviewPane pane)
  {
    final JComboBox zoomSelect = new JComboBox(pane.getZoomModel());
    zoomSelect.addActionListener(new ZoomSelectAction(pane.getZoomModel(), pane));
    zoomSelect.setAlignmentX(Component.RIGHT_ALIGNMENT);
    pane.addPropertyChangeListener(PreviewPane.PAGINATED_PROPERTY, new ReportJobListener(zoomSelect, pane));
    return zoomSelect;
  }

  /**
   * Creates a button using the given action properties for the button's
   * initialisation.
   *
   * @param action the action used to set up the button.
   * @return a button based on the supplied action.
   */
  private static JButton createButton(final Action action,
                                      final SwingGuiContext swingGuiContext)
  {
    final JButton button = new ActionButton(action);
    boolean needText = true;
    if (isLargeButtonsEnabled(swingGuiContext))
    {
      final Icon icon = (Icon) action.getValue(SwingCommonModule.LARGE_ICON_PROPERTY);
      if (icon != null && (icon.getIconHeight() > 1 && icon.getIconHeight() > 1))
      {
        button.setIcon(icon);
        needText = false;
      }
    }
    else
    {
      final Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
      if (icon != null && (icon.getIconHeight() > 1 && icon.getIconHeight() > 1))
      {
        button.setIcon(icon);
        needText = false;
      }
    }

    if (needText)
    {
      final Object value = action.getValue(Action.NAME);
      if (value != null)
      {
        button.setText(String.valueOf(value));
      }
    }
    else
    {
      button.setText(null);
      button.setMargin(new Insets(0, 0, 0, 0));
    }

    FloatingButtonEnabler.getInstance().addButton(button);
    return button;
  }

  private static boolean isLargeButtonsEnabled(final SwingGuiContext swingGuiContext)
  {
    final Configuration configuration = swingGuiContext.getConfiguration();
    if ("true".equals(configuration.getConfigProperty //$NON-NLS-1$
        ("org.jfree.report.modules.gui.swing.preview.LargeIcons"))) //$NON-NLS-1$
    {
      return true;
    }
    return false;
  }


  public static double getNextZoomOut(final double zoom,
                                      final double[] zoomFactors)
  {
    if (zoom <= zoomFactors[0])
    {
      return (zoom * 2.0) / 3.0;
    }

    final double largestZoom = zoomFactors[zoomFactors.length - 1];
    if (zoom > largestZoom)
    {
      final double linear = (zoom * 2.0) / 3.0;
      if (linear < largestZoom)
      {
        return largestZoom;
      }
      return linear;
    }

    for (int i = zoomFactors.length - 1; i >= 0; i--)
    {
      final double factor = zoomFactors[i];
      if (factor < zoom)
      {
        return factor;
      }
    }

    return (zoom * 2.0) / 3.0;
  }

  public static double getNextZoomIn(final double zoom,
                                     final double[] zoomFactors)
  {
    final double largestZoom = zoomFactors[zoomFactors.length - 1];
    if (zoom >= largestZoom)
    {
      return (zoom * 1.5);
    }

    final double smallestZoom = zoomFactors[0];
    if (zoom < smallestZoom)
    {
      final double linear = (zoom * 1.5);
      if (linear > smallestZoom)
      {
        return smallestZoom;
      }
      return linear;
    }

    for (int i = 0; i < zoomFactors.length; i++)
    {
      final double factor = zoomFactors[i];
      if (factor > zoom)
      {
        return factor;
      }
    }
    return (zoom * 1.5);
  }


  public static IconTheme createIconTheme(final Configuration config)
  {
    final String themeClass = config.getConfigProperty(ICON_THEME_CONFIG_KEY);
    final Object maybeTheme = ObjectUtilities.loadAndInstantiate(themeClass, PreviewPane.class, IconTheme.class);
    final IconTheme iconTheme;
    if (maybeTheme != null)
    {
      iconTheme = (IconTheme) maybeTheme;
    }
    else
    {
      iconTheme = new DefaultIconTheme();
    }
    iconTheme.initialize(config);
    return iconTheme;
  }

  public static ActionFactory createActionFactory(final Configuration config)
  {
    final String factoryClass = config.getConfigProperty(ACTION_FACTORY_CONFIG_KEY);
    final Object maybeFactory = ObjectUtilities.loadAndInstantiate
        (factoryClass, PreviewPane.class, ActionFactory.class);
    final ActionFactory actionFactory;
    if (maybeFactory != null)
    {
      actionFactory = (ActionFactory) maybeFactory;
    }
    else
    {
      actionFactory = new DefaultActionFactory();
    }
    return actionFactory;
  }

  public static CategoryTreeItem[] buildMenuTree(final ActionCategory[] categories)
  {
    final CategoryTreeItem[] tree = new CategoryTreeItem[categories.length];
    for (int i = 0; i < categories.length; i++)
    {
      final ActionCategory category = categories[i];
      tree[i] = new CategoryTreeItem(category);
    }

    for (int j = 0; j < tree.length; j++)
    {
      final CategoryTreeItem item = tree[j];
      final String itemName = item.getName();
      int parentWeight = 0;
      CategoryTreeItem parent = null;
      // now for each item, find the best parent item.
      for (int k = 0; k < tree.length; k++)
      {
        if (k == j)
        {
          // never add yourself ..
          continue;
        }
        final CategoryTreeItem treeItem = tree[k];
        final String parentName = treeItem.getName();
        if (itemName.startsWith(parentName) == false)
        {
          continue;
        }
        if (parentName.length() > parentWeight)
        {
          parent = treeItem;
          parentWeight = parentName.length();
        }
      }

      item.setParent(parent);
    }

    for (int j = 0; j < tree.length; j++)
    {
      final CategoryTreeItem item = tree[j];
      final CategoryTreeItem parent = item.getParent();
      if (parent != null)
      {
        parent.add(item);
      }
    }
    return tree;
  }

  public static HashMap loadActions(final SwingGuiContext swingGuiContext)
  {
    final HashMap actions = new HashMap();

    final Configuration configuration = swingGuiContext.getConfiguration();
    final ActionCategory[] categories = loadCategories(swingGuiContext);
    final ActionFactory factory = PreviewPaneUtilities.createActionFactory(configuration);

    for (int i = 0; i < categories.length; i++)
    {
      final ActionCategory category = categories[i];
      actions.put(category, factory.getActions(swingGuiContext, category.getName()));
    }
    return actions;
  }


  public static ActionCategory[] loadCategories(final SwingGuiContext swingGuiContext)
  {
    final ArrayList categories = new ArrayList();
    final Configuration configuration = swingGuiContext.getConfiguration();
    final Iterator keys = configuration.findPropertyKeys(CATEGORY_PREFIX);
    while (keys.hasNext())
    {
      final String enableKey = (String) keys.next();
      if (enableKey.endsWith(".enabled") == false) //$NON-NLS-1$
      {
        continue;
      }

      if ("true".equals(configuration.getConfigProperty(enableKey)) == false) //$NON-NLS-1$
      {
        continue;
      }

      final String base = enableKey.substring(0, enableKey.length() - ".enabled".length()); //$NON-NLS-1$
      if (base.length() == 0)
      {
        continue;
      }

      final String categoryKey = base.substring(CATEGORY_PREFIX.length());
      final String className = configuration.getConfigProperty(base + ".class"); //$NON-NLS-1$
      ActionCategory actionCategory;
      if (className == null)
      {
        actionCategory = new ActionCategory();
      }
      else
      {
        actionCategory = (ActionCategory) ObjectUtilities.loadAndInstantiate
            (className, PreviewPane.class, ActionCategory.class);
        if (actionCategory == null)
        {
          actionCategory = new ActionCategory();
        }
      }

      final String positionText = configuration.getConfigProperty(base + ".position"); //$NON-NLS-1$
      actionCategory.setPosition(StringUtil.parseInt(positionText, 0));
      actionCategory.setName(categoryKey);
      actionCategory.setResourceBase(configuration.getConfigProperty(base + ".resource-base")); //$NON-NLS-1$
      actionCategory.setResourcePrefix(configuration.getConfigProperty(base + ".resource-prefix")); //$NON-NLS-1$
      actionCategory.initialize(swingGuiContext);
      categories.add(actionCategory);
    }

    return (ActionCategory[]) categories.toArray
        (new ActionCategory[categories.size()]);
  }
}
