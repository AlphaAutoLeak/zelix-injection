package zelix.hack.hacks.xray.xray;

import zelix.hack.hacks.xray.etc.*;
import java.util.*;

public class AntiAntiXray
{
    public static List<RefreshingJob> jobs;
    
    public static void revealNewBlocks(final int radX, final int radY, final int radZ, final long delayInMS) {
        final RefreshingJob rfj = new RefreshingJob(new Runner(radX, radY, radZ, delayInMS));
        AntiAntiXray.jobs.add(rfj);
    }
    
    static {
        AntiAntiXray.jobs = new ArrayList<RefreshingJob>();
    }
}
