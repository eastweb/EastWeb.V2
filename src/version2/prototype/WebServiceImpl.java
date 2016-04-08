package version2.prototype;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import version2.prototype.Scheduler.SchedulerStatus;

@WebService(endpointInterface = "version2.prototype.WebServiceI")
public class WebServiceImpl implements WebServiceI {

    public WebServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String Test()
    {
        return "Hello World";
    }

    @Override
    public String[] GetProjects() {
        String[] projects = null;

        synchronized(EASTWebManager.schedulerStatuses)
        {
            projects = new String[EASTWebManager.schedulerStatuses.size()];
            for(int i = 0; i < EASTWebManager.schedulerStatuses.size(); i++) {
                projects[i] = EASTWebManager.schedulerStatuses.get(i).ProjectName;
            }
        }

        return projects;
    }

    @Override
    public Boolean getProjectStatus(String projectName) {
        boolean goal = false;
        synchronized(EASTWebManager.schedulerStatuses)
        {
            for(SchedulerStatus status : EASTWebManager.schedulerStatuses) {
                if(status.ProjectName.equals(projectName)) {
                    goal = (status.State == TaskState.RUNNING || status.State == TaskState.STARTED || status.State == TaskState.STARTING) ? true : false;
                }
            }
        }
        return goal;
    }

    @Override
    public String[] getPluginsForProject(String projectName) {
        String[] plugins = null;
        synchronized(EASTWebManager.schedulerStatuses)
        {
            for(SchedulerStatus status : EASTWebManager.schedulerStatuses) {
                if(status.ProjectName.equals(projectName)) {
                    plugins = new String[status.PluginInfo.size()];
                    for (int i = 0; i < status.PluginInfo.size(); i++) {
                        plugins[i] = status.PluginInfo.get(i).GetName();
                    }
                }
            }
        }
        return plugins;
    }

    @Override
    public String[] getIndicesForProject(String projectName) {
        ArrayList<String> pluginIndicies = new ArrayList<String>();
        synchronized(EASTWebManager.schedulerStatuses)
        {
            for(SchedulerStatus status : EASTWebManager.schedulerStatuses) {
                if(status.ProjectName.equals(projectName)){
                    for (int i = 0; i < status.PluginInfo.size(); i++) {
                        for(String index : status.PluginInfo.get(i).GetIndices()) {
                            pluginIndicies.add(index);
                        }
                    }
                }
            }
        }
        return pluginIndicies.stream().toArray(String[]::new);
    }

    @Override
    public String[] getIndicesForProjectWithPluginName(String projectName, String pluginName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getProjectSummaryInfo(String projectName) {
        String[] summaryInfo = null;
        synchronized(EASTWebManager.schedulerStatuses)
        {
            for(SchedulerStatus status : EASTWebManager.schedulerStatuses) {
                if(status.ProjectName.equals(projectName)) {
                    summaryInfo = new String[status.Summaries.size()];
                    for(int i = 0; i < status.Summaries.size(); i++) {
                        summaryInfo[i] = status.Summaries.get(i).toString();
                    }
                }
            }
        }
        return summaryInfo;
    }

    @Override
    public String[] getProjectOutputListing(String projectName, String pluginName, String index, Integer summaryIDNum) {
        String[] outputListing = null;
        synchronized(EASTWebManager.schedulerStatuses)
        {

        }
        return outputListing;
    }

    @Override
    public String[] getFile(String projectName, String pluginName, String index, int year, int dayOfyear, Integer summaryIDNum, String fileName) {
        String[] fileContents = null;
        synchronized(EASTWebManager.schedulerStatuses)
        {

        }
        return fileContents;
    }

}
