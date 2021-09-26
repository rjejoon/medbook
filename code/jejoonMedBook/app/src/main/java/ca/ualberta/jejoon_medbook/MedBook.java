package ca.ualberta.jejoon_medbook;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedBook implements Serializable {
    private ArrayList<Medicine> medList;

    public MedBook() {
        this.medList = new ArrayList<>();
    }

    public ArrayList<Medicine> getMedList() {
        return medList;
    }

    public void addMed(String name, LocalDate dateStart, int doseAmount, DoseUnit doseUnit, int dailyFreq) {
        Medicine med = new Medicine(name, dateStart, doseAmount, doseUnit, dailyFreq);
        medList.add(med);
    }

    public void addMed(Medicine med) {
        medList.add(med);
    }


}
