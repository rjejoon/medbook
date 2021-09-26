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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private final ArrayList<Medicine> medList;
    private final Context context;

    public MedicineAdapter(@NonNull Context context, @NonNull ArrayList<Medicine> medList) {
        super(context, 0, medList);
        this.medList = medList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.medicine_item, parent, false);
        }
        Medicine med = medList.get(position);

        TextView nameAmountUnit = view.findViewById(R.id.medine_name_with_dose_amount_and_unit);
        TextView dailyFreq = view.findViewById(R.id.daily_frequency);

        nameAmountUnit.setText(String.format(Locale.ROOT,
                                      "%s %d%s",
                                             med.getName(), med.getDoseAmount(), med.getDoseUnit()));
        String dailyFreqStr = String.format(Locale.ROOT,
                                     "%d %s everyday",
                                            med.getDailyFreq(), (med.getDailyFreq() > 1) ? "doses" : "dose");
        dailyFreq.setText(dailyFreqStr);

        return view;
    }
}
