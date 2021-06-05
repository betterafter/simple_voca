package com.danerdaner.simple_voca;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.danerdaner.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.RequiresApi;

public class FileIOManager {
    public static final String STRSAVEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WordBackup/";
    public static final String TAG = "[PoohReum]";
    public static final String RESERVED_FILE = "ALL";
    private File rootDir;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public FileIOManager(){
        // Create path, if not exist
        try {
            rootDir = makeDirectory(STRSAVEPATH);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i( TAG , STRSAVEPATH );
    }


    public File writeCategoryFile(String category, String content) throws IOException {
        // make file

        File file = makeFile(rootDir, (STRSAVEPATH+"simpleVoca_"+category+".csv"), true);
        writeFile(file , content.getBytes());

        return file;
    }

    public File getCategoryFile(String category){
        return new File(rootDir,STRSAVEPATH+"simpleVoca_"+category+".csv");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private File makeDirectory(String dir_path) throws IOException {

        if (MainActivity.mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                MainActivity.mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (MainActivity.mainActivity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { }
            MainActivity.mainActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        File dir = new File(dir_path);
        if (!dir.exists() || !dir.isDirectory())
        {
            dir.mkdirs();
            Log.i( TAG , "!dir.exists" );
        }else{
            Log.i( TAG , "dir.exists" );
        }

        return dir;
    }

    private File makeFile(File dir , String file_path, boolean overwrite) throws IOException {
        File file = null;
        boolean isSuccess = false;
        if(dir.isDirectory()){
            file = new File(file_path);
            if(file!=null){
                Log.i( TAG , "!file.exists" );
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    Log.i(TAG, "파일생성 여부 = " + isSuccess);
                }
            }else{
                Log.i( TAG , "file.exists" );
            }
        }
        return file;
    }

    private String getAbsolutePath(File file){
        return ""+file.getAbsolutePath();
    }

    private boolean deleteFile(File file){
        boolean result;
        if(file!=null&&file.exists()){
            file.delete();
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    private boolean isFile(File file){
        boolean result;
        if(file!=null&&file.exists()&&file.isFile()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    private boolean isDirectory(File dir){
        boolean result;
        if(dir!=null&&dir.isDirectory()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    private boolean isFileExist(File file){
        boolean result;
        if(file!=null&&file.exists()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    private boolean reNameFile(File file , File new_name){
        boolean result;
        if(file!=null&&file.exists()&&file.renameTo(new_name)){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    private String[] getList(File dir){
        if(dir!=null&&dir.exists())
            return dir.list();
        return null;
    }

    private boolean writeFile(File file , byte[] file_content){
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null){
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    public String readFile(File file){
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                for(int i=0 ; i<file.length();i++){
                    Log.d(TAG, ""+buffer[i]);
                }
                fis.close();
                Log.d(TAG, "readcount : "+readcount);
                return new String(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private boolean copyFile(File file , String save_file){
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

}
