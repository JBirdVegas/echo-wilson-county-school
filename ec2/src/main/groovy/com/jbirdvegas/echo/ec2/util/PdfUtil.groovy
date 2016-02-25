package com.jbirdvegas.echo.ec2.util

import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.lang3.tuple.Pair
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.util.PDFTextStripperByArea

import java.awt.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class PdfUtil {

    public static String getPageContent(String url) throws IOException {
        PDDocument pdDoc = PDDocument.load(url.toURL());
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0);

        int verticalPosition = getCurrentWeekPosition()
        int horizontalPosition = getCurrentDayPosition()

        Rectangle rect = getRectangleForPosition(specPage, verticalPosition, horizontalPosition)
        return getTextFromRectangle(pdDoc, rect)
    }

    public static String getPageContent(File pdfPath) throws IOException {
        return getPageContent(pdfPath.bytes)
    }

    public static String getPageContent(byte[] bytes) throws IOException {
        PDDocument pdDoc = PDDocument.load(new ByteArrayInputStream(bytes));
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0);

        int verticalPosition = getCurrentWeekPosition()
        int horizontalPosition = getCurrentDayPosition()

        Rectangle rect = getRectangleForPosition(specPage, verticalPosition, horizontalPosition)
        return getTextFromRectangle(pdDoc, rect)
    }

    static String getTextFromRectangle(PDDocument document, Rectangle rectangle) {
        StringBuilder result = new StringBuilder()
        try {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);

            println "Using rect: $rectangle"
            stripper.addRegion("class1", rectangle);

            def pages = document.getDocumentCatalog().getPages().kids
            println "Get pages: pages"
//            FontBoxFont.FontCache cache = null;
            PDPage firstPage = (PDPage) pages.get(0);
            println "Got first page: $firstPage"
            def regions = stripper.extractRegions(firstPage);
            println "Found regions: $regions"

            def region = stripper.getTextForRegion("class1")
            println "Region: $region"
            def lines = Arrays.asList(region.split('\n'))
            println "Lines: $lines"
            for (String line : lines) {
                try {
                    if (!NumberUtils.isDigits("${line.trim().charAt(0)}")) {
                        result.append(line).append('\n')
                        println "Appending string: $line"
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    println "String was out of bounds at position 0: $line"
                }
            }
            println "Found result: $result"
        } catch (IOException e) {
            println "Failed to get text"
            e.printStackTrace()
        }
        return result;
    }

    /**
     *
     * @param date AMAZON.DATE field, ex: 2015-07-00T9
     * @return position on the calendar
     */
    static Pair<Integer, Integer> getCalendarPositionsForDate(Date date) {
        println("Received date: $date")
        def instance = Calendar.getInstance()
        instance.setTime(date)

        def localDate = instance.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        Pair<Integer, Integer> pair = Pair.of(instance.get(Calendar.WEEK_OF_MONTH),
                getDayPosition(localDate.dayOfWeek))

        return pair
    }

    static Rectangle getRectangleForPosition(PDPage page, int vertical, int horizontal) {
        println "Getting rectangle for position $vertical, $horizontal"
        def border = 28
        def boxWidth = (int) (page.mediaBox.width) / 5
        def boxHeight = (int) (page.mediaBox.height) / 5

        return new Rectangle(
                (int) (((horizontal - 1) * boxWidth) - horizontal * 2.2),
                (int) (((vertical - 1) * boxHeight) + vertical),
                (int) Math.round(boxWidth),
                (int) Math.round(boxHeight - border));
    }

    static int getCurrentDayPosition() {
        LocalDate date = LocalDate.now()
        def week = date.getDayOfWeek()
        // default to monday position
        return getDayPosition(week)
    }

    static int getDayPosition(DayOfWeek dayOfWeek) {
        def position = 1
        switch (dayOfWeek) {
            case DayOfWeek.SUNDAY:
            case DayOfWeek.SATURDAY:
                // fall through to monday since no school on weekends
            case DayOfWeek.MONDAY:
                position = 1
                break
            case DayOfWeek.TUESDAY:
                position = 2
                break
            case DayOfWeek.WEDNESDAY:
                position = 3
                break
            case DayOfWeek.THURSDAY:
                position = 4
                break
            case DayOfWeek.FRIDAY:
                position = 5
                break
        }
        return position
    }

    static int getCurrentWeekPosition() {
        Calendar c = Calendar.getInstance()
        int week = c.get(Calendar.WEEK_OF_MONTH)
        println "Week: $week"
        week
    }
}