/**
 *
 */
package version2.prototype.download.ModisNBAR_V6;

import java.io.IOException;
import java.time.LocalDate;

import version2.prototype.Config;
import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.PluginMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.ProjectInfoMetaData.ProjectInfoPlugin;
import version2.prototype.Scheduler.Scheduler;
import version2.prototype.download.DownloadFactory;
import version2.prototype.download.DownloaderFactory;
import version2.prototype.download.ListDatesFiles;
import version2.prototype.download.ModisDownloadUtils.ModisLocalStorageDownloadFactory;
import version2.prototype.util.DatabaseCache;

/**
 * @author michael.devos
 *
 */
public class ModisNBARQCFactory_V6 extends DownloadFactory {

    public ModisNBARQCFactory_V6(Config configInstance, ProjectInfoFile projectInfoFile, ProjectInfoPlugin pluginInfo, DownloadMetaData downloadMetaData, PluginMetaData pluginMetaData,
            Scheduler scheduler, DatabaseCache outputCache, LocalDate startDate) {
        super(configInstance, projectInfoFile, pluginInfo, downloadMetaData, pluginMetaData, scheduler, outputCache, startDate);
    }

    /* (non-Javadoc)
     * @see version2.prototype.download.DownloadFactory#CreateDownloadFactory()
     */
    @Override
    public DownloaderFactory CreateDownloaderFactory(ListDatesFiles listDatesFiles) {
        return new ModisLocalStorageDownloadFactory(configInstance, "ModisNBARQCDownloader", projectInfoFile, pluginInfo, downloadMetaData, pluginMetaData, scheduler, outputCache, listDatesFiles,
                startDate);
    }

    /* (non-Javadoc)
     * @see version2.prototype.download.DownloadFactory#CreateListDatesFiles(version2.prototype.DataDate, version2.prototype.PluginMetaData.PluginMetaDataCollection.DownloadMetaData)
     */
    @Override
    public ListDatesFiles CreateListDatesFiles() throws IOException {
        return new ModisNBARQCListDatesFiles_V6(new DataDate(startDate), downloadMetaData, projectInfoFile);
    }

}