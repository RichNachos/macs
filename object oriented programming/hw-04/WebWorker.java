import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WebWorker extends Thread {
    private final WebFrame frame;
    private final String url;
    private final int rowId;
    public WebWorker(String url, int rowId, WebFrame frame) {
        this.frame = frame;
        this.url = url;
        this.rowId = rowId;
    }

    @Override
    public void run() {
        download();
    }
    private void updateFrame(long startTime, int downloadTime, int downloadSize) {
        String dataToSend = new SimpleDateFormat("hh:mm:ss").format(new Date(startTime)) + "  " + downloadTime + "ms  " + downloadSize + " bytes";
        frame.workerFinished(dataToSend, rowId);
    }

  // This is the core web/download i/o code...
 	public void download() {
        InputStream input = null;
        StringBuilder contents;
        try {
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            long startTime = System.currentTimeMillis();
            int downloadSize = 0;
            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
                downloadSize += len;
            }

            // Successful download if we get here
            long endTime = System.currentTimeMillis();
            int downloadTime = (int) (endTime - startTime);

            String dataToSend = new SimpleDateFormat("hh:mm:ss").format(new Date(startTime)) + "  " + downloadTime + "ms  " + downloadSize + " bytes";
            frame.workerFinished(dataToSend, rowId);
        }
        // Otherwise control jumps to a catch...
        catch(IOException ignored) {
            frame.workerFinished("error", rowId);
        } catch (InterruptedException exception) {
            // YOUR CODE HERE
            // deal with interruption
            frame.workerFinished("interrupted", rowId);
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }
}
