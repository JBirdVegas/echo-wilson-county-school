package com.jbirdvegas.echo.util

import com.jbirdvegas.echo.ec2.util.PdfUtil
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.Before
import org.junit.Test

import java.text.SimpleDateFormat

class PdfUtilTest extends GroovyTestCase {

    public File file

    // for some reason before annotation is being ignored by gradle and IDEA
    @Before
    public void before() {
        URL url = this.getClass().getResource("/February-2016-Elementary-Lunch-Menu.pdf");
        file = new File(url.getFile());
        assertTrue("Expected file to exist @ $file.absolutePath", file.exists())
    }

    @Test
    public void testPrintPdfDebug() {
        before()
        println "$file.absolutePath"
        println PdfUtil.getPageContent(file)

        def format = new SimpleDateFormat("yyyy-MM-dd")
        def parse = format.parse('2016-02-24')
        println "${PdfUtil.getCalendarPositionsForDate(parse)}"
    }

    @Test
    public void testDateFeb22() {
        before()
        PDDocument pdDoc = PDDocument.load(file);
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0)

        def forPosition = PdfUtil.getRectangleForPosition(specPage, 4, 1)
        assertEquals("Hamburger/Cheeseburger \n" +
                "Lettuce, Tomato & Pickle \n" +
                "Pulled Pork BBQ Sandwich\n" +
                "Triangle Potato Patties \n" +
                "Buttery Corn On The Cob \n" +
                "Seasonal Fresh Fruit", PdfUtil.getTextFromRectangle(pdDoc, forPosition).trim())
    }

    @Test
    public void testDateFeb18() {
        before()
        PDDocument pdDoc = PDDocument.load(file);
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0)

        def forPosition = PdfUtil.getRectangleForPosition(specPage, 3, 4)
        assertEquals("Corn Dog w/Dipping Sauce \n" +
                "Nachos w/Cheese & Salsa \n" +
                "Yogurt & String Cheese Combo \n" +
                "Baby Carrots w/Ranch Dip \n" +
                "Crinkle Cut French Fries \n" +
                "Chilled Peaches", PdfUtil.getTextFromRectangle(pdDoc, forPosition).trim())
    }

    @Test
    public void testPosition4and3() {
        before()
        PDDocument pdDoc = PDDocument.load(file);
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0)
        def forPosition = PdfUtil.getRectangleForPosition(specPage, 4, 1)

        def textFromRectangle = PdfUtil.getTextFromRectangle(pdDoc, forPosition).trim()
        println "Position: $textFromRectangle"
    }
}
