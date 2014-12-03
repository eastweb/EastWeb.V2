package version2.prototype.Scheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import version2.prototype.DataDate;
import version2.prototype.PluginMetaDataCollection.DownloadMetaData;
import version2.prototype.download.NldasDownloader;
import version2.prototype.download.cache.*;
import version2.prototype.ConfigReadException;
import version2.prototype.DirectoryLayout;
import version2.prototype.MetaData;


public class NldasDownloadTask implements Runnable {
    DataDate startDate;
    static final String downloadedCache =
            "C:\\Users\\jiameng\\Nldas\\downloaded.xml.gz";
    static final String onlineAvaliableCache =
            "C:\\Users\\jiameng\\Nldas\\avaliable.xml.gz";
    List<DataDate> available;
    List<DataDate> finished;
    DownloadMetaData mode;


    public NldasDownloadTask(DataDate dataDate, DownloadMetaData metaData) {
        startDate = dataDate;
        mode=metaData;

    }

    @Override
    public void run(){
        ArrayList<DataDate> download = null;
        try {
            download = getDownloadList();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // if no data need be downloaded,end the task
        if (download == null) {
            return;
        }

        for (DataDate item : download) {
            try {
                FileUtils.forceMkdir(getoutFile(item).getParentFile());
                new NldasDownloader(item, getoutFile(item),mode).download();
            } catch (Exception e) {
                // update the downloadedCache
                updateDownloadedCache();
                e.printStackTrace();
            }
            // add into finished
            finished.add(item);

        }
        // update the downloadedCache
        updateDownloadedCache();

    }

    private void updateDownloadedCache() {
        // update the downloadedCache
        final File file = new File(downloadedCache);
        try {
            FileUtils.forceMkdir(file.getParentFile());
            NldasCache cache =
                    new NldasCache(DataDate.today(), startDate, finished);

            cache.toFile(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stop() throws InterruptedException, IOException {
        this.wait();
        // update the downloadedCache
        final File file = new File(downloadedCache);
        FileUtils.forceMkdir(file.getParentFile());
        NldasCache cache =
                new NldasCache(DataDate.today(), startDate, finished);
        cache.toFile(file);
        stop();
    }

    private File getoutFile(DataDate date) throws ConfigReadException {

        return DirectoryLayout.getNldasDownload(date);
    }

    private ArrayList<DataDate> getDownloadList() throws IOException {
        available = updateAvailabeCache();
        finished = new ArrayList<DataDate>();
        try {
            finished =
                    NldasCache.fromFile(new File(downloadedCache)).getDates();
        } catch (IOException e) {

        }

        if (available == null) {
            return new ArrayList<DataDate>();
        } else if (finished == null) {
            return (ArrayList<DataDate>) available;
        } else {
            ArrayList<DataDate> modifiableAvailabe=new ArrayList<DataDate>(available);
            ArrayList<DataDate> modifiableFinished=new ArrayList<DataDate>(finished);
            Collections.sort(modifiableAvailabe);
            Collections.sort(modifiableFinished);
            available=modifiableAvailabe;
            finished=modifiableFinished;
            // compare available list and downloaded list, remove already downloaded date
            if(finished.isEmpty()){
                return (ArrayList<DataDate>) available;
            }else{
                int match = available.indexOf(finished.get(0));
                if (match == -1) {
                    return (ArrayList<DataDate>) available;
                } else {
                    for (int i = match; i < match + finished.size(); i++) {
                        available.remove(match);
                    }
                    return (ArrayList<DataDate>) available;
                }
            }

        }
    }

    private List<DataDate> updateAvailabeCache() {
        try {

            if (new File(onlineAvaliableCache).isFile()) {
                final NldasCache cache =NldasCache.fromFile(new File(onlineAvaliableCache));
                // Check freshness
                if (!CacheUtils.isFresh(cache)
                        || !cache.getStartDate().equals(startDate)) {
                    // Get a list of dates and construct a date cache
                    NldasCache updatedCache = getOnlineDataList();
                    // Write out the date cache
                    final File file = new File(onlineAvaliableCache);
                    FileUtils.forceMkdir(file.getParentFile());
                    updatedCache.toFile(file);
                    return updatedCache.getDates();
                } else {
                    return cache.getDates();
                }

            } else {
                // Get a list of dates and construct a date cache
                NldasCache updatedCache = getOnlineDataList();
                // Write out the date cache
                final File file = new File(onlineAvaliableCache);
                FileUtils.forceMkdir(file.getParentFile());
                updatedCache.toFile(file);
                return updatedCache.getDates();
            }

        } catch (IOException e) {

            return null;
        }

    }

    private NldasCache getOnlineDataList() throws IOException {
        // Get a list of dates and construct a date cache
        NldasCache updatedCache = null;
        try {
            updatedCache =
                    new NldasCache(DataDate.today(), startDate,
                            NldasDownloader.listDates(startDate));
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Write out the date cache
        final File file = new File(onlineAvaliableCache);
        FileUtils.forceMkdir(file.getParentFile());
        updatedCache.toFile(file);
        return updatedCache;
    }

}
