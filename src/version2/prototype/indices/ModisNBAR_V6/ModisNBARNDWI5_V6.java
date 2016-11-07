package version2.prototype.indices.ModisNBAR_V6;

import java.io.File;
import java.util.List;

import version2.prototype.indices.IndicesFramework;

/**
 * uses the same logic for ndwi5 and ndwi6
 * NDWI5 = (NIR-SWIR2)/(NIR+SWIR2)
 * NDWI6 = (NIR-SWIR)/(NIR+SWIR)
 * @author Isaiah Snell-Feikema
 */

/*
 *  1: Band 1: Red
 *  2: Band 2: NIR
 *  3: Band 3: Blue
 *  4: Band 4: Green
 *  5: Band 5: SWIR 1
 *  6: Band 6: SWIR 2
 *  7: Band 7: SWIR 3
 */
public class ModisNBARNDWI5_V6 extends IndicesFramework {
    private final int NIR;
    private final int SWIR;

    public ModisNBARNDWI5_V6(List<File> inputFiles, File outputFile, Integer noDataValue)
    {
        super(inputFiles, outputFile, noDataValue);

        int tempNIR = -1;
        int tempSWIR = -1;
        for(int i=0; i < mInputFiles.length; i++)
        {
            if(mInputFiles[i].getName().toLowerCase().contains(new String("band2")))
            {
                tempNIR = i;
            }
            else if(mInputFiles[i].getName().toLowerCase().contains(new String("band5")))
            {
                tempSWIR = i;
            }

            if(tempNIR > -1 && tempSWIR > -1) {
                break;
            }
        }

        NIR = tempNIR;
        SWIR = tempSWIR;
    }

    /**
     * Valid input value range: 0 to 65535
     * Valid output value range: -1 to 1
     */
    @Override
    protected double calculatePixelValue(double[] values) throws Exception {
        if (values[NIR] > 65535 || values[NIR] < 0 || values[SWIR] > 65535 || values[SWIR] < 0 || values[NIR] == noDataValue || values[SWIR] == noDataValue) {

            return noDataValue;
        } else {
            for(int i=0; i < values.length; i++) {
                values[i] = values[i] / 10000;
            }
            return (values[NIR] - values[SWIR]) / (values[SWIR] + values[NIR]);
        }
    }

    @Override
    protected String className() {
        return getClass().getName();
    }

}