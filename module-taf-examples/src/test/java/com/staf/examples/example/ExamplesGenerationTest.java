package com.staf.examples.example;

import com.staf.common.exception.AqaException;
import com.staf.common.metadata.ModuleType;
import com.staf.common.metadata.Toolset;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.testng.annotations.Test;
import org.zeroturnaround.zip.ZipUtil;

@Slf4j
public class ExamplesGenerationTest {
    private final String TOOLSET_DELIMITER = "::";
    private final String NEW_LINE_DELIMITER = "\n";
    private final String TEST_FOLDER_PATH = "/test/";
    private final String EXCEPTION_CLASS = "CommonTest";
    private final String EXAMPLES_FILE_PATH = "./examples/example";
    private final String DEPENDENT_PATH = "com/staf/examples";
    private final String DEPENDENT_PACKAGE = DEPENDENT_PATH.replaceAll("/", ".");
    private final String JAVA_EXT = ".java";
    private final String FTL_EXT = ".ftl";
    private final String PACKAGE_FTL_PLACEHOLDER = "[=package]";
    private final File PROJECT_DIR = new File("./");
    private final File EXAMPLES_FILE = new File(EXAMPLES_FILE_PATH);
    private final File EXAMPLES_ZIP_FILE = new File("./examples.zip");
    private final List<File> exampleFiles =
            new ArrayList<>(FileUtils.listFiles(PROJECT_DIR, new String[] {JAVA_EXT.substring(1)}, true));

    @Test
    public void generateExamples() {
        removeDirs(EXAMPLES_FILE, EXAMPLES_ZIP_FILE);
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(""));
        Set<Class<?>> toolsetClasses = reflections.getTypesAnnotatedWith(Toolset.class);
        log.info("Extracted number of classes for project generation: {}", toolsetClasses.size());
        Map<String, List<Class<?>>> grouped = toolsetClasses.stream()
                .collect(Collectors.groupingBy(clazz -> Arrays.stream(
                                clazz.getAnnotation(Toolset.class).value())
                        .map(ModuleType::getName)
                        .sorted()
                        .collect(Collectors.joining(TOOLSET_DELIMITER))));
        // debugging purpose
        grouped.forEach((group, classes) -> log.info(
                "\nGroup: {}.\nClasses: \n{}",
                group,
                classes.stream().map(Class::getName).collect(Collectors.joining(NEW_LINE_DELIMITER))));

        Map<String, List<File>> filesPerGroup = grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        (entry) -> formatGroupFolderName(entry.getKey()),
                        (entry) -> prepareFiles(entry.getKey(), entry.getValue())));

        // add narrow groups to a wider ones: only for a non-test classes
        filesPerGroup.forEach((group, files) -> filesPerGroup.entrySet().stream()
                .filter(entry ->
                        entry.getKey().contains(group) && !entry.getKey().equals(group))
                .forEach(entry -> {
                    log.info("Wider group: {}. Inner group: {}.", entry.getKey(), group);
                    files.forEach(file -> {
                        try {
                            if (file.getPath().contains(TEST_FOLDER_PATH)
                                    && !file.getName().contains(EXCEPTION_CLASS)) {
                                log.info("Narrow group logic for a test classes is not applied");
                                return;
                            }
                            FileUtils.copyFile(file, new File(file.getPath().replaceFirst(group, entry.getKey())));
                        } catch (IOException e) {
                            throw new AqaException("Unable to copy example" + " files: %s", e.getMessage());
                        }
                    });
                }));
        log.info("Files were generated. Next step: archiving.");
        ZipUtil.pack(EXAMPLES_FILE.getParentFile(), EXAMPLES_ZIP_FILE);
        log.info("Zip archive was created: {}.", EXAMPLES_ZIP_FILE);
        Assertions.assertThat(EXAMPLES_ZIP_FILE).exists();
    }

    private List<File> prepareFiles(String group, List<Class<?>> classes) {
        List<File> filesPerGroup = new ArrayList<>();
        log.info("Group: {}", group);
        log.info(classes.stream().map(Class::getSimpleName).collect(Collectors.joining(";\n")));

        // create folder for a toolset
        final File groupFolder = new File(String.format(
                "%s/%s",
                EXAMPLES_FILE.getPath(), StringUtils.isEmpty(group) ? "COMMON" : formatGroupFolderName(group)));
        log.info("Group File: {}", groupFolder);
        try {
            FileUtils.forceMkdir(groupFolder);
            log.info("group folder was created");
            classes.forEach(clazz -> {
                final File exampleFile = exampleFiles.stream()
                        .filter(fileNameFilter(clazz))
                        .findFirst()
                        .get();
                final String fileName =
                        exampleFile.getPath().replaceFirst("\\./", "").replaceAll(DEPENDENT_PATH, "");
                final File targetExampleFile = new File(
                        String.format("%s/%s", groupFolder.getPath(), fileName).replace(JAVA_EXT, FTL_EXT));
                log.info("Source file: {}. Target file: {}", exampleFile.getPath(), targetExampleFile.getPath());
                filesPerGroup.add(targetExampleFile);
                try {
                    // add template placeholder for custom package
                    String fileContent =
                            removeMetaData(FileUtils.readFileToString(exampleFile, Charset.defaultCharset())
                                    .replaceAll(DEPENDENT_PACKAGE, PACKAGE_FTL_PLACEHOLDER));

                    FileUtils.writeStringToFile(targetExampleFile, fileContent, Charset.defaultCharset());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filesPerGroup;
    }

    private void removeDirs(File... dirs) {
        Arrays.stream(dirs).forEach(dir -> {
            try {
                if (dir.isDirectory()) {
                    FileUtils.deleteDirectory(dir);
                } else {
                    FileUtils.delete(dir);
                }
            } catch (IOException e) {
                log.error("Unable to delete the directory: {}\n{}", dir, e.getMessage());
            }
        });
    }

    private String formatGroupFolderName(String group) {
        return group.replaceAll(":|\\W", "_").toUpperCase();
    }

    /**
     * Removes metadata from the class: i.e. @Toolset
     *
     * @param classString class String content
     * @return adjusted content
     */
    private String removeMetaData(String classString) {
        String metaDataReplacementPattern = "(^import\\s(static\\s)?com\\.common\\.metadata\\..+;\\n|@Toolset.*\\)\\n)";
        return classString.replaceAll(metaDataReplacementPattern, "");
    }

    private Predicate<File> fileNameFilter(final Class clazz) {
        return file -> file.getPath()
                .endsWith(String.format("/%s.java", clazz.getName().replaceAll("\\.", "/")));
    }
}
