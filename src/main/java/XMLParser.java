import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class XMLParser {
    String jarString;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    public XMLParser() {
        String url = null;
        try {
            url = URLDecoder.decode(XMLParser.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File jarDir = new File(url);
        jarString = jarDir.getParentFile().toString();

    }

    public void readFile() {
        BufferedReader reader;
        BufferedWriter writer;
        Path outfile = Paths.get(jarString+ System.getProperty("file.separator") + "out.txt");
        //FileWriter writer;
        try{
            //reader = new BufferedReader(new FileReader(jarString+ System.getProperty("file.separator") + "in.xml"));
            reader = new BufferedReader(new FileReader("E:\\_downloads\\2018\\stackoverflow.com-Posts\\Posts.xml"));
            //writer = new BufferedWriter(new FileWriter(jarString+ System.getProperty("file.separator") + "out.txt", true));
            writer = Files.newBufferedWriter(outfile, ENCODING, StandardOpenOption.WRITE);
            //writer = new FileWriter(jarString+ System.getProperty("file.separator") + "out.txt", true);
            String line = reader.readLine();
            int i = 0;
            int kept = 0;
            while(line != null) {
                i++;
                if(line.contains("architecture")) {
                    writer.write(line);
                    writer.newLine();
                    kept++;
                    System.out.println("line: " + i + " added to out file as line: " + kept);

                }
                //else {
                    //System.out.println("line: " + i + " not kept.");
                //}
                line = reader.readLine();
            }
            reader.close();
            writer.flush();
            writer.close();
            System.out.println("successful... read " + i + " line with " + kept + " found and saved to out file");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
