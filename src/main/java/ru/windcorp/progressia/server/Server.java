package ru.windcorp.progressia.server;

import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;

import ru.windcorp.jputil.functions.ThrowingRunnable;
import ru.windcorp.progressia.common.util.TaskQueue;
import ru.windcorp.progressia.common.world.WorldData;
import ru.windcorp.progressia.server.comms.ClientManager;
import ru.windcorp.progressia.server.world.WorldLogic;
import ru.windcorp.progressia.server.world.tasks.WorldAccessor;
import ru.windcorp.progressia.server.world.ticking.Change;
import ru.windcorp.progressia.server.world.ticking.Evaluation;

public class Server {
	
	/**
	 * Returns the {@link Server} instance whose main thread is the current thread.
	 * @return the server that operates in this thread
	 */
	public static Server getCurrentServer() {
		return ServerThread.getCurrentServer();
	}
	
	private final WorldLogic world;
	private final WorldAccessor worldAccessor = new WorldAccessor(this);
	
	private final ServerThread serverThread;
	
	private final ClientManager clientManager = new ClientManager(this);
	
	private final TaskQueue taskQueue = new TaskQueue(this::isServerThread);
	
	private final TickingSettings tickingSettings = new TickingSettings();
	
	public Server(WorldData world) {
		this.world = new WorldLogic(world, this);
		this.serverThread = new ServerThread(this);
		
		schedule(this::scheduleChunkTicks);
	}
	
	/**
	 * Returns this server's world.
	 * @return this server's {@link WorldLogic}
	 */
	public WorldLogic getWorld() {
		return world;
	}
	
	/**
	 * Returns this server's {@link ClientManager}.
	 * Use this to deal with communications, e.g. send packets.
	 * @return the {@link ClientManager} that handles this server
	 */
	public ClientManager getClientManager() {
		return clientManager;
	}
	
	/**
	 * Checks if this thread is the main thread of this server.
	 * @return {@code true} iff the invocation occurs in server main thread
	 */
	public boolean isServerThread() {
		return getCurrentServer() == this;
	}
	
	/**
	 * Requests that the provided task is executed once on next server tick.
	 * The task will be run in the main server thread. The task object is
	 * discarded after execution.
	 * 
	 * <p>Use this method to request a one-time (rare) action that must necessarily
	 * happen in the main server thread, such as initialization tasks or reconfiguration.
	 * 
	 * @param task the task to run
	 * @see #invokeNow(Runnable)
	 * @see #schedule(Consumer)
	 */
	public void invokeLater(Runnable task) {
		taskQueue.invokeLater(task);
	}

	/**
	 * Executes the tasks in the server main thread as soon as possible.
	 * 
	 * <p>If this method is invoked in the server main thread, then the task is
	 * run immediately (the method blocks until the task finishes). Otherwise
	 * this method behaves exactly like {@link #invokeLater(Runnable)}.
	 * 
	 * <p>Use this method to make sure that a piece of code is run in the main server
	 * thread.
	 * 
	 * @param task the task to run
	 * @see #invokeLater(Runnable)
	 * @see #schedule(Consumer)
	 */
	public void invokeNow(Runnable task) {
		taskQueue.invokeNow(task);
	}

	public <E extends Exception> void waitAndInvoke(
			ThrowingRunnable<E> task
	) throws InterruptedException, E {
		taskQueue.waitAndInvoke(task);
	}
	
	public void schedule(Consumer<Server> task) {
		taskQueue.schedule(() -> task.accept(this));
	}
	
	public void requestChange(Change change) {
		serverThread.getTicker().requestChange(change);
	}
	
	public void requestEvaluation(Evaluation evaluation) {
		serverThread.getTicker().requestEvaluation(evaluation);
	}
	
	/**
	 * Returns the duration of the last server tick. Server logic should assume that this much in-world time has passed.
	 * @return the length of the last server tick
	 */
	public double getTickLength() {
		return this.serverThread.getTicker().getTickLength();
	}
	
	public double getTPS() {
		return this.serverThread.getTicker().getTPS();
	}
	
	/**
	 * Returns the {@link WorldAccessor} object for this server. Use the provided accessor to
	 * request common {@link Evaluation}s and {@link Change}s.
	 * @return a {@link WorldAccessor}
	 * @see #requestChange(Change)
	 * @see #requestEvaluation(Evaluation)
	 */
	public WorldAccessor getWorldAccessor() {
		return worldAccessor;
	}
	
	/**
	 * Returns the ticking settings for this server.
	 * @return a {@link TickingSettings} object
	 */
	public TickingSettings getTickingSettings() {
		return tickingSettings;
	}

	/**
	 * Starts the server. This method blocks until the server enters normal operation or fails to start.
	 */
	public void start() {
		this.serverThread.start();
	}
	
	/**
	 * Performs the tasks from tasks queues and repeating tasks.
	 */
	public void tick() {
		taskQueue.runTasks();
	}
	
	/**
	 * Shuts the server down, disconnecting the clients with the provided message.
	 * This method blocks until the shutdown is complete.
	 * @param message the message to send to the clients as the disconnect reason
	 */
	public void shutdown(String message) {
		LogManager.getLogger().warn("Server.shutdown() is not yet implemented");
		serverThread.stop();
	}
	
	private void scheduleChunkTicks(Server server) {
		server.getWorld().getChunks().forEach(chunk -> requestEvaluation(chunk.getTickTask()));
	}

	/**
	 * Returns an instance of {@link java.util.Random Random} that can be used as a source of indeterministic
	 * randomness. World generation and other algorithms that must have random but reproducible results should
	 * not use this.
	 * @return a thread-safe indeterministic instance of {@link java.util.Random}.
	 */
	public java.util.Random getAdHocRandom() {
		return java.util.concurrent.ThreadLocalRandom.current();
	}

}
