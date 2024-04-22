package Entity;
import Entity.Post;


public class Comment
{
    private int id;
    private int post_id;
    private Post post;
    private String contenu_comment;
    private String date_comment;

    public Comment(String contenu_comment)
    {
        this.contenu_comment = contenu_comment;
    }

    public String getContent()
    {
        return contenu_comment;
    }
    public Comment()
    {}

    public Comment(int id, int post_id, String contenu_comment)
    {
        this.id = id;
        this.post_id = post_id;
        this.contenu_comment = contenu_comment;
    }
    public Comment(int id, String contenu_comment , String date_comment, Post post)
    {
        this.id = id;
        this.contenu_comment = contenu_comment;
        this.date_comment = date_comment;
        this.post = post;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getContenu_comment() {
        return contenu_comment;
    }

    public void setContenu_comment(String contenu_comment) {
        this.contenu_comment = contenu_comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", contenu_comment='" + contenu_comment + '\'' +
                '}';
    }
}