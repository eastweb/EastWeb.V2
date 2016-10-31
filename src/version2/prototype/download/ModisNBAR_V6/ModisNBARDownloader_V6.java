package version2.prototype.download.ModisNBAR_V6;

import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.download.ModisDownloadUtils.ModisDownloader;

public class ModisNBARDownloader_V6 extends ModisDownloader
{
    public ModisNBARDownloader_V6(DataDate date, String outFolder, DownloadMetaData data, String fileToDownload)
    {
        super(date, outFolder, data, fileToDownload);
    }
}