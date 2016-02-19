/**
 *
 */
package version2.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author michael.devos
 *
 */
public interface WebServiceI {
    public ArrayList<String> GetProjects();

    public Boolean GetProjectStatus(String projectName);

    public ArrayList<String> GetPluginsForProject(String projectName);

    public ArrayList<String> GetProjectSummaryInfo(String projectName);

    public HashMap<LocalDate, String> GetProjectOutputListing(String pluginName, Integer summaryIDNum);

    public ArrayList<String> GetFile(String projectName, String pluginName, Integer summaryIDNum, String fileName);
}
