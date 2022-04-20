import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    
    public byte[] toFileFromArray(String path) throws IOException{
        File file = new File(path);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return fileContent;
    }

    public void toArrayFromFile(byte[] data, String path) throws IOException{
        FileOutputStream fos = new FileOutputStream(path);

        fos.write(data);
        FileDescriptor fd = fos.getFD();

        fos.flush();
        fd.sync();
        fos.close();
    }
}
