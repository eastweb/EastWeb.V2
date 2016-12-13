package version2.prototype.processor.ModisNBARV6;

import java.io.File;

import version2.prototype.processor.Filter;
import version2.prototype.processor.ProcessData;

public class ModisNBARV6Filter extends Filter{
    private Integer noDataValue;

    public ModisNBARV6Filter(ProcessData data, Boolean deleteInputDirectory) {
        super(data, deleteInputDirectory);
        noDataValue = data.getNoDataValue();
    }

    @Override
    protected double filterValue(double value)
    {
        return 0;
    }

    @Override
    protected void filterByQCFlag(String qcLevel) throws Exception
    {
    }
}