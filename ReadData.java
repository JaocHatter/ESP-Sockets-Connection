import java.io.IOException;
import java.io.RandomAccessFile;
public class ReadData {
    public static void main(String[] args) throws IOException{
        RandomAccessFile reader = new RandomAccessFile("DataExtraida.txt","rw");
        String line;
        reader.seek(0);
        System.out.println("Data del sensor 1");
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }
        reader.close();
    }
}
