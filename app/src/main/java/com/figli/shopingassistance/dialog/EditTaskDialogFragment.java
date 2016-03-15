package com.figli.shopingassistance.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.figli.shopingassistance.R;
import com.figli.shopingassistance.Utils;
import com.figli.shopingassistance.database.DBHelper;
import com.figli.shopingassistance.model.ModelTask;

import java.util.Calendar;

/**
 * Created by Figli on 23.02.2016.
 */
public class EditTaskDialogFragment extends DialogFragment {

    public static EditTaskDialogFragment newInstance(ModelTask modelTask) {
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", modelTask.getTitle());
        args.putString("quantity", modelTask.getQuantity());
        args.putLong("date", modelTask.getDate());
        args.putInt("priority", modelTask.getPriority());
        args.putLong("timeStamp", modelTask.getTimeStamp());
        args.putString("description", modelTask.getDescription());

        editTaskDialogFragment.setArguments(args);
        return editTaskDialogFragment;
    }

    private EditingTaskListener editingTaskListener;

    public interface EditingTaskListener {
        void onTaskEdited(ModelTask updateTask);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editingTaskListener = (EditingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implemented EditingTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = args.getString("title");
        String quantity = args.getString("quantity");
        long date = args.getLong("date", 0);
        int priority = args.getInt("priority", 0);
        long timeStamp = args.getLong("timeStamp", 0);
        String description = args.getString("description");


        final ModelTask modelTask = new ModelTask(title, date, priority, 0, timeStamp, quantity, description);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_edit_task);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);

        final TextInputLayout textInputLayoutTitle = (TextInputLayout) container.findViewById(R.id.dialogTaskTitle);
        final AutoCompleteTextView editTextTitle = (AutoCompleteTextView) textInputLayoutTitle.getEditText();

        TextInputLayout textInputLayoutQuantity = (TextInputLayout) container.findViewById(R.id.dialogTaskQuantity);
        final EditText editTextQuantity = textInputLayoutQuantity.getEditText();

        Spinner spinnerPriority = (Spinner) container.findViewById(R.id.spinerTaskPriority);

        editTextTitle.setText(modelTask.getTitle());
        editTextTitle.setSelection(editTextTitle.length());

        editTextQuantity.setText(modelTask.getQuantity());
        editTextQuantity.setSelection(editTextQuantity.length());

        textInputLayoutTitle.setHint(getResources().getString(R.string.task_title));
        textInputLayoutQuantity.setHint(getResources().getString(R.string.dialog_product_quantity));

        builder.setView(container);


        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.priority) /*ModelTask.PRIORITY_LEVELS*/);

        spinnerPriority.setAdapter(priorityAdapter);

        spinnerPriority.setSelection(modelTask.getPriority());

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelTask.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);


        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modelTask.setTitle(editTextTitle.getText().toString());
                modelTask.setQuantity(editTextQuantity.getText().toString());

                modelTask.setStatus(ModelTask.STATUS_CURRENT);
                editingTaskListener.onTaskEdited(modelTask);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (editTextTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    textInputLayoutTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

                editTextTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            textInputLayoutTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
                            positiveButton.setEnabled(true);
                            textInputLayoutTitle.setErrorEnabled(false);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }
}
