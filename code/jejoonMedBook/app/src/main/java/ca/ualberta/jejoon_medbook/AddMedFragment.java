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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import ca.ualberta.jejoon_medbook.databinding.FragmentAddmedBinding;

public class AddMedFragment extends Fragment {

    private final short ADD_MODE = 1;
    private final short EDIT_MODE = 2;

    private MedBook medbook;
    private FragmentAddmedBinding binding;
    private OnFragmentInteractionListener listener;
    private ArrayAdapter<String> doseUnitAdapter;
    private Button addBtn;
    private Button editConfirmBtn;
    private Button deleteBtn;
    private Bundle args;
    private Medicine targetMed;
    private int targetPos;
    private short mode;



    public interface OnFragmentInteractionListener {
        void onOkPressed(Medicine newMed);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddmedBinding.inflate(inflater, container, false);

        args = getArguments();
        medbook = (MedBook) (args != null ? args.getSerializable("medbook") : null);
        if (medbook == null) {
            medbook = new MedBook();
        }
        targetPos = args.getInt("position");

        if (targetPos < 0) {
            // inflate add medicine button
            addBtn = (Button) inflater.inflate(R.layout.add_button, binding.getRoot(), false);
            binding.getRoot().addView(addBtn);

            mode = ADD_MODE;
        } else {
            // inflate edit confirm btn and delete btn
            ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.edit_button, binding.getRoot(), false);
            binding.getRoot().addView(cl);
            editConfirmBtn = cl.findViewById(R.id.edit_confirm_button);
            deleteBtn = cl.findViewById(R.id.delete_button);

            mode = EDIT_MODE;
            targetMed = medbook.getMedList().get(targetPos);
        }

        String[] doseUnits = Arrays.stream(DoseUnit.values()).map(DoseUnit::toString).toArray(String[]::new);
        doseUnitAdapter = new ArrayAdapter<String>(requireActivity(), R.layout.dropdown_med_item, doseUnits);

        binding.doseUnitAutoCompleteTextview.setAdapter(doseUnitAdapter);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText editText = binding.startDateEditText;

        MaterialDatePicker<Long> datePicker;
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {

            @Override
            public void onPositiveButtonClick(Object selection) {
                String selectedDate = datePicker.getHeaderText();
                SimpleDateFormat fromDatePicker = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat toUser = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String stringDate = toUser.format(fromDatePicker.parse(selectedDate));
                    editText.setText(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        editText.setOnClickListener((v) -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "date");
        });

        if (mode == ADD_MODE) {
            addBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    String medName = String.valueOf(binding.medicineNameEditText.getText());
                    LocalDate startDate = LocalDate.parse(binding.startDateEditText.getText().toString());
                    int doseAmount = Integer.parseInt(binding.doseAmountEditText.getText().toString());
                    DoseUnit doseUnit = DoseUnit.valueOf(binding.doseUnitAutoCompleteTextview.getText().toString().toUpperCase());
                    int dailyFreq = Integer.parseInt(binding.dailyFreqEditText.getText().toString());

                    Medicine newMed = new Medicine(medName, startDate, doseAmount, doseUnit, dailyFreq);
                    medbook.addMed(newMed);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("medbook", medbook);
                    // navigate back to MedBookFragment
                    Navigation.findNavController(view).navigate(R.id.action_SecondFragment_to_FirstFragment, bundle);
                }
            });
        }
        if (mode == EDIT_MODE) {
            // set texts of all EditTexts with target Medicine
            binding.medicineNameEditText.setText(targetMed.getName());
            binding.startDateEditText.setText(targetMed.getDateStart().toString());
            binding.doseAmountEditText.setText(Integer.toString(targetMed.getDoseAmount()));
            binding.doseUnitAutoCompleteTextview.setText(targetMed.getDoseUnit().toString(), false);    // set filter to false to preserve dropdown items
            binding.dailyFreqEditText.setText(Integer.toString(targetMed.getDailyFreq()));

            editConfirmBtn.setOnClickListener(v -> {
                String medName = String.valueOf(binding.medicineNameEditText.getText());
                LocalDate startDate = LocalDate.parse(binding.startDateEditText.getText().toString());
                int doseAmount = Integer.parseInt(binding.doseAmountEditText.getText().toString());
                DoseUnit doseUnit = DoseUnit.valueOf(binding.doseUnitAutoCompleteTextview.getText().toString().toUpperCase());
                int dailyFreq = Integer.parseInt(binding.dailyFreqEditText.getText().toString());

                targetMed.setName(medName);
                targetMed.setDateStart(startDate);
                targetMed.setDoseAmount(doseAmount);
                targetMed.setDoseUnit(doseUnit);
                targetMed.setDailyFreq(dailyFreq);

                Bundle args = new Bundle();
                args.putSerializable("medbook", medbook);

                // navigate back to MedBookFragment
                Navigation.findNavController(view).navigate(R.id.action_SecondFragment_to_FirstFragment, args);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}