package ws.design.com.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MahadiurJaman on 9/1/2018.
 */

public class Graph {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("warning_threshold")
    @Expose
    private Float warning_threshold;
    @SerializedName("alert_threshold")
    @Expose
    private Integer alert_threshold;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("axix_count")
    @Expose
    private Integer axix_count;
    @SerializedName("lines")
    @Expose
    private Integer lines;
    @SerializedName("point_count")
    @Expose
    private Integer point_count;

    @SerializedName("distance_seconds")
    @Expose
    private Integer distance_seconds;
    @SerializedName("points")
    @Expose
    private List<Point> points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWarning_threshold() {
        return warning_threshold;
    }

    public void setWarning_threshold(Float warning_threshold) {
        this.warning_threshold = warning_threshold;
    }

    public Integer getAlert_threshold() {
        return alert_threshold;
    }

    public void setAlert_threshold(Integer alert_threshold) {
        this.alert_threshold = alert_threshold;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getAxix_count() {
        return axix_count;
    }

    public void setAxix_count(Integer axix_count) {
        this.axix_count = axix_count;
    }

    public Integer getLines() {
        return lines;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    public Integer getPoint_count() {
        return point_count;
    }

    public void setPoint_count(Integer point_count) {
        this.point_count = point_count;
    }

    public Integer getDistance_seconds() {
        return distance_seconds;
    }

    public void setDistance_seconds(Integer distance_seconds) {
        this.distance_seconds = distance_seconds;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
