package org.solent.com528.project.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReplyMessage {

    private Integer code;

    private String debugMessage;

    private TicketMachineConfig ticketMachineConfig;
    
    private List<TicketMachine> ticketMachineList;

    public List<TicketMachine> getTicketMachineList() {
        return ticketMachineList;
    }

    public void setTicketMachineList(List<TicketMachine> ticketMachineList) {
        this.ticketMachineList = ticketMachineList;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public TicketMachineConfig getTicketMachineConfig() {
        return ticketMachineConfig;
    }

    public void setTicketMachineConfig(TicketMachineConfig ticketMachineConfig) {
        this.ticketMachineConfig = ticketMachineConfig;
    }

    
    
    
    @Override
    public String toString() {
        return "ReplyMessage{" + "code=" + code + ", debugMessage=" + debugMessage + ", TicketMachineConfig=" + ticketMachineConfig + '}';
    }
    
}
