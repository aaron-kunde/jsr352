/*
 * Copyright (c) 2014 Red Hat, Inc. and/or its affiliates.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Cheng Fang - Initial API and implementation
 */

package org.jberet.support.io;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A bean class that represents stock trade data.
 * The CSV file, IBM_unadjusted.txt, is downloaded from kibot.com free trial data:
 * http://www.kibot.com/buy.aspx
 * http://api.kibot.com/?action=history&symbol=IBM&interval=1&unadjusted=1&bp=1&user=guest
 * <p/>
 * The data is in standard CSV format with order of fields:
 * Date,Time,Open,High,Low,Close,Volume
 * <p/>
 * The data file contains no header line.  The data file is truncated to 10/13/2008,12:09 to stay within Excel row
 * number limit (1048576 rows max).
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class StockTrade implements Serializable {
    private static final long serialVersionUID = -55209987994863611L;

    @NotNull
    @Past
    @JsonProperty("Date")
    Date date;

    @NotNull
    @Size(min = 3, max = 5)
    @Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
    @JsonProperty("Time")
    String time;

    @NotNull
    @Max(1000)
    @Min(1)
    @JsonProperty("Open")
    double open;

    @Max(1000)
    @Min(1)
    @JsonProperty("High")
    double high;

    @Max(1000)
    @Min(1)
    @JsonProperty("Low")
    double low;

    @Max(1000)
    @Min(1)
    @JsonProperty("Close")
    double close;

    @NotNull
    @DecimalMin("100")
    @DecimalMax("9999999999")
    @JsonProperty("Volume")
    double volume;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(final double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(final double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(final double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(final double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(final double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StockTrade{");
        sb.append("date=").append(date);
        sb.append(", time='").append(time).append('\'');
        sb.append(", open=").append(open);
        sb.append(", high=").append(high);
        sb.append(", low=").append(low);
        sb.append(", close=").append(close);
        sb.append(", volume=").append(volume);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StockTrade)) return false;

        final StockTrade that = (StockTrade) o;

        if (Double.compare(that.close, close) != 0) return false;
        if (Double.compare(that.high, high) != 0) return false;
        if (Double.compare(that.low, low) != 0) return false;
        if (Double.compare(that.open, open) != 0) return false;
        if (Double.compare(that.volume, volume) != 0) return false;
        if (!date.equals(that.date)) return false;
        if (!time.equals(that.time)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date.hashCode();
        result = 31 * result + time.hashCode();
        temp = Double.doubleToLongBits(open);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(high);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(low);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(close);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
