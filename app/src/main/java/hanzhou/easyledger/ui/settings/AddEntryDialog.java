package hanzhou.easyledger.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;



/*
 *
 *   Dialog for handling adding category/ledger
 *
 * */

public class AddEntryDialog extends AppCompatDialogFragment {
    private AppCompatActivity mAppCompatActivity;

    private TextInputEditText mEditText;
    private TextInputLayout mTextInputLayout;
    private ArrayList<String> mList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mList = getArguments().getStringArrayList(Constant.SETTING_BUNDLE_LIST_OF_NAMES);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mAppCompatActivity);
        View view = LayoutInflater.
                from(mAppCompatActivity).
                inflate(R.layout.dialog_add_entry, null);

        mTextInputLayout = view.findViewById(R.id.setting_dialog_add_entry_textinputlayout_id);

        mEditText = view.findViewById(R.id.setting_dialog_add_entry_editText_id);

        builder.setView(view)
                .setNegativeButton(getString(R.string.dialog_negative_btn_title), cancelBTNClickListener)
                .setPositiveButton(getString(R.string.dialog_positive_btn_title), null);


        final AlertDialog dialog = builder.create();


        /*override default positiveButton action to do validation check*/
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateUserInput()) {
                            Intent intent = new Intent();
                            intent.putExtra(Constant.CATEGORY_ADD, mEditText.getText().toString());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


        return dialog;
    }

    private boolean validateUserInput() {

        String input = mEditText.getText().toString();
        if (input.trim().equals("")) {
            mTextInputLayout.setError(getString(R.string.setting_warning_msg_empty_string));
        } else if (mList.contains(input)) {
            mTextInputLayout.setError(getString(R.string.setting_warning_msg_duplicate_word));
        } else {
            mTextInputLayout.setError(null);
            return true;
        }
        return false;
    }

    private DialogInterface.OnClickListener cancelBTNClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            Toast.makeText(mAppCompatActivity, getString(R.string.setting_toast_msg_no_enter_added), Toast.LENGTH_LONG).show();
        }
    };


}
