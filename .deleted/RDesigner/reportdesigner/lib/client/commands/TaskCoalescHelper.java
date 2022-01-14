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
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 15:02:31
 */
public class TaskCoalescHelper
{
    @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
    public static void main(@NotNull String[] args) throws InterruptedException
    {
        Logger gridvisionLogger = Logger.getLogger("ch.gridvision");//NON-NLS
        Handler[] handlers = gridvisionLogger.getHandlers();
        for (Handler handler : handlers)
        {
            gridvisionLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setLevel(Level.ALL);
        gridvisionLogger.addHandler(consoleHandler);

        Logger.getLogger("ch.gridvision").setLevel(Level.ALL);//NON-NLS

        System.out.println("bla");//NON-NLS
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TaskCoalescHelper.main \"eek\" = " + "eek");

        TaskCoalescHelper taskCoalescHelper = new TaskCoalescHelper(100, true);
        int count = 0;
        for (int i = 0; i < 100000; i++)
        {
            count++;
            if (Math.random() > 0.99)
            {
                count = 0;
                System.out.println(System.currentTimeMillis());
                Thread.sleep(5000);
            }
            long millis = (long) (Math.random() * 10);
            System.out.print(".");
            Thread.sleep(millis);
            final int cc = count;
            taskCoalescHelper.addTask(new CoalesceableTask()
            {
                @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
                public void run()
                {
                    System.out.println("TaskCoalescHelper.run  t1 " + System.currentTimeMillis() + " cc = " + cc + "\n");//NON-NLS
                }
            });
        }


    }


    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(TaskCoalescHelper.class.getName());

    private long delay;
    @Nullable
    private transient CoalesceableTask nextTask;
    private transient long submittedMillis;


    public TaskCoalescHelper(long delay, final boolean inEDT)
    {
        this.delay = delay;

        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                //noinspection InfiniteLoopStatement
                while (true)
                {
                    //noinspection OverlyBroadCatchBlock
                    try
                    {
                        Runnable nextTask = getNextTask();
                        if (inEDT)
                        {
                            EventQueue.invokeAndWait(nextTask);
                        }
                        else
                        {
                            nextTask.run();
                        }
                    }
                    catch (Throwable e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TaskCoalescHelper.run ", e);
                    }
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }


    @Nullable
    public synchronized CoalesceableTask getNextTask()
    {
        while (true)
        {
            try
            {
                wait();
                if (nextTask != null)
                {
                    long timeToWait = delay - (System.currentTimeMillis() - submittedMillis);
                    while (timeToWait > 0)
                    {
                        wait(timeToWait);
                        timeToWait = delay - (System.currentTimeMillis() - submittedMillis);
                    }
                    CoalesceableTask task = nextTask;
                    nextTask = null;
                    return task;
                }
            }
            catch (InterruptedException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TaskCoalescHelper.getNextTask ", e);
            }
        }
    }


    public synchronized void addTask(@NotNull CoalesceableTask task)
    {
        submittedMillis = System.currentTimeMillis();
        nextTask = task;
        notifyAll();
    }


    public abstract static class CoalesceableTask implements Runnable
    {
        @NotNull
        @SuppressWarnings({"UnusedDeclaration"})
        private StackTraceElement[] callingStackTrace;//useful for debugging


        public CoalesceableTask()
        {
            callingStackTrace = Thread.currentThread().getStackTrace();//MARKED might have a performance impact, remove if necessary
        }


    }

}