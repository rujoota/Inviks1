package com.inviks.DBClasses;

public class MedicinesInCart
{
    private String medicineName;
    private double price;
    private double total;
    private String medicineId;
    private String orderId;
    private int qtyOrdered;
    private int qtyAvailable;

    /**
     * @return the medicineName
     */
    public String getMedicineName()
    {
        return medicineName;
    }

    /**
     * @param medicineName the medicineName to set
     */
    public void setMedicineName(String medicineName)
    {
        this.medicineName = medicineName;
    }

    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * @return the total
     */
    public double getTotal()
    {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total)
    {
        this.total = total;
    }

    /**
     * @return the medicineId
     */
    public String getMedicineId()
    {
        return medicineId;
    }

    /**
     * @param medicineId the medicineId to set
     */
    public void setMedicineId(String medicineId)
    {
        this.medicineId = medicineId;
    }

    /**
     * @return the orderId
     */
    public String getOrderId()
    {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    /**
     * @return the qty_ordered
     */
    public int getQtyOrdered()
    {
        return qtyOrdered;
    }

    /**
     * @param qty_ordered the qty_ordered to set
     */
    public void setQtyOrdered(int qty_ordered)
    {
        this.qtyOrdered = qty_ordered;
    }


    /**
     * @return the qtyAvailable
     */
    public int getQtyAvailable()
    {
        return qtyAvailable;
    }

    /**
     * @param qtyAvailable the qtyAvailable to set
     */
    public void setQtyAvailable(int qtyAvailable)
    {
        this.qtyAvailable = qtyAvailable;
    }
}

