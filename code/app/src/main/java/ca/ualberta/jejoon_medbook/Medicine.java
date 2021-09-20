package ca.ualberta.jejoon_medbook;

import java.util.Date;

public class Medicine {
    private Date dateStart;
    private String name;
    private int doseAmount;
    private DoseUnit doseUnit;
    private int dailyFreq;

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(int doseAmount) {
        this.doseAmount = doseAmount;
    }

    public DoseUnit getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(DoseUnit doseUnit) {
        this.doseUnit = doseUnit;
    }

    public int getDailyFreq() {
        return dailyFreq;
    }

    public int setDailyFreq(int dailyFreq) {
        this.dailyFreq = dailyFreq;
    }

}
