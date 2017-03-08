package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.utils.GrowableByteArray;

import java.io.*;

/**
 * A simple (and dumb) file system based database
 *
 * Created by gcampagn on 3/8/17.
 */
public class JavaDurableStorage {
    public interface Loader {
        void handleRecord(int modelId, int recordId, byte[] data);
    }

    private final File directory;

    public JavaDurableStorage(File directory) {
        this.directory = directory;
    }

    public void load(Loader loader) throws IOException {
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IOException("Failed to create database directory");
            }
        }
        File[] list = directory.listFiles();
        if (list == null)
            throw new IOException("Failed to list database directory");

        for (File child : list) {
            if (child.getName().equals(".") || child.getName().equals(".."))
                continue;
            loadOne(child, loader);
        }
    }

    private void loadOne(File file, Loader loader) throws IOException {
        String name = file.getName();
        String[] tokens = name.split("-");
        if (tokens.length != 2) {
            System.err.println("Badly formatted filename in database directory: " + name);
            return;
        }

        int modelId = Integer.valueOf(tokens[0]);
        int recordId = Integer.valueOf(tokens[1]);

        GrowableByteArray array = new GrowableByteArray();
        try (InputStream stream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            while (true) {
                int ok = stream.read(buffer);
                if (ok < 0) // end of file
                    break;
                array.write(buffer, 0, ok);
            }
        }

        loader.handleRecord(modelId, recordId, array.toByteArray());
    }

    public void save(RavelPacket pkt) throws IOException {
        File newFile = new File(directory, pkt.model_id + "-" + pkt.record_id);

        byte[] data = pkt.getRecordData();
        try (OutputStream stream = new FileOutputStream(newFile)) {
            stream.write(data);
        }
    }

    public void delete(int modelId, int recordId) throws IOException {
        File toDelete = new File(directory, modelId + "-" + recordId);
        if (!toDelete.delete())
            throw new IOException("Failed to delete file");
    }
}
