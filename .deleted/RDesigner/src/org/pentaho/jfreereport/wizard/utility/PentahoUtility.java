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

package org.pentaho.jfreereport.wizard.utility;

import java.util.Locale;

import org.pentaho.core.system.PentahoSystem;
import org.pentaho.core.system.StandaloneApplicationContext;
import org.pentaho.messages.util.LocaleHelper;
import org.pentaho.util.logging.ILogger;

public class PentahoUtility {

  private static boolean started = false;
  private static String solutionRoot = null;
  private static String baseURL = null;
  
  public static void startup() {
    startup(solutionRoot, baseURL);
  }
  
  public static void setSolutionRoot(String solutionRoot) {
    PentahoUtility.solutionRoot = solutionRoot;
  }
  
  public static String getSolutionRoot() {
    return solutionRoot;
  }

  public static void startup(String solutionRootPath, String baseURL) {

//    PentahoSystem.setUserDetailsRoleListService(new UserDetailsRoleListService());
    
    LocaleHelper.setLocale(Locale.getDefault());

    if (!started) {

      PentahoSystem.loggingLevel = ILogger.ERROR;

      if (PentahoSystem.getApplicationContext() == null) {

        StandaloneApplicationContext applicationContext = new StandaloneApplicationContext(solutionRootPath, ""); //$NON-NLS-1$

        // set the base url assuming there is a running server on port 8080
        applicationContext.setBaseUrl(baseURL);

        // Setup simple-jndi for datasources
        System.setProperty("java.naming.factory.initial", "org.osjava.sj.SimpleContextFactory"); //$NON-NLS-1$ //$NON-NLS-2$
        System.setProperty("org.osjava.sj.root", solutionRootPath + "/system/simple-jndi"); //$NON-NLS-1$ //$NON-NLS-2$
        System.setProperty("org.osjava.sj.delimiter", "/"); //$NON-NLS-1$ //$NON-NLS-2$
        PentahoSystem.init(applicationContext);
        started = true;
      }
    }
  }

  public static void shutdown() {
    try {
      PentahoSystem.shutdown();
      started = false;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static String getBaseURL() {
    return baseURL;
  }

  public static void setBaseURL(String baseURL) {
    PentahoUtility.baseURL = baseURL;
  }

}
