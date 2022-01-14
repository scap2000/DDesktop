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
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Provides utilities for retrieving the version number. The class assumes the
 * components of the version number are stored as properties in a properties
 * file. The class also has a singleton instance to cache the loading of the
 * version number for later use.
 *
 * @author Anthony de Shazor
 */
public class VersionHelper
{

    @NotNull
    public static final String RELEASE_MAJOR = "release.major.number";

    @NotNull
    public static final String RELEASE_MINOR = "release.minor.number";

    @NotNull
    public static final String RELEASE_MILESTONE = "release.milestone.number";

    @NotNull
    public static final String BUILD_NUMBER = "buildNum";

    @NotNull
    public static final String DEFAULT_VERSION_RESOURCE_URL = "/version.properties";

    @NotNull
    private static final VersionHelper emptyInstance = new VersionHelper();

    @NotNull
    private static VersionHelper instance = emptyInstance;

    @Nullable
    private String major = "";

    @Nullable
    private String minor = "";

    @Nullable
    private String milestone = "";

    @Nullable
    private String buildNum = "";


    /**
     * Retrieves the cached instance. Value will not be <code>null</code>.
     *
     * @return The cached instance.
     */
    @NotNull
    public static VersionHelper getInstance()
    {
        return instance;
    }


    /**
     * Sets the cached instance. Applications should call this method on
     * start-up before any other calls to {@link #getInstance()}.
     *
     * @param helper the desired helper. If <code>null</code>, the default empty
     *               instance will be used.
     */
    public static void setInstance(@Nullable VersionHelper helper)
    {
        instance = (helper == null) ? emptyInstance : helper;
    }


    /**
     * Constructs an empty instance. The version number fields are all blank.
     */
    public VersionHelper()
    {
    }


    /**
     * Constructs an instance loading the properties from the specified URL.
     *
     * @param url the URL of the properties file containing the version number.
     * @throws IOException on error reading from the properties file.
     */
    public VersionHelper(@NotNull String url) throws IOException
    {
        this(VersionHelper.class.getResourceAsStream(url));
    }


    /**
     * Loads the version from the specified input stream of a properites file.
     *
     * @param input the input stream.
     * @throws IOException on error reading from the properties stream.
     */
    public VersionHelper(@NotNull InputStream input) throws IOException
    {
        init(input);
    }


    /**
     * Initializes the instance, reaading the properties from the input stream.
     *
     * @param input the input stream.
     * @throws IOException on error reading from the properties stream.
     */
    private void init(@NotNull InputStream input) throws IOException
    {
        Properties props;

        props = new Properties();
        props.load(input);

        major = props.getProperty(RELEASE_MAJOR);
        minor = props.getProperty(RELEASE_MINOR);
        milestone = props.getProperty(RELEASE_MILESTONE);
        buildNum = props.getProperty(BUILD_NUMBER);
    }


    /**
     * Reports the major component of the version number.
     *
     * @return The major component of the version number. The value will not be <code>null</code>.
     */
    @NotNull
    public String getMajorRelease()
    {
        return (major == null) ? "0" : major;
    }


    /**
     * Reports the minor component of the version number.
     *
     * @return The minor component of the version number. The value will not be <code>null</code>.
     */
    @NotNull
    public String getMinorRelease()
    {
        return (minor == null) ? "0" : minor;
    }


    /**
     * Reports the milestone component of the version number.
     *
     * @return The milestone component of the version number. The value will not be <code>null</code>.
     */
    @NotNull
    public String getMilestoneRelease()
    {
        return (milestone == null) ? "0" : milestone;
    }


    /**
     * Reports the build number component of the version number.
     *
     * @return The build number component of the version number. The value will not be <code>null</code>.
     */
    @NotNull
    public String getBuildNumber()
    {
        return (buildNum == null) ? "0" : buildNum;
    }


    /**
     * Reports the version number in proper notation.
     *
     * @return The version number in proper notation.
     */
    @NotNull
    public String getVersion()
    {
        return MessageFormat.format("{0}.{1}.{2} build {3}", getMajorRelease(), getMinorRelease(), getMilestoneRelease(), getBuildNumber());//NON-NLS
    }
}
