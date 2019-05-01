import javax.xml.bind.annotation.XmlRootElement;

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
    String tags;
//    int answerCount;
    int commentCount;
    int favouriteCount;

    public Post(int id, int postTypeId, String creationDate, int score, int viewCount, String body, String lastEditDate, String lastActivityDate, String title, String tags, int commentCount, int favouriteCount ){
        this.id = id;
        this.postTypeId = postTypeId;
        this.creationDate = creationDate;
        this.score = score;
        this.viewCount = viewCount;
        this.body = body;
        this.lastEditDate = lastEditDate;
        this.lastActivityDate = lastActivityDate;
        this.title = title;
        this.tags = tags;
        this.commentCount = commentCount;
        this.favouriteCount = favouriteCount;
    }

    @Override
    public String toString(){
        return title + " with tags " + tags;
    }


}
