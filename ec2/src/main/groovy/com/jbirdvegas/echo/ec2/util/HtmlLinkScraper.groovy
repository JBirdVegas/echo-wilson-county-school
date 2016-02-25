package com.jbirdvegas.echo.ec2.util

class HtmlLinkScraper {
    // example
    // HtmlLinkScraper.getPdfUrl("http://www.wcschools.com/food-and-nutrition/menus-and-pricing/", "Elementary Lunch")
    public static String getPdfUrl(String urlToParse, String menuOfInterest) {
        String html = urlToParse.toURL().text
        String resultUrl = null
        html.eachLine { String line ->
            if (line.trim().toLowerCase().endsWith("target=\"_blank\">${menuOfInterest}</a></p>".toLowerCase())) {
                def slurper = new XmlSlurper()
                def doc = slurper.parseText(line)
                doc.depthFirst().collect { it }.findAll { it.name() == "a" }.each {
                    println "${it.text()}, ${it.@href.text()}"
                    resultUrl = it.@href.text()
                }
            }
        }
        return resultUrl
    }
}
