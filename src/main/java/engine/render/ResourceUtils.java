package engine.render;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ResourceUtils {
    public static ByteBuffer loadToByteBuffer(String resourcePath) {
        try (InputStream is = ResourceUtils.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            byte[] bytes = is.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return buffer;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read resource" + resourcePath, e);
        }
    }
}
