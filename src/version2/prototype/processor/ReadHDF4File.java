package version2.prototype.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadHDF4File
{
    private static String outputFolder;
    public static void main(String args[]) throws Exception
    {
        int bands[] = {1, 2, 5,6};
        /*String []files = {"C:\\test\\ModisNBAR\\h21v07data.hdf",
        "C:\\test\\ModisNBAR\\h21v08data.hdf"};
         */
        String [] files = {"C:\\test\\ModisLSTV6\\h33v11.hdf"};

        outputFolder = "C:\\test\\ModisLSTV6\\";

        //String [] bandFiles = bandNames(files, "MOD_Grid_BRDF:Nadir_Reflectance_Band", bands);
        String [] bandFiles = bandNames(files, "SUBDATASET_", bands);
        for (String name:bandFiles)
        {
            System.out.println(name);
        }
        //mosaicTiles(bandFiles, bands);

    }

    /* filename: files in the folder
     * sdsName: The name of the band, such as MOD_Grid_BRDF:Nadir_Reflectance_Band
     * band:  the input band
     * return:  an array with each element for the files to be mosaiced
     */
    private static String[] bandNames(String[] filenames, String sdsName, int[] bands)
    {
        Process p;

        int bandsLength = bands.length;

        String bandpattern = sdsName + "(" + String.valueOf(bands[0]);
        for (int i = 1; i<= bandsLength-1; i++)
        {
            bandpattern += "|" + String.valueOf(bands[i]);
        }
        bandpattern += ")_NAME";

        String [] bandNames = new String[bandsLength] ;

        try {
            for (int i = filenames.length - 1; i >= 0; i--)
            {
                // call the gdalinfo to list the HDF info including the sdsNames
                //String command = "gdalinfo " + filenames[i];
                String command = "C:\\Users\\yi.liu\\git\\EastWeb.V2\\lib\\gdal\\gdalinfo " + filenames[i];
                p = Runtime.getRuntime().exec(command);
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = "";
                int j = 0;
                System.out.println((line = reader.readLine())!= null);
                while ((line = reader.readLine())!= null) {
                    Pattern pattern = Pattern.compile( bandpattern);
                    Matcher matcher = pattern.matcher(line);
                    System.out.println(line);
                    if (matcher.find())
                    {
                        System.out.println("find");
                        if (bandNames[j] == null) {
                            bandNames[j] = " ";
                        }
                        // add bands to the string
                        bandNames[j++] += line.substring(line.indexOf('=') + 1) + " ";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bandNames;
    }

    private static void mosaicTiles(String [] bandNames, int bands[])
    {
        Process p;;
        String outputFile;
        try {
            for (int i = bandNames.length - 1; i >= 0; i--)
            {
                outputFile = outputFolder + "band" + String.valueOf(bands[i]) + ".tif";
                // call the gdalinfo to list the HDF info including the sdsNames
                String command = "gdalwarp --config GDAL_CACHEMAX 1000 -wm 1000 "
                        + bandNames[i] + " " +outputFile;
                p = Runtime.getRuntime().exec(command);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

