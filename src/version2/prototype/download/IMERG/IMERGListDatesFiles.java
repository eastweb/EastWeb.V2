package version2.prototype.download.IMERG;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
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

public class IMERGListDatesFiles extends ListDatesFiles{

    public IMERGListDatesFiles(DataDate date, DownloadMetaData data, ProjectInfoFile project) throws IOException
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
                if (year < sDate.getYear()) {
                    continue;
                }

                // List days in this year
                String yearDirectory =
                        String.format("%s/%s", mRoot, yearFile.getName());

                if (!ftpC.changeWorkingDirectory(yearDirectory)) {
                    throw new IOException(
                            "Couldn't navigate to directory: " + yearDirectory);
                }

                for(FTPFile monthFile : ftpC.listFiles())
                {
                    if (!monthFile.isDirectory()
                            || !dayDirPattern.matcher(monthFile.getName()).matches()) {
                        continue;
                    }

                    String monthDirectory =
                            String.format("%s/%s", yearDirectory, monthFile.getName());

                    if (!ftpC.changeWorkingDirectory(monthDirectory)) {
                        throw new IOException(
                                "Couldn't navigate to directory: " + monthDirectory);
                    }

                    for (FTPFile dayFile : ftpC.listFiles())
                    {
                        if (!monthFile.isDirectory()
                                || !dayDirPattern.matcher(monthFile.getName()).matches()) {
                            continue;
                        }

                        String dayDirectory =
                                String.format("%s/%s", yearDirectory, dayFile.getName());

                        if (!ftpC.changeWorkingDirectory(dayDirectory)) {
                            throw new IOException(
                                    "Couldn't navigate to directory: " + dayDirectory);
                        }

                        ArrayList<String> fileNames = new ArrayList<String>();

                        for (FTPFile file : ftpC.listFiles())
                        {
                            if(Thread.currentThread().isInterrupted()) {
                                break outerLoop;
                            }

                            if (file.isFile() &&
                                    mData.fileNamePattern.matcher(file.getName()).matches())
                            {
                                /* pattern of IMERG
                                 * {productname}.A%y4%m2%d2.%h4.002.grb
                                 *
                                 * 3B-DAY-GIS\.MS\.MRG\.3IMERG\.(\d{4}\d{2}\d{2})-S000000-E235959\.(\d{4})\.V03D\.tif
                                 */

                                fileNames.add(file.getName());
                                ------------------------------
                                String[] strings = file.getName().split("[.]");
                                final int month = Integer.parseInt(strings[1].substring(5, 7));
                                final int day = Integer.parseInt(strings[1].substring(7, 9));
                                final int hour = Integer.parseInt(strings[2]);
                                DataDate dataDate = new DataDate(hour, day, month, year);
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
            ErrorLog.add(Config.getInstance(), "NldasNOAH", mData.name, "NldasNOAHListDatesFiles.ListDatesFilesFTP problem while creating list using FTP.", e);
            return null;
        }

    }

}
