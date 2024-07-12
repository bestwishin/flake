package com.best.worm.storage;

import java.util.concurrent.atomic.AtomicLong;

public interface StoreSequenceService {
    boolean canPass(long current, long uid);

    AtomicLong getInitValue(long uid);

}
