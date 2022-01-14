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
*/
package org.pentaho.jfreereport.wizard.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class TinyHTTPd implements Runnable {
  public static String solutionRoot = "./"; //$NON-NLS-1$
  public static boolean die = false;
  Socket c;

  public TinyHTTPd(Socket socket) {
    c = socket;
  }

  public static void startServer(String solutionRoot, final int port) {
    TinyHTTPd.solutionRoot = solutionRoot;
    die = false;
    Runnable r = new Runnable() {
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(port);
          serverSocket.setSoTimeout(500);
          while (!die) {
            try {
              Socket client = serverSocket.accept();
              Thread t = new Thread(new TinyHTTPd(client));
              t.start();
            } catch (Exception e) {
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    Thread t = new Thread(r);
    t.setDaemon(true);
    t.start();
  }

  public static void stopServer() {
    die = true;
  }

  public static void main(String args[]) {
    startServer("./resources/solutions", 6736); //$NON-NLS-1$
  }

  public void run() {
    try {
      BufferedReader i = new BufferedReader(new InputStreamReader(c.getInputStream()));
      DataOutputStream o = new DataOutputStream(c.getOutputStream());
      try {
        while (true) {
          String s = i.readLine();
          if (s.length() < 1)
            break;
          if (s.startsWith("GET")) { //$NON-NLS-1$
            StringTokenizer t = new StringTokenizer(s, " "); //$NON-NLS-1$
            t.nextToken();
            String p = t.nextToken();
            p = solutionRoot + "/system/tmp/" + p.substring(p.indexOf("=") + 1); //$NON-NLS-1$ //$NON-NLS-2$
            File file = new File(p);
            int l = (int) new File(p).length();
            byte[] b = new byte[l];
            FileInputStream f = new FileInputStream(file);
            f.read(b);
            o.writeBytes("HTTP/1.0 200 OK\nContent-Length:" + l + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
            o.write(b, 0, l);
          }
        }
      } catch (Exception e) {
        o.writeBytes("HTTP/1.0 404 ERROR\n\n\n"); //$NON-NLS-1$
      }
      o.close();
    } catch (Exception e) {
    }
  }
}