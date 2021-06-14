package com.company.server.dto;


public class ChapterDto {

    /**
     * id
     */
    private String id;
    /**
     * 课程id
     */
    private String courseId;
    /**
     * 名称
     */
    private String name;
    /**
     * 时长|单位秒
     */
    private Integer time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", courseId=").append(courseId);
        sb.append(", name=").append(name);
        sb.append(", time=").append(time);
        sb.append("]");
        return sb.toString();
    }

}