/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com528.project.impl.dao.jpa;

import java.util.Date;
import javax.persistence.EntityManager;
import org.solent.com528.project.model.dao.PriceCalculatorDAO;
import org.solent.com528.project.model.dto.PriceBand;
import org.solent.com528.project.model.dto.PricingDetails;
import org.solent.com528.project.model.dto.Rate;

/**
 *
 * @author nedel
 */
public class PriceCalculatorDAOJPAImpl implements PriceCalculatorDAO{
    
    private EntityManager entityManager;
    
    public PriceCalculatorDAOJPAImpl(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public PricingDetails getPricingDetails() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getPricePerZone(Date startTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PricingDetails savePricingDetails(PricingDetails pricingDetails) {
        
        entityManager.getTransaction().begin();
        entityManager.persist(pricingDetails);
        entityManager.getTransaction().commit();
        return pricingDetails;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rate getRate(Date startTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPriceBand(PriceBand priceBand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePriceBand(PriceBand priceBand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getOffpeakPricePerZone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOffpeakPricePerZone(Double offpeakPricePerZone) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getPeakPricePerZone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPeakPricePerZone(Double peakPricePerZone) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
