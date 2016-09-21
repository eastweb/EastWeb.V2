package version2.prototype.download.TRMM3B42_New;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.PluginMetaData.FTP;
import version2.prototype.PluginMetaData.HTTP;

public class TestTRMM_NewListDates
{
    static public void main(String [ ] args)
    {
        String mode = "http";// the protocol type: ftp or http
        FTP myFtp = null;
        HTTP myHttp = new HTTP("http://disc2.gesdisc.eosdis.nasa.gov/data/TRMM_L3/TRMM_3B42_Daily.7/");
        String className = null;
        String timeZone = null;
        int filesPerDay = 1;
        String datePatternStr = "\\d{4}";
        String fileNamePatternStr =
                "3B42_daily\\.(\\d{4}d{2}d{2})\\.7\\.nc4";

        //LocalDate ld = LocalDate.parse("Wed Sep 14 00:00:01 CDT 2016", DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu"));
        LocalDate ld = LocalDate.parse("Wed Sep 14 00:00:01 CDT 2016", DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu"));

        DownloadMetaData data = new DownloadMetaData(null, null, null, 25000, true, null,
                mode, myFtp, myHttp, className, timeZone, filesPerDay,
                datePatternStr, fileNamePatternStr, ld);

        /* String Title, ArrayList<String> QualityControlMetaData,
        Integer DaysPerInputData, Integer Resolution,
        Boolean CompositesContinueIntoNextYear,
        ArrayList<String> ExtraDownloadFiles,
        String mode, FTP myFtp, HTTP myHttp,
        String downloadFactoryClassName, String timeZone,
        int filesPerDay, String datePatternStr, String fileNamePatternStr,
        LocalDate originDate
         */

        TRMM3B42_NewListDatesFiles iList;
        try {
            iList = new TRMM3B42_NewListDatesFiles(new DataDate(data.originDate), data, null);
            Map<DataDate, ArrayList<String>> tempDatesFiles = iList.CloneListDatesFiles();

            for (Map.Entry<DataDate, ArrayList<String>> entry : tempDatesFiles.entrySet())
            {
                System.out.println(entry.getKey() + ":/ " +  entry.getValue());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}