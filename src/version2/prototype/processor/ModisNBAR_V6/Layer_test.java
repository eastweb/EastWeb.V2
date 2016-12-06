package version2.prototype.processor.ModisNBAR_V6;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import version2.prototype.Config;
import version2.prototype.ErrorLog;
import version2.prototype.processor.ImageArray;
import version2.prototype.processor.ModisTileData;
import version2.prototype.processor.Mozaic;
import version2.prototype.processor.ProcessData;
import version2.prototype.util.FileSystem;
import version2.prototype.util.GdalUtils;

public class Layer_test {

    @Test
    public void runtest() {
        GdalUtils.register();

        File folder = new File("C:\\test1");

        for(File f: folder.listFiles())
        {

            System.out.println(f.getAbsolutePath());
            newFileBaseInfo(f.getAbsolutePath());
            //showFileBaseInfo(f.getAbsolutePath());
        }

    }


    private void newFileBaseInfo(String filename)
    {
        Dataset inputDS = gdal.Open(filename);

        int xSize = inputDS.GetRasterXSize() ;
        int ySize = inputDS.getRasterYSize() ;
        System.out.println("x: " + xSize + "y: " + ySize  );
        System.out.println("band count: " + inputDS.GetRasterCount());

        if( inputDS.GetRasterCount()!= 1)
        {return;}


        Band b = inputDS.GetRasterBand(1);

        int arr[] = new int[xSize* ySize];

        b.ReadRaster(0, 0, xSize, ySize, arr);

        for(int i = 0; i < 100; i++)
        {
            System.out.println(arr[i]);
        }
        System.out.println();

    }


    private void showFileBaseInfo(String filename)
    { synchronized (GdalUtils.lockObject)
        {
        try{
            int i;
            Dataset inputDS = gdal.Open(filename);
            System.out.println(inputDS);
            int xSize = inputDS.getRasterXSize();
            int ySize = inputDS.getRasterYSize();
            System.out.println(ySize);

            Band b = inputDS.GetRasterBand(1);
            System.out.println(b);
            System.out.println(ySize);


            int[] array = new int[xSize * ySize];
            // b.ReadRaster(0, 0, xSize, ySize,array);

            String domain = "SUBDATASETS";
            Enumeration<String> keys = inputDS.GetMetadata_Dict(domain).keys();
            double[] d = inputDS.GetGeoTransform();

            for(i = 0; i <d.length;i++)
            {
                System.out.println(d[i]);
            }

            /*
            while(myKey.hasMoreElements())
            {
                Object k = myKey.nextElement();
                Object v = inputDS.GetMetadata_Dict().get(k);

                System.out.println("Key: " + k.toString());
                System.out.println(" Value: " + v.toString());
            }
             */

            List<Object> list =  inputDS.GetMetadata_List(domain);


            for(i = 0; i < list.size(); i++)
            {
                System.out.println(list.get(i));
            }

            while (keys.hasMoreElements()) {
                Object aKey = keys.nextElement();
                Object aValue = inputDS.GetMetadata_Dict(domain).get(aKey);


                System.out.println(aKey.toString());
                //if(aKey.toString().contains("NAME"))
                {
                    System.out.println(aValue);
                    System.out.println("");
                }

                /*for(i=0; i<array.length; i++)
            {
                System.out.println(array[i]);

            }*/
            }


            //System.out.println("The band count of " + filename + " is " + inputDS.GetLayer());
            // System.out.println("The band size of " + filename + " is X:" + inputDS.getRasterXSize() + " Y: " + inputDS.getRasterYSize());

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        }
    }

}
