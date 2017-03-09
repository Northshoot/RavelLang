package org.stanford.ravel;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.OptionParser;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse command line options
 *
 * Created by gcampagn on 1/29/17.
 */
class RavelOptionParser {
    private class Option {
        public final String group;
        public final String name;
        public final String value;

        public Option(String group, String name, String value) {
            this.group = group;
            this.name = name;
            this.value = value;
        }
    }

    private String buildPath;
    private String inputPath;

    private boolean help;
    private boolean version;

    private static final Pattern X_FORMAT = Pattern.compile("-X([a-z][a-z0-9]*):([a-z][a-z0-9]*)=(.+)");
    private final List<Option> Xoptions = new ArrayList<>();
    private final Set<String> foptions = new HashSet<>();

    public boolean isHelp() {
        return help;
    }
    public boolean isVersion() {
        return version;
    }

    public boolean hasFOption(String opt) {
        return foptions.contains(opt);
    }

    public void defaultOn(String opt) {
        foptions.add(opt);
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void help() {
        System.err.println("Usage: java org.stanford.ravel.RavelCompiler INPUT_FILE");
        System.err.println("Options:");
        System.err.println(" -o DIR                    : write outputs in DIR (defaults to the parent directory of INPUT_FILE)");
        System.err.println(" -Xgroup:opt=value        : set custom backend options (eg generated Java package name)");
        System.err.println(" -fdump-scope-tree        : dump scope tree on standard output (for debugging)");
        System.err.println(" -fdump-ir                : dump IR on standard output (for debugging)");
        System.err.println(" -fno-encrypt             : disable record encryption");
        System.err.println(" -fno-mac                 : disable record MAC");
        System.err.println(" -ffield-level-encryption : use field-level encryption instead of record-level");
        System.err.println(" -fhomomorphic-encryption : enable homomorphic encryption (requires -ffield-level-encryption)");
        System.err.println(" --help                   : show this help");
        System.err.println(" --version                : show version");
    }

    public void version() {
        System.err.println("Ravel Compiler version 0.1");
    }

    public void parse(String[] args) throws InvalidOptionException {
        boolean seenDashDash = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!seenDashDash && arg.startsWith("-")) {
                if (arg.equals("--")) {
                    seenDashDash = true;
                } else if (arg.equals("-o")) {
                    if (i+1 == args.length || args[i+1].startsWith("-"))
                        throw new InvalidOptionException("Missing argument to -o");
                    buildPath = args[i+1];
                    i++;
                } else if (arg.startsWith("-X")) {
                    Matcher matcher = X_FORMAT.matcher(arg);
                    if (matcher.matches()) {
                        Xoptions.add(new Option(matcher.group(1), matcher.group(2), matcher.group(3 )));
                    } else {
                        throw new InvalidOptionException("Invalid option format for " + arg);
                    }
                } else if (arg.startsWith("-f")) {
                    if (arg.startsWith("-fno-"))
                        foptions.remove(arg.substring(4));
                    else
                        foptions.add(arg.substring(2));
                } else if (arg.equals("--help")) {
                    help = true;
                } else if (arg.equals("--version")) {
                    version = true;
                } else {
                    throw new InvalidOptionException("Unrecognized option " + arg);
                }
            } else {
                if (inputPath != null)
                    throw new InvalidOptionException("Multiple inputs specified");
                inputPath = arg;
            }
        }

        if (inputPath == null && !help && !version)
            throw new InvalidOptionException("Must specify an input file");
        if (buildPath == null && inputPath != null)
            buildPath = Paths.get(inputPath).toAbsolutePath().getParent().toString() + "/rout/";
    }

    public void applyXOptions(OptionParser parser) throws InvalidOptionException {
        for (Option opt : Xoptions)
            parser.setOption(opt.group, opt.name, opt.value);
    }
}
