package com.app.dtk.redsocialturistico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.model.Users;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.ImageFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.UsersFirestoreProvider;
import com.app.dtk.redsocialturistico.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imageViewBack, circleBackUser;
    private ImageView img_Portada;
    private TextInputEditText txt_nameUser, txt_phone;
    private AppCompatButton btn_update;

    private AlertDialog.Builder builderSelect;
    //private ProgressBar progressBar;
    //private LinearLayoutCompat linearLayoutCompat;

    // Foto 1
    String absolutePhotoPath1;
    String photoPath1;
    File filePhoto1;
    CharSequence options[];

    // Foto 2
    String absolutePhotoPath2;
    String photoPath2;
    File filePhoto2;

    private static final int NUM_GALLERY_REQUEST_CODE_USER = 1;
    private static final int NUM_GALLERY_REQUEST_CODE_PORTADA = 2;
    private static final int NUM_CAMERA_REQUEST_CODE_USER = 3;
    private static final int NUM_CAMERA_REQUEST_CODE_PORTADA = 4;

    File fileImage1;
    File fileImage2;

    String nameUser = "";
    String phone = "";
    String imgUrlPerfil = "";
    String imgUrlCover = "";

    ImageFirebaseProvider imageFirebaseProvider;
    UsersFirestoreProvider usersFirestoreProvider;
    AuthFirebaseProvider authFirebaseProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        builderSelect = new AlertDialog.Builder(this);
        builderSelect.setTitle("Selecciona una opcion");
        options = new CharSequence[]{"Imagen de galeria", "Tomar una foto"};

        getViewId();

        imageFirebaseProvider = new ImageFirebaseProvider();
        usersFirestoreProvider = new UsersFirestoreProvider();
        authFirebaseProvider = new AuthFirebaseProvider();

        getUsers();
    }

    private void getViewId() {
        imageViewBack = findViewById(R.id.id_circleBack);
        imageViewBack.setOnClickListener(this);

        img_Portada = findViewById(R.id.id_img_Portada);
        img_Portada.setOnClickListener(this);

        circleBackUser= findViewById(R.id.id_circleBackUser);
        circleBackUser.setOnClickListener(this);

        txt_nameUser = findViewById(R.id.id_txt_nameUser);
        txt_phone = findViewById(R.id.id_txt_phone);

        btn_update = findViewById(R.id.id_btn_update);
        btn_update.setOnClickListener(this);

        /*progressBar = findViewById(R.id.id_spinkit_progress);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SCREEN);

        linearLayoutCompat = findViewById(R.id.id_linearLayout_transparent);
        linearLayoutCompat.setVisibility(View.INVISIBLE);*/
    }

    @Override
    public void onClick(View view) {
        int option1 = 1;
        int option2 = 2;
        switch (view.getId()) {
            case R.id.id_circleBack:
                //goToView(LoginActivity.class); goRegister();
                finish();
                break;
            case R.id.id_circleBackUser:
                selectOptionImg(option1);
                break;
            case R.id.id_img_Portada:
                selectOptionImg(option2);
                break;
            case R.id.id_btn_update:
                goUpdate();
                break;
        }
    }

    private void selectOptionImg(final int numbreImg) {
        builderSelect.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (numbreImg == 1) {
                        openGallery(NUM_GALLERY_REQUEST_CODE_USER);
                    } else if (numbreImg == 2) {
                        openGallery(NUM_GALLERY_REQUEST_CODE_PORTADA);
                    }
                } else if (i == 1) {
                    if (numbreImg == 1) {
                        cameraImg(NUM_CAMERA_REQUEST_CODE_USER);
                    } else if (numbreImg == 2) {
                        cameraImg(NUM_CAMERA_REQUEST_CODE_PORTADA);
                    }
                }
            }
        });
        builderSelect.show();
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    private void cameraImg(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoF = null;
            try {
                photoF = createPhotoFile(requestCode);
            } catch (Exception e) {
                Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoF != null) {
                Uri uri = FileProvider.getUriForFile(EditPerfilActivity.this, "com.app.dtk.redsocialturistico", photoF);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File fileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file = File.createTempFile(new Date() + "_photo", ".jpg", fileDir);

        if (requestCode == NUM_CAMERA_REQUEST_CODE_USER) {
            photoPath1 = "file: " + file.getAbsolutePath();
            absolutePhotoPath1 = file.getAbsolutePath();
        } else if (requestCode == NUM_CAMERA_REQUEST_CODE_PORTADA) {
            photoPath2 = "file: " + file.getAbsolutePath();
            absolutePhotoPath2 = file.getAbsolutePath();
        }

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SELECCION DE IMAGEN DESDE GALERIA
        if (requestCode == NUM_GALLERY_REQUEST_CODE_USER && resultCode == RESULT_OK) {
            try {
                filePhoto1 = null;
                fileImage1 = FileUtil.from(this, data.getData());
                circleBackUser.setImageBitmap(BitmapFactory.decodeFile(fileImage1.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Error en " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == NUM_GALLERY_REQUEST_CODE_PORTADA && resultCode == RESULT_OK) {
            try {
                filePhoto2 = null;
                fileImage2 = FileUtil.from(this, data.getData());
                img_Portada.setImageBitmap(BitmapFactory.decodeFile(fileImage2.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Error en " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        // SELECCION DE IMAGEN DESDE CAMARA
        if (requestCode == NUM_CAMERA_REQUEST_CODE_USER && resultCode == RESULT_OK) {
            fileImage1 = null;
            filePhoto1 = new File(absolutePhotoPath1);
            Picasso.with(EditPerfilActivity.this).load(photoPath1).into(circleBackUser);
        }

        if (requestCode == NUM_CAMERA_REQUEST_CODE_PORTADA && resultCode == RESULT_OK) {
            fileImage2 = null;
            filePhoto2 = new File(absolutePhotoPath2);
            Picasso.with(EditPerfilActivity.this).load(photoPath2).into(img_Portada);
        }
    }

    private void goUpdate() {
        nameUser = txt_nameUser.getText().toString();
        phone = txt_phone.getText().toString();

        if(!nameUser.isEmpty() && !phone.isEmpty()) {
            // Seleccion de ambas imagenes de galeria MIN 07:32
            if (fileImage1 != null && fileImage2 != null) {
                saveImageStoragePerfilAndCover(fileImage1, fileImage2);
            }
            // Seleccion de ambas imagenes de camara
            else if (filePhoto1 != null && filePhoto2 != null) {
                saveImageStoragePerfilAndCover(filePhoto1, filePhoto2);
            }
            // Seleccion de Imagen de galeria y camara
            else if (fileImage1 != null && filePhoto2 != null) {
                saveImageStoragePerfilAndCover(fileImage1, filePhoto2);
            }
            // Seleccion de Imagen de camara y galeria
            else if (filePhoto1 != null && fileImage2 != null) {
                saveImageStoragePerfilAndCover(filePhoto1, fileImage2);
            }
            // Seleccion de imagen de CAMARA
            else if (filePhoto1 != null) {
                saveImage(filePhoto1, true);
            }
            // Seleccion de imagen de CAMARA
            else if (filePhoto2 != null) {
                saveImage(filePhoto2, false);
            }
            // Seleccion de imagen de GALERIA
            else if (fileImage1 != null) {
                saveImage(fileImage1, true);
            }
            // Seleccion de imagen de GALERIA
            else if (fileImage2 != null) {
                saveImage(fileImage2, false);
            }
            else {
                Users u = new Users();

                u.setId_users(authFirebaseProvider.getFirebaseUid());
                u.setUsername(nameUser);
                u.setPhone(phone);

                u.setImg_perfil(imgUrlPerfil);
                u.setImg_cover(imgUrlCover);

                updateUserstFirestore(u);
            }
        } else {
            Toast.makeText(this, "Ingresa todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(File file, final boolean isImgPerfil) {
        //linearLayoutCompat.setVisibility(View.VISIBLE);
        imageFirebaseProvider.createImg(this, file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageFirebaseProvider.getStorageURL().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            Users u = new Users();

                            u.setId_users(authFirebaseProvider.getFirebaseUid());
                            u.setUsername(nameUser);
                            u.setPhone(phone);

                            if (isImgPerfil) {
                                u.setImg_perfil(url);
                                u.setImg_cover(imgUrlCover);
                            } else {
                                u.setImg_cover(url);
                                u.setImg_perfil(imgUrlPerfil);
                            }

                            updateUserstFirestore(u);
                        }
                    });
                    Toast.makeText(EditPerfilActivity.this, "Guardo la imagen con EXITO", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditPerfilActivity.this, "ERROR al guardar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveImageStoragePerfilAndCover(File file1, final File file2) {
        //linearLayoutCompat.setVisibility(View.VISIBLE);
        imageFirebaseProvider.createImg(this, file1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imgDownloadUrl(file2);
                    Toast.makeText(EditPerfilActivity.this, "Guardo la imagen con EXITO", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditPerfilActivity.this, "ERROR al guardar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void imgDownloadUrl(final File file) {
        imageFirebaseProvider.getStorageURL().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String url = uri.toString();
                imageFirebaseProvider.createImg(EditPerfilActivity.this, file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImg2) {
                        if (taskImg2.isSuccessful()) {
                            imageFirebaseProvider.getStorageURL().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri2) {
                                    String url2 = uri2.toString();
                                    Users u = new Users();

                                    u.setId_users(authFirebaseProvider.getFirebaseUid());
                                    u.setUsername(nameUser);
                                    u.setPhone(phone);
                                    u.setImg_perfil(url);
                                    u.setImg_cover(url2);

                                    updateUserstFirestore(u);
                                }
                            });
                        }
                    }
                });

            }
        });
    }

    private void updateUserstFirestore(Users u) {
        usersFirestoreProvider.updateUsers(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //linearLayoutCompat.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Toast.makeText(EditPerfilActivity.this, "El usuario se actualizo con exito", Toast.LENGTH_SHORT).show();
                    //goToView(MainActivity.class);
                } else {
                    Toast.makeText(EditPerfilActivity.this, "El usuario NO se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUsers() {
        usersFirestoreProvider.readUsers(authFirebaseProvider.getFirebaseUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        nameUser = documentSnapshot.getString("userName");
                        txt_nameUser.setText(nameUser);
                    }
                    if (documentSnapshot.contains("phone")) {
                        phone = documentSnapshot.getString("phone");
                        txt_phone.setText(phone);
                    }
                    if (documentSnapshot.contains("img_perfil")) {
                        imgUrlPerfil = documentSnapshot.getString("img_perfil");
                        if (imgUrlPerfil != null) {
                            if (!imgUrlPerfil.isEmpty()) {
                                Picasso.with(EditPerfilActivity.this).load(imgUrlPerfil).into(circleBackUser);
                            }
                        }
                    }
                    if (documentSnapshot.contains("img_cover")) {
                        imgUrlCover = documentSnapshot.getString("img_cover");
                        if (imgUrlCover != null) {
                            if (!imgUrlCover.isEmpty()) {
                                Picasso.with(EditPerfilActivity.this).load(imgUrlCover).into(img_Portada);
                            }
                        }
                    }
                    // video 9 - min 5
                } else {

                }
            }
        });
    }
}