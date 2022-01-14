package org.digitall.projects.digitall;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;

public class AboutBoxPanel extends BasicContainerPanel {

    private BasicLabel labelTitle = new BasicLabel();
    private BasicLabel labelAuthor = new BasicLabel();
    private BasicLabel labelCopyright = new BasicLabel();
    private BasicLabel labelCompany = new BasicLabel();
    private GridBagLayout layoutMain = new GridBagLayout();
    private Border border = BorderFactory.createEtchedBorder();

    public AboutBoxPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout( layoutMain );
	this.setBorder( border );
	labelTitle.setText( "Title" );
	labelAuthor.setText( "Author" );
	labelCopyright.setText( "Copyright" );
	labelCompany.setText( "Company" );
	this.add( labelTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 0, 15), 0, 0) );
	this.add( labelAuthor, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 15), 0, 0) );
	this.add( labelCopyright, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 15), 0, 0) );
	this.add( labelCompany, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 5, 15), 0, 0) );
    }

}
