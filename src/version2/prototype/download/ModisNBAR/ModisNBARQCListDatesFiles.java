package version2.prototype.download.ModisNBAR;

import java.io.IOException;

import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.DownloadMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.download.ModisDownloadUtils.ModisListDatesFiles;

public class ModisNBARQCListDatesFiles extends ModisListDatesFiles
{
    public ModisNBARQCListDatesFiles(DataDate startDate, DownloadMetaData data, ProjectInfoFile project)
            throws IOException
    {
        super(startDate, data, project);
    }

}
