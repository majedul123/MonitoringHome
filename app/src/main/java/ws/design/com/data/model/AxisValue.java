package com.chikeandroid.retrofittutorial.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MahadiurJaman on 9/1/2018.
 */

public class AxisValue {
    @SerializedName("x")
    @Expose
    private Float x;
    @SerializedName("y")
    @Expose
    private Float y;
    @SerializedName("z")
    @Expose
    private Float z;

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }
}
