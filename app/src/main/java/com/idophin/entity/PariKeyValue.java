/*
 * Copyright (c) 2017. 4. 27. by Idophin Co.Ltd. All rights reserved.
 *
 */

package com.idophin.entity;

public class PariKeyValue {

    public final String key;
    public final String value;

    public PariKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PariKeyValue pair = (PariKeyValue) o;

        if (key != null ? !key.equals(pair.key) : pair.key != null)
            return false;
        if (value != null ? !value.equals(pair.value) : pair.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}
