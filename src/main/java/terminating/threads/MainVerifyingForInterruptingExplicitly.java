package terminating.threads;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class MainVerifyingForInterruptingExplicitly {

    private static final IllegalArgumentException MANDATORY_ARGUMENT = new IllegalArgumentException(
            "Arguments are mandatory, and must be provided two values, for the \"base\" and \"power\" ones."
    );

    public static void main(String[] args) {
        if (args == null) {
            throw MANDATORY_ARGUMENT;
        } else if (args.length != 2) {
            throw MANDATORY_ARGUMENT;
        }

        String base = args[0];
        String power = args[1];

        Thread thread = new Thread(
                new LongComputationTask(new BigInteger(base), new BigInteger(power)),
                "LongComputationTask"
        );

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
        }

        thread.interrupt();

        if (thread.isAlive()) {
            System.out.println(
                    "[" + thread.getName() + "]" +
                            " thread is ALIVE even though it has been asked to be interrupted."
            );
        } else {
            System.out.println(
                    "[" + thread.getName() + "] " +
                            "has been properly terminated (not ALIVE anymore), either because it completed its task " +
                            "or because it received an interruption signal."
            );
        }

        System.out.println("Finishing current main thread");
    }

    private static class LongComputationTask implements Runnable {

        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            try {
                System.out.println(base + " ^ " + power + " = " + pow(base, power));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private BigInteger pow(BigInteger base, BigInteger power) throws InterruptedException {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException(
                            "The current thread took too long, and has been asked to be terminated."
                    );
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
