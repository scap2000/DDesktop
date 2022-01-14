package org.digitall.projects.kratos.galpon;

import java.util.Enumeration;
import java.util.Properties;

public abstract class Launcher {

    public static void main(String[] args) {
	String classpath = "./3rdParty/reporter/jfree-0.8.9.2/bsf-2.4.0.jar:./3rdParty/reporter/jfree-0.8.9.2/bsh-1.3.0.jar:./3rdParty/reporter/jfree-0.8.9.2/gnujaxp.jar:./3rdParty/reporter/jfree-0.8.9.2/itext-1.5.2.jar:./3rdParty/reporter/jfree-0.8.9.2/jcommon-1.0.12.jar:./3rdParty/reporter/jfree-0.8.9.2/jcommon-serializer-0.2.0.jar:./3rdParty/reporter/jfree-0.8.9.2/jfreechart-1.0.5.jar:./3rdParty/reporter/jfree-0.8.9.2/jlfgr-1_0.jar:./3rdParty/reporter/jfree-0.8.9.2/libfonts-0.3.4.jar:./3rdParty/reporter/jfree-0.8.9.2/libformula-0.1.16.jar:./3rdParty/reporter/jfree-0.8.9.2/libloader-0.3.7.jar:./3rdParty/reporter/jfree-0.8.9.2/librepository-0.1.6.jar:./3rdParty/reporter/jfree-0.8.9.2/libxml-0.9.11.jar:./3rdParty/reporter/jfree-0.8.9.2/pixie-0.8.10.jar:./3rdParty/reporter/jfree-0.8.9.2/poi-3.0.1-jdk122-final-20071014.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/commons-lang-2.2.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-actionsequence-dom-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-core-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-cwm-1.5.4.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-data-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-i18n-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-messages-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-meta-1.6.0.GA.158.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-mql-builder-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-plugin-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-publisher-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-report-wizard-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-report-wizard-core.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-reporting-engine-classic-0.8.9-rc6.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-reporting-engine-classic-ext-0.8.9-rc6.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-repository-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-security-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-test-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-ui-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-util-1.6.0.GA.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-versionchecker.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-vfs.jar:/digitall/desarrollo/jdevhome/mywork/DDesktop./3rdParty/reporter/jfree-0.8.9.2/designer/svgsalamander.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/commons-lang-2.2.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-actionsequence-dom-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-core-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-cwm-1.5.4.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-data-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-i18n-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-messages-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-meta-1.6.0.GA.158.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-mql-builder-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-plugin-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-publisher-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-report-wizard-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-report-wizard-core.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-reporting-engine-classic-0.8.9-rc6.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-reporting-engine-classic-ext-0.8.9-rc6.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-repository-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-security-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-test-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-ui-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-util-1.6.0.GA.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-versionchecker.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/pentaho-vfs.jar:./3rdParty/reporter/jfree-0.8.9.2/designer/svgsalamander.jar:./3rdParty/sql/postgresql-8.2-507.jdbc3.jar:./digitall/digitall-resourcesrequests-0.9.1.jar:./digitall/digitall-corporation-0.9.0.jar:./digitall/digitall-libs-0.9.0.jar:./digitall/digitall-resourcescontrol-0.9.0.jar:./digitall/digitall-calendar.jar:./digitall/digitall-cashflow-0.9.1.jar:./digitall/digitall-common-libs-0.9.1.jar:./digitall/digitall-licenses-0.9.1.jar:./digitall/digitall-files-0.9.1.jar:./digitall/digitall-taxes-0.9.1.jar:./digitall/digitall-kratos-0.9.1.jar:./digitall/digitall-chwy-0.9.1.jar:./digitall/digitall-base-0.9.0.jar:./kratos-galpon-0.9.1.jar";
	String osName = System.getProperty("os.name");
	try {
	    if (osName.startsWith("Mac OS")) {
	    } else if (osName.startsWith("Windows")) {
		classpath = classpath.replace('/','\\').replace(':',';');
	    } else {
		//Assume Linux or Unix
	    }
	    Properties props = System.getProperties();
	    // Enumerate all system properties
	    Enumeration enume = props.propertyNames();
	    for (; enume.hasMoreElements(); ) {
	        // Get property name
	        String propName = (String)enume.nextElement();
	        // Get property value
	        String propValue = (String)props.get(propName);
	        System.out.println(propName + ": " + propValue);
	    }
	    classpath = System.getProperty("java.class.path");
	    System.out.println("Launching with classpath: " + classpath);
	    Process process = Runtime.getRuntime().exec("java -classpath " + classpath + " -Xms256M -Xmx512M org.digitall.projects.kratos.galpon.KratosGalpon");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
