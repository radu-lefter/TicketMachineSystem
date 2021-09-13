<%-- 
    Document   : openGate
    Created on : Apr 14, 2020, 12:23:26 PM
    Author     : cgallen
--%>


<%@page import="java.util.ArrayList"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
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
       
    String startStationStr = request.getParameter("sStation");
    if (startStationStr == null || startStationStr.isEmpty()) {
        startStationStr = "UNDEFINED";
    }
    
    String destStationStr = request.getParameter("dStation");
    if (startStationStr == null || startStationStr.isEmpty()) {
        startStationStr = "UNDEFINED";
    }
    
    String[] parts1 = startStationStr.split("-");
    String strSt = parts1[0];
    String strZn = parts1[1];
    
    String[] parts2 = destStationStr.split("-");
    String dstSt = parts2[0];
    String dstZn = parts2[1];
    
    String validFromStr = request.getParameter("validFrom");
    if (validFromStr == null || validFromStr.isEmpty()) {
        validFromStr = df.format(new Date());
    }

    String validToStr = request.getParameter("validTo");
    // valid to initialised to date plus one day
    if (validToStr == null || validToStr.isEmpty()) {
        validToStr = df.format(new Date(new Date().getTime() + 1000 * 60 * 60 * 24));
    }
    
    
    String rStart = strSt.replaceAll(" ", "+");
    
       String recieve;
       String buffer = "";
       URL jsonpage = new URL("http://localhost:8080/projectfacadeweb/rest/stationService/getMachineByStationName?name="+rStart);
       URLConnection urlcon = jsonpage.openConnection();
       BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

       while ((recieve = buffread.readLine()) != null)
       buffer += recieve;
       buffread.close();
    
       JsonObject jobj = new Gson().fromJson(buffer, JsonObject.class);

       JsonArray tml = jobj.getAsJsonArray("ticketMachineList");
       JsonElement tmObj = tml.get(0);
       JsonObject tmFirst = tmObj.getAsJsonObject();
       //JsonObject tm = jobj.getAsJsonObject("ticketMachineList");
       String uuid = tmFirst.get("uuid").toString();
       String realUuid = uuid. replaceAll("^\"+|\"+$", "");
    
    
    
    
    /*
    {"code":200,"debugMessage":null,"ticketMachineConfig":null,
    "ticketMachineList":[
    {"uuid":"32e64507-2ef6-44e1-9651-511d49b5bbc6","station":{"name":"Aldgate","zone":3}},
    {"uuid":"4c43d03c-873c-4f17-bbf8-73ae32a01b92","station":{"name":"Aldgate","zone":3}},
    {"uuid":"4cee732f-9397-4d24-a970-ca5eb0d62add","station":{"name":"Aldgate","zone":3}}]}
    */
    
       String recieve1;
       String buffer1 = "";
       URL jsonpage1 = new URL("http://localhost:8080/projectfacadeweb/rest/stationService/getTicketMachineConfig?uuid="+realUuid);
       URLConnection urlcon1 = jsonpage1.openConnection();
       BufferedReader buffread1 = new BufferedReader(new InputStreamReader(urlcon1.getInputStream()));

       while ((recieve1 = buffread1.readLine()) != null)
       buffer1 += recieve1;
       buffread1.close();
    
       //System.out.println(buffer);
      JsonObject jobj1 = new Gson().fromJson(buffer1, JsonObject.class);

      //getting values for the peak/offpeak prices
      JsonObject tconf1 = jobj1.getAsJsonObject("ticketMachineConfig");
      JsonObject price1 = tconf1.getAsJsonObject("pricingDetails");
      String peakPPZ = price1.get("peakPricePerZone").toString();
      String offpeakPPZ = price1.get("offpeakPricePerZone").toString();
      
      //getting price bands
      ArrayList<Integer> bands = new ArrayList<Integer>(); ;
      JsonArray pBand = price1.getAsJsonArray("priceBand");
      int size = pBand.size();
      for(int i =0; i< size; i++){
      
      JsonElement pbObj1 = pBand.get(i);
      JsonObject pbFirst = pbObj1.getAsJsonObject();
      int pb1 = Integer.parseInt(pbFirst.get("hour").toString());

      bands.add(pb1);
      
    }
    
    //getting purchasing hour
      int purchased = Integer.parseInt(validFromStr.substring(11, 13));
      
   
      
      
      //getting zone difference
      int strZnInt = Integer.parseInt(strZn);
      int dstZnInt = Integer.parseInt(dstZn);
      int znDiff = Math.abs(strZnInt-dstZnInt);
      
      
      //calculating the price
      double price = 0;
      if(purchased > bands.get(0) && purchased < bands.get(1)){
      price = Double.parseDouble(offpeakPPZ) * znDiff;
    }else if (purchased > bands.get(1) && purchased < bands.get(2)){
      price = Double.parseDouble(peakPPZ) * znDiff;
    } else if(purchased > bands.get(2)){
      price = Double.parseDouble(offpeakPPZ) * znDiff;
    }
      
      
      /*
      {"code":200,"debugMessage":null,
      "ticketMachineConfig":{"uuid":"32e64507-2ef6-44e1-9651-511d49b5bbc6","stationName":"Aldgate","stationZone":3,
      "pricingDetails":{"peakPricePerZone":5.0,"offpeakPricePerZone":2.5,
      "priceBand":[{"hour":0,"minutes":0,"rate":"OFFPEAK"},
      {"hour":9,"minutes":0,"rate":"PEAK"},
      {"hour":11,"minutes":30,"rate":"OFFPEAK"}]},
      "station":null},"ticketMachineList":null}
      */

   
    
    

    

    String ticketStr = "";
    try {
        Date validFrom = df.parse(validFromStr);
        Date validTo = df.parse(validToStr);

        Ticket ticket = gateManagementService.createTicket(strZn, validFrom, validTo, strSt);

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
                    <td>Starting zone</td>
                    <td><input type="text" name="zones" value="<%=strZn%>"></td>
                </tr>
                <tr>
                    <td>Starting Station:</td>
                    <td><input type="text" name="startStation" value="<%=strSt%>"></td>
                </tr>
                <tr>
                    <td>Destination zone</td>
                    <td><input type="text" name="zones" value="<%=dstZn%>"></td>
                </tr>
                <tr>
                    <td>Destination Station:</td>
                    <td><input type="text" name="destStation" value="<%=dstSt%>"></td>
                </tr>
                <tr>
                    <td>Valid From Time:</td>
                    <td><input type="text" name="validFrom" value="<%=validFromStr%>"></td>
                </tr>
                <tr>
                    <td>Valid To Time:</td>
                    <td><input type="text" name="validTo" value="<%=validToStr%>"></td>
                </tr>
                <tr>
                    <td>Ticket price</td>
                    <td><%=price%></td>
                </tr>
                <tr>
                    <td>Ticket price</td>
                    <td><%=purchased%></td>
                </tr>
         
            </table>
            <button type="submit" >Create Ticket</button>
        </form> 
        <h1>generated ticket xml</h1>
        <textarea id="ticketTextArea" rows="10" cols="120"><%=ticketStr%></textarea>
        
        <p><a href="../projectfacadeweb-client/openGate.jsp" target="_blank">Test your ticket at the gate</a></p>

    </body>
</html>


