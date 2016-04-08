/**
 *
 */
package version2.prototype;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 * @author michael.devos
 *
 */
@WebService
public interface WebServiceI {
    @WebMethod
    public String Test();

    @WebMethod
    public String[] GetProjects();

    @WebMethod
    public Boolean getProjectStatus(String projectName);

    @WebMethod
    public String[] getPluginsForProject(String projectName);

    @WebMethod
    public String[] getIndicesForProject(String projectName);

    @WebMethod
    public String[] getIndicesForProjectWithPluginName(String projectName, String pluginName);

    @WebMethod
    public String[] getProjectSummaryInfo(String projectName);

    @WebMethod
    public String[] getProjectOutputListing(String projectName, String pluginName, String index, Integer summaryIDNum);

    @WebMethod
    public String[] getFile(String projectName, String pluginName, String index, int year, int dayOfyear, Integer summaryIDNum, String fileName);
}
