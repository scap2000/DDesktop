package org.digitall.projects.tests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.icons.IconTypes;

public class MainPanel extends BasicPanel {

    private JFrame parent;
    private ImageIcon dateContainerImage = IconTypes.getIcon("iconos/ui/desktoppanel/datecontainer.png");
    private ImageIcon btnContainerCenterImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainercenter.png");
    private ImageIcon btnContainerRightImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainerright.png");
    private ImageIcon btnContainerLeftImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainerleft.png");
    private ImageIcon rightDownImage = IconTypes.getIcon("iconos/ui/desktoppanel/rightdown.png");
    private ImageIcon rightUpImage = IconTypes.getIcon("iconos/ui/desktoppanel/rightup.png");
    private ImageIcon topLeftImage = IconTypes.getIcon("iconos/ui/desktoppanel/leftup.png");
    private ImageIcon btnPlayerImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnplayer.png");
    private ImageIcon btnNextImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnnext.png");
    private ImageIcon btnPreviousImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnprevious.png");
    private ImageIcon btnExitImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnexit.png");
    private ImageIcon btnMinimizeImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnminimize.png");
    private BasicPanel jpLeft = new BasicPanel();
    private BasicPanel jpRight = new BasicPanel();
    private BasicPanel jpCenter = new BasicPanel();
    private BorderLayout blMain = new BorderLayout();
    private BorderLayout blCenter = new BorderLayout();
    private BorderLayout blCentralPanel = new BorderLayout();
    private BorderLayout blRight = new BorderLayout();
    private BorderLayout blLeft = new BorderLayout();
    private GridBagLayout gblBtnContainer = new GridBagLayout();
    private GridBagLayout gblTopRight = new GridBagLayout();
    private GridBagLayout gblBottomRight = new GridBagLayout();
    private GridBagLayout gblTopLeft = new GridBagLayout();
    private GridBagLayout gblBottomLeft = new GridBagLayout();
    private BasicButton btnStickyNotes = new BasicButton(IconTypes.stickynotes_32x32);
    private BasicButton btnPlayer = new BasicButton(btnPlayerImage);
    private BasicButton btnUnassigned = new BasicButton();
    private BasicButton btnPrevious = new BasicButton(btnPreviousImage);
    private BasicButton btnNext = new BasicButton(btnNextImage);
    private BasicPanel jpCentralPanel = new BasicPanel();
    private BasicPanel jpTopRight = new BasicPanel();
    private BasicPanel jpDesktopButtons = new BasicPanel();
    private BasicPanel jpTopLeft = new BasicPanel();
    private BasicPanel jpBottomRight = new BasicPanel();
    private BasicLabel lblDateContainer = Environment.lblCalendar;
    private BasicPanel jpBtnContainer = new BasicPanel();
    private BasicPanel jpBottomLeft = new BasicPanel();
    private BasicPanel jpButtonsContainer = new BasicPanel();
    private BasicLabel lblCenterContainer = new BasicLabel(btnContainerCenterImage);
    private BasicLabel lblRightContainer = new BasicLabel(btnContainerRightImage);
    private BasicLabel lblLeftContainer = new BasicLabel(btnContainerLeftImage);
    private BasicButton btnExit = new BasicButton(btnExitImage);
    private BasicButton btnMinimize = new BasicButton(btnMinimizeImage);
    private BoxLayout blButtonsContainer;
    private BasicLabel lblLeftFiller = new BasicLabel();
    private BasicLabel lblBottomRight = new BasicLabel(rightDownImage);
    private BasicLabel lblTopRight = new BasicLabel(rightUpImage);
    private BasicButton lblTopLeft = new BasicButton(topLeftImage);
    private BasicButton btnMain = new BasicButton(IconTypes.main_32x32);
    private BasicButton btnCashflowAdmin = new BasicButton(IconTypes.cashflow_32x32);
    private BasicButton btnResourcesRequests = new BasicButton(IconTypes.resources_32x32);
    private BasicButton btnResourcesControl = new BasicButton(IconTypes.assets_32x32);
    private BasicButton btnLogistics = new BasicButton(IconTypes.report_16x16);
    private BasicButton btnTasks = new BasicButton(IconTypes.taxes_32x32);
    private BasicButton btnSystemMap = new BasicButton(IconTypes.gaia_32x32);
    private Separator lblSeparator1 = new Separator();
    private Separator lblSeparator2 = new Separator();
    private Separator lblSeparator3 = new Separator();
    private Separator lblSeparator4 = new Separator();
    private Separator lblSeparator5 = new Separator();
    private Separator lblSeparator6 = new Separator();

    public MainPanel(JFrame _parent) {
	try {
	    parent = _parent;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(blMain);
	this.setBounds(new Rectangle(10, 10, 800, 100));
	this.setSize(new Dimension(924, 100));
	jpLeft.setPreferredSize(new Dimension(150, 10));
	jpLeft.setLayout(blLeft);
	jpLeft.setOpaque(false);
	jpRight.setPreferredSize(new Dimension(150, 10));
	jpRight.setLayout(blRight);
	jpRight.setOpaque(false);
	jpCenter.setLayout(blCenter);
	jpCenter.setOpaque(false);
	btnStickyNotes.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnStickyNotes_actionPerformed(e);
				      }

				  }
	);
	jpCentralPanel.setLayout(blCentralPanel);
	jpCentralPanel.setOpaque(false);
	lblDateContainer.setIcon(dateContainerImage);
	lblDateContainer.setHorizontalAlignment(SwingConstants.CENTER);
	lblDateContainer.setPreferredSize(new Dimension(365, 36));
	lblDateContainer.setSize(new Dimension(359, 26));
	lblDateContainer.setIconTextGap(0);
	lblDateContainer.setVerticalAlignment(SwingConstants.TOP);
	lblDateContainer.setHorizontalTextPosition(SwingConstants.CENTER);
	jpBtnContainer.setLayout(gblBtnContainer);
	jpBtnContainer.setPreferredSize(new Dimension(114, 33));
	jpBtnContainer.setOpaque(false);
	jpBottomRight.setLayout(gblBottomRight);
	jpBottomRight.setOpaque(false);
	jpBottomRight.setMaximumSize(new Dimension(75, 32));
	jpBottomRight.setMinimumSize(new Dimension(75, 32));
	jpBottomRight.setPreferredSize(new Dimension(85, 32));
	jpBottomLeft.setPreferredSize(new Dimension(70, 32));
	jpBottomLeft.setLayout(gblBottomLeft);
	jpBottomLeft.setOpaque(false);
	btnPlayer.setHorizontalAlignment(SwingConstants.CENTER);
	btnPlayer.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnPlayer_actionPerformed(e);
				 }

			     }
	);
	btnUnassigned.setMaximumSize(new Dimension(70, 15));
	btnUnassigned.setPreferredSize(new Dimension(70, 15));
	btnPrevious.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnPrevious_actionPerformed(e);
				   }

			       }
	);
	btnNext.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnNext_actionPerformed(e);
			       }

			   }
	);
	lblCenterContainer.setHorizontalAlignment(SwingConstants.CENTER);
	lblCenterContainer.setPreferredSize(new Dimension(234, 24));
	lblRightContainer.setPreferredSize(new Dimension(80, 24));
	lblLeftContainer.setHorizontalAlignment(SwingConstants.RIGHT);
	lblLeftContainer.setPreferredSize(new Dimension(80, 24));
	btnExit.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnExit_actionPerformed(e);
			       }

			   }
	);
	btnMinimize.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnMinimize_actionPerformed(e);
				   }

			       }
	);
	jpButtonsContainer.setBounds(new Rectangle(29, 44, 394, 20));
	jpButtonsContainer.setPreferredSize(new Dimension(10, 51));
	jpButtonsContainer.setOpaque(false);
	blButtonsContainer = new BoxLayout(jpButtonsContainer, BoxLayout.X_AXIS);
	jpButtonsContainer.setLayout(blButtonsContainer);
	jpButtonsContainer.setSize(new Dimension(577, 51));
	jpButtonsContainer.setMaximumSize(new Dimension(82, 51));
	lblLeftFiller.setMaximumSize(new Dimension(20, 22));
	lblLeftFiller.setMinimumSize(new Dimension(20, 22));
	lblLeftFiller.setPreferredSize(new Dimension(0, 22));
	lblBottomRight.setIconTextGap(0);
	lblBottomRight.setPreferredSize(new Dimension(85, 29));
	lblBottomRight.setHorizontalAlignment(SwingConstants.LEFT);
	lblTopLeft.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      lblTopLeft_actionPerformed(e);
				  }

			      }
	);
	btnMain.setRolloverEnabled(true);
	btnMain.setRolloverIcon(IconTypes.main_ro_32x32);
	btnMain.setHorizontalAlignment(SwingConstants.CENTER);
	btnMain.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnMain_actionPerformed(e);
			       }

			   }
	);
	btnCashflowAdmin.setRolloverEnabled(true);
	btnCashflowAdmin.setRolloverIcon(IconTypes.cashflow_ro_32x32);
	btnCashflowAdmin.setHorizontalAlignment(SwingConstants.CENTER);
	btnCashflowAdmin.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
					    btnCashflowAdmin_actionPerformed(e);
					}

				    }
	);
	btnResourcesRequests.setRolloverEnabled(true);
	btnResourcesRequests.setRolloverIcon(IconTypes.resources_ro_32x32);
	btnResourcesRequests.setHorizontalAlignment(SwingConstants.CENTER);
	btnResourcesRequests.addActionListener(new ActionListener() {

					    public void actionPerformed(ActionEvent e) {
						btnResourcesRequests_actionPerformed(e);
					    }

					}
	);
	btnResourcesControl.setRolloverEnabled(true);
	btnResourcesControl.setRolloverIcon(IconTypes.assets_ro_32x32);
	btnResourcesControl.setHorizontalAlignment(SwingConstants.CENTER);
	btnResourcesControl.addActionListener(new ActionListener() {

					   public void actionPerformed(ActionEvent e) {
					       btnResourcesControl_actionPerformed(e);
					   }

				       }
	);
	btnLogistics.setRolloverEnabled(true);
	btnLogistics.setRolloverIcon(IconTypes.reports_ro_32x32);
	btnLogistics.setHorizontalAlignment(SwingConstants.CENTER);
	btnLogistics.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnLogistics_actionPerformed(e);
				    }

				}
	);
	btnTasks.setRolloverEnabled(true);
	btnTasks.setRolloverIcon(IconTypes.taxes_ro_32x32);
	btnTasks.setHorizontalAlignment(SwingConstants.CENTER);
	btnTasks.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnTasks_actionPerformed(e);
				}

			    }
	);
	btnSystemMap.setRolloverEnabled(true);
	btnSystemMap.setRolloverIcon(IconTypes.gaia_ro_32x32);
	btnSystemMap.setHorizontalAlignment(SwingConstants.CENTER);
	btnSystemMap.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnSystemMap_actionPerformed(e);
				    }

				}
	);
	jpTopRight.setLayout(gblTopRight);
	jpTopRight.setOpaque(false);
	jpTopRight.setMinimumSize(new Dimension(150, 68));
	jpTopRight.setPreferredSize(new Dimension(150, 68));
	jpTopRight.setSize(new Dimension(150, 68));
	jpTopRight.setMaximumSize(new Dimension(150, 68));
	jpTopRight.setBounds(new Rectangle(0, 0, 150, 68));
	jpDesktopButtons.setOpaque(false);
	jpTopLeft.setLayout(gblTopLeft);
	jpTopLeft.setOpaque(false);
	jpBottomLeft.add(btnPlayer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 60), 0, 0));
	jpLeft.add(jpBottomLeft, BorderLayout.SOUTH);
	jpTopLeft.add(lblTopLeft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 0), 0, 0));
	jpLeft.add(jpTopLeft, BorderLayout.CENTER);
	this.add(jpLeft, BorderLayout.WEST);
	jpDesktopButtons.add(btnPrevious, null);
	jpDesktopButtons.add(btnNext, null);
	jpBottomRight.add(jpDesktopButtons, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 53, 0, 0), 40, 7));
	jpBottomRight.add(lblBottomRight, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 65, 0, 0), 0, 3));
	jpRight.add(jpBottomRight, BorderLayout.SOUTH);
	jpTopRight.add(btnExit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 35, 0, 0), 0, 0));
	jpTopRight.add(btnMinimize, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(-6, 0, 0, 0), 0, 0));
	jpTopRight.add(lblTopRight, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 25), 0, 0));
	jpRight.add(jpTopRight, BorderLayout.CENTER);
	this.add(jpRight, BorderLayout.EAST);
	jpCenter.add(btnStickyNotes, BorderLayout.EAST);
	jpCentralPanel.add(jpBtnContainer, BorderLayout.CENTER);
	jpCentralPanel.add(lblDateContainer, BorderLayout.NORTH);
	jpButtonsContainer.add(lblLeftFiller, null);
	jpButtonsContainer.add(btnMain, null);
	jpButtonsContainer.add(lblSeparator1, null);
	jpButtonsContainer.add(btnCashflowAdmin, null);
	jpButtonsContainer.add(lblSeparator2, null);
	jpButtonsContainer.add(btnResourcesRequests, null);
	jpButtonsContainer.add(lblSeparator3, null);
	jpButtonsContainer.add(btnResourcesControl, null);
	jpButtonsContainer.add(lblSeparator4, null);
	jpButtonsContainer.add(btnLogistics, null);
	jpButtonsContainer.add(lblSeparator5, null);
	jpButtonsContainer.add(btnTasks, null);
	jpButtonsContainer.add(lblSeparator6, null);
	jpButtonsContainer.add(btnSystemMap, null);
	jpBtnContainer.add(jpButtonsContainer, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(-22, 0, 8, 0), 450, 0));
	jpBtnContainer.add(lblCenterContainer, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0), 0, 0));
	jpBtnContainer.add(lblRightContainer, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(12, 0, 0, 0), 0, 0));
	jpBtnContainer.add(lblLeftContainer, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 0, 0, 0), 0, 0));
	jpCenter.add(btnUnassigned, BorderLayout.WEST);
	jpCenter.add(jpCentralPanel, BorderLayout.CENTER);
	this.add(jpCenter, BorderLayout.CENTER);
    }
    //JPANEL with Gradient Background

    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D)g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	int w = getWidth();
	int h = getHeight();
	GradientPaint gradient = new GradientPaint(0, 0, BasicConfig.PANEL_GRADIENT_START_COLOR, 0, h, BasicConfig.PANEL_GRADIENT_END_COLOR, false);
	g2.setPaint(gradient);
	g2.fillRect(0, 0, w, h);
    }

    private void btnExit_actionPerformed(ActionEvent e) {
	closeApplication();
    }

    public void closeApplication() {
	int answer = JOptionPane.showConfirmDialog(this, "¿Desea cerrar el sistema?", "Cerrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (answer == JOptionPane.YES_OPTION) {
	    //AVISAR A LOS MODULOS QUE SE VA A CERRAR TODO!!!
	    /**
	     * GRABAR OPCIONES
	     * */
	    Environment.cfg.setProperty("SelectedTab", String.valueOf(Environment.mainTabbedPane.getSelectedIndex()));
	    System.exit(0);
	}
    }

    private void btnStickyNotes_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_STICKYNOTES));
    }

    private void btnMain_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_MAIN));
    }

    private void btnCashflowAdmin_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_CASHFLOW));
    }

    private void btnResourcesRequests_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_RESOURCES));
    }

    private void btnResourcesControl_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_ASSETS));
    }

    private void btnLogistics_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_REPORTS));
    }

    private void btnTasks_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_TAXES));
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
    
    }

    private void btnNext_actionPerformed(ActionEvent e) {

    }

    private void btnPlayer_actionPerformed(ActionEvent e) {
	//Environment.mp3Player.setVisible(!Environment.mp3Player.isVisible());

    }

    private void lblTopLeft_actionPerformed(ActionEvent e) {
	BrowserLauncher.openURL("http://www.digitallsh.com.ar/");
    }

    private void btnSystemMap_actionPerformed(ActionEvent e) {
	Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_GAIA));
    }

    private void btnMinimize_actionPerformed(ActionEvent e) {
	parent.setState(JFrame.ICONIFIED);
    }

    private class Separator extends BasicLabel {

	private int width = 15;

	public Separator() {
	    super(IconTypes.getIcon("iconos/ui/desktoppanel/btnseparator.png"));
	    setPreferredSize(new Dimension(width, 24));
	    setSize(new Dimension(width, 24));
	    setMinimumSize(new Dimension(width, 24));
	    setMaximumSize(new Dimension(width, 24));
	}

    }

}
