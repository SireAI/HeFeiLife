package com.sire.corelibrary.Networking.downlaod.downloadCore;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:自定义精度的body
 * ==================================================
 */
public class DownloadResponseBody extends ResponseBody {

    private final String fileSize;
    private ResponseBody responseBody;
    private DownloadProgressListener progressListener;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, String fileSize, DownloadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        this.fileSize = fileSize;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != progressListener) {
                    progressListener.update(totalBytesRead, TextUtils.isEmpty(fileSize)?responseBody.contentLength():Long.valueOf(fileSize), bytesRead == -1);
                }
                return bytesRead;
            }
        };

    }
}
