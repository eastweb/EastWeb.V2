package version2.prototype.processor.NldasForcing;

import java.io.File;
import java.util.ArrayList;

import version2.prototype.DataDate;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.ProjectInfoMetaData.ProjectInfoPlugin;
import version2.prototype.Scheduler.ProcessName;
import version2.prototype.processor.PrepareProcessTask;
import version2.prototype.util.FileSystem;

public class NldasForcingPrepareProcessTask extends PrepareProcessTask {

    public NldasForcingPrepareProcessTask(ProjectInfoFile mProject, ProjectInfoPlugin mPlugin, DataDate mDate) {
        super(mProject, mPlugin, mDate);
    }

    @Override
    public String[] getInputFolders(int stepId)
    {
        ArrayList<String> folders = new ArrayList<String>();

        // Format: Input (of this step) -> Output (of this step)
        switch(stepId)
        {
        case 1:
            // Download -> Composite
            folders.add(String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Download", date.getYear(), date.getDayOfYear()));
            break;
        case 2:
            // Composite -> Reproject
            folders.add(String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Composite", date.getYear(), date.getDayOfYear()));
            break;
        case 3:
            // Reproject -> Mask
            folders.add(String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Reproject", date.getYear(), date.getDayOfYear()));
            break;
        case 4:
            // Mask -> Clip
            folders.add(String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Mask", date.getYear(), date.getDayOfYear()));
            break;
        default:
            folders = null;
            break;
        }
        return folders.toArray(new String[folders.size()]);
    }

    @Override
    public String getOutputFolder(int stepId) {
        String outputFolder = "";

        // Format: Input (of this step) -> Output (of this step)
        switch(stepId)
        {
        case 1:
            // Download -> Composite
            outputFolder = String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Composite", date.getYear(), date.getDayOfYear());
            break;
        case 2:
            // Composite -> Reproject
            outputFolder = String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Reproject", date.getYear(), date.getDayOfYear());
            break;
        case 3:
            // Reproject -> Mask
            outputFolder = String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessWorkerTempDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Mask", date.getYear(), date.getDayOfYear());
            break;
        case 4:
            // Mask -> Clip
            outputFolder = String.format("%s"+ File.separator + "%s" + File.separator + "%04d" + File.separator+"%03d",
                    FileSystem.GetProcessOutputDirectoryPath(project.GetWorkingDir(), project.GetProjectName(), plugin.GetName(), ProcessName.PROCESSOR),
                    "Output", date.getYear(), date.getDayOfYear());
            break;
        default:
            outputFolder = null;
            break;
        }

        return outputFolder;
    }

    @Override
    public int[] getDataBands() {
        return new int[]
                {1,// air temperature (K) at 2 meters above the surface
                2, // specific humidity (kg/kg) at 2 meters above the surface
                //    surface pressure (Pa)
                //    U wind component (m/s) at 10 meters above the surface
                //    V wind component (m/s) at 10 meters above the surface
                //    surface downward longwave radiation (W/m^2)
                //    fraction of total precipitation that is convective (no units): from NARR
                //    CAPE: Convective Available Potential Energy (J/kg): from NARR
                //    potential evaporation (kg/m^2): from NARR
                10,// precipitation hourly total (kg/m^2)
                };//  surface downward shortwave radiation (W/m^2) -- bias-corrected
    }

    @Override
    public int[] getQCBands() {
        return null;
    }
}
