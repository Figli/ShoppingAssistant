package com.figli.shopingassistance.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TimePicker;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.figli.shopingassistance.MainActivity;
import com.figli.shopingassistance.R;
import com.figli.shopingassistance.Utils;
import com.figli.shopingassistance.adapter.TaskAdapter;
import com.figli.shopingassistance.database.DBHelper;
import com.figli.shopingassistance.database.DBQueryManager;
import com.figli.shopingassistance.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Figli on 11.02.2016.
 */
public class AddingTaskDialogFragment extends DialogFragment {

    private AddingTaskListener addingTaskListener;
    private DBHelper dbHelper;

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask newTask);
        void onTaskAddingCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            addingTaskListener = (AddingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "implemet AddingTaskListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_tittle);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);

        dbHelper = new DBHelper(getActivity());
        String[] allTasks = dbHelper.getAllTasks();

        final TextInputLayout textInputLayoutTitle = (TextInputLayout) container.findViewById(R.id.dialogTaskTitle);
//        final AutoCompleteTextView editTextTitle = (AutoCompleteTextView) textInputLayoutTitle.getEditText();
        final AutoCompleteTextView editTextTitle = (AutoCompleteTextView) container.findViewById(R.id.dialogAutoComplite);



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allTasks);

        editTextTitle.setAdapter(arrayAdapter);
        editTextTitle.setThreshold(1);

        TextInputLayout textInputLayoutQuantity = (TextInputLayout) container.findViewById(R.id.dialogTaskQuantity);
        final EditText editTextQuantity = textInputLayoutQuantity.getEditText();

        Spinner spinnerPriority = (Spinner) container.findViewById(R.id.spinerTaskPriority);

        textInputLayoutTitle.setHint(getResources().getString(R.string.task_title));
        textInputLayoutQuantity.setHint(getResources().getString(R.string.dialog_product_quantity));

        builder.setView(container);

        final ModelTask modelTask = new ModelTask();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.priority) /*ModelTask.PRIORITY_LEVELS*/);

        spinnerPriority.setAdapter(priorityAdapter);

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
                addingTaskListener.onTaskAdded(modelTask);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingTaskListener.onTaskAddingCancel();
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
