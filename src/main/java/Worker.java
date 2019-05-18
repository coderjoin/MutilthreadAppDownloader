import entity.Appinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qiaoying on 2018/4/25.
 */

public class Worker implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ThreadPool threadPool;

    protected ExecutorService executorService;

    protected int threadNum = 1;

    protected ApkStorage apkStorage;

    protected List<Appinfo> startUrls;

    //protected Scheduler scheduler = new DownloadScheduler();

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    protected boolean exitWhenComplete = true;

    protected boolean destroyWhenExit = true;

    private int emptySleepTime = 30000;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    private BlockingQueue<Appinfo> queue = new LinkedBlockingQueue<Appinfo>();


    public static Worker create(ApkStorage apkStorage){
        return new Worker(apkStorage);
    }

    public Worker(ApkStorage apkStorage){
        this.apkStorage = apkStorage;
    }

    public  Worker startUrl(List<Appinfo> startUrls){
        for (int i = 0,length = startUrls.size(); i < length; i++){
            //scheduler.push(startUrls.get(i));
            queue.add(startUrls.get(i));
        }
        System.out.println(queue.size());
        return this;
    }

    public  Worker thread(int threadNum){
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }



    public void run() {
        checkRunningStat();
        if (threadPool == null || threadPool.isShutdown()){
            if (executorService != null && !executorService.isShutdown()){
                threadPool = new ThreadPool(threadNum,executorService);
            }else {
                threadPool = new ThreadPool(threadNum);
            }
        }
        //threadPool = new ThreadPool(threadNum);

        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {

             final Appinfo appinfo = queue.poll();
             //logger.info("pkgName:"+apkDownloadInfo.getPkgName());

            if (appinfo == null) {
                if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                    break;
                }
                // wait until new url added
                waitNewUrl();
            } else {
                threadPool.execute(new Runnable() {

                    public void run() {
                        try {

                            processRequest(appinfo);
                            logger.info("process request " + appinfo.getId() + " ok");
                            //onSuccess(request);
                        } catch (Exception e) {
                            //onError(request);
                            logger.error("process request " + appinfo.getId() + " error", e);
                        } finally {
                            //pageCount.incrementAndGet();
                            signalNewUrl();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        // release some resources
        if (destroyWhenExit) {
            close();
        }
        //logger.info("Worker {} closed! {} pages downloaded.", getUUID(), pageCount.get());
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    public void close() {

        threadPool.shutdown();
    }

    private void processRequest(Appinfo appinfo){
        try {
            apkStorage.saveApk(appinfo.getId(),appinfo.getAppdownurl());
        } catch (IOException e){

        }

    }


}
