package com.gamepari.hungryadventure;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.assets.AssetImageTask;
import com.gamepari.hungryadventure.contents.ModelFood;

/**
 * Created by gamepari on 2/17/15.
 */
public class UnlockFragment extends DialogFragment {


    private UnlockDialogListener mUnlockDialogListener;
    private ModelFood mFood;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mUnlockDialogListener = (UnlockDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UnlockDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFood = (ModelFood) getArguments().getSerializable("food");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View v = inflater.inflate(R.layout.dialog_unlock, null);

        TextView tvStep = (TextView) v.findViewById(R.id.tv_unlock);
        tvStep.setText(String.valueOf(mFood.getmRequiredStepCount()));

        ImageView ivThumb = (ImageView) v.findViewById(R.id.iv_lockedcity);

        new AssetImageTask(getActivity(), ivThumb).execute(mFood.getmAssetImagePath());

        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.unlock, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mUnlockDialogListener.onDialogPositiveClick(mFood);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UnlockFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();

    }

    public interface UnlockDialogListener {
        public void onDialogPositiveClick(ModelFood food);

        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
