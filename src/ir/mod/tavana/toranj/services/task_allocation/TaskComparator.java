package ir.mod.tavana.toranj.services.task_allocation;

import java.util.Comparator;


public class TaskComparator implements Comparator<Task> {
	public int compare(Task t1, Task t2)
    {
        if (t1.getProfit() < t2.getProfit())
        {
            return -1;
        }
        if (t1.getProfit() > t2.getProfit())
        {
            return 1;
        }
        return 0;
    }
}
