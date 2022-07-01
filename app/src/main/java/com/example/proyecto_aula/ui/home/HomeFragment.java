package com.example.proyecto_aula.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto_aula.R;
import com.example.proyecto_aula.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String nombre;
    private Bitmap bitmapImage;
    private ImageView imageView,carrito;
    private TextView tv_Nombre,NUMERO;
    private LinearLayout bt_download;
    private SharedPreferences sharedPref;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPref = this.getActivity().getSharedPreferences("pref",Context.MODE_PRIVATE);
        String nombre = sharedPref.getString("nombre", "Error");
        String encoded = sharedPref.getString("imagen", null);
        tv_Nombre = root.findViewById(R.id.userName);
        bt_download = root.findViewById(R.id.bt_Download);
        carrito = root.findViewById(R.id.carrito);
        NUMERO = root.findViewById(R.id.NUMERO);
        tv_Nombre.setText(nombre);
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        imageView = root.findViewById(R.id.imagenQR);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQRtoGallery();
            }
        });

        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificacion2();
            }
        });

        return root;

    }

    public void notificacion2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_oferta)
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void saveQRtoGallery(){
        /*BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        FileOutputStream fileOutputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File  dir = new File(file.getAbsolutePath()+"/QrUniversity");
        dir.mkdirs();

        @SuppressLint("DefaultLocale") String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir, filename);
        try {
            fileOutputStream = new FileOutputStream(outFile);
        } catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

        try{
            fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        }catch (Exception e){

        }*/
        try {
            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/QrUniversity");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = String.format("%d.jpg", Long.parseLong(sharedPref.getString("documento", "Error")));
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*imageView.buildDrawingCache();

        Bitmap bmp = imageView.getDrawingCache();

        File  dir = new File(Environment.getExternalStorageDirectory()+"QrUniversity");
        //File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

        File file = new File(dir, sharedPref.getString("nombre", "Error") + ".jpg");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            scanFile(this.getActivity(), Uri.fromFile(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Toast.makeText(getContext(), "Guardado con exito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}