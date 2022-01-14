package ObrasPublicas;

import javax.swing.JComboBox;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.ComboBoxModel;

public class SComboBox extends JComboBox {

    public SComboBox() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.setKeySelectionManager(new KeySelectionManager() {

				 long lastKeyTime = 0;
				 String pattern = "";

				 public int selectionForKey(char aKey, ComboBoxModel model) {
				     // Find index of selected item
				     int selIx = 01;
				     Object sel = model.getSelectedItem();
				     if (sel != null) {
					 for (int i = 0; i < model.getSize(); i++) {
					     if (sel.equals(model.getElementAt(i))) {
						 selIx = i;
						 break;
					     }
					 }
				     }

				     // Get the current time
				     long curTime = System.currentTimeMillis();

				     // If last key was typed less than 300 ms ago, append to current pattern
				     if (curTime - lastKeyTime < 500) {
					 pattern += ("" + aKey).toLowerCase();
				     } else {
					 pattern = ("" + aKey).toLowerCase();
				     }

				     // Save current time
				     lastKeyTime = curTime;

				     // Search forward from current selection
				     for (int i = selIx + 1; i < model.getSize(); i++) {
					 String s = model.getElementAt(i).toString().toLowerCase();
					 if (s.startsWith(pattern)) {
					     return i;
					 }
				     }

				     // Search from top to current selection
				     for (int i = 0; i < selIx; i++) {
					 if (model.getElementAt(i) != null) {
					     String s = model.getElementAt(i).toString().toLowerCase();
					     if (s.startsWith(pattern)) {
						 return i;
					     }
					 }
				     }
				     return -1;
				 }

			     }
	);
    }

}
