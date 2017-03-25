package version2.prototype.processor.IMERG_RTV04;

import version2.prototype.processor.ProcessData;
import version2.prototype.processor.Reproject;

public class IMERG_RTV04Reproject extends Reproject{

    public IMERG_RTV04Reproject(ProcessData data, Boolean deleteInputDirectory) {
        super(data, deleteInputDirectory);
        NoProj =  false;
    }

}
