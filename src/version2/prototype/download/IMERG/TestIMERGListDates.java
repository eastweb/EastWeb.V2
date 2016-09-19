package version2.prototype.download.IMERG;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


import version2.prototype.DataDate;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.PluginMetaData.FTP;
import version2.prototype.PluginMetaData.HTTP;

public class TestIMERGListDates
{
    static public void main(String [ ] args)
    {
        String mode = "FTP";// the protocol type: ftp or http
        FTP myFtp = new FTP("arthurhou.pps.eosdis.nasa.gov", "/gpmdata",
                "eastweb.system@gmail.com", "eastweb.system@gmail.com");
        HTTP myHttp = null;
        String className = null;
        String timeZone = null;
        int filesPerDay = 1;
        String datePatternStr = "\\d{4}";
        String fileNamePatternStr =
                "3B-DAY-GIS\\.MS\\.MRG\\.3IMERG\\.(\\d{4}\\d{2}\\d{2})-S000000-E235959\\.(\\d{4})\\.V03D\\.tif";

        LocalDate ld = LocalDate.parse("Fri Jan 01 00:00:01 CDT 2016", DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu"));

        DownloadMetaData data = new DownloadMetaData(null, null, null, 10000, true, null,
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

        IMERGListDatesFiles iList;
        try {
            iList = new IMERGListDatesFiles(new DataDate(data.originDate), data, null);
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
