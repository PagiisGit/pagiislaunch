package pagiisnet.pagiisnet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class GalleryBottomSheet extends BottomSheetDialogFragment {

    private GridView gridView;
    private GalleryAdaptor galleryAdapter;
    private ArrayList<String> imageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_bottom_sheet, container, false);

        gridView = view.findViewById(R.id.gridView);
        loadGalleryImages();

        return view;
    }

    private void loadGalleryImages() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                imageList.add(cursor.getString(0));
            }
            cursor.close();
        }

        galleryAdapter = new GalleryAdaptor(getContext(), imageList);
        gridView.setAdapter(galleryAdapter);
    }
}

