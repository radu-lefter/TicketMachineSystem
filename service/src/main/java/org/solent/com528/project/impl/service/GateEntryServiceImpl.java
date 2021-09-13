/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.impl.service;

import solent.ac.uk.com504.examples.ticketgate.crypto.AsymmetricCryptography;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import javax.crypto.NoSuchPaddingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com528.project.model.dto.Ticket;
import org.solent.com528.project.impl.service.GateEntryService;

/**
 *
 * @author cgallen
 */
public class GateEntryServiceImpl implements GateEntryService {

    final static Logger LOG = LogManager.getLogger(GateEntryServiceImpl.class);

    private String publicKeyFileOnClasspath = null;

    public GateEntryServiceImpl(String publicKeyFileOnClasspath) {
        this.publicKeyFileOnClasspath = publicKeyFileOnClasspath;
    }

    @Override
    public boolean openGate(Ticket ticket, String zonesTravelledStr, Date currentTime) {
        LOG.debug("openGate called with zonesTravelled= " + zonesTravelledStr
                + "ticket=" + ticket);

        try {

            AsymmetricCryptography ac = new AsymmetricCryptography();
            PublicKey publicKey = ac.getPublicFromClassPath(publicKeyFileOnClasspath);
            String encodedKey = ticket.getEncryptedHash();
            String decrypted_content = ac.decryptText(encodedKey, publicKey);

            // check if zones travelled greater than number of zones on ticket
            int zonesTravelled = Integer.parseInt(zonesTravelledStr);
            int zones = Integer.parseInt(ticket.getZones());

            if (zonesTravelled > zones ) {
                return false;
            }
            
            long currentTimeMs = currentTime.getTime();
            long validFromMs = ticket.getValidFrom().getTime();
            long validToMs = ticket.getValidTo().getTime();
            
            if(currentTimeMs < validFromMs ){
                return false;
            }
            if(currentTimeMs > validToMs ){
                return false;
            }

            // check if decoded string matches ticket content
            return (ticket.getContent().equals(decrypted_content));

        } catch (Exception ex) {
            throw new RuntimeException("problem verifying ticket=" + ticket, ex);
        }

    }

}
