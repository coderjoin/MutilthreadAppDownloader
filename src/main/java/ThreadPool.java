import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qiaoying on 2018/4/25.
 */
public class ThreadPool {

    private int threadNum;

    private AtomicInteger threadAlive = new AtomicInteger();

    private ExecutorService executorService;

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition condition = reentrantLock.newCondition();

    public ThreadPool(int threadNum){
        this.threadNum = threadNum;
        this.executorService = Executors.newFixedThreadPool(threadNum);
    }

    public ThreadPool(int threadNum,ExecutorService executorService){
        this.threadNum = threadNum;
        this.executorService = executorService;
    }

    public int getThreadAlive(){
        return threadAlive.get();
    }

    public void execute(final Runnable runnable){
        if (threadAlive.get() >= threadNum){
            try{
               reentrantLock.lock();
               while (threadAlive.get() >= threadNum){
                   try{
                      condition.await();
                   }catch (InterruptedException e){

                   }
               }
            } finally {
                reentrantLock.unlock();
            }
        }
        threadAlive.incrementAndGet();
        executorService.execute(new Runnable() {
            public void run() {
                try{
                    runnable.run();
                } finally {
                    try{
                        reentrantLock.lock();
                        threadAlive.decrementAndGet();
                        condition.signal();
                    } finally {
                        reentrantLock.unlock();
                    }
                }
            }
        });
    }

    public boolean isShutdown(){
        return executorService.isShutdown();
    }

    public void shutdown(){
        executorService.shutdown();
    }

}
