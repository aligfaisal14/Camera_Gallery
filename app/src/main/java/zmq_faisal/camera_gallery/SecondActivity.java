package zmq_faisal.camera_gallery;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondActivity extends Activity implements View.OnClickListener {

    Button captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private List<String> listOfImagesPath;
    File mainImageDirectory,subImageDirectory;
    public String GridViewDemo_ImagePath,subFolder;

//    private GridLayoutManager lLayout;
//    private RecyclerView recyclerView;
//    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        makeMainFolder();
        captureBtn = (Button)findViewById(R.id.capture_btn1);
        captureBtn.setOnClickListener(this);
        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();


//        recyclerView= (RecyclerView)findViewById(R.id.my_recycler_view);
//        recyclerView.setLayoutManager(lLayout);
//        adapter=new MyAdapter(SecondActivity.this,listOfImagesPath);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(SecondActivity.this, 2));

        grid = ( GridView) findViewById(R.id.gridviewimg);

        if(listOfImagesPath!=null){
            grid.setAdapter(new ImageListAdapter(SecondActivity.this,listOfImagesPath));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   onCameraClick();
                } else {
                    Toast.makeText(SecondActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    void makeMainFolder(){
        GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LetsMapImages/";
        mainImageDirectory = new File(GridViewDemo_ImagePath);
        mainImageDirectory.mkdirs();

        subImageDirectory = new File(mainImageDirectory.getAbsolutePath()+"/"+"ABC/");
        subImageDirectory.mkdirs();

        subFolder=subImageDirectory.getAbsolutePath();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {
// TODO Auto-generated method stub
        if (arg0.getId() == R.id.capture_btn1) {
            boolean result=Utility.checkPermission(SecondActivity.this);
            if(result){
                 onCameraClick();
            }
//            try {
//                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(captureIntent, CAMERA_CAPTURE);
//            } catch(ActivityNotFoundException anfe){
//                String errorMessage = "oops - your device doesn't support capturing images!";
//                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//                toast.show();
//            }
        }

    }

    void onCameraClick(){
//        boolean result=Utility.checkPermission(SecondActivity.this);
//        if(result){
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
//        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                String imgcurTime = dateFormat.format(new Date());


                String _path = subImageDirectory.getAbsolutePath().toString()+"/" + imgcurTime+".jpg";
                System.out.println("Folder Nwsme...."+_path);
                try {
                    FileOutputStream out = new FileOutputStream(_path);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                if(listOfImagesPath!=null){
                    grid.setAdapter(new ImageListAdapter(SecondActivity.this,listOfImagesPath));
                }
            }
        }
    }

    private List<String> RetriveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(subFolder);
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);
            System.out.println("Total Files in folder...."+files.length);
            if(files.length==5){
                captureBtn.setEnabled(false);
                for(int i=0; i<files.length; i++){
                    File file = files[i];
                    if(file.isDirectory())
                        continue;
                    tFileList.add(file.getPath());
                }
            }
            else{
                for(int i=0; i<files.length; i++){
                    File file = files[i];
                    if(file.isDirectory())
                        continue;
                    tFileList.add(file.getPath());
                }
            }

        }
        return tFileList;
    }

    public class ImageListAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private List<String> imgPic;
        public ImageListAdapter(Context c, List<String> thePic)
        {
            context = c;
            imgPic = thePic;
        }
        public int getCount() {
            if(imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            CircleImageView cr;
            ImageView imageView;
            BitmapFactory.Options bfOptions=new BitmapFactory.Options();
            bfOptions.inDither=false;                     //Disable Dithering mode
            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage=new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);
                cr=new CircleImageView(context);
                cr.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                cr.setPadding(0, 0, 0, 0);
                cr.setBorderColor(Color.RED);
                cr.setBorderWidth(5);
                cr.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                cr = (CircleImageView) convertView;
            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));
                if(fs!=null) {
                    bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    cr.setImageBitmap(bm);
                    cr.setId(position);
                    cr.setLayoutParams(new GridView.LayoutParams(300, 200));

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return cr;
        }

        @Override
        public void onClick(View v) {

        }
    }





    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<String> thePic;
        Context context;
//        private ClickListner clickListner;

        public MyAdapter(Context context, List<String> thePic){
            inflator=LayoutInflater.from(context);
            this.thePic=thePic;
            this.context=context;
            System.out.println("Size of data " + thePic.size());
        }

        public void delete(int postion){
            thePic.remove(postion);
            notifyItemRemoved(postion);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview,parent,false);
            MyViewHolder holder=new MyViewHolder(view);
            System.out.println("onCreateViewHolder ");
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            String current=thePic.get(position);
            FileInputStream fs = null;
            Bitmap bm;
//            BitmapFactory.Options bfOptions=new BitmapFactory.Options();
//            bfOptions.inDither=false;                     //Disable Dithering mode
//            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
//            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
//            bfOptions.inTempStorage=new byte[32 * 1024];
            try {
                fs = new FileInputStream(new File(thePic.get(position).toString()));
                if(fs!=null) {
                    bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, null);
//                    cr.setImageBitmap(bm);
//                    cr.setId(position);
//                    cr.setLayoutParams(new GridView.LayoutParams(300, 200));
                    holder.icon.setImageBitmap(bm);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

//            holder.text.setText(current.getTitle());
//            holder.icon.setImageResource(current.getItemId());
//            System.out.println("Positoion " + position + "  " + current.getTitle() + " " + current.getItemId());

        }

        @Override
        public int getItemCount() {

            return thePic.size();

        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView text,text1;
            CircleImageView icon;

            public MyViewHolder(final View itemView) {
                super(itemView);
//                text= (TextView) itemView.findViewById(R.id.country_name);
                icon= (CircleImageView) itemView.findViewById(R.id.meal_image_order);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("POstion.........12   "+getPosition());
                    }
                });
            }

            @Override
            public void onClick(View v) {
//                comMenu.getItemId(getPosition());
//                System.out.println("Item clicked..."+getPosition());
            }
        }
    }

}
