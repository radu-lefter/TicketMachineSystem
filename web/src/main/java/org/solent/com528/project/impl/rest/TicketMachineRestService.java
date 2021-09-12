/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.impl.rest;

/**
 *
 * @author gallenc
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com528.project.impl.web.WebObjectFactory;
import org.solent.com528.project.model.dao.PriceCalculatorDAO;
import org.solent.com528.project.model.dao.StationDAO;
import org.solent.com528.project.model.dto.PriceBand;
import org.solent.com528.project.model.dto.PricingDetails;
import org.solent.com528.project.model.dto.Rate;

import org.solent.com528.project.model.dto.ReplyMessage;
import org.solent.com528.project.model.dto.Station;
import org.solent.com528.project.model.dto.TicketMachineConfig;
import org.solent.com528.project.model.service.ServiceFacade;

import org.solent.com528.project.model.dao.TicketMachineDAO;
import org.solent.com528.project.model.dto.TicketMachine;

/**
 * To make the ReST interface easier to program. All of the replies are contained in ReplyMessage classes but only the fields indicated are populated with each
 * reply. All replies will contain a code and a debug message.
 */
@Path("/stationService")
public class TicketMachineRestService {

    // SETS UP LOGGING 
    // note that log name will be org.solent.com528.factoryandfacade.impl.rest.TicketMachineRestService
    final static Logger LOG = LogManager.getLogger(TicketMachineRestService.class);
    
    private ServiceFacade serviceFacade = WebObjectFactory.getServiceFacade();
    private TicketMachineDAO ticketMachineDAO = serviceFacade.getTicketMachineDAO();

    /**
     * this is a very simple rest test message which only returns a string
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/
     *
     * @return String simple message
     */
    @GET
    public String message() {
        LOG.debug("stationService called");
        return "Hello, rest!";
    }

    /**
     * get heartbeat
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/getTicketMachineConfig?uuid=xyz
     *
     * @return list of all Animals in List<String> replyMessage.getStringList()
     */
    @GET
    @Path("/getTicketMachineConfig")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTicketMachineConfig(@QueryParam("uuid") String uuid) {
        try {

            ServiceFacade serviceFacade = WebObjectFactory.getServiceFacade();
            StationDAO stationDAO = serviceFacade.getStationDAO();
            PriceCalculatorDAO priceCalculatorDAO = serviceFacade.getPriceCalculatorDAO();
            
            ReplyMessage replyMessage = new ReplyMessage();
            LOG.debug("/getTicketMachineConfig called  uuid=" + uuid);
            // NOTE change this to uuid.isEmpty() if using java 8
            if (uuid == null || uuid.isEmpty()) {
                throw new IllegalArgumentException("uuid query must be defined ?uuid=xxx");
            }
            // get this from local properties
            TicketMachine ticketMachine = ticketMachineDAO.findByUuid(uuid);
            Station station = ticketMachine.getStation();
            String stationName = station.getName();
            Integer stationZone = station.getZone();
            Long stationId = station.getId();
            //String stationName = "Waterloo";
            //Integer stationZone = 1;

            // YOU WOULD GET THIS FROM THE DAO'S IN THE SERVICE FACADE   
            
            PricingDetails pricingDetails = new PricingDetails();
            pricingDetails.setOffpeakPricePerZone(2.50);
            pricingDetails.setPeakPricePerZone(5.00);
            
            List<PriceBand> priceBandList = new ArrayList();
            pricingDetails.setPriceBandList(priceBandList);

            // adding 3 price bands
            PriceBand priceBand1 = new PriceBand();
            priceBand1.setRate(Rate.OFFPEAK);
            priceBand1.setHour(0);
            priceBand1.setMinutes(0);
            priceBandList.add(priceBand1);

            PriceBand priceBand2 = new PriceBand();
            priceBand2.setRate(Rate.PEAK);
            priceBand2.setHour(9);
            priceBand2.setMinutes(0);
            priceBandList.add(priceBand2);
            
            PriceBand priceBand3 = new PriceBand();
            priceBand3.setRate(Rate.OFFPEAK);
            priceBand3.setHour(11);
            priceBand3.setMinutes(30);
            priceBandList.add(priceBand3);

            // STATION LIST
            
            //List<Station> stationList = stationDAO.findAll();
            
//            List<Station> stationList = new ArrayList();
//            Station station = new Station();
//            station.setName("Waterloo");
//            station.setZone(1);
//            stationList.add(station);
//            Station station2 = new Station();
//            station2.setName("Abbey Road");
//            station2.setZone(2);
//            stationList.add(station2);
//            Station station3 = new Station();
//            station3.setName("Acton Town");
//            station3.setZone(3);
//            stationList.add(station3);

            // 200 CODE
            replyMessage.setCode(Response.Status.OK.getStatusCode());
            
            
            TicketMachineConfig ticketMachineConfig = new TicketMachineConfig();

            ticketMachineConfig.setPricingDetails(pricingDetails);


            ticketMachineConfig.setStationName(stationName);
            ticketMachineConfig.setUuid(uuid);

            ticketMachineConfig.setStationZone(stationZone);
            
            

            
            
            //TicketMachineConfig ticketMachineConfig = serviceFacade.getTicketMachineConfig(uuid);

            replyMessage.setTicketMachineConfig(ticketMachineConfig);

            return Response.status(Response.Status.OK).entity(replyMessage).build();

        } catch (Exception ex) {
            LOG.error("error calling /getHeartbeat ", ex);
            ReplyMessage replyMessage = new ReplyMessage();
            replyMessage.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            replyMessage.setDebugMessage("error calling /getTicketMachineConfig " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(replyMessage).build();
        }
    }
    
   /**
     * getMachineByStationName
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/getMachineByStationName?name=xyz
     * http://localhost:8080/projectfacadeweb/rest/stationService/getMachineByStationName?name=West%Finchley
     * 
     *
     * @return list of all machines in List<TicketMachine> replyMessage.getTicketMachineList()
     */ 
    
    @GET
    @Path("/getMachineByStationName")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMachineByStationName(@QueryParam("name") String name) {
        ReplyMessage replyMessage = new ReplyMessage();
        LOG.debug("/getMachineByStationName called  name=" + name);
        List<TicketMachine> machines = ticketMachineDAO.findByStationName(name);
        LOG.debug("/getMachineByStationName called  machines size=" + machines.size());
        //create dummy stations if empty
        if(machines.isEmpty()){
            //generate ticket machines
            machines = ticketMachineDAO.findAll();
        }
        
        replyMessage.setCode(Response.Status.OK.getStatusCode());
        replyMessage.setTicketMachineList(machines);
        return Response.status(Response.Status.OK).entity(replyMessage).build();
    }
    
    /**
     * getAllTicketMachines
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/getAllTicketMachines
    
     *
     * @return list of all machines in List<TicketMachine> replyMessage.getTicketMachineList()
     */ 
    
    @GET
    @Path("/getAllTicketMachines")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllTicketMachines() {
        ReplyMessage replyMessage = new ReplyMessage();
        //LOG.debug("/getMachineByStationName called  name=" + name);
        List<TicketMachine> machines = ticketMachineDAO.findAll();
        //LOG.debug("/getMachineByStationName called  machines size=" + machines.size());
        //create dummy stations if empty
        
        replyMessage.setCode(Response.Status.OK.getStatusCode());
        replyMessage.setTicketMachineList(machines);
        return Response.status(Response.Status.OK).entity(replyMessage).build();
    }
    
    /**
     * Get deleteAllTicketMachines
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/deleteAllTicketMachines
    
     *
     * 
     */ 
    
    @GET
    @Path("/deleteAllTicketMachines")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteAllTicketMachines() {
        ReplyMessage replyMessage = new ReplyMessage();
        //LOG.debug("/getMachineByStationName called  name=" + name);
        ticketMachineDAO.deleteAll();
        
        //check and populate list in the reply message
        List<TicketMachine> machines = ticketMachineDAO.findAll();
        replyMessage.setCode(Response.Status.OK.getStatusCode());
        replyMessage.setTicketMachineList(machines);
        return Response.status(Response.Status.OK).entity(replyMessage).build();
    }
    
    //generate ticketmachines
    
    /**
     * Get generateTicketMachineForEachStation
     *
     * http://localhost:8080/projectfacadeweb/rest/stationService/generateTicketMachineForEachStation
    
     *
     * 
     */ 
    @GET
    @Path("/generateTicketMachineForEachStation")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response generateTicketMachineForEachStation(){
        
         //ServiceFacade serviceFacade = WebObjectFactory.getServiceFacade();
         //TicketMachineDAO ticketMachineDAO = serviceFacade.getTicketMachineDAO();
         StationDAO stationDAO = serviceFacade.getStationDAO();
         List<TicketMachine> machines= new ArrayList<>();
         List<Station> stationList= stationDAO.findAll();
         
        //generate random machines
        
        for(Station s: stationList){
            Random rn = new Random();
            for(Integer i = 0; i<rn.nextInt(10); i++){
                TicketMachine t = new TicketMachine();
                t.setStation(s);
                t = ticketMachineDAO.save(t);
                machines.add(t);
            }
            
        }
        
        //check ticket machines match
        for(TicketMachine dummy: machines){
            String uuid = dummy.getUuid();
            TicketMachine found = ticketMachineDAO.findByUuid(uuid);
            System.out.println(dummy);
        }
        
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setCode(Response.Status.OK.getStatusCode());
        replyMessage.setTicketMachineList(machines);
        return Response.status(Response.Status.OK).entity(replyMessage).build();
         
    }
    
    

}
