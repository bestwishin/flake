package com.best.worm.sequence;

import com.best.worm.process.Event;
import com.best.worm.storage.LocalStoreSequenceService;
import com.best.worm.storage.StoreSequenceService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;



public class SimpleAllocateSequenceService implements AllocateSequenceService {
    private StoreSequenceService storageSequenceService;
    private Map<Integer, AtomicLong> visitMap;

    public SimpleAllocateSequenceService() throws IOException {
        this.storageSequenceService = new LocalStoreSequenceService();
        this.visitMap = new HashMap<>();

    }

    @Override
    public void process(Event event) {
        int uid;
        try {
            uid = Integer.parseInt(event.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            event.error();
            return;
        }
        Optional<Long>  op = storageSequenceService.initValue(uid);
        if (op.isPresent() && visitMap.get(uid) == null) {
            visitMap.put(uid, new AtomicLong(op.get()));
        }

        AtomicLong al = visitMap.get(uid);
        if (al == null) {
            event.error();
            return;
        }
        
        boolean bol = storageSequenceService.canPass(al.get(), uid);
        if (!bol) {
            event.error();
            return;
        }
        event.output(al.incrementAndGet());
    }

    
}
