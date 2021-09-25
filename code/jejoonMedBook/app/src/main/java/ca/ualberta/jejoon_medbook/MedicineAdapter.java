package ca.ualberta.jejoon_medbook;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        TextView startDate = view.findViewById(R.id.start_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        nameAmountUnit.setText(String.format(Locale.ENGLISH,
                                      "%s %d%s",
                                              med.getName(), med.getDoseAmount(), med.getDoseUnit()));
        String dailyFreqStr = String.format(Locale.ENGLISH,
                                     "%d %s everyday",
                                            med.getDailyFreq(), (med.getDailyFreq() > 1) ? "doses" : "dose");
        dailyFreq.setText(dailyFreqStr);
        startDate.setText(String.format(Locale.ENGLISH, "since %s", med.getDateStart().format(formatter)));

        return view;
    }
}
