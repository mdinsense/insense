//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.09 at 05:16:37 PM EDT 
//


package com.ensense.insense.core.generated.jaxb.analytics.detailedview;

import javax.xml.bind.annotation.XmlRegistry;

import com.ensense.insense.data.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData;
import com.ensense.insense.data.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags.TagVariablesData;
import com.ensense.insense.data.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cts.mint.generated.jaxb.analytics.detailedview package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cts.mint.generated.jaxb.analytics.detailedview
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TagVariablesData }
     * 
     */
    public TagVariablesData createDetailedViewWebAnalyticsTagDataDetailedViewTagsTagVariablesData() {
        return new TagVariablesData();
    }

    /**
     * Create an instance of {@link DetailedViewWebAnalyticsTagData }
     * 
     */
    public DetailedViewWebAnalyticsTagData createDetailedViewWebAnalyticsTagData() {
        return new DetailedViewWebAnalyticsTagData();
    }

    /**
     * Create an instance of {@link DetailedViewTags }
     * 
     */
    public DetailedViewTags createDetailedViewWebAnalyticsTagDataDetailedViewTags() {
        return new DetailedViewTags();
    }

}
