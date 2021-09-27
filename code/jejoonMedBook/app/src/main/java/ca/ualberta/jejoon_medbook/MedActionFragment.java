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

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import ca.ualberta.jejoon_medbook.databinding.FragmentMedActionBinding;

public class MedActionFragment extends Fragment {

    private final short ADD_MODE = 1;
    private final short EDIT_MODE = 2;

    private MedBook medbook;
    private FragmentMedActionBinding binding;
    private Button addBtn;
    private Button editConfirmBtn;
    private Button deleteBtn;
    private Medicine targetMed;
    private int targetPos;
    private short mode;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMedActionBinding.inflate(inflater, container, false);

        // receive arguments from source fragment
        Bundle args = getArguments();
        medbook = (MedBook) (args != null ? args.getSerializable("medbook") : null);
        if (medbook == null) {
            medbook = new MedBook();
        }
        targetPos = args != null ? args.getInt("position") : -1;

        if (targetPos < 0) {
            // add medicine
            mode = ADD_MODE;

            addBtn = (Button) inflater.inflate(R.layout.add_button, binding.getRoot(), false);
            binding.getRoot().addView(addBtn);
        } else {
            // edit medicine
            mode = EDIT_MODE;

            ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.edit_button, binding.getRoot(), false);
            binding.getRoot().addView(cl);
            editConfirmBtn = cl.findViewById(R.id.edit_confirm_button);
            deleteBtn = cl.findViewById(R.id.delete_button);

            targetMed = medbook.getMedList().get(targetPos);
        }

        // inflate DoseUnit enum dropdown
        String[] doseUnits = Arrays.stream(DoseUnit.values()).map(DoseUnit::toString).toArray(String[]::new);
        ArrayAdapter<String> doseUnitAdapter = new ArrayAdapter<String>(requireActivity(), R.layout.dropdown_doseunit_item, doseUnits);
        binding.doseUnitAutoCompleteTextview.setAdapter(doseUnitAdapter);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText dateEditText = binding.startDateEditText;

        MaterialDatePicker<Long> datePicker;
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {

            @Override
            public void onPositiveButtonClick(Object selection) {
                String selectedDateStr = datePicker.getHeaderText();
                SimpleDateFormat fromDatePicker = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat toUser = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String stringDate = toUser.format(fromDatePicker.parse(selectedDateStr));
                    dateEditText.setText(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "start date");
            }
        });

        dateEditText.setOnClickListener(v -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "start date");
        });

        if (mode == ADD_MODE) {
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!areInputsValid()) {
                        return;
                    }
                    clearErrors();

                    // extract medicine info from user
                    String medName = String.valueOf(binding.medicineNameEditText.getText());
                    LocalDate startDate = LocalDate.parse(binding.startDateEditText.getText().toString());
                    int doseAmount = Integer.parseInt(binding.doseAmountEditText.getText().toString());
                    DoseUnit doseUnit = DoseUnit.valueOf(binding.doseUnitAutoCompleteTextview.getText().toString().toUpperCase());
                    int dailyFreq = Integer.parseInt(binding.dailyFreqEditText.getText().toString());

                    Medicine newMed = new Medicine(medName, startDate, doseAmount, doseUnit, dailyFreq);
                    medbook.addMed(newMed);

                    // send MedBook to destination fragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("medbook", medbook);

                    // navigate back to MedBookFragment
                    Navigation.findNavController(view).navigate(R.id.action_MedActionFragment_to_MedBookFragment, bundle);
                }
            });
        }
        if (mode == EDIT_MODE) {
            // set texts with targeted Medicine info
            binding.medicineNameEditText.setText(targetMed.getName());
            binding.startDateEditText.setText(targetMed.getDateStart().toString());
            binding.doseAmountEditText.setText(Integer.toString(targetMed.getDoseAmount()));
            binding.doseUnitAutoCompleteTextview.setText(targetMed.getDoseUnit().toString(), false);    // set filter to false to preserve dropdown items
            binding.dailyFreqEditText.setText(Integer.toString(targetMed.getDailyFreq()));

            editConfirmBtn.setOnClickListener(v -> {

                if (!areInputsValid()) {
                    return;
                }
                clearErrors();

                // extract medicine info from user
                String medName = String.valueOf(binding.medicineNameEditText.getText());
                LocalDate startDate = LocalDate.parse(binding.startDateEditText.getText().toString());
                int doseAmount = Integer.parseInt(binding.doseAmountEditText.getText().toString());
                DoseUnit doseUnit = DoseUnit.valueOf(binding.doseUnitAutoCompleteTextview.getText().toString().toUpperCase());
                int dailyFreq = Integer.parseInt(binding.dailyFreqEditText.getText().toString());

                // update medicine info
                targetMed.setName(medName);
                targetMed.setDateStart(startDate);
                targetMed.setDoseAmount(doseAmount);
                targetMed.setDoseUnit(doseUnit);
                targetMed.setDailyFreq(dailyFreq);

                // send MedBook to destination fragment
                Bundle args = new Bundle();
                args.putSerializable("medbook", medbook);

                // navigate back to MedBookFragment
                Navigation.findNavController(view).navigate(R.id.action_MedActionFragment_to_MedBookFragment, args);
            });

            deleteBtn.setOnClickListener(v -> {
                medbook.getMedList().remove(targetPos);

                // send MedBook to destination fragment
                Bundle args = new Bundle();
                args.putSerializable("medbook", medbook);

                // navigate back to MedBookFragment
                Navigation.findNavController(view).navigate(R.id.action_MedActionFragment_to_MedBookFragment, args);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean areInputsValid() {

        clearErrors();

        boolean valid = true;
        // null errors
        if (binding.medicineNameEditText.getText().toString().equals("")) {
            valid = false;
            binding.medicineNameTextInputLayout.setError(getString(R.string.null_error_msg));
        }
        if (binding.doseAmountEditText.getText().toString().equals("")) {
            valid = false;
            binding.doseAmountTextInputLayout.setError(getString((R.string.null_error_msg)));
        }
        if (binding.doseUnitAutoCompleteTextview.getText().toString().equals("Dose unit")) {
            valid = false;
        }
        if (binding.dailyFreqEditText.getText().toString().equals("")) {
            valid = false;
            binding.dailyFreqTextInputLayout.setError(getString((R.string.null_error_msg)));
        }

        // date parse error
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(binding.startDateEditText.getText().toString());
            // strict mode - check 30 or 31 days, leap year
            sdf.setLenient(false);
        } catch (ParseException e) {
            valid = false;
            binding.startDateTextInputLayout.setError("yyyy-mm-dd");
        }

        return valid;
    }

    public void clearErrors() {
        binding.medicineNameTextInputLayout.setError(null);
        binding.doseAmountTextInputLayout.setError(null);
        binding.dailyFreqTextInputLayout.setError(null);
        binding.startDateTextInputLayout.setError(null);

    }
}