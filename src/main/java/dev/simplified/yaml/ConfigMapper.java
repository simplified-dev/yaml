package dev.sbs.api.io.yaml;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.io.yaml.exception.InvalidConfigurationException;
import dev.sbs.api.util.StringUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public abstract class ConfigMapper extends YamlMap {

    private final transient Yaml yaml;
    private final transient ConcurrentLinkedMap<String, ConcurrentList<String>> comments = Concurrent.newLinkedMap();
    private final transient ConcurrentList<String> header;
    transient File configFile;
    transient ConfigSection root;

    protected ConfigMapper(File configDir, String fileName, Iterable<String> header) {
        if (StringUtil.isEmpty(fileName)) throw new IllegalArgumentException("Filename cannot be null!");
        this.configFile = new File(configDir, String.format("config/%s%s", fileName, (fileName.endsWith(".yml") ? "" : ".yml")));
        ConcurrentList<String> headerList = Concurrent.newList();
        header.forEach(headerList::add);
        this.header = headerList.toUnmodifiableList();
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(
            new CustomClassLoaderConstructor(
                ConfigMapper.class.getClassLoader(),
                new LoaderOptions()
                    .setProcessComments(true)
            ),
            new Representer(dumperOptions)
        );
    }

    public void addComment(String key, String value) {
        if (!this.comments.containsKey(key))
            this.comments.put(key, Concurrent.newList());

        this.comments.get(key).add(value);
    }

    public void clearComments() {
        this.comments.clear();
    }

    private void convertMapsToSections(Map<?, ?> input, ConfigSection section) {
        if (input == null) return;

        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map)
                this.convertMapsToSections((Map<?, ?>) value, section.create(key));
            else
                section.set(key, value, false);
        }
    }

    public final String getFullName() {
        return this.configFile.getName();
    }

    public final String getName() {
        return this.getFullName().replace(".yml", "");
    }

    public final File getParentDirectory() {
        return this.configFile.getAbsoluteFile().getParentFile();
    }

    protected void loadFromYaml() throws InvalidConfigurationException {
        this.root = new ConfigSection();

        try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(this.configFile), StandardCharsets.UTF_8)) {
            Object object = this.yaml.load(fileReader);

            if (object != null)
                convertMapsToSections((Map<?, ?>) object, this.root);
        } catch (Exception ex) {
            throw new InvalidConfigurationException(ex, "Could not load YML.");
        }
    }

    protected void saveToYaml() throws InvalidConfigurationException {
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(this.configFile), StandardCharsets.UTF_8)) {
            if (this.header.notEmpty()) {
                for (String line : this.header)
                    fileWriter.write("# " + line + "\n");

                fileWriter.write("\n");
            }

            int depth = 0;
            ArrayList<String> keyChain = new ArrayList<>();
            String yamlString = this.yaml.dump(root.getValues(true));
            StringBuilder writeLines = new StringBuilder();
            String[] yamlSplit = yamlString.split("\n");

            for (int y = 0; y < yamlSplit.length; y++) {
                String line = yamlSplit[y];
                int spaces = line.length() - line.replaceAll("^\\s+", "").length();

                if (line.startsWith(new String(new char[depth]).replace("\0", " "))) {
                    keyChain.add(line.split(":")[0].trim());
                    depth += 2;
                } else {
                    if (line.startsWith(new String(new char[depth - 2]).replace("\0", " ")))
                        keyChain.remove(keyChain.size() - 1);
                    else {
                        if (spaces == 0) {
                            keyChain = new ArrayList<>();
                            depth = 2;
                        } else {
                            ArrayList<String> temp = new ArrayList<>();
                            depth = spaces;
                            int index = 0;

                            for (int i = 0; i < spaces; i += 2, index++)
                                temp.add(keyChain.get(index));

                            keyChain = temp;
                            depth += 2;
                        }
                    }

                    keyChain.add(line.split(":")[0].trim());
                }

                String search = StringUtil.join(".", keyChain);
                if (this.comments.containsKey(search)) {
                    for (String comment : comments.get(search)) {
                        writeLines.append(new String(new char[depth - 2]).replace("\0", " "));
                        writeLines.append("# ");
                        writeLines.append(comment);
                        writeLines.append("\n");
                    }
                }

                writeLines.append(line);

                if (y < yamlSplit.length - 1) {
                    String nextLine = yamlSplit[y + 1];
                    String nextLineStripped = nextLine.replaceAll("^\\s+", "");
                    int nextSpaces = nextLine.length() - nextLineStripped.length();

                    if (!nextLineStripped.startsWith("-")) {
                        if (nextSpaces == 0)
                            writeLines.append('\n');
                    }

                    writeLines.append("\n");
                }
            }

            fileWriter.write(writeLines.toString());
        } catch (IOException ex) {
            throw new InvalidConfigurationException(ex, "Could not save YML.");
        }
    }

}
