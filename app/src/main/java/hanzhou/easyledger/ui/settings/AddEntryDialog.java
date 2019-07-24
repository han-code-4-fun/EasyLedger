package hanzhou.easyledger.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;

public class AddEntryDialog  extends AppCompatDialogFragment {
    private AppCompatActivity mAppCompatActivity;

    private EditText mEditText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mAppCompatActivity);
        View view = LayoutInflater.
                from(mAppCompatActivity).
                inflate(R.layout.dialog_add_entry, null);

        mEditText = view.findViewById(R.id.setting_dialog_add_entry_editText_id);
        builder.setView(view)
                .setNegativeButton(getString(R.string.dialog_negative_btn_title), cancelBTNClickListener)
                .setPositiveButton(getString(R.string.dialog_positive_btn_title),  saveBtnClickListener );

        return builder.create();
    }

    private DialogInterface.OnClickListener cancelBTNClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            Toast.makeText(mAppCompatActivity, "No Entry Added", Toast.LENGTH_LONG).show();
        }
    };

    private DialogInterface.OnClickListener saveBtnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent();
            intent.putExtra(Constant.CATEGORY_ADD, mEditText.getText().toString());

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

        }
    };
}
