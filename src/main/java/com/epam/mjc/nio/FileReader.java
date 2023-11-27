package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;


public class FileReader {

    public Profile getDataFromFile(File file) {
        return convertFileContentToProfile(extractContent(file));
    }

    private Profile convertFileContentToProfile(String content) {
        Map<String, String > keyValue = getKeyValue(content);
        Profile profile = new Profile();
        profile.setName(keyValue.get("Name"));
        profile.setAge(Integer.valueOf(keyValue.get("Age")));
        profile.setEmail(keyValue.get("Email"));
        profile.setPhone(Long.valueOf(keyValue.get("Phone")));
        return profile;
    }

    private String extractContent(File file) {
        try(RandomAccessFile aFile = new RandomAccessFile(file, "r")) {
            StringBuilder content = new StringBuilder();
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(48);
            int bytesRead = inChannel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    content.append((char) buffer.get());
                }
                buffer.clear();
                bytesRead = inChannel.read(buffer);
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> getKeyValue(String content) {
        Map<String, String> keyValue = new HashMap<>();
        String[] lines;
        lines = content.split("\n");
        for (String line:lines) {
            String[] extractedFields = getFieldsFromContent(line);
            keyValue.put(extractedFields[0], extractedFields[1]);
        }
        return keyValue;
    }

    private String[] getFieldsFromContent(String line) {
        return line.split(": ");
    }
}
