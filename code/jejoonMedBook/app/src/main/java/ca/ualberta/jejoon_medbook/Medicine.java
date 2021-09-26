/*
Copyright 2021 Jejoon Ryu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package ca.ualberta.jejoon_medbook;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Medicine implements Serializable {
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
