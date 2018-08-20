package org.dimdev.riftloader;

import com.google.gson.annotations.SerializedName;

public enum Side {
    @SerializedName("client") CLIENT,
    @SerializedName("server") SERVER,
    @SerializedName("both") BOTH;

    public boolean includes(Side side) {
        return this == BOTH || this == side;
    }
}
