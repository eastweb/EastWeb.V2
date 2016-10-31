package version2.prototype.download.ModisNBAR_V6;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import version2.prototype.Config;
import version2.prototype.DataDate;
import version2.prototype.ErrorLog;
import version2.prototype.PluginMetaData.DownloadMetaData;
import version2.prototype.download.DownloadFailedException;
import version2.prototype.download.GlobalDownloader;
import version2.prototype.download.ListDatesFiles;
import version2.prototype.download.RegistrationException;
import version2.prototype.download.ModisNBAR.ModisNBARDownloader;
import version2.prototype.util.DataFileMetaData;
import version2.prototype.util.DatabaseConnection;
import version2.prototype.util.DatabaseConnector;
import version2.prototype.util.DownloadFileMetaData;
import version2.prototype.util.FileSystem;

/**
 * @author Yi Liu
 *
 */
public class ModisNBARGlobalDownloader_V6 extends GlobalDownloader
{
    public ModisNBARGlobalDownloader_V6(int myID, Config configInstance, String pluginName, DownloadMetaData metaData, ListDatesFiles listDatesFiles, LocalDate startDate) throws ClassNotFoundException, ParserConfigurationException, SAXException,
    IOException, SQLException, RegistrationException {
        super(myID, configInstance, pluginName, metaData, listDatesFiles, startDate);
        // TODO Auto-generated constructor stub
    }

    @Override
    /*
     * Step 1: get all downloads from ListDatesFiles
     * Step 2: Pull all cached downloads
     * Step 3: Remove already downloaded files from ListDatesFiles
     * Step 4: Create downloader and run downloader for all that's left
     */
    public void run()
    {
        DatabaseConnection con = DatabaseConnector.getConnection(configInstance);
        if(con == null) {
            return;
        }
        Statement stmt;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while attempting to handle download.", e);
            return;
        }

        // Step 1: get all downloads from ListDatesFiles
        Map<DataDate, ArrayList<String>> datesFiles = listDatesFiles.CloneListDatesFiles();

        // Step 2: Pull all cached downloads
        ArrayList<DataFileMetaData> cachedD = new ArrayList<DataFileMetaData>();

        try {
            cachedD = GetAllDownloadedFiles();
        } catch (ClassNotFoundException | SQLException | ParserConfigurationException | SAXException | IOException | RegistrationException e) {
            ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while setting up current list of missing download files.", e);
        }

        // Step 3: Remove already downloaded files from ListDatesFiles
        for (DataFileMetaData d: cachedD)
        {
            DownloadFileMetaData downloaded =  d.ReadMetaDataForProcessor();

            // get the year and dayOfyear from each downloaded file
            DataDate thisDate = new DataDate( downloaded.day, downloaded.year);

            // get the files associated with the date in the ListDatesFiles
            ArrayList <String> files = datesFiles.get(thisDate);

            Iterator<String> fIter = files.iterator();

            while (fIter.hasNext())
            {
                String strPath = downloaded.dataFilePath;
                strPath = strPath.substring(strPath.lastIndexOf(File.separator)+1, strPath.lastIndexOf("."));

                // remove the file if it is found in the downloaded list
                if ((fIter.next().toLowerCase()).contains((strPath.toLowerCase())))
                {
                    fIter.remove();
                }
            }

            datesFiles.put(thisDate, files);
        }


        // Step 4
        for(Map.Entry<DataDate, ArrayList<String>> entry : datesFiles.entrySet())
        {
            String outFolder;

            try {
                outFolder = FileSystem.GetGlobalDownloadDirectory(configInstance, pluginName, metaData.name);

                DataDate dd = entry.getKey();

                for (String f : entry.getValue())
                {

                    if (f != null)
                    {
                        ModisNBARDownloader downloader = new ModisNBARDownloader(dd, outFolder, metaData, f);

                        try{
                            downloader.download();
                        } catch (DownloadFailedException e) {
                            ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while running ModisNBARDownloader.", e);
                        } catch (Exception e) {
                            ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while running ModisNBARDownloader.", e);
                        }

                        try {
                            AddDownloadFile(stmt, dd.getYear(), dd.getDayOfYear(), downloader.getOutputFilePath());
                        } catch (ClassNotFoundException | SQLException | ParserConfigurationException | SAXException | IOException | RegistrationException e) {
                            ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while attempting to add download file.", e);
                        }
                    }

                }
            } catch (IOException e) {
                ErrorLog.add(Config.getInstance(), pluginName, metaData.name, "ModisNBARGlobalDownloader.run problem while attempting to handle download.", e);
            }
        }
        try {
            stmt.close();
        } catch (SQLException e) { /* do nothing */ }
        con.close();
    }

}


