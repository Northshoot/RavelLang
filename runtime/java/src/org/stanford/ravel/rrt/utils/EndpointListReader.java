package org.stanford.ravel.rrt.utils;

import org.stanford.ravel.rrt.tiers.Endpoint;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Reads a list of endpoints from a file
 *
 * Created by gcampagn on 2/8/17.
 */
public class EndpointListReader implements Closeable, AutoCloseable {
    private final Scanner scanner;

    public EndpointListReader(InputStream is) {
        scanner = new Scanner(is);
    }

    public Endpoint next() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\t");
            if (split.length < 2) {
                System.err.println("Badly formatted line " + line);
                continue;
            }

            String name = split[0];
            URI url;
            try {
                url = new URI(split[1]);
            } catch (URISyntaxException e) {
                System.err.println("Badly formatted line " + line);
                continue;
            }
            Map<String, String> options = new HashMap<>();
            for (int i = 2; i < split.length; i++) {
                String[] option = split[i].split("=", 2);
                if (option.length != 2) {
                    System.err.println("Badly formatted option " + split[i]);
                    continue;
                }
                options.put(option[0], option[1]);
            }

            try {
                return Endpoint.fromString(name, url, options);
            } catch (MalformedURLException e) {
                System.err.println("Badly formatted line " + line);
                continue;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
