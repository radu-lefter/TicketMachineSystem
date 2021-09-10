/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.impl.service;

import org.solent.com528.project.impl.service.GateManagementService;
import org.solent.com528.project.impl.service.GateEntryService;


/**
 *
 * @author cgallen
 */
public class ServiceFactoryImpl  {

    private static GateEntryService gateEntryService = null;

    private static GateManagementService gateManagementService = null;
    
    // these should match the generated file names in pom.xml
    private static final String PUBLIC_KEY_FILE="publicKey";
    private static final String PRIVATE_KEY_FILE="privateKey";

    public static GateEntryService getGateEntryService() {
        if (gateEntryService == null) {
            synchronized (ServiceFactoryImpl.class) {
                if (gateEntryService == null) {
                    String publicKeyFileOnClasspath=PUBLIC_KEY_FILE;
                    gateEntryService = new GateEntryServiceImpl(publicKeyFileOnClasspath);
                }
            }
        }
        return gateEntryService;
    }


    public static GateManagementService getGateManagementService() {
        if (gateManagementService == null) {
            synchronized (ServiceFactoryImpl.class) {
                if (gateManagementService == null) {
                    String privateKeyFileOnClasspath= PRIVATE_KEY_FILE;
                    gateManagementService = new GateManagementServiceImpl(privateKeyFileOnClasspath);
                }
            }
        }
        return gateManagementService;
    }

}
