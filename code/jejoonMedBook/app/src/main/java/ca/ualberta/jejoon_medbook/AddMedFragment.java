package ca.ualberta.jejoon_medbook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
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
import java.util.Date;

import ca.ualberta.jejoon_medbook.databinding.FragmentAddmedBinding;

public class AddMedFragment extends Fragment {

    private String selectedDate;
    private FragmentAddmedBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddmedBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText editText = binding.startDateTextField.findViewById(R.id.start_date_editText);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
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

        editText.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "date");
                editText.setHint("yyyy-mm-dd");

            } else {
                editText.setHint("");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}