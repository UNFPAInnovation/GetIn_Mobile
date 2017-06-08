package org.sana.net.http.handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class FileHandler implements ResponseHandler<File> {
    public static final String TAG = FileHandler.class.getSimpleName();

    final String path;
    final File file;

    public FileHandler(String path){
        this.path = path;
        file = new File(path);
    }

    public FileHandler(File file){
        this.file = file;
        path = file.getPath();
    }

    @Override
    public File handleResponse(HttpResponse response){
        if(file.exists()) file.delete();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                FileOutputStream os = new FileOutputStream(file);
                InputStream is = entity.getContent();
                int inByte;
                while((inByte = is.read()) != -1) os.write(inByte);
                is.close();
                os.close();
            } catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        return file;
    }
}
