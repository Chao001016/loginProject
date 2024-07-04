package pojo;

import java.io.Serializable;
import java.util.Date;

/**
* 问题表
* @TableName question
*/
public class Question implements Serializable {

    /**
    * 唯一标识
    */
    private Long id;
    /**
    * 题目标签
    */
    private String tag;
    /**
    * 选项ID，用于选择题
    */
    private String optionId;
    /**
    * 题目分值
    */
    private Integer score;
    /**
    * 题目类型，1选择，2判断，3填空，4问题
    */
    private Integer type;
    /**
    * 题目状态，0私有，1公开
    */
    private Integer state;
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
    * 逻辑删除，1删除，0未被删除
    */
    private Integer isDelete;
    /**
    * 题目内容
    */
    private byte[] content;
    /**
    * 问题答案
    */
    private byte[] answer;
    /**
    * 题目分析
    */
    private byte[] analysis;

    /**
    * 唯一标识
    */
    public void setId(Long id){
    this.id = id;
    }

    /**
    * 题目标签
    */
    private void setTag(String tag){
    this.tag = tag;
    }

    /**
    * 选项ID，用于选择题
    */
    public void setOptionId(String optionId){
    this.optionId = optionId;
    }

    /**
    * 题目分值
    */
    public void setScore(Integer score){
    this.score = score;
    }

    /**
    * 题目类型，1选择，2判断，3填空，4问题
    */
    public void setType(Integer type){
    this.type = type;
    }

    /**
    * 题目状态，0私有，1公开
    */
    public void setState(Integer state){
    this.state = state;
    }

    /**
    * 创建人
    */
    public void setCreator(Long creator){
    this.creator = creator;
    }

    /**
    * 创建时间
    */
    public void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间
    */
    public void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }

    /**
    * 逻辑删除，1删除，0未被删除
    */
    public void setIsDelete(Integer isDelete){
    this.isDelete = isDelete;
    }

    /**
    * 题目内容
    */
    public void setContent(byte[] content){
    this.content = content;
    }

    /**
    * 问题答案
    */
    public void setAnswer(byte[] answer){
    this.answer = answer;
    }

    /**
    * 题目分析
    */
    public void setAnalysis(byte[] analysis){
    this.analysis = analysis;
    }


    /**
    * 唯一标识
    */
    public Long getId(){
    return this.id;
    }

    /**
    * 题目标签
    */
    public String getTag(){
    return this.tag;
    }

    /**
    * 选项ID，用于选择题
    */
    public String getOptionId(){
    return this.optionId;
    }

    /**
    * 题目分值
    */
    public Integer getScore(){
    return this.score;
    }

    /**
    * 题目类型，1选择，2判断，3填空，4问题
    */
    public Integer getType(){
    return this.type;
    }

    /**
    * 题目状态，0私有，1公开
    */
    public Integer getState(){
    return this.state;
    }

    /**
    * 创建人
    */
    public Long getCreator(){
    return this.creator;
    }

    /**
    * 创建时间
    */
    public Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间
    */
    public Date getUpdateTime(){
    return this.updateTime;
    }

    /**
    * 逻辑删除，1删除，0未被删除
    */
    public Integer getIsDelete(){
    return this.isDelete;
    }

    /**
    * 题目内容
    */
    public byte[] getContent(){
    return this.content;
    }

    /**
    * 问题答案
    */
    public byte[] getAnswer(){
    return this.answer;
    }

    /**
    * 题目分析
    */
    public byte[] getAnalysis(){
    return this.analysis;
    }

}
