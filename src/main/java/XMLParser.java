import com.ximpleware.*;
import com.ximpleware.EOFException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public void customParse() {
        BufferedReader reader;
        Path file = Paths.get(jarString+ System.getProperty("file.separator") + "out.txt");
        List<Post> posts = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(String.valueOf(file)));
            String line = reader.readLine();
            int i = 0;
            int postCount = 0;
            while(line != null && i < 1000) {
                i++;
                Post p =parseLine(line);
                if(p != null) {
                    posts.add(p);
                    postCount++;
                }

                line = reader.readLine();

            }
            System.out.println(postCount + " posts found from " + i + " entries");
            for(Post p : posts)
                System.out.println(p.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parseWithVTD() {
        try {
            File f = new File(jarString + System.getProperty("file.separator") + "out.txt");
            FileInputStream fis = new FileInputStream(f);
            byte[] ba = new byte[(int) f.length()];
            fis.read(ba);
            VTDGen vg = new VTDGen();
            vg.setDoc(ba);
            vg.parse(false);
            VTDNav vn = vg.getNav();
            if (vn.matchElement("post")) {

            }
        } catch (IOException e) {
            System.out.println("io error");
            e.printStackTrace();
        } catch (EOFException e) {
            System.out.println("endoffile error");
            e.printStackTrace();
        } catch (NavException e) {
            System.out.println("nav error");
            e.printStackTrace();
        } catch (EncodingException e) {
            System.out.println("encoding error");
            e.printStackTrace();
        } catch (EntityException e) {
            System.out.println("entity error");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("parse error");
            e.printStackTrace();
        }

    }

    public Post parseLine(String line) {
        org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
        try {
            org.jdom.Document doc = saxBuilder.build(new StringReader(line));
            if (Integer.parseInt(doc.getRootElement().getAttributeValue("PostTypeId")) == 1) {
                Post p = new Post(Integer.parseInt(doc.getRootElement().getAttributeValue("Id")),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("PostTypeId")),
                        doc.getRootElement().getAttributeValue("CreationDate"),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("Score")),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("ViewCount")),
                        doc.getRootElement().getAttributeValue("Body"),
                        doc.getRootElement().getAttributeValue("LastEditDate"),
                        doc.getRootElement().getAttributeValue("LastActivityDate"),
                        doc.getRootElement().getAttributeValue("Title"),
                        doc.getRootElement().getAttributeValue("Tags"),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("CommentCount")),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("FavoriteCount")));
                return p;
            }
            return null;
        } catch (JDOMException e) {
            System.out.println("JDME");
            // handle JDOMException
        } catch (IOException e) {
            System.out.println("IOE");
            // handle IOException
        } catch (NumberFormatException e) {
            System.out.println("NFE");
        }
        return null;
    }
}
