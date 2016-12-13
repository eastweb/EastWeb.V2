package version2.prototype.processor.ModisNBARV6;

import version2.prototype.processor.Mozaic;
import version2.prototype.processor.ProcessData;

public class ModisNBARV6MozaicData extends Mozaic{

    public ModisNBARV6MozaicData(ProcessData data, Boolean deleteInputDirectory) throws InterruptedException {
        super(data, deleteInputDirectory);
    }

    @Override
    protected int[] getBands() {
        return new int[] {8, 9, 10, 11, 12, 13, 14};
    }
}
