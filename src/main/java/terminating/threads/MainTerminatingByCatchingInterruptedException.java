package terminating.threads;

public class MainTerminatingByCatchingInterruptedException {
    public static void main( String[] args ) {
        Thread blockingTask = new Thread(new BlockingTask());

        blockingTask.start();

        // invoking/blocking blockingTask explicitly
        blockingTask.interrupt();

        System.out.println("Finishing main thread.");
    }

    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(50_000_000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread.");
            }
        }
    }
}
