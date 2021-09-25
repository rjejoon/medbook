package ca.ualberta.jejoon_medbook;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.Objects;

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

        binding = FragmentMedbookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medbook = new MedBook();
        initMedListForDebugging();      // TODO delete after debugging

        medAdapter = new MedicineAdapter(requireActivity(), medbook.getMedList());
        listview = binding.medsListview;
        listview.setAdapter(medAdapter);

        binding.addMedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Debug", String.valueOf(medAdapter));
        listview.setAdapter(medAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // TODO method to be erased later
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initMedListForDebugging() {
        for (int i=0; i<15; i++) {
            medbook.addMed("Finastride" + i, LocalDate.of(2020, 8, 28), 5, DoseUnit.MG, 2);
        }
    }


}