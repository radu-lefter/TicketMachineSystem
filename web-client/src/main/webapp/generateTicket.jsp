<%-- 
    Document   : openGate
    Created on : Apr 14, 2020, 12:23:26 PM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="org.solent.com528.project.model.util.DateTimeAdapter"%>
<%@page import="org.solent.com528.project.impl.service.GateEntryServiceImpl"%>
<%@page import="org.solent.com528.project.impl.service.GateManagementServiceImpl"%>
<%@page import="org.solent.com528.project.impl.service.ServiceFactoryImpl"%>
<%@page import="org.solent.com528.project.impl.service.GateEntryService"%>
<%@page import="org.solent.com528.project.impl.service.GateManagementService"%>
<%@page import="org.solent.com528.project.model.dto.Ticket"%>
<%@page import="javax.xml.bind.JAXBContext"%>
<%@page import="java.io.StringWriter"%>
<%@page import="javax.xml.bind.Marshaller"%>

<%
    String errorMessage = "";

    GateManagementService gateManagementService = ServiceFactoryImpl.getGateManagementService();
    // pull in standard date format
    DateFormat df = new SimpleDateFormat(DateTimeAdapter.DATE_FORMAT);

    String validFromStr = request.getParameter("validFrom");
    if (validFromStr == null || validFromStr.isEmpty()) {
        validFromStr = df.format(new Date());
    }

    String validToStr = request.getParameter("validTo");
    // valid to initialised to date plus one day
    if (validToStr == null || validToStr.isEmpty()) {
        validToStr = df.format(new Date(new Date().getTime() + 1000 * 60 * 60 * 24));
    }
    
    String zonesStr = request.getParameter("zones");
    if (zonesStr == null || zonesStr.isEmpty()) {
        zonesStr = "1";
    }
    
    String startStationStr = request.getParameter("startStation");
    if (startStationStr == null || startStationStr.isEmpty()) {
        startStationStr = "UNDEFINED";
    }
    

    String ticketStr = "";
    try {
        Date validFrom = df.parse(validFromStr);
        Date validTo = df.parse(validToStr);

        Ticket ticket = gateManagementService.createTicket(zonesStr, validFrom, validTo, startStationStr);

        ticketStr = ticket.toXML();
    } catch (Exception ex) {
        errorMessage = ex.getMessage();
    }


%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage gate Locks</title>
    </head>
    <body>
        <h1>Generate a New Ticket</h1>
        <!-- print error message if there is one -->
        <div style="color:red;"><%=errorMessage%></div>

        <form action="./generateTicket.jsp"  method="post">
            <table>
                <tr>
                    <td>Zones:</td>
                    <td><input type="text" name="zones" value="<%=zonesStr%>"></td>
                </tr>
                <tr>
                    <td>Starting Station:</td>
                    <td><input type="text" name="startStation" value="<%=startStationStr%>"></td>
                </tr>
                <tr>
                    <td>Destination Station:</td>
                    <td><input type="text" name="destStation" value=""></td>
                </tr>
                <tr>
                    <td>Valid From Time:</td>
                    <td><input type="text" name="validFrom" value="<%=validFromStr%>"></td>
                </tr>
                <tr>
                    <td>Valid To Time:</td>
                    <td><input type="text" name="validTo" value="<%=validToStr%>"></td>
                </tr>
            </table>
            <button type="submit" >Create Ticket</button>
        </form> 
        <h1>generated ticket xml</h1>
        <textarea id="ticketTextArea" rows="10" cols="120"><%=ticketStr%></textarea>

    </body>
</html>

