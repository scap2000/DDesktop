package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import nickyb.sqleonardo.querybuilder.QueryBuilder;
import nickyb.sqleonardo.querybuilder.QueryModel;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.commons.mql.ui.mqldesigner.IMQLQueryCallback;
import org.pentaho.commons.mql.ui.mqldesigner.MQLQueryBuilderDialog;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 24.02.2007 Time: 12:47:51
 */
@SuppressWarnings({"ALL"})//TODO temporary
public class QueryPanel extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(QueryPanel.class.getName());
    
    @NotNull
    private JList queryList;
    @NotNull
    private QueryListModel queryListModel;
    @NotNull
    private JButton addButton;
    @NotNull
    private JButton removeButton;
    @NotNull
    private JLabel queryNameLabel;
    @NotNull
    private JTextField queryNameTextField;
    @NotNull
    private JTextArea queryTextArea;
    @NotNull
    private JButton designerButton;

    private boolean multiQueryCapable;

    private String connectionType = MultiDataSetReportElementConfigurator.JNDI_CARD;
    private String xmiPath;



    public QueryPanel(@NotNull final JDialog parent, @NotNull final JList jndiSourceList, @NotNull ArrayList<Query> queries)
    {
        // noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        // noinspection ConstantConditions
        if (jndiSourceList == null)
        {
            throw new IllegalArgumentException("jndiSourceList must not be null");
        }
        // noinspection ConstantConditions
        if (queries == null)
        {
            throw new IllegalArgumentException("queries must not be null");
        }
        if (queries.isEmpty())
        {
            throw new IllegalArgumentException("queries must have at least one query (main query)");
        }

        ArrayList<Query> copyQueries = new ArrayList<Query>();
        for (Query query : queries)
        {
            copyQueries.add(new Query(query.getQueryName(), query.getQuery()));
        }

        multiQueryCapable = true;

        queryListModel = new QueryListModel(copyQueries);
        queryList = new JList(queryListModel);

        queryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        queryNameTextField = ComponentFactory.createTextField(true, true);
        queryNameLabel = ComponentFactory.createLabel("R", "QueryPanel.QueryNameLabel", queryNameTextField);

        queryTextArea = ComponentFactory.createTextArea(true, true);
        designerButton = ComponentFactory.createButton("R", "QueryPanel.DesignerButton");

        addButton = ComponentFactory.createButton("R", "QueryPanel.AddButton");
        removeButton = ComponentFactory.createButton("R", "QueryPanel.RemoveButton");

        queryTextArea.setWrapStyleWord(true);
        queryTextArea.setLineWrap(true);

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, 4dlu, fill:default:grow", "pref, 4dlu, fill:10dlu:grow, 4dlu, default");
        @NonNls
        CellConstraints cc = new CellConstraints();

        setLayout(formLayout);

        @NonNls
        FormLayout listPanelLayout = new FormLayout("fill:default:grow, 4dlu, fill:default:grow", "fill:default:grow, 4dlu, default");
        JPanel listPanel = new JPanel(listPanelLayout);
        listPanel.setLayout(listPanelLayout);
        listPanel.add(new JScrollPane(queryList), cc.xyw(1, 1, 3));
        listPanel.add(addButton, cc.xy(1, 3));
        listPanel.add(removeButton, cc.xy(3, 3));

        //add(listPanel, cc.xywh(1, 1, 1, 5));

        JPanel queryNameHelperPanel = new JPanel(new BorderLayout());
        queryNameHelperPanel.add(queryNameLabel, BorderLayout.WEST);
        queryNameHelperPanel.add(queryNameTextField, BorderLayout.CENTER);

        add(queryNameHelperPanel, cc.xy(3, 1));
        add(new JScrollPane(queryTextArea), cc.xy(3, 3));
        add(designerButton, cc.xy(3, 5, "center, center"));

        queryList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (queryList.getSelectedIndex() != -1)
                {
                    queryNameTextField.setText(queryListModel.getElementAt(queryList.getSelectedIndex()).getQueryName());
                    queryTextArea.setText(queryListModel.getElementAt(queryList.getSelectedIndex()).getQuery());
                }
                else
                {
                    // queryNameTextField.setText("");
                    // queryTextArea.setText("");
                }
                updateComponentState();
            }
        });

        queryListModel.addListDataListener(new ListDataListener()
        {
            public void intervalAdded(@NotNull ListDataEvent e)
            {
                updateComponentState();
            }


            public void intervalRemoved(@NotNull ListDataEvent e)
            {
                updateComponentState();
            }


            public void contentsChanged(@NotNull ListDataEvent e)
            {
                updateComponentState();
            }
        });

        queryNameTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            private void update()
            {
                Query query = queryListModel.getElementAt(queryList.getSelectedIndex());
                query.setQueryName(queryNameTextField.getText());

                queryListModel.notifyQueryChanged(queryList.getSelectedIndex());
            }
        });
        queryTextArea.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                update();
            }


            private void update()
            {
                Query query = queryListModel.getElementAt(queryList.getSelectedIndex());
                query.setQuery(queryTextArea.getText());

                queryListModel.notifyQueryChanged(queryList.getSelectedIndex());
            }
        });

        designerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (jndiSourceList.getSelectedValue() == null && !connectionType.equals(MultiDataSetReportElementConfigurator.MQL_CARD))
                {
                    JOptionPane.showMessageDialog(QueryPanel.this, TranslationManager.getInstance().getTranslation("R", "QueryPanel.SelectDatasource"));
                    return;
                }
                if (connectionType.equals(MultiDataSetReportElementConfigurator.MQL_CARD))
                {
                    try
                    {
                        CWMStartup.loadCWMInstance("resources/metadata/repository.properties", "resources/metadata/PentahoCWM.xml");
                        CWMStartup.loadMetadata(xmiPath, "resources");

                        @SuppressWarnings({"ALL"})//TODO temporary
                        IMQLQueryCallback callback = new IMQLQueryCallback()
                        {

                            public void queryGenerated(String query)
                            {
                                queryTextArea.setText(query);
                            }

                        };

//                        Display display = new Display(); // display object to manage SWT lifecycle.
                        Canvas canvas = new Canvas();
                        Shell shell = SWT_AWT.new_Shell(Display.getDefault(), canvas);
                        MQLQueryBuilderDialog mqlQueryBuilder = new MQLQueryBuilderDialog(shell);
                        if (queryTextArea.getText() != null && !queryTextArea.getText().equals(""))
                        {
                            MQLQuery mqlQuery = new MQLQuery(queryTextArea.getText(), "en_US", new CwmSchemaFactory()); //$NON-NLS-1$
                            mqlQueryBuilder = new MQLQueryBuilderDialog(shell, mqlQuery);
                        }
                        mqlQueryBuilder.setCallback(callback);
//                        try
//                        {
//                            shell.moveAbove(null);
//                        }
//                        catch (Exception ex)
//                        {
//                        }
                        mqlQueryBuilder.open();
                        MQLQuery mqlQuery = mqlQueryBuilder.getMqlQuery();
                        queryTextArea.setText(mqlQuery.getXML());
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
                else if (connectionType.equals(MultiDataSetReportElementConfigurator.JNDI_CARD))
                {

                    final JNDISource selectedJNDISource = (JNDISource) jndiSourceList.getSelectedValue();

                    Driver driver = null;
                    try
                    {
                        driver = DriverManager.getDriver(selectedJNDISource.getConnectionString());
                    }
                    catch (Exception ex)
                    {
                        // if we don't find this connection, it isn't registered, so we'll try to find it on the classpath
                    }
                    if (driver == null)
                    {
                        try
                        {
                            Class driverClass = Class.forName(selectedJNDISource.getDriverClass());
                            driver = (Driver) driverClass.newInstance();
                            DriverManager.registerDriver(driver);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    Properties info = new Properties();
                    info.put("user", selectedJNDISource.getUsername()); //$NON-NLS-1$
                    info.put("password", selectedJNDISource.getPassword()); //$NON-NLS-1$
                    Connection conn = null;
                    try
                    {
                        conn = driver.connect(selectedJNDISource.getConnectionString(), info);
                    }
                    catch (SQLException ex)
                    {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }

                    final QueryBuilder queryBuilder = new QueryBuilder(conn);
                    queryBuilder.autoAlias = false;
                    String schema = null;
                    try
                    {
                        if (conn.getMetaData().supportsSchemasInTableDefinitions())
                        {
                            List schemas = new ArrayList();
                            ResultSet rs = conn.getMetaData().getSchemas();
                            while (rs.next())
                            {
                                String name = rs.getString(1).trim();
                                if (!schemas.contains(name))
                                {
                                    schemas.add(name);
                                }
                            }
                            rs.close();

                            // bring up dialog
                            final CenterPanelDialog chooseSchemaDialog = CenterPanelDialog.createDialog(parent, "Choose Schema", true);
                            chooseSchemaDialog.setResizable(true);
                            chooseSchemaDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            WindowUtils.setLocationRelativeTo(chooseSchemaDialog, parent);
                            chooseSchemaDialog.setLayout(new BorderLayout());
                            JComboBox comboBox = new JComboBox(schemas.toArray());
                            chooseSchemaDialog.add(comboBox, BorderLayout.NORTH);
                            JPanel buttonPanel = new JPanel();
                            JButton okButton = new JButton("OK");
                            okButton.addActionListener(new ActionListener()
                            {

                                public void actionPerformed(@NotNull ActionEvent e)
                                {
                                    chooseSchemaDialog.dispose();
                                }

                            });
                            buttonPanel.add(okButton);
                            chooseSchemaDialog.add(buttonPanel, BorderLayout.SOUTH);

                            chooseSchemaDialog.pack();
                            chooseSchemaDialog.setVisible(true);
                            schema = (String) comboBox.getSelectedItem();
                        }
                    }
                    catch (Exception ex)
                    {
                    }

                    final JDialog frame = new JDialog(parent, "SQLeonardo Query Builder", false);
                    frame.setLayout(new BorderLayout());
                    frame.add(queryBuilder, BorderLayout.CENTER);

                    JPanel buttonPanel = new JPanel();
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(new ActionListener()
                    {

                        public void actionPerformed(@NotNull ActionEvent arg0)
                        {
                            String query = queryBuilder.getQueryModel().toString(true);
                            queryTextArea.setText(query);
                            frame.setVisible(false);
                            frame.dispose();
                        }

                    });
                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(new ActionListener()
                    {

                        public void actionPerformed(@NotNull ActionEvent arg0)
                        {
                            frame.setVisible(false);
                            frame.dispose();
                        }

                    });
                    JButton previewButton = new JButton("Preview");
                    previewButton.addActionListener(new ActionListener()
                    {

                        public void actionPerformed(@NotNull ActionEvent arg0)
                        {
                            try
                            {
                                String query = queryBuilder.getQueryModel().toString(true);
                                JDBCTableModel tableModel = new JDBCTableModel(selectedJNDISource, query, 500);

                                CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(frame, TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElementConfigurator.Title"), true);
                                centerPanelDialog.add(new JScrollPane(new JTable(tableModel)));
                                centerPanelDialog.pack();
                                centerPanelDialog.setResizable(true);
                                centerPanelDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                centerPanelDialog.setSize(1024, 768);
                                WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
                                centerPanelDialog.setVisible(true);

                            }
                            catch (Exception e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "QueryPanel.actionPerformed ", e);
                            }
                        }

                    });

                    buttonPanel.add(previewButton);
                    buttonPanel.add(okButton);
                    buttonPanel.add(cancelButton);
                    frame.add(buttonPanel, BorderLayout.SOUTH);

                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(900, 700);
                    WindowUtils.setLocationRelativeTo(frame, parent);
                    frame.setVisible(true);

                    try
                    {
                        QueryModel queryModel = nickyb.sqleonardo.querybuilder.syntax.SQLParser.toQueryModel(queryTextArea.getText());
                        queryBuilder.setQueryModel(queryModel);
                    }
                    catch (Exception e1)
                    {
                    }

                    try
                    {
                        if (schema != null)
                        {
                            QueryModel qm = queryBuilder.getQueryModel();
                            qm.setSchema(schema);
                            queryBuilder.setQueryModel(qm);
                        }
                    }
                    catch (Exception e1)
                    {
                    }

                    // final JDialog frame = new JDialog(parent, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.DesignerTitle"), true);
                    //
                    // final boolean[] ok = new boolean[]{false};
                    // JNDISource selectedJNDISource = (JNDISource) jndiSourceList.getSelectedValue();
                    // final QueryComposerPanel queryPanel = new QueryComposerPanel(selectedJNDISource.getDriverClass(), selectedJNDISource.getConnectionString(), selectedJNDISource.getUsername(), selectedJNDISource.getPassword(), new
                    // AbstractAction()
                    // {
                    // public void actionPerformed(@Nullable ActionEvent e)//Nullable is a special case for the ClickableImage
                    // {
                    // ok[0] = true;
                    // frame.dispose();
                    // }
                    // });
                    //
                    // queryPanel.connect();
                    //
                    // frame.getContentPane().add(queryPanel, BorderLayout.CENTER);
                    // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    // frame.setSize(900, 700);
                    // frame.setLocationRelativeTo(parent);
                    // frame.setVisible(true);
                    //
                    // if (ok[0])
                    // {
                    // String query = queryPanel.getQuery();
                    // queryTextArea.setText(query);
                    // }
                }
            }
        });

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                queryListModel.addQuery(new Query("new", ""));// NON-NLS
                int index = queryListModel.getSize() - 1;
                queryList.setSelectedIndex(index);
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int index = queryList.getSelectedIndex();
                queryListModel.removeQuery(index);
                if (queryListModel.getSize() > 0)
                {
                    queryList.setSelectedIndex(Math.max(0, index - 1));
                }
            }
        });

        if (!copyQueries.isEmpty())
        {
            queryList.setSelectedIndex(0);
        }

        updateComponentState();
    }


    @NotNull
    public JButton getDesignerButton()
    {
        return designerButton;
    }


    private void updateComponentState()
    {
        addButton.setEnabled(multiQueryCapable);
        removeButton.setEnabled(queryList.getSelectedIndex() > 0);

        queryNameTextField.setEnabled(queryList.getSelectedIndex() != -1);
        queryTextArea.setEnabled(queryList.getSelectedIndex() != -1);
        designerButton.setEnabled(queryList.getSelectedIndex() != -1);
    }


    public boolean isMultiQueryCapable()
    {
        return multiQueryCapable;
    }


    public void setMultiQueryCapable(boolean multiQueryCapable)
    {
        this.multiQueryCapable = multiQueryCapable;
        if (!multiQueryCapable)
        {
            for (int i = 1; i < queryListModel.getSize(); i++)
            {
                queryListModel.removeQuery(i);
            }
        }

        updateComponentState();
    }


    @NotNull
    public ArrayList<Query> getQueries()
    {
        return queryListModel.getQueries();
    }


    private static class QueryListModel extends AbstractListModel
    {
        @NotNull
        private ArrayList<Query> queries;


        private QueryListModel(@NotNull ArrayList<Query> queries)
        {
            // noinspection ConstantConditions
            if (queries == null)
            {
                throw new IllegalArgumentException("queries must not be null");
            }

            for (Query query : queries)
            {
                // noinspection ConstantConditions
                if (query == null)
                {
                    throw new IllegalArgumentException("query must not be null");
                }
            }

            this.queries = queries;
        }


        public void addQuery(@NotNull Query query)
        {
            // noinspection ConstantConditions
            if (query == null)
            {
                throw new IllegalArgumentException("query must not be null");
            }

            queries.add(query);

            fireIntervalAdded(this, queries.size() - 1, queries.size() - 1);
        }


        public void notifyQueryChanged(int index)
        {
            fireContentsChanged(this, index, index);
        }


        @NotNull
        public Query removeQuery(int index)
        {
            Query query = queries.remove(index);
            fireIntervalRemoved(this, index, index);
            return query;
        }


        public int getSize()
        {
            return queries.size();
        }


        @NotNull
        public ArrayList<Query> getQueries()
        {
            return new ArrayList<Query>(queries);
        }


        @NotNull
        public Query getElementAt(int index)
        {
            return queries.get(index);
        }
    }


    public String getConnectionType()
    {
        return connectionType;
    }


    public void setConnectionType(String connectionType)
    {
        this.connectionType = connectionType;
    }


    public String getXmiPath()
    {
        return xmiPath;
    }


    public void setXmiPath(String xmiPath)
    {
        this.xmiPath = xmiPath;
    }

}
