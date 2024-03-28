package pagiisnet.pagiisnet.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import pagiisnet.pagiisnet.R;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity().getApplicationContext(), R.style.BottomSheetDialogueTheme);

        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.sirocco_expore, view.findViewById(R.id.siroccoBottomSheetContainer));

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

}
