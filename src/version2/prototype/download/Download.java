package version2.prototype.download;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import version2.prototype.Process;
import version2.prototype.ThreadState;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.PluginMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.ProjectInfoMetaData.ProjectInfoPlugin;
import version2.prototype.Scheduler.ProcessName;
import version2.prototype.Scheduler.Scheduler;
import version2.prototype.util.DataFileMetaData;
import version2.prototype.util.DatabaseCache;
import version2.prototype.util.GeneralUIEventObject;

public class Download extends Process<Void>{
    private ArrayList<DownloadWorker> workers;

    public Download(ProjectInfoFile projectInfoFile, ProjectInfoPlugin pluginInfo, PluginMetaData pluginMetaData, Scheduler scheduler,
            ThreadState state, ProcessName processName, String inputTableName, ExecutorService executor)
    {
        super(projectInfoFile, pluginInfo, pluginMetaData, scheduler, state, processName, inputTableName, executor);
        workers = new ArrayList<DownloadWorker>(0);
    }

    @Override
    public Void call() throws Exception {
        DownloadWorker worker;

        // General get data files
        ArrayList<DataFileMetaData> cachedFiles = new ArrayList<DataFileMetaData>();
        cachedFiles = DatabaseCache.GetAvailableFiles(projectInfoFile.GetProjectName(), pluginInfo.GetName(), mInputTableName);
        if(cachedFiles.size() > 0)
        {
            if(mState == ThreadState.RUNNING)
            {
                worker = new DownloadWorker(this, projectInfoFile, pluginInfo, pluginMetaData, cachedFiles);
                workers.add(worker);
                executor.submit(worker);
            }
        }

        // TODO: Need to define when "finished" state has been reached as this doesn't work with asynchronous.
        scheduler.NotifyUI(new GeneralUIEventObject(this, "Download Finished", 100));
        return null;
    }

}
