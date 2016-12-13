package version2.prototype.processor.ModisLSTV6;

import version2.prototype.processor.Mozaic;
import version2.prototype.processor.ProcessData;

public class ModisLSTV6Mozaic extends Mozaic{

    public ModisLSTV6Mozaic(ProcessData data, Boolean deleteInputDirectory) throws InterruptedException {
        super(data, deleteInputDirectory);
    }

    @Override
    protected int[] getBands() {
        return new int[] {1,2, 5, 6};
    }
}
