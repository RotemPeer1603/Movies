package com.rotem.movies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ChooseUrlFragment extends DialogFragment
{
    TextView txtImageUrl = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo));

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_choose_image, null);

        txtImageUrl = view.findViewById(R.id.txtImageUrl);

        String imageUrl = getArguments().getString("imageUrl");

        txtImageUrl.setText(imageUrl);

        TextView btnOk = view.findViewById(R.id.btn_ok);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);


        btnOk.setOnClickListener(new ButtonConfirmListener());
        btnCancel.setOnClickListener(new ButtonCancelListener());


        builder.setView(view);

        dialog = builder.create();


        return dialog;
    }

    public void onResume()
    {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        // window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }


    private class ButtonConfirmListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            String imageUrl = txtImageUrl.getText().toString();
            ((UpdateMovieActivity) getActivity()).updateImageUrl(imageUrl);
            dismiss();
        }
    }

    private class ButtonCancelListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            dismiss();
        }

    }
}
