package santjoans.client.transaction.async;

public class TaskManager {
	
	static private TaskChain currentTask = null;
	
	static public void execute(TaskChain taskChain) {
		if (currentTask != null) {
			currentTask.cancel();
		}
		currentTask = taskChain;
		currentTask.execute();
	}
	
}
