/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.model.dto;

import java.io.StringWriter;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.solent.com528.project.model.util.DateTimeAdapter;

/**
 * THIS IS A VERY BASIC TICKET - YOU WILL NEED TO IMPROVE THIS
 *
 * @author cgallen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ticket {
    
    private String zones;

    private String startStation;

    private Double cost;

    private String encryptedHash;

    private Rate rate;

    private Date issueDate;
    
    
    // this makes sure we use a common format for marshalling date
    @XmlElement(name = "validFrom")
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date validFrom;

    @XmlElement(name = "validTo")
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date validTo;
    

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getEncryptedHash() {
        return encryptedHash;
    }

    public void setEncryptedHash(String encryptedHash) {
        this.encryptedHash = encryptedHash;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    
    public String getZones() {
        return zones;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }
    
    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    
    /**
     * serialise ticket as xml 
    */
    public String toXML() {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("org.solent.com528.project.model.dto");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw1 = new StringWriter();
            jaxbMarshaller.marshal(this, sw1);
            return sw1.toString();
        } catch (Exception ex) {
            throw new RuntimeException("problem marshalling ticket", ex);
        }
    }

    @Override
    public String toString() {
        return "Ticket{" + "startStation=" + startStation + ", cost=" + cost + ", encryptedHash=" + encryptedHash + ", rate=" + rate + ", issueDate=" + issueDate + '}';
    }



}
