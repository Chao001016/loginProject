package pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签表
 * @TableName tag
 */
public class Tag implements Serializable {
    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 标签名称
     */
    private String content;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除,1删除，0未删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Tag() {
    }
    public Tag(Tag tag) {
        this.id = tag.getId();
        this.content = tag.getContent();
        this.creator = tag.getCreator();
        this.createTime = tag.getCreateTime();
        this.updateTime = tag.getUpdateTime();
        this.isDelete = tag.getIsDelete();
    }
}