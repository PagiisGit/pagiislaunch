package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class setProfileDialogue extends AppCompatDialogFragment {

    private EditText profileName;
    private setProfileDialogueListener listener ;




    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_set_profile_dialogue_box, null);

        profileName = view.findViewById(R.id.profileNameText);

        builder.setView(view).setTitle("Set  Details").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Return To Profile Activity

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String userName = profileName.getText().toString().trim();
                listener.applyTexts(userName);


            }
        });




        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (setProfileDialogueListener) context;
        } catch (ClassCastException e) {
            throw new  ClassCastException(context +
                    "Must implement SetProfileDialogue");
        }
    }

    //Set Interface to parse Data into the Right Field.

    public interface  setProfileDialogueListener
    {
        void applyTexts(String username);

    }


}


