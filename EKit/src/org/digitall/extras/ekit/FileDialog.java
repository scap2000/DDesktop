/*
GNU Lesser General Public License

FileDialog
Copyright (C) 2000 Howard Kistler & other contributors

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.digitall.extras.ekit;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;

public class FileDialog extends BasicDialog implements ActionListener {

    private EkitCore ParentEkit;
    private JList FileList;
    private String FileDir;
    private String[] Files;
    private String SelectedFile;

    public FileDialog(EkitCore parentEkit, String fileDir, String[] fileList, String title, boolean modal) {
	super(parentEkit.getFrame(), title, modal);
	FileDir = fileDir;
	Files = fileList;
	ParentEkit = parentEkit;
	init();
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
	if (e.getActionCommand().equals("save")) {
	    hide();
	} else if (e.getActionCommand().equals("cancel")) {
	    SelectedFile = null;
	    hide();
	}
    }

    public void init() {
	SelectedFile = "";
	Container contentPane = getContentPane();
	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	setBounds(100, 100, 300, 200);
	setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	FileList = new JList(Files);
	FileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	FileList.clearSelection();
	ListSelectionModel lsm = FileList.getSelectionModel();
	lsm.addListSelectionListener(new ListSelectionListener() {

				  public void valueChanged(ListSelectionEvent e) {
				      if (!e.getValueIsAdjusting()) {
					  ListSelectionModel sm = FileList.getSelectionModel();
					  if (!sm.isSelectionEmpty()) {
					      SelectedFile = Files[sm.getMinSelectionIndex()];
					  }
				      }
				  }

			      }
	);
	BasicScrollPane fileScrollPane = new BasicScrollPane(FileList);
	fileScrollPane.setAlignmentX(LEFT_ALIGNMENT);
	BasicPanel centerPanel = new BasicPanel();
	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
	centerPanel.add(fileScrollPane);
	centerPanel.setBorder(BorderFactory.createTitledBorder("Files"));
	BasicPanel buttonPanel = new BasicPanel();
	buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	BasicButton saveButton = new BasicButton("Accept");
	saveButton.setActionCommand("save");
	saveButton.addActionListener(this);
	BasicButton cancelButton = new BasicButton("Cancel");
	cancelButton.setActionCommand("cancel");
	cancelButton.addActionListener(this);
	buttonPanel.add(saveButton);
	buttonPanel.add(cancelButton);
	contentPane.add(centerPanel);
	contentPane.add(buttonPanel);
	setVisible(true);
    }

    public String getSelectedFile() {
	if (SelectedFile != null) {
	    SelectedFile = FileDir + "/" + SelectedFile;
	}
	return SelectedFile;
    }

}
