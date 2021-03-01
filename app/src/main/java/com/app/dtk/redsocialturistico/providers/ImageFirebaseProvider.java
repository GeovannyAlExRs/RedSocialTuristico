package com.app.dtk.redsocialturistico.providers;

import android.content.Context;

import com.app.dtk.redsocialturistico.utils.CompressorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageFirebaseProvider {

    StorageReference storageReference;

    public ImageFirebaseProvider() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask createImg(Context context, File file) {
        byte[] imgByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500);

        StorageReference storage = FirebaseStorage.getInstance().getReference().child("BGM_"+ new Date() + ".jpg");
        storageReference = storage;

        return storage.putBytes(imgByte);
    }

    public StorageReference getStorageURL() {
        return storageReference;
    }
}
