/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.impl.service;

import java.util.Date;
import org.solent.com528.project.model.dto.Ticket;

public interface GateEntryService {

    public boolean openGate(Ticket ticket, String zonesTravelled, Date currentTime);
    
}
