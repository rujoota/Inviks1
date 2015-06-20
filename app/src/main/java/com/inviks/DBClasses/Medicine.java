package com.inviks.DBClasses;

public class Medicine
{
    private String medicineId;
    private String medicineName;
    private String shortDescription;
    private double price;
    private double composition;
    private String companyName;
    private String form;
    private int packSize;
    private int quantity;
    private String longDescription;
    private String salt;
    private String packUnit;
    private double discount;
    private String additionalInfo;
    private String saltInfo;
    private String formInfo;
    /**
     * @return the formInfo
     */
    public String getFormInfo()
    {
        return formInfo;
    }

    /**
     * @param formInfo the medicineId to set
     */
    public void setFormInfo(String formInfo)
    {
        this.formInfo = formInfo;
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
     * @return the medicineName
     */
    public String getMedicineName()
    {
        return medicineName;
    }

    /**
     * @return the shortDescription
     */
    public String getShortDescription()
    {
        return shortDescription;
    }

    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param medicineName the medicineName to set
     */
    public void setMedicineName(String medicineName)
    {
        this.medicineName = medicineName;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * @return the composition
     */
    public double getComposition()
    {
        return composition;
    }

    /**
     * @param composition the composition to set
     */
    public void setComposition(double composition)
    {
        this.composition = composition;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * @return the form
     */
    public String getForm()
    {
        return form;
    }

    /**
     * @param form the form to set
     */
    public void setForm(String form)
    {
        this.form = form;
    }

    /**
     * @return the packSize
     */
    public int getPackSize()
    {
        return packSize;
    }

    /**
     * @param packSize the packSize to set
     */
    public void setPackSize(int packSize)
    {
        this.packSize = packSize;
    }

    /**
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * @return the longDescription
     */
    public String getLongDescription()
    {
        return longDescription;
    }

    /**
     * @param longDescription the longDescription to set
     */
    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }

    /**
     * @return the salt
     */
    public String getSalt()
    {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    /**
     * @return the packUnit
     */
    public String getPackUnit()
    {
        return packUnit;
    }

    /**
     * @param packUnit the packUnit to set
     */
    public void setPackUnit(String packUnit)
    {
        this.packUnit = packUnit;
    }

    /**
     * @return the discount
     */
    public double getDiscount()
    {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    /**
     * @return the additionalInfo
     */
    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    /**
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return the saltInfo
     */
    public String getSaltInfo()
    {
        return saltInfo;
    }

    /**
     * @param saltInfo the saltInfo to set
     */
    public void setSaltInfo(String saltInfo)
    {
        this.saltInfo = saltInfo;
    }
}
