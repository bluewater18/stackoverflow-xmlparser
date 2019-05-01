import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;

@XmlRootElement
public class Post {
    int id;
    int postTypeId;
//    int acceptedAnswerId;
    String creationDate;
    int score;
    int viewCount;
    String body;
//    int ownerUserId;
//    int lastEditorUserId;
//    String lastEditorDisplayName;
    String lastEditDate;
    String lastActivityDate;
    String title;
    String stringTags;
    ArrayList<String> tags;
//    int answerCount;
    int commentCount;
    int favouriteCount;

    public Post(int id, int postTypeId, String creationDate, String score, String viewCount, String body, String lastEditDate, String lastActivityDate, String title, String tags, String commentCount, String favouriteCount ){
        this.id = id;
        this.postTypeId = postTypeId;
        this.creationDate = creationDate;
        if(score != null)
            this.score = tryNullOrZero(score);
        if(viewCount != null)
            this.viewCount = tryNullOrZero(viewCount);
        this.body = body;
        this.lastEditDate = lastEditDate;
        this.lastActivityDate = lastActivityDate;
        this.title = title;
        if(tags != null && tags.length() > 0) {
            String temp;
            temp = tags.replaceAll("<", "");
            temp = temp.replaceAll(">", ",");
            this.tags = new ArrayList<>(Arrays.asList(temp.split(",")));
        }
        this.stringTags = tags;
        if(commentCount != null)
            this.commentCount = tryNullOrZero(commentCount);
        if(favouriteCount != null)
            this.favouriteCount = tryNullOrZero(favouriteCount);
    }

    private int tryNullOrZero(String score) {
        int temp = 0;
        try {
            temp = Integer.parseInt(score);
        } catch (NumberFormatException e){

        }
        return temp;
    }

    @Override
    public String toString(){
        return title + " with tags " + tags;
    }

    public String getStringTags() {
        return stringTags;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }
}
