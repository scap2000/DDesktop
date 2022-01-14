/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Feb 01, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard;

import java.util.LinkedList;
import java.util.List;
import org.pentaho.jfreereport.wizard.ui.IWizardListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;

public class WizardManager {
  /**
   * 
   */
  List eventListenerList = new LinkedList();

  /**
   * 
   */
  protected List steps = new LinkedList();

  /**
   * 
   */
  protected WizardPanel currentStep = null;

  /**
   * 
   */
  protected WizardPanel previousStep = null;

  /**
   * 
   * 
   * @param l
   */
  public void addWizardListener(IWizardListener l) {
    eventListenerList.add(l);
  }

  /**
   * 
   * 
   * @param l
   */
  public void removeWizardListener(IWizardListener l) {
    eventListenerList.remove(l);
  }

  /**
   * 
   */
  public void removeAllWizardListeners() {
    eventListenerList.clear();
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    if ((currentStep == null) || (steps == null) || !currentStep.isNextAllowed()) {
      return false;
    }
    // we must check all steps leading up to this step
    boolean nextAllowed = true;
    for (int i = 0; nextAllowed && (i <= steps.indexOf(currentStep)); i++) {
      WizardPanel step = (WizardPanel) steps.get(i);
      nextAllowed = step.isContinueAllowed();
    }
    return nextAllowed;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isNextAllowed() {
    boolean continueAllowed = isContinueAllowed();
    if (continueAllowed && (currentStep == steps.get(steps.size() - 1))) {
      continueAllowed = false;
    }
    return continueAllowed;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isFinishAllowed() {
    boolean finishAllowed = true;
    for (int i = 0; (i < steps.size()) && finishAllowed; i++) {
      WizardPanel p = (WizardPanel) steps.get(i);
      finishAllowed = p.isContinueAllowed();
    }
    return finishAllowed;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isBackAllowed() {
    if ((currentStep == null) || (steps == null) || (currentStep == steps.get(0)) || !currentStep.isBackAllowed()) {
      return false;
    }
    return true;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean next() {
    boolean status = currentStep.nextFired(currentStep);
    if (status) {
      previousStep = currentStep;
      currentStep = (WizardPanel) steps.get(steps.indexOf(currentStep) + 1);
      List delayedEventList = new LinkedList();
      for (int i = 0; i < eventListenerList.size(); i++) {
        IWizardListener l = (IWizardListener) eventListenerList.get(i);
        if (previousStep == l) {
          continue;
        } else if (l instanceof WizardPanel) {
          l.nextFired(previousStep);
        } else {
          delayedEventList.add(l);
        }
      }
      for (int i = 0; i < delayedEventList.size(); i++) {
        IWizardListener l = (IWizardListener) delayedEventList.get(i);
        l.nextFired(previousStep);
      }
      previousStep.stateChanged = false;
    }
    return status;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean back() {
    currentStep.backFired(currentStep);
    previousStep = currentStep;
    currentStep = (WizardPanel) steps.get(steps.indexOf(currentStep) - 1);
    List delayedEventList = new LinkedList();
    for (int i = 0; i < eventListenerList.size(); i++) {
      IWizardListener l = (IWizardListener) eventListenerList.get(i);
      if (previousStep == l) {
        continue;
      } else if (l instanceof WizardPanel) {
        l.backFired(previousStep);
      } else {
        delayedEventList.add(l);
      }
    }
    for (int i = 0; i < delayedEventList.size(); i++) {
      IWizardListener l = (IWizardListener) delayedEventList.get(i);
      l.backFired(previousStep);
    }
    return true;
  }

  public boolean publish() {
    currentStep.finishFired(currentStep);
    List delayedEventList = new LinkedList();
    for (int i = 0; i < eventListenerList.size(); i++) {
      IWizardListener l = (IWizardListener) eventListenerList.get(i);
      if (previousStep == l) {
        continue;
      } else if (l instanceof WizardPanel) {
        l.publishFired(currentStep);
      } else {
        delayedEventList.add(l);
      }
    }
    for (int i = 0; i < delayedEventList.size(); i++) {
      IWizardListener l = (IWizardListener) delayedEventList.get(i);
      l.publishFired(currentStep);
    }
    currentStep.stateChanged = false;
    return true;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean finish() {
    currentStep.finishFired(currentStep);
    List delayedEventList = new LinkedList();
    for (int i = 0; i < eventListenerList.size(); i++) {
      IWizardListener l = (IWizardListener) eventListenerList.get(i);
      if (previousStep == l) {
        continue;
      } else if (l instanceof WizardPanel) {
        l.finishFired(currentStep);
      } else {
        delayedEventList.add(l);
      }
    }
    for (int i = 0; i < delayedEventList.size(); i++) {
      IWizardListener l = (IWizardListener) delayedEventList.get(i);
      l.finishFired(currentStep);
    }
    currentStep.stateChanged = false;
    return true;
  }

  public boolean preview() {
    boolean status = currentStep.previewFired(currentStep);
    List delayedEventList = new LinkedList();
    for (int i = 0; status && i < eventListenerList.size(); i++) {
      IWizardListener l = (IWizardListener) eventListenerList.get(i);
      if (previousStep == l) {
        continue;
      } else if (l instanceof WizardPanel && l != currentStep) {
        status = l.previewFired(currentStep);
      } else {
        delayedEventList.add(l);
      }
    }
    for (int i = 0; status && i < delayedEventList.size(); i++) {
      IWizardListener l = (IWizardListener) delayedEventList.get(i);
      status = l.previewFired(currentStep);
    }
    currentStep.stateChanged = false;
    return status;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean cancel() {
    // any cleanup?
    boolean status = currentStep.cancelFired(currentStep);
    if (status) {
      List delayedEventList = new LinkedList();
      for (int i = 0; i < eventListenerList.size(); i++) {
        IWizardListener l = (IWizardListener) eventListenerList.get(i);
        if (previousStep == l) {
          continue;
        } else if (l instanceof WizardPanel) {
          status = l.cancelFired(currentStep);
        } else {
          delayedEventList.add(l);
        }
      }
      for (int i = 0; i < delayedEventList.size() && status; i++) {
        IWizardListener l = (IWizardListener) delayedEventList.get(i);
        status = l.cancelFired(currentStep);
      }
    }
    return status;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return Returns the currentStep.
   */
  public WizardPanel getCurrentStep() {
    return currentStep;
  }

  /**
   * 
   * 
   * @param step
   */
  public void setCurrentStep(WizardPanel step) {
    currentStep = step;
  }

  /**
   * 
   * 
   * @param steps
   */
  public void setSteps(List steps) {
    this.steps = steps;
  }

  /**
   * 
   * 
   * @param step
   */
  public void addStep(WizardPanel step) {
    steps.add(step);
  }

  /**
   * 
   */
  public void update() {
    for (int i = 0; i < steps.size(); i++) {
      WizardPanel p = (WizardPanel) steps.get(i);
      p.updateState();
    }
  }

  /**
   * DOCUMENT ME!
   * 
   * @return Returns the previousStep.
   */
  public WizardPanel getPreviousStep() {
    return previousStep;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param previousStep
   *          The previousStep to set.
   */
  public void setPreviousStep(WizardPanel previousStep) {
    this.previousStep = previousStep;
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public List getSteps() {
    return steps;
  }
}
