/**
 *
 */
package version2.prototype;

/**
 * @author michael.devos
 *
 */
public interface WebServiceI {
    public String[] GetProjects();

    public Boolean getProjectStatus(String projectName);

    public String[] getPluginsForProject(String projectName);

    public String[] getIndicesForProject(String projectName);
	
    public String[] getIndicesForProject(String projectName, String pluginName);

    public String[] getProjectSummaryInfo(String projectName);

    public String[] getProjectOutputListing(String projectName, String pluginName, String index, Integer summaryIDNum);

    public String[] getFile(String projectName, String pluginName, String index, int year, int dayOfyear, Integer summaryIDNum, String fileName);
}
