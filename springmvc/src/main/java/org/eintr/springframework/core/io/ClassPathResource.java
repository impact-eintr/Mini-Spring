package org.eintr.springframework.core.io;

import java.io.FileNotFoundException;
import org.eintr.springframework.util.ClassUtils;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;

    public ClassPathResource(String path) {
        this(path, (ClassLoader)null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        } else if (this.clazz != null) {
            is = this.clazz.getResourceAsStream(this.path);
        } else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (is == null) {
            throw new FileNotFoundException(this.path + "can not find");
        }
        return is;
    }

    @Override
    public String getFilename() {
        return path;
    }
}
