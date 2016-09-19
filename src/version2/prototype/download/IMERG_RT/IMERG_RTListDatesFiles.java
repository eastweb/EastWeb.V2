package version2.prototype.download.IMERG_RT;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import version2.prototype.Config;
import version2.prototype.DataDate;
import version2.prototype.ErrorLog;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.download.ConnectionContext;
import version2.prototype.download.ListDatesFiles;

public class IMERG_RTListDatesFiles extends ListDatesFiles{

    public IMERG_RTListDatesFiles(DataDate date, DownloadMetaData data, ProjectInfoFile project) throws IOException
    {
        super(date, data, project);
    }


    @Override
    protected Map<DataDate, ArrayList<String>> ListDatesFilesHTTP()
    {
        return null;
    }

    @Override
    protected Map<DataDate, ArrayList<String>> ListDatesFilesFTP()
    {
        Map<DataDate, ArrayList<String>>  tempMapDatesToFiles = new HashMap<DataDate, ArrayList<String>>();
        //System.out.println(sDate);
        final Pattern yearDirPattern = Pattern.compile("\\d{4}");
        final Pattern monthDirPattern = Pattern.compile("\\d{2}");
        final Pattern dayDirPattern = Pattern.compile("\\d{2}");

        FTPClient ftpC = null;

        try
        {
            ftpC = (FTPClient) ConnectionContext.getConnection(mData);
        }
        catch (ConnectException e)
        {
            System.out.println("Can't connect to download website, please check your URL.");
            return null;
        }

        String mRoot = mData.myFtp.rootDir;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try
        {
            if (!ftpC.changeWorkingDirectory(mRoot))
            {
                throw new IOException("Couldn't navigate to directory: " + mRoot);
            }

            tempMapDatesToFiles =  new HashMap<DataDate, ArrayList<String>>();

            outerLoop: for (FTPFile yearFile : ftpC.listFiles())
            {
                if(Thread.currentThread().isInterrupted()) {
                    break;
                }

                if (!yearFile.isDirectory()
                        || !yearDirPattern.matcher(yearFile.getName()).matches()) {
                    continue;
                }

                int year = Integer.parseInt(yearFile.getName());

                System.out.println(sDate.getYear());
                if (year < sDate.getYear()) {
                    continue;
                }

                /* if it is the current year,
                the files for download are stored in the folder in month (01-12)
                under the directory without year folder
                 if (year < currentYear) -  go for the folder of yyyy/mm
                 */
                String newDir = mRoot;
                if (year < currentYear)
                {
                    // add year folder
                    newDir =
                            String.format("%s/%s", mRoot, yearFile.getName());

                    if (!ftpC.changeWorkingDirectory(newDir)) {
                        throw new IOException(
                                "Couldn't navigate to directory: " + newDir);
                    }
                }

                for(FTPFile monthFile : ftpC.listFiles())
                {
                    if (!monthFile.isDirectory()
                            || !dayDirPattern.matcher(monthFile.getName()).matches()) {
                        continue;
                    }

                    System.out.println(monthFile.getName());
                    String monthDir =
                            String.format("%s/%s", newDir, monthFile.getName());

                    if (!ftpC.changeWorkingDirectory(monthDir)) {
                        throw new IOException(
                                "Couldn't navigate to directory: " + monthDir);
                    }

                    for (FTPFile dayFile : ftpC.listFiles())
                    {
                        if (!monthFile.isDirectory()
                                || !dayDirPattern.matcher(monthFile.getName()).matches()) {
                            continue;
                        }

                        ArrayList<String> fileNames = new ArrayList<String>();

                        for (FTPFile file : ftpC.listFiles())
                        {
                            if(Thread.currentThread().isInterrupted()) {
                                break outerLoop;
                            }

                            // files of all the days in a month are stored under the month folder
                            /*if (mData.fileNamePattern.matcher(file.getName()).matches())
                            {
                                System.out.println(file.getName());
                            }*/
                            if (file.isFile() &&
                                    mData.fileNamePattern.matcher(file.getName()).matches())
                            {
                                /* pattern of IMERG_RT
                                 * 3B-HHR-L\.MS\.MRG\.3IMERG\.(\d{4}\d{2}\d{2})-S233000-E235959\.1410\.V03E\.1day\.tif((\.gz){0,1})
                                 */

                                fileNames.add(file.getName());

                                String[] str = file.getName().split("[.]");

                                final int month = Integer.parseInt(str[4].substring(4, 6));
                                final int day = Integer.parseInt(str[4].substring(6, 8));
                                // always get the last hour of the day -  23
                                DataDate dataDate = new DataDate(23, day, month, year);
                                if (dataDate.compareTo(sDate) >= 0)
                                {
                                    tempMapDatesToFiles.put(dataDate, fileNames);
                                }
                            }
                        }
                    }
                }

            }

            ftpC.disconnect();
            ftpC = null;
            return tempMapDatesToFiles;
        }
        catch (Exception e)
        {
            ErrorLog.add(Config.getInstance(), "IMERG_RT", mData.name, "IMERG_RTListDatesFiles.ListDatesFilesFTP problem while creating list using FTP.", e);
            return null;
        }

    }

}
