package ca.ualberta.jejoon_medbook;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDate;

import ca.ualberta.jejoon_medbook.databinding.FragmentMedbookBinding;

public class MedBookFragment extends Fragment {

    private FragmentMedbookBinding binding;

    private MedBook medbook;
    private MedicineAdapter medAdapter;
    private ListView listview;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Bundle args = getArguments();
        medbook = (MedBook) (args != null ? args.getSerializable("medbook") : null);
        if (medbook == null) {
            medbook = new MedBook();
        }

        binding = FragmentMedbookBinding.inflate(inflater, container, false);
        listview = binding.medsListview;

        TextView totalDailyFreqTextView = (TextView) inflater.inflate(R.layout.total_daily_freq, listview, false);

        medAdapter = new MedicineAdapter(requireActivity(), medbook.getMedList());

        if (!medbook.getMedList().isEmpty()) {
            totalDailyFreqTextView.setText("Total daily doses: " + medbook.getTotalDailyFreq());
            listview.addFooterView(totalDailyFreqTextView);
        }

        Log.d("initFrag", medbook.getMedList().toString());

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listview.setAdapter(medAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Medicine targetMed = medAdapter.getItem(i);
                Bundle args = new Bundle();
                args.putSerializable("medbook", medbook);
                args.putInt("position", i);
                Navigation.findNavController(view)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, args);
            }
        });

        binding.addMedFab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("medbook", medbook);
            args.putInt("position", -1);        // pos: -1 indicates the destination fragment that it needs to add new medicine

            Navigation.findNavController(view)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, args);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // TODO method to be erased later
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initMedListForDebugging() {
        for (int i=0; i<9; i++) {
            medbook.addMed("cetirizine hydrochloride", LocalDate.of(2020, 9, 25), 5, DoseUnit.MG, 1);
        }
    }

}
