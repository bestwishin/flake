package com.best.worm.process;

import java.util.concurrent.ArrayBlockingQueue;

import com.best.worm.sequence.AllocateSequenceService;
import com.best.worm.sequence.SimpleAllocateSequenceService;

public class EventProcessor implements Runnable {
    private static EventProcessor instance;
    private ArrayBlockingQueue<Event> queue;
    private AllocateSequenceService sequenceService;

    public static EventProcessor getInstance() throws Exception {
        if (instance == null) {
            instance = new EventProcessor(new SimpleAllocateSequenceService());
            Thread t = new Thread(instance);
            t.start();
        }
        return instance;
    }

    public EventProcessor(AllocateSequenceService sequenceService) throws Exception {
        this.queue = new ArrayBlockingQueue<>(500);
        this.sequenceService = sequenceService;
    }

    public void publish(Event event) {
        queue.offer(event);
    }

    @Override
    public void run() {
        while (true) {
            try {
                sequenceService.process(queue.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
