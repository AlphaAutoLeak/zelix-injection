package zelix.hack.hacks.xray.etc;

public class RefreshingJob
{
    public Runner refresher;
    public Thread runner;
    
    public RefreshingJob(final Runner refresher) {
        this.refresher = refresher;
        (this.runner = new Thread(refresher)).start();
    }
    
    public void cancel() {
        this.refresher.isRunning = false;
    }
}
