package database.classes;

import java.util.Date;

public class Comment {

    private String title;
    private String content;
    private Date publishDate;
    private String authorUsername;

    public Comment(String title, String content, Date publishDate, String authorUsername) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.authorUsername = authorUsername;
    }

    public Comment(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Comment fromTitleAndContent(String title, String content) {
        return new Comment(title, content);
    }

    public Comment withCurrentDate() {
        this.setPublishDate(new Date());
        return this;
    }

    public Comment writtenBy(String authorUsername) {
        this.setAuthorUsername(authorUsername);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

}
