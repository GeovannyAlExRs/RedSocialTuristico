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
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.model.Post;
import com.app.dtk.redsocialturistico.providers.AuthFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.ImageFirebaseProvider;
import com.app.dtk.redsocialturistico.providers.PostFirebaseProvider;
import com.app.dtk.redsocialturistico.utils.FileUtil;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_post1, img_post2;
    private ImageView img_ic_deporte, img_ic_hotel, img_ic_playa, img_ic_restaurante;
    private TextView txt_category;
    private TextInputEditText txt_title_post, txt_description_post, txt_reference_post;
    private AppCompatButton btn_save;
    private CircleImageView imageViewBack;
    private AlertDialog.Builder builderSelect;
    private ProgressBar progressBar;
    private LinearLayoutCompat linearLayoutCompat;

    // Foto 1
    String absolutePhotoPath1;
    String photoPath1;
    File filePhoto1;
    CharSequence options[];

    // Foto 2
    String absolutePhotoPath2;
    String photoPath2;
    File filePhoto2;

    private static final int NUM_GALLERY_REQUEST_CODE1 = 1;
    private static final int NUM_GALLERY_REQUEST_CODE2 = 2;
    private static final int NUM_CAMERA_REQUEST_CODE3 = 3;
    private static final int NUM_CAMERA_REQUEST_CODE4 = 4;

    File fileImage1;
    File fileImage2;

    String title = "";
    String description = "";
    String reference = "";
    String category = null;

    int band = 0;

    //Post p = new Post();

    ImageFirebaseProvider imageFirebaseProvider;
    PostFirebaseProvider postFirebaseProvider;
    AuthFirebaseProvider authFirebaseProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        builderSelect = new AlertDialog.Builder(this);
        builderSelect.setTitle("Selecciona una opcion");
        options = new CharSequence[]{"Imagen de galeria", "Tomar una foto"};

        getViewId();

        imageFirebaseProvider = new ImageFirebaseProvider();
        postFirebaseProvider = new PostFirebaseProvider();
        authFirebaseProvider = new AuthFirebaseProvider();
    }

    private void getViewId() {
        imageViewBack = findViewById(R.id.id_circleBack);
        imageViewBack.setOnClickListener(this);

        txt_title_post = findViewById(R.id.id_txt_title_post);
        txt_description_post = findViewById(R.id.id_txt_description_post);
        txt_reference_post = findViewById(R.id.id_txt_reference_post);

        txt_category = findViewById(R.id.id_txt_category);

        img_ic_deporte = findViewById(R.id.id_img_ic_deporte);
        img_ic_deporte.setOnClickListener(this);

        img_ic_hotel = findViewById(R.id.id_img_ic_hotel);
        img_ic_hotel.setOnClickListener(this);

        img_ic_playa = findViewById(R.id.id_img_ic_playa);
        img_ic_playa.setOnClickListener(this);

        img_ic_restaurante = findViewById(R.id.id_img_ic_restaurante);
        img_ic_restaurante.setOnClickListener(this);

        img_post1 = findViewById(R.id.id_img_post1);
        img_post1.setOnClickListener(this);

        img_post2 = findViewById(R.id.id_img_post2);
        img_post2.setOnClickListener(this);

        btn_save = findViewById(R.id.id_btn_save);
        btn_save.setOnClickListener(this);

        progressBar = findViewById(R.id.id_spinkit_progress);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SCREEN);

        linearLayoutCompat = findViewById(R.id.id_linearLayout_transparent);
        linearLayoutCompat.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        int option1 = 1;
        int option2 = 2;
        switch (view.getId()) {
            case R.id.id_circleBack:
                finish();
                break;
            case R.id.id_img_post1:
                Log.w("OPCION", "Seleccionaste 1 xD");
                selectOptionImg(option1);
                break;
            case R.id.id_img_post2:
                Log.w("OPCION", "Seleccionaste 2 xD");
                selectOptionImg(option2);
                break;
            case R.id.id_img_ic_deporte:
                category = "Deporte";
                txt_category.setText(category);
                break;
            case R.id.id_img_ic_hotel:
                category = "Hotel";
                txt_category.setText(category);
                break;
            case R.id.id_img_ic_playa:
                category = "Playa";
                txt_category.setText(category);
                break;
            case R.id.id_img_ic_restaurante:
                category = "Restaurante";
                txt_category.setText(category);
                break;
            case R.id.id_btn_save:
                band = 1;
                newPost();
                break;
        }
    }

    private void selectOptionImg(final int numbreImg) {
        builderSelect.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (numbreImg == 1) {
                        openGallery(NUM_GALLERY_REQUEST_CODE1);
                    } else if (numbreImg == 2) {
                        openGallery(NUM_GALLERY_REQUEST_CODE2);
                    }
                } else if (i == 1) {
                    if (numbreImg == 1) {
                        cameraImg(NUM_CAMERA_REQUEST_CODE3);
                    } else if (numbreImg == 2) {
                        cameraImg(NUM_CAMERA_REQUEST_CODE4);
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
                Uri uri = FileProvider.getUriForFile(PostActivity.this, "com.app.dtk.redsocialturistico", photoF);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File fileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file = File.createTempFile(new Date() + "_photo", ".jpg", fileDir);

        if (requestCode == NUM_CAMERA_REQUEST_CODE3) {
            photoPath1 = "file: " + file.getAbsolutePath();
            absolutePhotoPath1 = file.getAbsolutePath();
        } else if (requestCode == NUM_CAMERA_REQUEST_CODE4) {
            photoPath2 = "file: " + file.getAbsolutePath();
            absolutePhotoPath2 = file.getAbsolutePath();
        }

        return file;
    }

    private void newPost() {
        title = txt_title_post.getText().toString();
        description = txt_description_post.getText().toString();
        reference = txt_reference_post.getText().toString();

        if (!title.isEmpty() && !description.isEmpty() && !reference.isEmpty() && !category.isEmpty()) {
            // Seleccion de ambas imagenes de galeria MIN 07:32
            if (fileImage1 != null && fileImage2 != null) {
                saveImageStorage(fileImage1, fileImage2);
            }
            // Seleccion de ambas imagenes de camara
            else if (filePhoto1 != null && filePhoto2 != null) {
                saveImageStorage(filePhoto1, filePhoto2);
            }
            // Seleccion de Imagen de galeria y camara
            else if (fileImage1 != null && filePhoto2 != null) {
                saveImageStorage(fileImage1, filePhoto2);
            }
            // Seleccion de Imagen de camara y galeria
            else if (filePhoto1 != null && fileImage2 != null) {
                saveImageStorage(filePhoto1, fileImage2);
            }
            else {
                Toast.makeText(PostActivity.this, "Selecciona una imagen", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(PostActivity.this, "Completa todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImageStorage(File file1, final File file2) {
        linearLayoutCompat.setVisibility(View.VISIBLE);
        imageFirebaseProvider.createImg(this, file1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imgDownloadUrl(file2);
                    Toast.makeText(PostActivity.this, "Guardo la imagen con EXITO", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PostActivity.this, "ERROR al guardar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void imgDownloadUrl(final File file) {
        imageFirebaseProvider.getStorageURL().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String url = uri.toString();
                /*if (band == 1) {
                    p.setImg1(url);
                    band = 2;
                    saveImageStorage(fileImage2);
                } else if (band == 2) {
                    Log.d("MEMORIA", "POST: " + p.getImg1().toString());
                    //String url = uri.toString();
                    Log.d("IDDOC", "POST: " + postFirebaseProvider.getFirebaseId());
                    p.setId_post(postFirebaseProvider.getFirebaseId());
                    p.setImg2(url);
                    p.setTitle(title);
                    p.setDescription(description);
                    p.setReference(reference);
                    p.setCategory(category);
                    p.setIdUsers(authFirebaseProvider.getFirebaseUid());

                    createPostFirestore(p);
                }*/
                imageFirebaseProvider.createImg(PostActivity.this, file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImg2) {
                        if (taskImg2.isSuccessful()) {
                            imageFirebaseProvider.getStorageURL().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri2) {
                                    String url2 = uri2.toString();
                                    Post p = new Post();

                                    p.setImg1(url);
                                    p.setImg2(url2);
                                    p.setTitle(title);
                                    p.setDescription(description);
                                    p.setReference(reference);
                                    p.setCategory(category);
                                    p.setIdUsers(authFirebaseProvider.getFirebaseUid());
                                    p.setTimestamp(new Date().getTime());

                                    createPostFirestore(p);
                                }
                            });
                        }
                    }
                });

            }
        });
    }

    private void createPostFirestore(Post p) {
        postFirebaseProvider.createPost(p).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                linearLayoutCompat.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    clearFrmPost();
                    Toast.makeText(PostActivity.this, "Guardo el POST con Exito", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PostActivity.this, "No se pudo guardar el POST", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearFrmPost() {
        title = "";
        description = "";
        reference = "";
        category = "";

        txt_title_post.setText(title);
        txt_description_post.setText(description);
        txt_reference_post.setText(reference);
        txt_category.setText(category);

        fileImage1 = null;
        fileImage2 = null;

        img_post1.setImageResource(R.drawable.ic_images_solid);
        img_post2.setImageResource(R.drawable.ic_images_solid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SELECCION DE IMAGEN DESDE GALERIA
        if (requestCode == NUM_GALLERY_REQUEST_CODE1 && resultCode == RESULT_OK) {
            try {
                filePhoto1 = null;
                fileImage1 = FileUtil.from(this, data.getData());
                img_post1.setImageBitmap(BitmapFactory.decodeFile(fileImage1.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Error en " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == NUM_GALLERY_REQUEST_CODE2 && resultCode == RESULT_OK) {
            try {
                filePhoto2 = null;
                fileImage2 = FileUtil.from(this, data.getData());
                img_post2.setImageBitmap(BitmapFactory.decodeFile(fileImage2.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Error en " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        // SELECCION DE IMAGEN DESDE CAMARA
        if (requestCode == NUM_CAMERA_REQUEST_CODE3 && resultCode == RESULT_OK) {
            fileImage1 = null;
            filePhoto1 = new File(absolutePhotoPath1);
            Picasso.with(PostActivity.this).load(photoPath1).into(img_post1);
        }

        if (requestCode == NUM_CAMERA_REQUEST_CODE4 && resultCode == RESULT_OK) {
            fileImage2 = null;
            filePhoto2 = new File(absolutePhotoPath2);
            Picasso.with(PostActivity.this).load(photoPath2).into(img_post2);
        }
    }
}