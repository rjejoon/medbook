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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ca.ualberta.jejoon_medbook.databinding.FragmentMedbookBinding;

public class MedBookFragment extends Fragment {

    private FragmentMedbookBinding binding;

    private MedBook medbook;
    private MedicineAdapter medAdapter;
    private ListView medsListView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // receieve Arguments from source fragment
        Bundle args = getArguments();
        medbook = (MedBook) (args != null ? args.getSerializable("medbook") : null);
        if (medbook == null) {
            medbook = new MedBook();
        }

        binding = FragmentMedbookBinding.inflate(inflater, container, false);
        medsListView = binding.medsListview;

        medAdapter = new MedicineAdapter(requireActivity(), medbook.getMedList());

        if (!medbook.getMedList().isEmpty()) {
            // include total daily freq view only if there is at least one medicine in the list
            TextView totalDailyFreqTextView = (TextView) inflater.inflate(R.layout.total_daily_freq, medsListView, false);
            totalDailyFreqTextView.setText("Total daily doses: " + medbook.getTotalDailyFreq());
            medsListView.addFooterView(totalDailyFreqTextView);
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medsListView.setAdapter(medAdapter);

        // edit & delete
        medsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("medbook", medbook);
                args.putInt("position", i);
                Navigation.findNavController(view)
                        .navigate(R.id.action_MedBookFragment_to_MedActionFragment, args);       // navigate to MedActionFragment
            }
        });

        // add
        binding.addMedFab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("medbook", medbook);
            args.putInt("position", -1);        // pos: -1 tells MedActionFragment to add a new medicine

            Navigation.findNavController(view)
                    .navigate(R.id.action_MedBookFragment_to_MedActionFragment, args);       // navigate to MedActionFragment
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
