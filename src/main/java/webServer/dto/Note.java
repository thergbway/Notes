package webServer.dto;

import java.sql.Timestamp;

public class Note {
    private Integer id;
    private Timestamp creationTime;
    private Timestamp changeTime;
    private String content;
    private String title;
    private String tags;

    public Note() {
    }

    public Note(Timestamp creationTime, Timestamp changeTime, String content, String title, String tags) {
        this.creationTime = creationTime;
        this.changeTime = changeTime;
        this.content = content;
        this.title = title;
        this.tags = tags;
    }

    public Note(Integer id, Timestamp creationTime, Timestamp changeTime, String content, String title, String tags) {
        this.id = id;
        this.creationTime = creationTime;
        this.changeTime = changeTime;
        this.content = content;
        this.title = title;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", creationTime=" + creationTime +
                ", changeTime=" + changeTime +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
