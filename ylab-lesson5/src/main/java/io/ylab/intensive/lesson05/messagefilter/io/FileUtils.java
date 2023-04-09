package io.ylab.intensive.lesson05.messagefilter.io;

import java.io.*;

public class FileUtils {

    public static String replaceExtension(String filename, String ext, String replacement) {
        int pos = filename.lastIndexOf(ext);
        if (pos == -1) {
            return filename + replacement;
        } else {
            return filename.substring(0, pos) + replacement;
        }
    }

    public static void copyWithXorOperation(File file, File outputFile) throws IOException {
        try (InputStream in = new XorFileInputStream(file);
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            System.out.println("Finished writing to " + outputFile);
        }
    }
}
