package ws.design.com.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MahadiurJaman on 9/1/2018.
 */

public class Point {
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("axis_value")
    @Expose
    private AxisValue axis_value;

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public AxisValue getAxis_value() {
        return axis_value;
    }

    public void setAxis_value(AxisValue axis_value) {
        this.axis_value = axis_value;
    }
}
