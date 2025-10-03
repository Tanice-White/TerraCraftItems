package io.tanice.terracraftitems.paper.item.datatype;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class StringArray implements PersistentDataType<byte[], String[]> {

    public static final StringArray INSTANCE = new StringArray();

    @Nonnull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Nonnull
    @Override
    public Class<String[]> getComplexType() {
        return String[].class;
    }

    @Nonnull
    @Override
    public byte[] toPrimitive(@Nonnull String[] strings, @Nonnull PersistentDataAdapterContext context) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeInt(strings.length);
            for (String s : strings) {
                byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                dos.writeInt(bytes.length);
                dos.write(bytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize string array", e);
        }
    }

    @Nonnull
    @Override
    public String[] fromPrimitive(@Nonnull byte[] bytes, @Nonnull PersistentDataAdapterContext context) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             DataInputStream dis = new DataInputStream(bais)) {

            int length = dis.readInt();
            String[] result = new String[length];

            for (int i = 0; i < length; i++) {
                int strLength = dis.readInt();
                byte[] strBytes = new byte[strLength];
                dis.readFully(strBytes);
                result[i] = new String(strBytes, StandardCharsets.UTF_8);
            }
            return result;
        } catch (IOException e) {
            return new String[0];
        }
    }
}
