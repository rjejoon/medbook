package ca.ualberta.jejoon_medbook;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;

public class MedBook {
    private ArrayList<Medicine> medList = new ArrayList<>();

    public MedBook() {

    }

    public ArrayList<Medicine> getMedList() {
        return medList;
    }

    public boolean addMed(String name, LocalDate dateStart, int doseAmount, DoseUnit doseUnit, int dailyFreq) {
        Medicine med = new Medicine(name, dateStart, doseAmount, doseUnit, dailyFreq);
        medList.add(med);

        return true;
    }


}
