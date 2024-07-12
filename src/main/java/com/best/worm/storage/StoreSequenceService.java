package com.best.worm.storage;

import java.util.Optional;

public interface StoreSequenceService {
    boolean canPass(long current, int uid);

    Optional<Long> initValue(int uid);

}
