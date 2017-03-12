package version2.prototype.processor;

import java.io.File;

public class ReadHDF4File {
    public static void main(String args[]) throws Exception
    {
        int [] bands = {1,2, 5, 6};
        int bandsLength = bands.length;

        String bandpattern = "SUBDATASET_" + "(" + String.valueOf(bands[0]);
        for (int i = 1; i<= bandsLength-1; i++)
        {
            bandpattern += "|" + String.valueOf(bands[i]);
        }
        bandpattern += ")_NAME";

        TestHDFGDAL hdf = new TestHDFGDAL(bandpattern, bands);
        String [] names = hdf.bandNames(new File []{new File("C:\\test\\ModisLSTV6\\h33v11.hdf")});
        for (String name:names)
        {
            System.out.println(name);
        }

        hdf.readTif("c:\\test\\TRMM\\output.tif");
    }
}
