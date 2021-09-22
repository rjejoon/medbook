package ca.ualberta.jejoon_medbook;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class Medicine {
    private String name;
    private LocalDate dateStart;
    private int doseAmount;
    private DoseUnit doseUnit;
    private int dailyFreq;

    public Medicine(String name, LocalDate dateStart, int doseAmount, DoseUnit doseUnit, int dailyFreq) {
        this.name = name;
        this.dateStart = dateStart;
        this.doseAmount = doseAmount;
        this.doseUnit = doseUnit;
        this.dailyFreq = dailyFreq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // TODO upto 40 chars
        this.name = name;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
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

    public void setDailyFreq(int dailyFreq) {
        this.dailyFreq = dailyFreq;
    }

    @Override
    public String toString() {
        return String.format("%s %d%s  %d daily, since %s", name, doseAmount, doseUnit, dailyFreq, dateStart);
    }
}
