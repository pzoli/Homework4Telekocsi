import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class VoidDispatchService extends AbstractExecutorService {
	private boolean running = false;

	public VoidDispatchService() {
		running = true;
	}

	@Override
	public void shutdown() {
		running = false;
	}

	@Override
	public boolean isShutdown() {
		return !running;
	}

	@Override
	public boolean isTerminated() {
		return !running;
	}

	@Override
	public void execute(Runnable r) {
		r.run();
	}

	@Override
	public List<Runnable> shutdownNow() {
		running = false;
		return new ArrayList<Runnable>(0);
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return true;
	}
}