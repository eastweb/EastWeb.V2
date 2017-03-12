package version2.prototype.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;

import version2.prototype.util.GdalUtils;

public class TestHDFGDAL {

    private int [] bands;
    private String bandpattern;

    public TestHDFGDAL(String pattern, int [] bands)
    {
        this.bands = bands;
        bandpattern = pattern;
    }

    public String[] bandNames(File[] filenames)
    {
        Process p;

        String [] bandNames = new String[bands.length] ;

        try {
            for (int i = filenames.length - 1; i >= 0; i--)
            {
                // call the gdalinfo to list the HDF info including the sdsNames
                String command = "./lib/gdal/gdalinfo " + filenames[i].getAbsolutePath();
                p = Runtime.getRuntime().exec(command);
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = "";
                int j = 0;

                while ((line = reader.readLine())!= null) {
                    Pattern pattern = Pattern.compile(bandpattern);
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.find())
                    {
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

    public void readTif(String file)
    {
        GdalUtils.register();

        synchronized (GdalUtils.lockObject)
        {
            String tifFile = file;
            Dataset baseDS= gdal.Open(tifFile);

            Band baseBand = baseDS.GetRasterBand(1);

            int xSize = baseBand.GetXSize();
            int ySize = baseBand.GetYSize();

            double[] baseArr = new double[xSize * ySize];
            baseBand.ReadRaster(0, 0, xSize, ySize, baseArr);

            int count = 0;
            int count_nodata = 0;

            for (int i = 0; i < 50000; i++)
            {
                if ((baseArr[i] !=0 ) && (baseArr[i] != -9999) )
                {//System.out.println(baseArr[i]);
                    count++;
                }

                if (baseArr[i] < 0 )
                {System.out.println(baseArr[i]);
                count_nodata++;
                }
            }
            System.out.println("count : " + count);
            System.out.println("count no data : " + count_nodata);


            baseDS.delete();
        }
    }
}
