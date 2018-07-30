package dk.eboks.app.util;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import timber.log.Timber;

public class CountingFileRequestBody extends RequestBody {

    private static final int SEGMENT_SIZE = 4096; // okio.Segment.SIZE

    private final File file;
    private final ProgressListener listener;
    private final String contentType;
    private long fileSize;

    public CountingFileRequestBody(File file, String contentType, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
        this.fileSize = contentLength();
        Timber.e("Filesize is " + fileSize);
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();

                if(total != 0 && listener != null) {
                    this.listener.transferred((double) total / (double) fileSize);
                }


            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally {
            Util.closeQuietly(source);
        }
    }

    public interface ProgressListener {
        void transferred(double num);
    }

}