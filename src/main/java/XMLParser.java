import com.ximpleware.*;
import com.ximpleware.EOFException;
import javafx.geometry.Pos;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.Inet4Address;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class XMLParser {
    String jarString;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    Map<String, Integer> tags;
    Map<String, Integer> titleWords;
    ArrayList<String> commonWords;

    public XMLParser() {
        String url = null;
        try {
            url = URLDecoder.decode(XMLParser.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File jarDir = new File(url);
        jarString = jarDir.getParentFile().toString();
        tags = new HashMap<>();
        titleWords = new HashMap<>();
        commonWords = Utils.commonWords();
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
        //BufferedWriter writer;
        //Path file = Paths.get(jarString+ System.getProperty("file.separator") + "out.txt");
        Path file = Paths.get(jarString+ System.getProperty("file.separator") + "architecture_posts.txt");

        List<Post> posts = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(String.valueOf(file)));
            //writer = Files.newBufferedWriter(outfile, ENCODING, StandardOpenOption.WRITE);
            String line = reader.readLine();
            int i = 0;
            int postCount = 0;
            while(line != null && i < Integer.MAX_VALUE) {
                i++;
                Post p = parseLine(line);
                if(p != null) {
                    if (p.getStringTags().contains("<architecture>")) {
                        //writer.write(line);
                        //writer.newLine();
                        posts.add(p);
                        postCount++;
                    }
                }

                line = reader.readLine();

            }
            reader.close();
            //writer.flush();
            //writer.close();

            //countTags(posts);
            //printTags();
            countTitleWords(posts);
            printTitle();
            System.out.println(postCount + " posts found with the tag architecture " + i + " entries");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printTitle(){
        List<Map.Entry<String, Integer>> list = new LinkedList<>(titleWords.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                return(stringIntegerEntry.getValue()).compareTo(t1.getValue());
            }
        });
        for(Map.Entry<String, Integer> entry : list){
            System.out.println("Word " + entry.getKey() + " occurred " + entry.getValue() + " times in title");
        }

    }

    private void countTitleWords(List<Post> posts){
        for(Post p : posts){
            if(p.getTitle() != null)
                for(String t : p.getTitle().split(" ")){
                    t = t.toLowerCase();
                    if(commonWords.contains(t))
                        continue;
                    if(titleWords.containsKey(t))
                        titleWords.put(t, titleWords.get(t)+1);
                    else
                        titleWords.put(t,1);
                }
        }
    }

    private void printTags() {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(tags.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                return(stringIntegerEntry.getValue()).compareTo(t1.getValue());
            }
        });
        for(Map.Entry<String, Integer> entry : list){
            System.out.println("tag " + entry.getKey() + " occurred " + entry.getValue() + " times");
        }
    }

    private void countTags(List<Post> posts) {
        for(Post p: posts){
            for(String t: p.getTags()){
                if(tags.containsKey(t)){
                    tags.put(t, tags.get(t)+1);
                } else {
                    tags.put(t, 1);
                }
            }
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
                        doc.getRootElement().getAttributeValue("Score"),
                        doc.getRootElement().getAttributeValue("ViewCount"),
                        doc.getRootElement().getAttributeValue("Body"),
                        doc.getRootElement().getAttributeValue("LastEditDate"),
                        doc.getRootElement().getAttributeValue("LastActivityDate"),
                        doc.getRootElement().getAttributeValue("Title"),
                        doc.getRootElement().getAttributeValue("Tags"),
                        doc.getRootElement().getAttributeValue("CommentCount"),
                        doc.getRootElement().getAttributeValue("FavoriteCount"));
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
