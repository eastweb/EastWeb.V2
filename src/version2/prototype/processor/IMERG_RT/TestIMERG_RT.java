package version2.prototype.processor.IMERG_RT;

import java.io.File;

import version2.prototype.Projection;
import version2.prototype.Projection.ResamplingType;
import version2.prototype.util.GdalUtils;

public class TestIMERG_RT
{
    public static void main(String [ ] args)
    {
        String inFile = "c:\\test\\IMERG_RT\\test_RT2.tif.gz";
        String outFile = "C:\\test\\IMERG_RT\\output.tif";

        IMERG_RTConvert img = new IMERG_RTConvert(inFile);
        try {
            img.convertFiles();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Projection proj = new Projection(ResamplingType.BILINEAR, 1000);
        String shapefile = "C:\\test\\shapefiles\\Woreda_hacked.shp";
        String pFile = "C:\\test\\IMERG_RT\\testProj1.tif";

        GdalUtils.project(new File(outFile), shapefile, proj, new File(pFile));

    }

}