package com.adsk.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

/**
 * Created by vatsalya on 5/4/16.
 */
public class Mover extends Thread
{
    String threadName;
    Mover(String threadName)
    {
        super(threadName);
        this.threadName = threadName;
        start();
    }
    public void run()
    {
//        for (int i=0 ;i<3;i++)
//        {
//            System.out.println(threadName + "Printing the count " + i);
//        }
        Random rand = new Random();
        String tempFolderPath = "/Users/vatsalya/AutoCAD360-Server/analytics/src/main/mac/TEMP/"+threadName+"/";
        String tempDrawingPath =tempFolderPath + String.valueOf(Math.abs(rand.nextInt())) + ".fil";
        String originalFilePath = tempFolderPath + "OriginalDwg.txt";
        File tempFile = new File(tempDrawingPath);
        File originalFile = InputStreamToFile(originalFilePath);

//        System.out.println(threadName+"original File exists" + originalFile.exists());
//        System.out.println(threadName+"tempFile exists" + tempFile.exists());

        try {
            Files.move(originalFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(threadName+" -- > SUCCESS");

        } catch (IOException e) {
            System.err.println(threadName+" -- >"+e);
        }
//        finally{
//            System.out.println(threadName+"File deleted - "+originalFile.delete());
//        }

//        boolean renameSuccess = originalFile.renameTo(tempFile);

        System.out.println(threadName+"My thread run is over");
    }

    public static File InputStreamToFile( String outputFilename)
    {
        try
        {
            String str = "This is a String ~ GoGoGo"+currentThread().getName();

            // convert String into InputStream
            InputStream is = new ByteArrayInputStream(str.getBytes());

            FileOutputStream fos = new FileOutputStream(outputFilename);

            byte[] buf = new byte[10 * 1024]; //chunks of 10KB

            int read = 0;

            while ((read = is.read(buf)) > 0)
            {
                fos.write(buf, 0, read);
            }

            fos.flush();
            fos.close();

            return new File(outputFilename);
        }
        catch(IOException e)
        {
            return null;
        }

    }
}
