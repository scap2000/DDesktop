package org.pentaho.reportdesigner.crm.report.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.modules.Module;
import org.jfree.report.modules.gui.config.VerticalLayout;
import org.jfree.report.modules.gui.config.editor.EditorFactory;
import org.jfree.report.modules.gui.config.editor.ModuleEditor;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;

public class ConfigEditorPanel extends JPanel
{
    @NotNull
    private final JTextArea descriptionArea;

    @NotNull
    private final JLabel moduleNameField;

    @NotNull
    private final JPanel editorArea;

    @Nullable
    private ModuleEditor moduleEditor;


    /**
     * Creates a new ConfigEditorPanel.
     */
    public ConfigEditorPanel()
    {
        moduleNameField = new JLabel();
        Font font = moduleNameField.getFont();
        moduleNameField.setFont(FontUtils.getDerivedFont(font, Font.BOLD, font.getSize() + 4));

        descriptionArea = new JTextArea();
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        editorArea = new JPanel();
        editorArea.setLayout(new BorderLayout());

        JPanel contentArea = new JPanel();
        contentArea.setLayout(new VerticalLayout());
        contentArea.add(moduleNameField);
        contentArea.add(descriptionArea);

        setLayout(new BorderLayout());
        add(contentArea, BorderLayout.NORTH);
        add(editorArea, BorderLayout.CENTER);
    }


    public void editModule(@NotNull final Module module, @NotNull final HierarchicalConfiguration config, @NotNull final ConfigDescriptionEntry[] entries)
    {
        moduleNameField.setText(module.getName());
        descriptionArea.setText(module.getDescription());

        editorArea.removeAll();

        moduleEditor = EditorFactory.getInstance().getModule(module, config, entries);
        ModuleEditor me = moduleEditor;
        if (me != null)
        {
            JComponent comp = me.getComponent();
            fixEditorPaneStyle(comp);
            editorArea.add(comp);
            me.reset();
        }
        revalidate();
        repaint();
    }


    private void fixEditorPaneStyle(@NotNull Container comp)
    {
        for (int i = 0; i < comp.getComponentCount(); i++)
        {
            Component child = comp.getComponent(i);
            if (child instanceof JEditorPane)
            {
                JEditorPane editorPane = (JEditorPane) child;
                HTMLDocument htmlDocument = (HTMLDocument) editorPane.getDocument();
                htmlDocument.getStyleSheet().addRule("body { font-family:sans-serif; }");//NON-NLS

            }
            else if (child instanceof Container)
            {
                Container container = (Container) child;
                fixEditorPaneStyle(container);
            }
        }
    }


    public void clear()
    {
        moduleNameField.setText("");
        descriptionArea.setText("");
        editorArea.removeAll();
        revalidate();
        repaint();
    }


    public void reset()
    {
        if (moduleEditor != null)
        {
            moduleEditor.reset();
        }
    }


    public void store()
    {
        if (moduleEditor != null)
        {
            moduleEditor.store();
        }
    }

}
