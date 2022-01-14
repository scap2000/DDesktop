package org.pentaho.reportdesigner.crm.report.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.config.ConfigEditor;
import org.jfree.report.modules.gui.config.model.ConfigTreeModel;
import org.jfree.report.modules.gui.config.model.ConfigTreeModelException;
import org.jfree.report.modules.gui.config.model.ConfigTreeModuleNode;
import org.jfree.report.modules.gui.config.model.ConfigTreeRootNode;
import org.jfree.report.modules.gui.config.model.ConfigTreeSectionNode;
import org.jfree.util.ObjectUtilities;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/**
 * User: Martin
 * Date: 17.03.2007
 * Time: 11:10:53
 */
public class ReportConfigurationEditor extends JPanel
{
    @NotNull
    private final ConfigEditorPanel detailEditorPane;

    @NotNull
    private ConfigTreeModel treeModel;

    @NotNull
    private final HierarchicalConfiguration currentReportConfiguration;


    public ReportConfigurationEditor(@NotNull ReportConfiguration reportConfiguration) throws ConfigTreeModelException
    {
        currentReportConfiguration = new HierarchicalConfiguration(JFreeReportBoot.getInstance().getGlobalConfig());

        HashMap<String, String> configProperties = reportConfiguration.getConfigProperties();
        Set<String> keys = configProperties.keySet();
        for (String key : keys)
        {
            String value = configProperties.get(key);
            currentReportConfiguration.setConfigProperty(key, value);
        }

        detailEditorPane = new ConfigEditorPanel();

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createEntryTree(), detailEditorPane);
        splitPane.setDividerLocation(250);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }


    @NotNull
    private JComponent createEntryTree() throws ConfigTreeModelException
    {
        final InputStream in = ObjectUtilities.getResourceRelativeAsStream("config-description.xml", ConfigEditor.class);//NON-NLS
        if (in == null)
        {
            throw new IllegalStateException("Missing resource 'config-description.xml'");
        }
        treeModel = new ConfigTreeModel(in);
        treeModel.init(currentReportConfiguration);

        final TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        final JTree tree = new JTree(treeModel);
        tree.setSelectionModel(selectionModel);
        tree.setCellRenderer(new DefaultTreeCellRenderer()
        {
            @NotNull
            public Component getTreeCellRendererComponent(@NotNull JTree tree, @Nullable Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                Object v = value;
                if (value instanceof ConfigTreeRootNode)
                {
                    v = "<Root>";//NON-NLS
                }
                else if (value instanceof ConfigTreeSectionNode)
                {
                    ConfigTreeSectionNode node = (ConfigTreeSectionNode) value;
                    v = node.getName();
                }
                else if (value instanceof ConfigTreeModuleNode)
                {
                    ConfigTreeModuleNode node = (ConfigTreeModuleNode) value;
                    v = node.getModule().getName();
                }
                return super.getTreeCellRendererComponent(tree, v, sel, expanded, leaf, row, hasFocus);
            }
        });

        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new ModuleTreeSelectionHandler());

        for (int i = 0; i < 100; i++)
        {
            tree.expandRow(i);
        }

        tree.setSelectionRow(1);

        final JScrollPane pane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return pane;
    }


    @NotNull
    public ReportConfiguration getReportConfiguration()
    {
        detailEditorPane.store();

        HashMap<String, String> configProperties = new HashMap<String, String>();
        Enumeration enumeration = currentReportConfiguration.getConfigProperties();
        while (enumeration.hasMoreElements())
        {
            String key = (String) enumeration.nextElement();
            configProperties.put(key, currentReportConfiguration.getConfigProperty(key));
        }
        return new ReportConfiguration(configProperties);
    }


    private class ModuleTreeSelectionHandler implements TreeSelectionListener
    {

        private ModuleTreeSelectionHandler()
        {
        }


        public void valueChanged(@NotNull final TreeSelectionEvent e)
        {
            final TreePath path = e.getPath();
            final Object lastPathElement = path.getLastPathComponent();
            if (lastPathElement instanceof ConfigTreeModuleNode)
            {
                final ConfigTreeModuleNode node = (ConfigTreeModuleNode) lastPathElement;
                detailEditorPane.store();
                detailEditorPane.editModule(node.getModule(), node.getConfiguration(), node.getAssignedKeys());
            }
            else
            {
                detailEditorPane.store();
                detailEditorPane.clear();
            }
        }
    }

}
