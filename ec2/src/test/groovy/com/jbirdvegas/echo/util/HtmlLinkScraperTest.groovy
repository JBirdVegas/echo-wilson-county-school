package com.jbirdvegas.echo.util

import com.jbirdvegas.echo.ec2.util.HtmlLinkScraper
import groovy.transform.CompileStatic
import org.junit.Assert
import org.junit.Test

class HtmlLinkScraperTest {

    @Test
    void parseElemLunchMenuUrlTest() {
        Assert.assertNotNull(getLink("Elementary Lunch"))
    }

    @Test
    void parseMiddleLunchMenuUrlTest() {
        Assert.assertNotNull(getLink("Middle School Lunch"))
    }

    @Test
    void parseElemAndMiddleBreakfastMenuUrlTest() {
        Assert.assertNotNull(getLink("Elementary and Middle School Breakfast"))
    }

    @Test
    void parseHighBreakfastMenuUrlTest() {
        Assert.assertNotNull(getLink("High School Lunch"))
    }

    @Test
    void parseHighSchoolLunchMenuUrlTest() {
        Assert.assertNotNull(getLink("High School Breakfast"))
    }

    @CompileStatic
    private static String getLink(String urlLabel) {
        def url = "http://www.wcschools.com/food-and-nutrition/menus-and-pricing"
        def link = HtmlLinkScraper.getPdfUrl(url, urlLabel)
        println "Label {$urlLabel} found link: $link"
        link
    }
}
