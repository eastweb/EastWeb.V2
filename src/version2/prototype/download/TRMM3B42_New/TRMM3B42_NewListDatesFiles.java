package version2.prototype.download.TRMM3B42_New;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import version2.prototype.Config;
import version2.prototype.DataDate;
import version2.prototype.ErrorLog;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.ProjectInfoMetaData.ProjectInfoFile;
import version2.prototype.ProjectInfoMetaData.ProjectInfoPlugin;
import version2.prototype.download.DownloadUtils;
import version2.prototype.download.ListDatesFiles;

// @Author: Yi Liu

public class TRMM3B42_NewListDatesFiles extends ListDatesFiles
{
    public TRMM3B42_NewListDatesFiles(DataDate startDate, DownloadMetaData data, ProjectInfoFile project) throws IOException
    {
        super(startDate, data, project);
    }

    @Override
    protected Map<DataDate, ArrayList<String>> ListDatesFilesFTP() {
        return null;
    }

    @Override
    protected Map<DataDate, ArrayList<String>> ListDatesFilesHTTP()
    {
        HashMap<DataDate, ArrayList<String>> tempMapDatesFiles =  new HashMap<DataDate, ArrayList<String>>();

        final String mHostURL = mData.myHttp.url;

        try
        {
            ByteArrayOutputStream folderOutStream = new ByteArrayOutputStream();
            DownloadUtils.downloadToStream(new URL(mHostURL), folderOutStream);

            List<String> availableYears = Arrays.asList(folderOutStream.toString().split("[\\r\\n]+"));

            outerLoop: for(String paramY : availableYears)
            {
                if(Thread.currentThread().isInterrupted())
                {
                    break;
                }

                String yearPattern = "(19|20)\\d\\d";
                String monthPattern = "(0?[1-9]|1[012])";

                //String datePattern = "((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])/";
                Pattern pattern = Pattern.compile(yearPattern);
                Matcher matcher = pattern.matcher(paramY);

                // URL structure:  host/yyyy/mm
                if(matcher.find())
                {
                    try
                    {
                        int year = Integer.parseInt(matcher.group());

                        if (year >= sDate.getYear())
                        {
                            String yearFolderURL = mHostURL + String.format("%04d", year);

                            ArrayList<String> monthList = new ArrayList<String>();

                            ByteArrayOutputStream monthFolderOutstream = new ByteArrayOutputStream();
                            DownloadUtils.downloadToStream(new URL(yearFolderURL), monthFolderOutstream);

                            List<String> availableMonths = Arrays.asList(monthFolderOutstream.toString().split("[\\r\\n]+"));

                            for(String paramM : availableMonths)
                            {
                                Pattern patternM = Pattern.compile(monthPattern);
                                Matcher matcherM = patternM.matcher(paramM);

                                if (matcherM.find())
                                {
                                    //System.out.println(paramM);
                                    try
                                    {
                                        //System.out.println(matcherM.group());
                                        int month = Integer.parseInt(matcherM.group());
                                        System.out.println("month: " + month);
                                    }
                                    catch(Exception e)
                                    {
                                        ErrorLog.add(Config.getInstance(), mData.Title, mData.name, "ModisListDatesFiles.ListDatesFilesHTTP problem while getting file list.", e);
                                    }
                                }
                            }

                        }
                    }
                    catch(Exception e)
                    {
                        ErrorLog.add(Config.getInstance(), mData.Title, mData.name, "ModisListDatesFiles.ListDatesFilesHTTP problem while getting file list.", e);
                    }
                }
            }

        }
        catch(Exception e)
        {
            ErrorLog.add(Config.getInstance(), mData.Title, mData.name, "ModisListDatesFiles.ListDatesFilesHTTP problem while getting file list.", e);
        }


        return tempMapDatesFiles;


    }
}