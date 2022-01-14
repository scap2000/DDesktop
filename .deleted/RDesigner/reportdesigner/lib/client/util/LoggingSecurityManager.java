/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 15.02.2006
 * Time: 08:33:31
 */
public class LoggingSecurityManager extends SecurityManager
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(LoggingSecurityManager.class.getName());

    @NotNull
    private SecurityManager delegate;


    public LoggingSecurityManager(@NotNull SecurityManager delegate)
    {
        this.delegate = delegate;
    }


    @NotNull
    public Object getSecurityContext()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.getSecurityContext ");
        return delegate.getSecurityContext();
    }


    public void checkPermission(@NotNull Permission perm)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPermission ");
        delegate.checkPermission(perm);
    }


    public void checkPermission(@NotNull Permission perm, @NotNull Object context)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPermission ");
        delegate.checkPermission(perm, context);
    }


    public void checkCreateClassLoader()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkCreateClassLoader ");
        delegate.checkCreateClassLoader();
    }


    public void checkAccess(@NotNull Thread t)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkAccess ");
        delegate.checkAccess(t);
    }


    public void checkAccess(@NotNull ThreadGroup g)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkAccess ");
        delegate.checkAccess(g);
    }


    public void checkExit(int status)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkExit ");
        delegate.checkExit(status);
    }


    public void checkExec(@NotNull String cmd)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkExec ");
        delegate.checkExec(cmd);
    }


    public void checkLink(@NotNull String lib)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkLink ");
        delegate.checkLink(lib);
    }


    public void checkRead(@NotNull FileDescriptor fd)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkRead ");
        delegate.checkRead(fd);
    }


    public void checkRead(@NotNull String file)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkRead ");
        delegate.checkRead(file);
    }


    public void checkRead(@NotNull String file, @NotNull Object context)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkRead ");
        delegate.checkRead(file, context);
    }


    public void checkWrite(@NotNull FileDescriptor fd)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkWrite ");
        delegate.checkWrite(fd);
    }


    public void checkWrite(@NotNull String file)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkWrite ");
        delegate.checkWrite(file);
    }


    public void checkDelete(@NotNull String file)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkDelete ");
        delegate.checkDelete(file);
    }


    public void checkConnect(@NotNull String host, int port)
    {
        delegate.checkConnect(host, port);
    }


    public void checkConnect(@NotNull String host, int port, @NotNull Object context)
    {
    }


    public void checkListen(int port)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkListen ");
        delegate.checkListen(port);
    }


    public void checkAccept(@NotNull String host, int port)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkAccept ");
        delegate.checkAccept(host, port);
    }


    public void checkMulticast(@NotNull InetAddress maddr)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkMulticast ");
        delegate.checkMulticast(maddr);
    }


    public void checkPropertiesAccess()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPropertiesAccess ");
        delegate.checkPropertiesAccess();
    }


    public void checkPropertyAccess(@NotNull String key)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPropertyAccess ");
        delegate.checkPropertyAccess(key);
    }


    public boolean checkTopLevelWindow(@NotNull Object window)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkTopLevelWindow ");
        return delegate.checkTopLevelWindow(window);
    }


    public void checkPrintJobAccess()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPrintJobAccess ");
        delegate.checkPrintJobAccess();
    }


    public void checkSystemClipboardAccess()
    {
        delegate.checkSystemClipboardAccess();
    }


    public void checkAwtEventQueueAccess()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkAwtEventQueueAccess ");
        delegate.checkAwtEventQueueAccess();
    }


    public void checkPackageAccess(@NotNull String pkg)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPackageAccess ");
        delegate.checkPackageAccess(pkg);
    }


    public void checkPackageDefinition(@NotNull String pkg)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkPackageDefinition ");
        delegate.checkPackageDefinition(pkg);
    }


    public void checkSetFactory()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkSetFactory ");
        delegate.checkSetFactory();
    }


    public void checkMemberAccess(@NotNull Class<?> clazz, int which)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkMemberAccess ");
        delegate.checkMemberAccess(clazz, which);
    }


    public void checkSecurityAccess(@NotNull String target)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.checkSecurityAccess ");
        delegate.checkSecurityAccess(target);
    }


    @NotNull
    public ThreadGroup getThreadGroup()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "LoggingSecurityManager.getThreadGroup ");
        return delegate.getThreadGroup();
    }
}
