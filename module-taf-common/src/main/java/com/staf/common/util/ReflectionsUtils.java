package com.staf.common.util;

import com.staf.common.exception.AqaException;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ReflectionsUtils {

    public List<String> getFullClassNames(final String className) {
        final List<String> targetClasses = new ArrayList<>();
        try {
            final Enumeration<URL> roots = ReflectionsUtils.class.getClassLoader().getResources("");

            for (Iterator<URL> iterator = roots.asIterator(); iterator.hasNext();) {
                final File root = new File(iterator.next().getPath());
                if (root.listFiles() != null) {
                    for (File file : root.listFiles()) {
                        targetClasses.addAll(getClassFromFile(className, file));
                    }
                }
            }
            if (targetClasses.isEmpty()) {
                throw new AqaException("Target class '%s' isn't found", className);
            }
            return targetClasses;
        } catch (IOException e) {
            throw new AqaException("Issue occurred during class finding");
        }
    }

    private List<String> getClassFromFile(final String className, final File file) throws IOException {
        try (Stream<Path> classStream = Files.find(Paths.get(file.toURI()),
                Integer.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            return classStream.filter(classFile -> classFile.toAbsolutePath().toString().matches(String.format(".*%s.class", className)))
                    .map(ReflectionsUtils::getClassPackagePath)
                    .collect(Collectors.toList());
        }
    }

    private String getClassPackagePath(final Path classFilePath) {
        return RegexpUtils.getStringByRegexp(classFilePath.toAbsolutePath().toString(), ".*/target/classes/(.*).class", 1)
                .replaceAll("/", ".");
    }

}
