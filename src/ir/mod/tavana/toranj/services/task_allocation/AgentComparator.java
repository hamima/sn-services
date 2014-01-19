package ir.mod.tavana.toranj.services.task_allocation;

import java.util.Comparator;


public class AgentComparator implements Comparator<AgentTask> {
	public int compare(AgentTask a1, AgentTask a2)
    {
        if (a1.getBid() < a2.getBid())
        {
            return -1;
        }
        if (a1.getBid() > a2.getBid())
        {
            return 1;
        }
        return 0;
    }

}
