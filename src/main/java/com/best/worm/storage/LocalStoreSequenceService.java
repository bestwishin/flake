package com.best.worm.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

import com.best.worm.config.ConfigService;
import com.best.worm.config.SimpleConfigService;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LocalStoreSequenceService implements StoreSequenceService {
    private String filePath = "D:\\max_sequence.bin";
    private ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    private Map<Integer, Section> map;
    private ConfigService configService;

    public LocalStoreSequenceService() throws IOException {
        this.configService = new SimpleConfigService();
        Set<Integer> idSet = configService.scope();
        Map<Integer, Section> ans = new HashMap<>();
        for (Integer i : idSet) {
            long value = load(i);
            ans.put(i, new Section(i, value, value));
        }
        this.map = ans;
    }

    public boolean canPass(long current, long uid) {
        int id = getSectionId(uid);
        Section section = map.get(id);
        if (section == null) {
            return false;
        }
        if (current + 1 > section.getCurrentValue()) {
            long target = section.getCurrentValue() + configService.step();
            boolean r = save(target, section.getId());
            if (r) {
                section.setCurrentValue(target);
            } else {
                return false;
            }
        }
        return true;
    }

    public AtomicLong getInitValue(long uid) {
        int id = getSectionId(uid);
        Section section = map.get(id);
        if (section == null) {
            return null;
        }
        return new AtomicLong(section.getInitValue());

    }

    private int getSectionId(long uid) {
        return (int) (uid / 100000 + 1);
    }

    private boolean save(long counter, int id) {
        try {
            Path tempFile = writeTempSequenceFile(counter);
            Path permanentFile = Paths.get(filePath + id);
            Files.move(tempFile, permanentFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Path writeTempSequenceFile(long counter) throws Exception {
        Path tempFile = Files.createTempFile(
                "temp",
                ".bin",
                new FileAttribute<?>[0]);
        tempFile.toFile().deleteOnExit();
        buffer.clear();
        buffer.putLong(counter);
        Files.write(tempFile, buffer.array(), StandardOpenOption.WRITE, StandardOpenOption.SYNC);
        return tempFile;
    }

    private long load(int i) throws IOException {
        Path path = Paths.get(filePath + i);
        if (!Files.exists(path)) {
            return 0;
        }
        byte[] bytes = Files.readAllBytes(path);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }

    public Map<Integer, Section> load(Set<Integer> idSet) throws IOException {
        Map<Integer, Section> ans = new HashMap<>();
        for (Integer i : idSet) {
            long value = load(i);
            ans.put(i, new Section(i, value, value));
        }
        return ans;
    }
}
