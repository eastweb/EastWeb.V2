package test.download.ModisNBAR_V6;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import version2.prototype.Config;
import version2.prototype.DataDate;
//import version2.prototype.PluginMetaData.PluginMetaDataCollection;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.PluginMetaData.FTP;
import version2.prototype.PluginMetaData.HTTP;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.download.ListDatesFiles;
import version2.prototype.download.RegistrationException;
//import version2.prototype.download.ModisNBAR.ModisNBARGlobalDownloader;
import version2.prototype.download.ModisNBAR.ModisNBARListDatesFiles;
//import version2.prototype.download.ModisNBAR.ModisNBARQCGlobalDownloader;
import version2.prototype.download.ModisNBAR_V6.ModisNBARQCGlobalDownloader_V6;


public class TestModisNABRQCGlobalDownloader_V6 {
    private static Config configInstance = Config.getAnInstance("src/test/config.xml");

    private static DownloadMetaData data;
    private static ProjectInfoFile p;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        String mode = "HTTP";// the protocol type: ftp or http
        FTP myFtp = null;
        HTTP myHttp = new HTTP("http://e4ftl01.cr.usgs.gov/MOTA/MCD43A4.006/");;
        // String className = null;
        String timeZone = null;
        int filesPerDay = -1;
        String datePatternStr = "\\d{4}";
        String Title="ModisNBAR_V6";
        ArrayList<String> QualityControlMetaData = null;
        int DaysPerInputData = 16;
        int Resolution = 500;
        Boolean CompositesContinueIntoNextYear = false;
        ArrayList<String> ExtraDownloadFiles = null;// or QC in plugin_ModisNBAR_V6 ExtraDownloadFiles name is QC
        LocalDate originDate = null ;
        String downloadFactoryClassName = "ModisNBARFactory_V6";// or null because the string className = null

        String fileNamePatternStr = "MCD43A2.A(\\d{7}).h(\\d{2})v(\\d{2}).006.(\\d{13}).hdf";

        // Just for test
        originDate = LocalDate.parse("Sun Mar 01 00:00:01 CDT 2015", DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu"));

        data = new DownloadMetaData(Title,QualityControlMetaData,DaysPerInputData, Resolution, CompositesContinueIntoNextYear, ExtraDownloadFiles,mode,myFtp,myHttp,
                downloadFactoryClassName,timeZone, filesPerDay,datePatternStr,fileNamePatternStr, originDate);
        p = new ProjectInfoFile(configInstance, "C:\\Users\\Shihan\\Desktop\\EastWeb.V2\\projects\\test NBAR_V6.xml");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        data = null;
    }

    @Test
    public void testRun() throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException, SQLException, RegistrationException {
        ListDatesFiles ldf= new ModisNBARListDatesFiles(new DataDate(data.originDate), data, p);
        LocalDate startDate = LocalDate.now().minusDays(14);

        ModisNBARQCGlobalDownloader_V6 ttd = new ModisNBARQCGlobalDownloader_V6(1, Config.getAnInstance("C:\\Users\\Shihan\\Desktop\\EastWeb.V2\\src\\test\\config.xml"), "ModisNBAR_V6",  data,  ldf, startDate);
        ttd.run();
    }

}