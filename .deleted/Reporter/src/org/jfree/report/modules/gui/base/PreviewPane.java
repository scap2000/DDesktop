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
 * PreviewPane.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.gui.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.print.PageFormat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.jfree.layout.CenterLayout;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.PageDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;
import org.jfree.report.modules.gui.base.internal.ActionCategory;
import org.jfree.report.modules.gui.base.internal.ActionPluginComparator;
import org.jfree.report.modules.gui.base.internal.CategoryTreeItem;
import org.jfree.report.modules.gui.base.internal.PageBackgroundDrawable;
import org.jfree.report.modules.gui.base.internal.PreviewPaneUtilities;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.common.StatusListener;
import org.jfree.report.modules.gui.commonswing.ActionPlugin;
import org.jfree.report.modules.gui.commonswing.ReportEventSource;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.gui.commonswing.SwingUtil;
import org.jfree.report.modules.gui.commonswing.WindowSizeLimiter;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;
import org.jfree.report.modules.output.pageable.graphics.PrintReportProcessor;
import org.jfree.report.util.StringUtil;
import org.jfree.report.util.Worker;
import org.jfree.report.util.i18n.Messages;
import org.jfree.ui.Drawable;
import org.jfree.ui.DrawablePanel;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.11.2006, 19:36:13
 *
 * @author Thomas Morgner
 */
public class PreviewPane extends JPanel implements ReportEventSource {

    private class PreviewGuiContext implements SwingGuiContext {

	protected PreviewGuiContext() {

	}

	public Window getWindow() {
	    return SwingUtil.getWindowAncestor(PreviewPane.this);
	}

	public Locale getLocale() {
	    final JFreeReport report = getReportJob();
	    if (report != null) {
		return report.getResourceBundleFactory().getLocale();
	    }
	    return Locale.getDefault();
	}

	public IconTheme getIconTheme() {
	    return PreviewPane.this.getIconTheme();
	}

	public Configuration getConfiguration() {
	    final JFreeReport report = getReportJob();
	    if (report != null) {
		return report.getConfiguration();
	    }
	    return JFreeReportBoot.getInstance().getGlobalConfig();
	}

	public StatusListener getStatusListener() {
	    return PreviewPane.this.getStatusListener();
	}

	public ReportEventSource getEventSource() {
	    return PreviewPane.this;
	}

    }

    private class PreviewPaneStatusUpdater implements Runnable {

	private StatusType type;
	private String text;

	protected PreviewPaneStatusUpdater(final StatusType type, final String text) {
	    this.type = type;
	    this.text = text;
	}

	public StatusType getType() {
	    return type;
	}

	public String getText() {
	    return text;
	}

	/**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
	public void run() {
	    setStatusType(type);
	    setStatusText(text);
	}

    }

    /**
   * The StatusListener here shields the preview pane from any attempt to tamper with it.
   */
    private class PreviewPaneStatusListener implements StatusListener {

	protected PreviewPaneStatusListener() {

	}

	public void setStatus(final StatusType type, final String text) {
	    if (SwingUtilities.isEventDispatchThread()) {
		setStatusType(type);
		setStatusText(text);
	    } else {
		SwingUtilities.invokeLater(new PreviewPaneStatusUpdater(type, text));
	    }
	}

    }

    private class RepaginationRunnable implements Runnable, ReportProgressListener {

	private PrintReportProcessor processor;

	protected RepaginationRunnable(final PrintReportProcessor processor) {
	    this.processor = processor;
	}

	public void reportProcessingStarted(final ReportProgressEvent event) {
	    forwardReportStartedEvent(event);
	}

	public void reportProcessingUpdate(final ReportProgressEvent event) {
	    forwardReportUpdateEvent(event);
	}

	public void reportProcessingFinished(final ReportProgressEvent event) {
	    forwardReportFinishedEvent(event);
	}

	/**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
	public void run() {
	    this.processor.addReportProgressListener(this);
	    try {
		final UpdatePaginatingPropertyHandler startPaginationNotify = new UpdatePaginatingPropertyHandler(processor, true, false, 0);
		if (SwingUtilities.isEventDispatchThread()) {
		    startPaginationNotify.run();
		} else {
		    SwingUtilities.invokeLater(startPaginationNotify);
		}
		// Perform the pagination ..
		final int pageCount = processor.getNumberOfPages();
		final UpdatePaginatingPropertyHandler endPaginationNotify = new UpdatePaginatingPropertyHandler(processor, false, true, pageCount);
		if (SwingUtilities.isEventDispatchThread()) {
		    endPaginationNotify.run();
		} else {
		    SwingUtilities.invokeLater(endPaginationNotify);
		}
	    } catch (Exception e) {
		final UpdatePaginatingPropertyHandler endPaginationNotify = new UpdatePaginatingPropertyHandler(processor, false, false, 0);
		if (SwingUtilities.isEventDispatchThread()) {
		    endPaginationNotify.run();
		} else {
		    SwingUtilities.invokeLater(endPaginationNotify);
		}
		Log.error("Pagination failed.", e);
		//$NON-NLS-1$
	    } finally {
		this.processor.removeReportProgressListener(this);
	    }
	}

    }

    private class UpdatePaginatingPropertyHandler implements Runnable {

	private boolean paginating;
	private boolean paginated;
	private int pageCount;
	private PrintReportProcessor processor;

	protected UpdatePaginatingPropertyHandler(final PrintReportProcessor processor, final boolean paginating, final boolean paginated, final int pageCount) {
	    this.processor = processor;
	    this.paginating = paginating;
	    this.paginated = paginated;
	    this.pageCount = pageCount;
	}

	public void run() {
	    if (processor != getPrintReportProcessor()) {
		Log.debug(messages.getString("PreviewPane.DEBUG_NO_LONGER_VALID"));
		//$NON-NLS-1$
		return;
	    }
	    Log.debug(messages.getString("PreviewPane.DEBUG_PAGINATION", String.valueOf(paginating), String.valueOf(pageCount)));
	    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    if (paginating == false) {
		setNumberOfPages(pageCount);
		if (getPageNumber() < 1) {
		    setPageNumber(1);
		} else if (getPageNumber() > pageCount) {
		    setPageNumber(pageCount);
		}
	    }
	    setPaginating(paginating);
	    setPaginated(paginated);
	}

    }

    private class PreviewUpdateHandler implements PropertyChangeListener {

	protected PreviewUpdateHandler() {

	}

	public void propertyChange(final PropertyChangeEvent evt) {
	    final String propertyName = evt.getPropertyName();
	    if (PAGINATING_PROPERTY.equals(propertyName)) {
		if (isPaginating()) {
		    drawablePanel.setDrawable(getPaginatingDrawable());
		} else {
		    updateVisiblePage(getPageNumber());
		}
	    } else if (REPORT_JOB_PROPERTY.equals(propertyName)) {
		if (getReportJob() == null) {
		    drawablePanel.setDrawable(getNoReportDrawable());
		}
		// else the paginating property will be fired anyway ..
	    } else if (PAGE_NUMBER_PROPERTY.equals(propertyName)) {
		if (isPaginating()) {
		    return;
		}
		updateVisiblePage(getPageNumber());
	    }
	}

    }

    private class UpdateZoomHandler implements PropertyChangeListener {

	protected UpdateZoomHandler() {

	}

	/**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
	public void propertyChange(final PropertyChangeEvent evt) {
	    if ("zoom".equals(evt.getPropertyName()) == false)//$NON-NLS-1$
	    {
		return;
	    }
	    final double zoom = getZoom();
	    pageDrawable.setZoom(zoom);
	    zoomModel.setSelectedKey(new Double(zoom));
	    if (zoomModel.getSelectedKey() == null) {
		zoomModel.setSelectedItem(formatZoomText(zoom));
	    }
	    drawablePanel.revalidate();
	}

    }
    private static final double[] ZOOM_FACTORS = { 0.5, 0.75, 1, 1.25, 1.50, 2.00 };
    private static final int DEFAULT_ZOOM_INDEX = 2;
    public static final String STATUS_TEXT_PROPERTY = "statusText";
    //$NON-NLS-1$
    public static final String STATUS_TYPE_PROPERTY = "statusType";
    //$NON-NLS-1$
    public static final String REPORT_CONTROLLER_PROPERTY = "reportController";
    //$NON-NLS-1$
    public static final String ZOOM_PROPERTY = "zoom";
    //$NON-NLS-1$
    public static final String CLOSED_PROPERTY = "closed";
    //$NON-NLS-1$
    public static final String REPORT_JOB_PROPERTY = "reportJob";
    //$NON-NLS-1$
    public static final String PAGINATING_PROPERTY = "paginating";
    //$NON-NLS-1$
    public static final String PAGINATED_PROPERTY = "paginated";
    //$NON-NLS-1$
    public static final String PAGE_NUMBER_PROPERTY = "pageNumber";
    //$NON-NLS-1$
    public static final String NUMBER_OF_PAGES_PROPERTY = "numberOfPages";
    //$NON-NLS-1$
    public static final String ICON_THEME_PROPERTY = "iconTheme";
    //$NON-NLS-1$
    public static final String TITLE_PROPERTY = "title";
    //$NON-NLS-1$
    public static final String MENU_PROPERTY = "menu";
    //$NON-NLS-1$
    /** The preferred width key. */
    public static final String PREVIEW_PREFERRED_WIDTH = "org.jfree.report.modules.gui.base.PreferredWidth";
    //$NON-NLS-1$
    /** The preferred height key. */
    public static final String PREVIEW_PREFERRED_HEIGHT = "org.jfree.report.modules.gui.base.PreferredHeight";
    //$NON-NLS-1$
    /** The maximum width key. */
    public static final String PREVIEW_MAXIMUM_WIDTH = "org.jfree.report.modules.gui.base.MaximumWidth";
    //$NON-NLS-1$
    /** The maximum height key. */
    public static final String PREVIEW_MAXIMUM_HEIGHT = "org.jfree.report.modules.gui.base.MaximumHeight";
    //$NON-NLS-1$
    /** The maximum zoom key. */
    public static final String ZOOM_MAXIMUM_KEY = "org.jfree.report.modules.gui.base.MaximumZoom";
    //$NON-NLS-1$
    /** The minimum zoom key. */
    public static final String ZOOM_MINIMUM_KEY = "org.jfree.report.modules.gui.base.MinimumZoom";
    //$NON-NLS-1$
    /** The default maximum zoom. */
    private static final float ZOOM_MAXIMUM_DEFAULT = 20.0f;
    // 2000%
    /** The default minimum zoom. */
    private static final float ZOOM_MINIMUM_DEFAULT = 0.01f;
    // 1%
    /**
   * @deprecated use the paginating property instead
   */
    public static final String LOCK_INTERFACE_PROPERTY = "lockInterface";
    //$NON-NLS-1$
    private static final String MENUBAR_AVAILABLE_KEY = "org.jfree.report.modules.gui.base.MenuBarAvailable";
    //$NON-NLS-1$
    private static final String TOOLBAR_AVAILABLE_KEY = "org.jfree.report.modules.gui.base.ToolbarAvailable";
    //$NON-NLS-1$
    private static final String TOOLBAR_FLOATABLE_KEY = "org.jfree.report.modules.gui.base.ToolbarFloatable";
    //$NON-NLS-1$
    private Drawable paginatingDrawable;
    private Drawable noReportDrawable;
    private PageBackgroundDrawable pageDrawable;
    private DrawablePanel drawablePanel;
    private ReportController reportController;
    private JMenu[] menus;
    private JToolBar toolBar;
    private String statusText;
    private String title;
    private StatusType statusType;
    private boolean closed;
    private JFreeReport reportJob;
    private int numberOfPages;
    private int pageNumber;
    private SwingGuiContext swingGuiContext;
    private IconTheme iconTheme;
    private double zoom;
    private boolean paginating;
    private boolean paginated;
    private PrintReportProcessor printReportProcessor;
    private Worker paginationWorker;
    private JPanel innerReportControllerHolder;
    private JPanel toolbarHolder;
    private JPanel outerReportControllerHolder;
    private boolean reportControllerInner;
    private String reportControllerLocation;
    private JComponent reportControllerComponent;
    private KeyedComboBoxModel zoomModel;
    private PreviewPane.PreviewPaneStatusListener statusListener;
    private static final JMenu[] EMPTY_MENU = new JMenu[0];
    private boolean toolbarFloatable;
    private ArrayList reportProgressListener;
    private double maxZoom;
    private double minZoom;
    private Messages messages;
    private WindowSizeLimiter sizeLimiter;

    /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
    public PreviewPane() {
	messages = new Messages(getLocale(), SwingPreviewModule.BUNDLE_NAME);
	sizeLimiter = new WindowSizeLimiter();
	this.menus = EMPTY_MENU;
	setLayout(new BorderLayout());
	zoomModel = new KeyedComboBoxModel();
	zoomModel.setAllowOtherValue(true);
	zoom = ZOOM_FACTORS[DEFAULT_ZOOM_INDEX];
	final Configuration configuration = JFreeReportBoot.getInstance().getGlobalConfig();
	minZoom = getMinimumZoom(configuration);
	maxZoom = getMaximumZoom(configuration);
	pageDrawable = new PageBackgroundDrawable();
	drawablePanel = new DrawablePanel();
	drawablePanel.setOpaque(false);
	drawablePanel.setBackground(Color.green);
	drawablePanel.setDoubleBuffered(true);
	drawablePanel.setDrawable(pageDrawable);
	swingGuiContext = new PreviewGuiContext();
	final JPanel reportPaneHolder = new JPanel(new CenterLayout());
	reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	reportPaneHolder.add(drawablePanel);
	final JScrollPane s1 = new JScrollPane(reportPaneHolder);
	s1.getVerticalScrollBar().setUnitIncrement(20);
	innerReportControllerHolder = new JPanel();
	innerReportControllerHolder.setLayout(new BorderLayout());
	innerReportControllerHolder.add(s1, BorderLayout.CENTER);
	toolbarHolder = new JPanel();
	toolbarHolder.setLayout(new BorderLayout());
	toolbarHolder.add(innerReportControllerHolder, BorderLayout.CENTER);
	outerReportControllerHolder = new JPanel();
	outerReportControllerHolder.setLayout(new BorderLayout());
	outerReportControllerHolder.add(toolbarHolder, BorderLayout.CENTER);
	add(outerReportControllerHolder, BorderLayout.CENTER);
	addPropertyChangeListener(new PreviewUpdateHandler());
	addPropertyChangeListener("zoom", new UpdateZoomHandler());
	//$NON-NLS-1$
	statusListener = new PreviewPaneStatusListener();
	initializeWithoutJob();
    }

    public synchronized PrintReportProcessor getPrintReportProcessor() {
	return printReportProcessor;
    }

    protected synchronized void setPrintReportProcessor(final PrintReportProcessor printReportProcessor) {
	this.printReportProcessor = printReportProcessor;
    }

    public JMenu[] getMenu() {
	return menus;
    }

    protected void setMenu(final JMenu[] menus) {
	if (menus == null) {
	    throw new NullPointerException();
	}
	final JMenu[] oldmenu = this.menus;
	this.menus = (JMenu[])menus.clone();
	firePropertyChange(MENU_PROPERTY, oldmenu, this.menus);
    }

    public JToolBar getToolBar() {
	return toolBar;
    }

    public String getStatusText() {
	return statusText;
    }

    public void setStatusText(final String statusText) {
	final String oldStatus = this.statusText;
	this.statusText = statusText;
	firePropertyChange(STATUS_TEXT_PROPERTY, oldStatus, statusText);
    }

    public StatusType getStatusType() {
	return statusType;
    }

    public void setStatusType(final StatusType statusType) {
	final StatusType oldType = this.statusType;
	this.statusType = statusType;
	firePropertyChange(STATUS_TYPE_PROPERTY, oldType, statusType);
    }

    public ReportController getReportController() {
	return reportController;
    }

    public void setReportController(final ReportController reportController) {
	final ReportController oldController = this.reportController;
	this.reportController = reportController;
	firePropertyChange(REPORT_CONTROLLER_PROPERTY, oldController, reportController);
	// Now add the controller to the GUI ..
	refreshReportController(reportController);
    }

    private void refreshReportController(final ReportController newReportController) {
	if (newReportController != null) {
	    final JComponent rcp = newReportController.getControlPanel();
	    // if either the controller component or its position (inner vs outer)
	    // and border-position has changed, then refresh ..
	    if (reportControllerComponent != rcp || reportControllerInner != newReportController.isInnerComponent() || ObjectUtilities.equal(reportControllerLocation, newReportController.getControllerLocation())) {
		if (reportControllerComponent != null) {
		    outerReportControllerHolder.remove(reportControllerComponent);
		    innerReportControllerHolder.remove(reportControllerComponent);
		}
		final String sanLocation = sanitizeLocation(newReportController.getControllerLocation());
		final boolean innerComponent = newReportController.isInnerComponent();
		if (rcp != null) {
		    if (innerComponent) {
			innerReportControllerHolder.add(rcp, sanLocation);
		    } else {
			outerReportControllerHolder.add(rcp, sanLocation);
		    }
		}
		reportControllerComponent = rcp;
		reportControllerLocation = sanLocation;
		reportControllerInner = innerComponent;
	    }
	} else {
	    if (reportControllerComponent != null) {
		outerReportControllerHolder.remove(reportControllerComponent);
		innerReportControllerHolder.remove(reportControllerComponent);
	    }
	    reportControllerComponent = null;
	}
	if (reportJob != null) {
	    initializeFromReport();
	} else {
	    initializeWithoutJob();
	}
    }

    private String sanitizeLocation(final String location) {
	if (BorderLayout.NORTH.equals(location)) {
	    return BorderLayout.NORTH;
	}
	if (BorderLayout.SOUTH.equals(location)) {
	    return BorderLayout.SOUTH;
	}
	if (BorderLayout.WEST.equals(location)) {
	    return BorderLayout.WEST;
	}
	if (BorderLayout.EAST.equals(location)) {
	    return BorderLayout.EAST;
	}
	return BorderLayout.NORTH;
    }

    public JFreeReport getReportJob() {
	return reportJob;
    }

    public void setReportJob(final JFreeReport reportJob) {
	final JFreeReport oldJob = this.reportJob;
	this.reportJob = reportJob;
	firePropertyChange(REPORT_JOB_PROPERTY, oldJob, reportJob);
	if (reportJob == null) {
	    setPaginated(false);
	    setPageNumber(0);
	    setNumberOfPages(0);
	    initializeWithoutJob();
	} else {
	    initializeFromReport();
	}
    }

    public double getZoom() {
	return zoom;
    }

    public void setZoom(final double zoom) {
	final double oldZoom = this.zoom;
	this.zoom = Math.max(Math.min(zoom, maxZoom), minZoom);
	if (this.zoom != oldZoom) {
	    firePropertyChange(ZOOM_PROPERTY, oldZoom, zoom);
	}
    }

    public boolean isClosed() {
	return closed;
    }

    public void setClosed(final boolean closed) {
	final boolean oldClosed = this.closed;
	this.closed = closed;
	firePropertyChange(CLOSED_PROPERTY, oldClosed, closed);
	if (closed) {
	    prepareShutdown();
	}
    }

    private void prepareShutdown() {
	synchronized (this) {
	    if (paginationWorker != null) {
		//noinspection SynchronizeOnNonFinalField
		synchronized (paginationWorker) {
		    paginationWorker.finish();
		}
		paginationWorker = null;
	    }
	    if (printReportProcessor != null) {
		printReportProcessor.close();
		printReportProcessor = null;
	    }
	    closeToolbar();
	}
    }

    private int getUserDefinedCategoryPosition() {
	return StringUtil.parseInt(swingGuiContext.getConfiguration().getConfigProperty("org.jfree.report.modules.gui.swing.user-defined-category.position"), 15000);
	//$NON-NLS-1$
    }

    public Locale getLocale() {
	if (getParent() == null) {
	    try {
		return super.getLocale();
	    } catch (IllegalComponentStateException ex) {
		return Locale.getDefault();
	    }
	}
	return super.getLocale();
    }

    public int getNumberOfPages() {
	return numberOfPages;
    }

    public void setNumberOfPages(final int numberOfPages) {
	final int oldPageNumber = this.numberOfPages;
	this.numberOfPages = numberOfPages;
	firePropertyChange(NUMBER_OF_PAGES_PROPERTY, oldPageNumber, numberOfPages);
    }

    public int getPageNumber() {
	return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
	final int oldPageNumber = this.pageNumber;
	this.pageNumber = pageNumber;
	//Log.debug("Setting PageNumber: " + pageNumber);
	firePropertyChange(PAGE_NUMBER_PROPERTY, oldPageNumber, pageNumber);
    }

    public IconTheme getIconTheme() {
	return iconTheme;
    }

    protected void setIconTheme(final IconTheme theme) {
	final IconTheme oldTheme = this.iconTheme;
	this.iconTheme = theme;
	firePropertyChange(ICON_THEME_PROPERTY, oldTheme, theme);
    }

    protected void initializeFromReport() {
	final PageDefinition pageDefinition = reportJob.getPageDefinition();
	if (pageDefinition.getPageCount() > 0) {
	    final PageFormat pageFormat = pageDefinition.getPageFormat(0);
	    pageDrawable.setDefaultWidth((int)pageFormat.getWidth());
	    pageDrawable.setDefaultHeight((int)pageFormat.getHeight());
	}
	setTitle(messages.getString("PreviewPane.PREVIEW_TITLE", reportJob.getName()));
	//$NON-NLS-1$
	final Configuration configuration = reportJob.getConfiguration();
	setIconTheme(PreviewPaneUtilities.createIconTheme(configuration));
	performInitialization(configuration);
	startPagination();
    }

    protected void initializeWithoutJob() {
	setTitle(messages.getString("PreviewPane.EMPTY_TITLE"));
	//$NON-NLS-1$
	final Configuration configuration = JFreeReportBoot.getInstance().getGlobalConfig();
	setIconTheme(PreviewPaneUtilities.createIconTheme(configuration));
	performInitialization(configuration);
    }

    private void performInitialization(final Configuration configuration) {
	applyDefinedDimension(configuration);
	zoomModel.clear();
	for (int i = 0; i < ZOOM_FACTORS.length; i++) {
	    zoomModel.add(new Double(ZOOM_FACTORS[i]), formatZoomText(ZOOM_FACTORS[i]));
	}
	zoom = ZOOM_FACTORS[DEFAULT_ZOOM_INDEX];
	zoomModel.setSelectedKey(new Double(ZOOM_FACTORS[DEFAULT_ZOOM_INDEX]));
	final HashMap actions = PreviewPaneUtilities.loadActions(swingGuiContext);
	//Santiago: modificacion de la linea siguiente para eliminar los plug-ins de exportación
	//setMenu(EMPTY_MENU);
	if ("true".equals(configuration.getConfigProperty(MENUBAR_AVAILABLE_KEY)))//$NON-NLS-1$
	{
	    buildMenu(actions);
	} else {
	    setMenu(EMPTY_MENU);
	}
	if (toolBar != null) {
	    toolbarHolder.remove(toolBar);
	}
	if ("true".equals(configuration.getConfigProperty(TOOLBAR_AVAILABLE_KEY)))//$NON-NLS-1$
	{
	    final boolean floatable = isToolbarFloatable() || "true".equals(configuration.getConfigProperty(TOOLBAR_FLOATABLE_KEY));
	    //$NON-NLS-1$
	    toolBar = buildToolbar(actions, floatable);
	} else {
	    toolBar = null;
	}
	if (toolBar != null) {
	    toolbarHolder.add(toolBar, BorderLayout.NORTH);
	}
    }

    /**
   * Read the defined dimensions from the report's configuration and set them to
   * the Dialog. If a maximum size is defined, add a WindowSizeLimiter to check
   * the maximum size
   *
   * @param configuration the report-configuration of this dialog.
   */
    private void applyDefinedDimension(final Configuration configuration) {
	String width = configuration.getConfigProperty(PREVIEW_PREFERRED_WIDTH);
	String height = configuration.getConfigProperty(PREVIEW_PREFERRED_HEIGHT);
	// only apply if both values are set.
	if (width != null && height != null) {
	    try {
		final Dimension pref = createCorrectedDimensions(Integer.parseInt(width), Integer.parseInt(height));
		setPreferredSize(pref);
	    } catch (Exception nfe) {
		Log.warn("Preferred viewport size is defined, but the specified values are invalid.");
		//$NON-NLS-1$
	    }
	}
	width = configuration.getConfigProperty(PREVIEW_MAXIMUM_WIDTH);
	height = configuration.getConfigProperty(PREVIEW_MAXIMUM_HEIGHT);
	removeComponentListener(sizeLimiter);
	// only apply if at least one value is set.
	if (width != null || height != null) {
	    try {
		final int iWidth = (width == null) ? Short.MAX_VALUE : (int)parseRelativeFloat(width);
		final int iHeight = (height == null) ? Short.MAX_VALUE : (int)parseRelativeFloat(height);
		final Dimension pref = createCorrectedDimensions(iWidth, iHeight);
		setMaximumSize(pref);
		addComponentListener(sizeLimiter);
	    } catch (Exception nfe) {
		Log.warn("Maximum viewport size is defined, but the specified values are invalid.");
		//$NON-NLS-1$
	    }
	}
    }

    protected float parseRelativeFloat(final String value) {
	if (value == null) {
	    throw new NumberFormatException();
	}
	final String tvalue = value.trim();
	if (tvalue.length() > 0 && tvalue.charAt(tvalue.length() - 1) == '%')//$NON-NLS-1$
	{
	    final String number = tvalue.substring(0, tvalue.length() - 1);
	    //$NON-NLS-1$
	    return Float.parseFloat(number) * -1.0f;
	} else {
	    return Float.parseFloat(tvalue);
	}
    }

    /**
   * Correct the given width and height. If the values are negative, the height
   * and width is considered a proportional value where -100 corresponds to
   * 100%. The proportional attributes are given is relation to the screen width
   * and height.
   *
   * @param w the to be corrected width
   * @param h the height that should be corrected
   * @return the dimension of width and height, where all relative values are
   *         normalized.
   */
    private Dimension createCorrectedDimensions(int w, int h) {
	final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	if (w < 0) {
	    w = (w * screenSize.width / -100);
	}
	if (h < 0) {
	    h = (h * screenSize.height / -100);
	}
	return new Dimension(w, h);
    }

    private JToolBar buildToolbar(final HashMap actions, final boolean floatable) {
	final JToolBar toolBar = new JToolBar();
	toolBar.setFloatable(floatable);
	final ArrayList list = new ArrayList();
	final Iterator iterator = actions.values().iterator();
	while (iterator.hasNext()) {
	    final ActionPlugin[] plugins = (ActionPlugin[])iterator.next();
	    for (int i = 0; i < plugins.length; i++) {
		list.add(plugins[i]);
	    }
	}
	final ActionPlugin[] plugins = (ActionPlugin[])list.toArray(new ActionPlugin[list.size()]);
	Arrays.sort(plugins, new ActionPluginComparator());
	PreviewPaneUtilities.addActionsToToolBar(toolBar, plugins, this);
	return toolBar;
    }

    public void setToolbarFloatable(final boolean toolbarFloatable) {
	this.toolbarFloatable = toolbarFloatable;
    }

    public boolean isToolbarFloatable() {
	return toolbarFloatable;
    }

    private void closeToolbar() {
	if (toolBar.getParent() != toolbarHolder) {
	    // ha!, we detected that the toolbar is floating ...
	    // Log.debug (currentToolbar.getParent());
	    final Window w = SwingUtilities.windowForComponent(toolBar);
	    if (w != null) {
		w.setVisible(false);
		w.dispose();
	    }
	}
	toolBar.setVisible(false);
    }

    public SwingGuiContext getSwingGuiContext() {
	return swingGuiContext;
    }

    public KeyedComboBoxModel getZoomModel() {
	return zoomModel;
    }

    private String formatZoomText(final double zoom) {
	final NumberFormat numberFormat = NumberFormat.getPercentInstance(swingGuiContext.getLocale());
	return (numberFormat.format(zoom));
    }

    private void buildMenu(final HashMap actions) {
	final HashMap menus = new HashMap();
	final int userPos = getUserDefinedCategoryPosition();
	final ActionCategory[] categories = new ActionCategory[actions.size()];
	boolean insertedUserDefinedActions = false;
	int catCount = 0;
	final Iterator iterator = actions.entrySet().iterator();
	while (iterator.hasNext()) {
	    final Map.Entry entry = (Map.Entry)iterator.next();
	    final ActionCategory cat = (ActionCategory)entry.getKey();
	    categories[catCount] = cat;
	    catCount += 1;
	    final ActionPlugin[] plugins = (ActionPlugin[])entry.getValue();
	    if (insertedUserDefinedActions == false && cat.getPosition() > userPos) {
		final ReportController controller = getReportController();
		if (controller != null) {
		    controller.initialize(this);
		    final JMenu[] controlerMenus = controller.getMenus();
		    for (int i = 0; i < controlerMenus.length; i++) {
			final ActionCategory userCategory = new ActionCategory();
			userCategory.setName("X-User-Category-" + i);
			//$NON-NLS-1$
			userCategory.setPosition(userPos + i);
			menus.put(userCategory, controlerMenus[i]);
		    }
		}
		insertedUserDefinedActions = true;
	    }
	    final JMenu menu = PreviewPaneUtilities.createMenu(cat);
	    PreviewPaneUtilities.buildMenu(menu, plugins, this);
	    menus.put(cat, menu);
	}
	final CategoryTreeItem[] categoryTreeItems = PreviewPaneUtilities.buildMenuTree(categories);
	final ArrayList menuList = new ArrayList();
	for (int i = 0; i < categoryTreeItems.length; i++) {
	    final CategoryTreeItem item = categoryTreeItems[i];
	    final JMenu menu = (JMenu)menus.get(item.getCategory());
	    // now connect all menus ..
	    final CategoryTreeItem[] childs = item.getChilds();
	    Arrays.sort(childs);
	    for (int j = 0; j < childs.length; j++) {
		final CategoryTreeItem child = childs[j];
		final JMenu childMenu = (JMenu)menus.get(child.getCategory());
		if (childMenu != null) {
		    menu.add(childMenu);
		}
	    }
	    if (item.getParent() == null) {
		menuList.add(item);
	    }
	}
	Collections.sort(menuList);
	final ArrayList retval = new ArrayList();
	for (int i = 0; i < menuList.size(); i++) {
	    final CategoryTreeItem item = (CategoryTreeItem)menuList.get(i);
	    final JMenu menu = (JMenu)menus.get(item.getCategory());
	    if (menu.getItemCount() > 0) {
		retval.add(menu);
	    }
	}
	setMenu((JMenu[])retval.toArray(new JMenu[retval.size()]));
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(final String title) {
	final String oldTitle = this.title;
	this.title = title;
	firePropertyChange(TITLE_PROPERTY, oldTitle, title);
    }

    public double[] getZoomFactors() {
	return (double[])ZOOM_FACTORS.clone();
    }

    public boolean isPaginated() {
	return paginated;
    }

    public void setPaginated(final boolean paginated) {
	final boolean oldPaginated = this.paginated;
	this.paginated = paginated;
	firePropertyChange(PAGINATED_PROPERTY, oldPaginated, paginated);
    }

    public boolean isPaginating() {
	return paginating;
    }

    public void setPaginating(final boolean paginating) {
	final boolean oldPaginating = this.paginating;
	this.paginating = paginating;
	firePropertyChange(PAGINATING_PROPERTY, oldPaginating, paginating);
	firePropertyChange(LOCK_INTERFACE_PROPERTY, oldPaginating, paginating);
    }

    private synchronized void startPagination() {
	if (paginationWorker != null) {
	    // make sure that old pagination handler does not run longer than
	    // necessary..
	    //noinspection SynchronizeOnNonFinalField
	    synchronized (paginationWorker) {
		paginationWorker.finish();
	    }
	    paginationWorker = null;
	}
	if (printReportProcessor != null) {
	    printReportProcessor.close();
	    printReportProcessor = null;
	}
	try {
	    final JFreeReport reportJob = getReportJob();
	    printReportProcessor = new PrintReportProcessor(reportJob);
	    paginationWorker = new Worker();
	    paginationWorker.setWorkload(new RepaginationRunnable(printReportProcessor));
	} catch (ReportProcessingException e) {
	    Log.error("Unable to start report pagination:", e);
	}
    }

    public Drawable getNoReportDrawable() {
	return noReportDrawable;
    }

    public void setNoReportDrawable(final Drawable noReportDrawable) {
	this.noReportDrawable = noReportDrawable;
    }

    public Drawable getPaginatingDrawable() {
	return paginatingDrawable;
    }

    public void setPaginatingDrawable(final Drawable paginatingDrawable) {
	this.paginatingDrawable = paginatingDrawable;
    }

    protected void updateVisiblePage(final int pageNumber) {
	//
	if (printReportProcessor == null) {
	    throw new IllegalStateException();
	}
	// todo: This can be very expensive - so we better move this off the event-dispatcher
	final int pageIndex = getPageNumber() - 1;
	if (pageIndex < 0 || pageIndex >= printReportProcessor.getNumberOfPages()) {
	    this.pageDrawable.setBackend(null);
	    this.drawablePanel.setDrawable(pageDrawable);
	} else {
	    final PageDrawable drawable = printReportProcessor.getPageDrawable(pageIndex);
	    this.pageDrawable.setBackend(drawable);
	    this.drawablePanel.setDrawable(pageDrawable);
	}
    }

    private StatusListener getStatusListener() {
	return statusListener;
    }

    public void addReportProgressListener(final ReportProgressListener progressListener) {
	if (progressListener == null) {
	    throw new NullPointerException();
	}
	if (reportProgressListener == null) {
	    reportProgressListener = new ArrayList();
	}
	reportProgressListener.add(progressListener);
    }

    public void removeReportProgressListener(final ReportProgressListener progressListener) {
	if (reportProgressListener == null) {
	    return;
	}
	reportProgressListener.remove(progressListener);
    }

    protected void forwardReportStartedEvent(final ReportProgressEvent event) {
	if (reportProgressListener == null) {
	    return;
	}
	for (int i = 0; i < reportProgressListener.size(); i++) {
	    final ReportProgressListener listener = (ReportProgressListener)reportProgressListener.get(i);
	    listener.reportProcessingStarted(event);
	}
    }

    protected void forwardReportUpdateEvent(final ReportProgressEvent event) {
	if (reportProgressListener == null) {
	    return;
	}
	for (int i = 0; i < reportProgressListener.size(); i++) {
	    final ReportProgressListener listener = (ReportProgressListener)reportProgressListener.get(i);
	    listener.reportProcessingUpdate(event);
	}
    }

    protected void forwardReportFinishedEvent(final ReportProgressEvent event) {
	if (reportProgressListener == null) {
	    return;
	}
	for (int i = 0; i < reportProgressListener.size(); i++) {
	    final ReportProgressListener listener = (ReportProgressListener)reportProgressListener.get(i);
	    listener.reportProcessingFinished(event);
	}
    }

    private double getMaximumZoom(final Configuration configuration) {
	String value = configuration.getConfigProperty(ZOOM_MAXIMUM_KEY);
	return StringUtil.parseFloat(value, ZOOM_MAXIMUM_DEFAULT);
    }

    private double getMinimumZoom(final Configuration configuration) {
	String value = configuration.getConfigProperty(ZOOM_MINIMUM_KEY);
	return StringUtil.parseFloat(value, ZOOM_MINIMUM_DEFAULT);
    }

}
