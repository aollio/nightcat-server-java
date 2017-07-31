package com.nightcat.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ym_user_experience", schema = "nightcat", catalog = "")
public class Experience {
    private String id;
    private String uid;
    private String name;
    private String description;
    private Integer fav_count;
    private Integer comment_count;
    private Integer view_count;
    private Timestamp create_time;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "name",length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description",length = 300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "fav_count")
    public Integer getFav_count() {
        return fav_count;
    }

    public void setFav_count(Integer fav_count) {
        this.fav_count = fav_count;
    }

    @Basic
    @Column(name = "comment_count")
    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    @Basic
    @Column(name = "view_count")
    public Integer getView_count() {
        return view_count;
    }

    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (fav_count != null ? !fav_count.equals(that.fav_count) : that.fav_count != null) return false;
        if (comment_count != null ? !comment_count.equals(that.comment_count) : that.comment_count != null) return false;
        if (view_count != null ? !view_count.equals(that.view_count) : that.view_count != null) return false;
        if (create_time != null ? !create_time.equals(that.create_time) : that.create_time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fav_count != null ? fav_count.hashCode() : 0);
        result = 31 * result + (comment_count != null ? comment_count.hashCode() : 0);
        result = 31 * result + (view_count != null ? view_count.hashCode() : 0);
        result = 31 * result + (create_time != null ? create_time.hashCode() : 0);
        return result;
    }
}