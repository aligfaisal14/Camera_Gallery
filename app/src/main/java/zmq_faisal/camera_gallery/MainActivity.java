package zmq_faisal.camera_gallery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Button btnselect;
    private int  SELECT_FILE = 1;
    private  int REQUEST_CAMERA = 0;
    private ImageView ivImage;
    private String userChoosenTask;
    CircleImageView c1,c2,c3;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnselect= (Button) findViewById(R.id.btnSelectPhoto);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        c1= (CircleImageView) findViewById(R.id.meal_image_order1);
        c2= (CircleImageView) findViewById(R.id.meal_image_order2);
        c3= (CircleImageView) findViewById(R.id.meal_image_order3);
        list=new ArrayList<String>(5);
        System.out.println("ABC....."+c1.getDrawable()+" "+c2.getDrawable()+" "+c3.getDrawable()+"  "+list.size());
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)

                onCaptureImageResult(data,requestCode);

        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }


    private void onCaptureImageResult(Intent data,int req) {
        Bitmap bm=null;
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        System.out.println("Afgh...."+thumbnail.toString());

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

            list.add(destination.toString());
            System.out.println("allah..."+fo.toString()+"    "+destination.toString()+"  "+list.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size()!=0){
            for(int i=0;i<list.size();i++){
                File imgFile = new  File(list.get(i));
                if(imgFile.exists()){
                     bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    if(i==0){
                        c1.setImageBitmap(bm);
                    }
                    if(i==1){
                        c2.setImageBitmap(bm);
                    }
                    if(i==2){
                        c3.setImageBitmap(bm);
                    }
                }

            }

        }

//        switch (req){
//            case 0:
//                c1.setImageBitmap(thumbnail);
//
//                break;
//            case 1:
//                c1.setImageBitmap(thumbnail);
//
//                break;
//            case 3:
//                c3.setImageBitmap(thumbnail);
//
//                break;
//        }

//if(c1.getDrawable().equals("null")){
//    c1.setImageBitmap(thumbnail);
//}
//        else  if(c2.getDrawable().equals("null")){
//    c2.setImageBitmap(thumbnail);
//        }
//        else{
//    c3.setImageBitmap(thumbnail);
//        }
//        switch (req){
//            case 0:
//                c1.setImageBitmap(thumbnail);
//                REQUEST_CAMERA=REQUEST_CAMERA+1;
//                break;
//            case 1:
//                c2.setImageBitmap(thumbnail);
//                REQUEST_CAMERA=REQUEST_CAMERA+1;
//                break;
//            case 3:
//                c3.setImageBitmap(thumbnail);
//                REQUEST_CAMERA=REQUEST_CAMERA+1;
//                break;
//            default:
//                System.out.println("ABC....."+c1.getDrawable()+" "+c2.getResources().getAssets()+" "+c3.getResources());
//                break;
//        }
        System.out.println("ABC....."+c1.getDrawable()+" "+c2.getDrawable()+" "+c3.getDrawable());

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }


}
