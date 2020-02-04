package com.adsk.analytics.utils;

import java.io.*;
import java.util.Properties;

public class FileUtils
{
    public static String getFilenameFromPath(String pathname, String delimiter)
    {
        String filename;
        int index = pathname.lastIndexOf(delimiter);

        if (index == -1)
        {
            filename = pathname;
        }
        else if (index == pathname.length() - 1)
        {
            filename = "";
        }
        else
        {
            filename = pathname.substring(index + 1);
        }

        return filename;
    }

	public static String getFilenameFromPath(String pathname, char delimiter)
	{
        return getFilenameFromPath(pathname, String.valueOf(delimiter));
	}

	public static boolean isDWG(String filename)
	{
		return isDWGFile(getFilenameExtension(filename));
	}

	public static boolean isIMG(String filename)
	{
		String ext = getFilenameExtension(filename).toLowerCase();

		return ((ext.equals("jpg")) || (ext.equals("gif")) ||
				(ext.equals("png")) || (ext.equals("tif")) ||
				(ext.equals("tiff")) || (ext.equals("bmp")));
	}

	public static String getFilenameExtension(String fileName)
	{
		if (fileName == null)
		{
			return null;
		}

		int periodIndex = fileName.lastIndexOf(".");

		if (periodIndex > -1)
		{
			return fileName.substring(periodIndex + 1).toLowerCase();
		}

		return "";
	}

	public static boolean isDWGFile(String type)
	{
		return ("dwg".equalsIgnoreCase(type));
	}

	public static String parentFolderFromPath(String s)
	{
		if (s.length() == 0)
		{
			return "";
		}

		s = s.replace('\\', '/');

		if ((s.lastIndexOf('/')) == (s.length() - 1))
		{
			s = s.substring(0, s.length() - 1);
		}

		int pos = s.lastIndexOf('/');

		if (pos >= 0)
		{
			return s.substring(0, pos + 1);
		}
		else
		{
			return "";
		}
	}

	public static File InputStreamToFile(InputStream is, String outputFilename)
	{
		try
		{
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

    public static String getFileAsStream(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            br.close();
        }

        return sb.toString();
    }

    public static Properties getPropertiesFile(String fileName){
        Properties props = new Properties();
        if(fileName == null) return null;
        try {
            props.load(new FileInputStream(fileName));
        } catch (IOException e) {
            return null;
        }
        return props;
    }

    public static String readFileFromClassPath(String fileName){
        InputStream instream = null;
        try {
            instream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (instream != null) {
                return convertStreamToString(instream);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String changeFileNameToCopyName(String name, String fileExtension)
    {
        String regex = fileExtension + "$";
        name = name.replaceAll(regex, "copy" + fileExtension);

        return name;
    }
}
