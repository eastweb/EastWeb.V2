package version2.prototype.processor.IMERG;

import java.io.File;

import version2.prototype.Projection;
import version2.prototype.Projection.ResamplingType;
import version2.prototype.util.GdalUtils;

public class TestIMERG
{
    public static void main(String [ ] args)
    {
        String inFile = "c:\\test\\IMERG\\test.tif";
        String outFile = "C:\\test\\IMERG\\output.tif";

        IMERGConvert img = new IMERGConvert(inFile);
        try {
            img.convertFiles();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Projection proj = new Projection(ResamplingType.BILINEAR, 1000);
        String shapefile = "C:\\test\\shapefiles\\Woreda_hacked.shp";
        String pFile = "C:\\test\\IMERG\\testProj1.tif";

        GdalUtils.project(new File(outFile), shapefile, proj, new File(pFile));

    }

}