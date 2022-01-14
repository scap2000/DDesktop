package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Principal extends JFrame {

    private JLabel statusBar = new JLabel();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuFile = new JMenu();
    private JMenuBar menuBar = new JMenuBar();
    private JPanel panelCenter = new JPanel();
    private BorderLayout layoutMain = new BorderLayout();
    private JButton jButton1 = new JButton();

    public Principal() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.setJMenuBar(menuBar);
	this.getContentPane().setLayout(layoutMain);
	panelCenter.setLayout(null);
	jButton1.setText("jButton1");
	jButton1.setBounds(new Rectangle(75, 85, 88, 25));
	jButton1.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    jButton1_actionPerformed(e);
				}

			    }
	);
	jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

			       public void mouseClicked(MouseEvent e) {
				   jButton1_mouseClicked(e);
			       }

			   }
	);
	this.setSize(new Dimension(400, 300));
	menuFile.setText("File");
	menuFileExit.setText("Exit");
	menuFileExit.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent ae) {
					fileExit_ActionPerformed(ae);
				    }

				}
	);
	menuHelp.setText("Help");
	menuHelpAbout.setText("About");
	menuHelpAbout.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent ae) {
					 helpAbout_ActionPerformed(ae);
				     }

				 }
	);
	statusBar.setText("");
	menuFile.add(menuFileExit);
	menuBar.add(menuFile);
	menuHelp.add(menuHelpAbout);
	menuBar.add(menuHelp);
	this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	panelCenter.add(jButton1, null);
	this.getContentPane().add(panelCenter, BorderLayout.CENTER);
    }

    void fileExit_ActionPerformed(ActionEvent e) {
	System.exit(0);
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(this, new Principal_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void jButton1_mouseClicked(MouseEvent e) {
	JFrame tmp = new SelectorOrigen();
	tmp.show();
	/*    JFrame tmp2 = new Cruce();
    tmp2.show();*/
	/*    JFrame tmp3 = new Mapa();
    tmp3.show();*/
    }

    private void jButton1_actionPerformed(ActionEvent e) {

    }

}
