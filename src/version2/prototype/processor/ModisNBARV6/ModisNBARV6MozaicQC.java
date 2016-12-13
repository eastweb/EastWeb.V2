package version2.prototype.processor.ModisNBARV6;

import version2.prototype.processor.Mozaic;
import version2.prototype.processor.ProcessData;

public class ModisNBARV6MozaicQC extends Mozaic{

    public ModisNBARV6MozaicQC(ProcessData data, Boolean deleteInputDirectory) throws InterruptedException {
        super(data, deleteInputDirectory);
    }

    @Override
    protected int[] getBands() {
        return new int[] {12, 13, 14, 15, 16, 17, 18};
    }
}
