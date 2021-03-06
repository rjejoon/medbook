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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedBook implements Serializable {

    private final ArrayList<Medicine> medList;

    public MedBook() {
        this.medList = new ArrayList<>();
    }

    public ArrayList<Medicine> getMedList() {
        return medList;
    }

    public void addMed(Medicine med) {
        medList.add(med);
    }

    public int getTotalDailyFreq() {
        int total = 0;
        for (Medicine med : medList) {
            total += med.getDailyFreq();
        }
        return total;
    }
}
