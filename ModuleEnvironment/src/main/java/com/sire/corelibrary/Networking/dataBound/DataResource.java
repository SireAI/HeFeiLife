/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sire.corelibrary.Networking.dataBound;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/20
 * Author:sire
 * Description: data warp with data status
 * ==================================================
 */
public class DataResource<T> {

    @NonNull
    public  DataStatus status =  DataStatus.INIT;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    public DataResource(@NonNull DataStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> DataResource<T> success(@Nullable T data) {
        return new DataResource<>(DataStatus.SUCCESS, data, null);
    }

    public static <T> DataResource<T> error(String msg, @Nullable T data) {
        return new DataResource<>(DataStatus.ERROR, data, msg);
    }

    public static <T> DataResource<T> loading(@Nullable T data) {
        return new DataResource<>(DataStatus.LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataResource<?> resource = (DataResource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (message != null ? !message.equals(resource.message) : resource.message != null) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataResource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
