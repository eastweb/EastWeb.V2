package test.download.NldasNOAH;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.PluginMetaDataCollection;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.DownloadMetaData;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.FTP;
import version2.prototype.PluginMetaData.PluginMetaDataCollection.HTTP;
import version2.prototype.download.ListDatesFiles;
import version2.prototype.download.NldasNOAH.NldasNOAHGlobalDownloader;
import version2.prototype.download.NldasNOAH.NldasNOAHListDatesFiles;

public class TestNldasNOAHGlobalDownloader {

    private static DownloadMetaData data;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String mode = "FTP";// the protocol type: ftp or http
        FTP myFtp = PluginMetaDataCollection.CreateFTP("hydro1.sci.gsfc.nasa.gov",
                "/data/s4pa/NLDAS/NLDAS_NOAH0125_H.002", "anonymous", "anonymous");
        HTTP myHttp = null;
        String className = null;
        String timeZone = null;
        int filesPerDay = 24;
        String datePatternStr = "\\d{4}";
        String fileNamePatternStr = "NLDAS_NOAH0125_H\\.A(\\d{4})(\\d{2})(\\d{2})\\.(\\d{4})\\.002\\.grb";
        LocalDate ld = LocalDate.parse("Wed Jul 15 00:00:01 CDT 2015", DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu"));
        data = PluginMetaDataCollection.CreateDownloadMetaData(mode, myFtp, myHttp, className, timeZone, filesPerDay, datePatternStr, fileNamePatternStr, ld);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        data = null;
    }

    @Test
    public void testRun() throws IOException {
        ListDatesFiles ldf= new NldasNOAHListDatesFiles(new DataDate(data.originDate), data);

        NldasNOAHGlobalDownloader ttd = new NldasNOAHGlobalDownloader(1,"NLDASNOAH",  data,  ldf);

        ttd.run();

    }

}
