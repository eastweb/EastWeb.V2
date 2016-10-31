package version2.prototype.download.ModisNBAR_V6;

import java.io.IOException;

import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.download.ModisDownloadUtils.ModisListDatesFiles;

public class ModisNBARQCListDatesFiles_V6 extends ModisListDatesFiles
{
    public ModisNBARQCListDatesFiles_V6(DataDate startDate, DownloadMetaData data, ProjectInfoFile project) throws IOException
    {
        super(startDate, data, project);
    }

}
