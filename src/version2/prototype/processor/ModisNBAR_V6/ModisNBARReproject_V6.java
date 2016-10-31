package version2.prototype.processor.ModisNBAR_V6;

import version2.prototype.processor.ProcessData;
import version2.prototype.processor.Reproject;

public class ModisNBARReproject_V6 extends Reproject {

    public ModisNBARReproject_V6(ProcessData data, Boolean deleteInputDirectory) {
        super(data, deleteInputDirectory);
        NoProj =  false;
    }


}