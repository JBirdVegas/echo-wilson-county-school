package com.jbirdvegas.echo.ec2

import com.google.gson.Gson
import com.jbirdvegas.echo.ec2.util.HtmlLinkScraper
import com.jbirdvegas.echo.ec2.util.PdfUtil
import org.apache.commons.lang3.tuple.Pair
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage

import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.text.SimpleDateFormat

@Path('/rest')
public class AlexaServlet {
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path('/{meal}')
    public String getMenuForSchool(@PathParam('meal') String meal, String json) {
        def gson = new Gson()
        Request request = gson.fromJson(json, Request.class)
        println "Found request: $json"
        Pair<Integer, Integer> calendarPositionsForDate = PdfUtil.getCalendarPositionsForDate(mSimpleDateFormat.parse(request.when))
        System.out.println("Received school: " + request.school + " meal: " + meal + " positions: " + calendarPositionsForDate.toString())

        def url = HtmlLinkScraper.getPdfUrl("http://www.wcschools.com/food-and-nutrition/menus-and-pricing", "$request.school $meal")
        PDDocument pdDoc = PDDocument.load(url.toURL())
        PDPage specPage = (PDPage) pdDoc.getDocumentCatalog().getAllPages().get(0);
        def position = PdfUtil.getRectangleForPosition(specPage, calendarPositionsForDate.left, calendarPositionsForDate.right)
        def menuText = PdfUtil.getTextFromRectangle(pdDoc, position).trim()

        def response = new Response()
        response.meal = meal
        response.menu = menuText
        response.school = request.school
        return gson.toJson(response)
    }

    class Request {
        public String when;
        public String school;
    }

    class Response {
        public String meal;
        public String menu;
        public String school;
    }
}