package org.fisco.bcos.sdk.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.fisco.bcos.sdk.channel.ResponseCallback;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.model.Message;
import org.fisco.bcos.sdk.model.Response;

public class HttpConnection implements Connection {

    private final String url;

    public HttpConnection(ConfigOption config) {
        this.url = config.getNetworkConfig().getPeers().get(0);
    }
    /** close connection */
    public void close() {}

    public String getEndpoint() {
        return url;
    }
    /**
     * connect to node
     *
     * @return true if connected
     */
    public Boolean connect() {
        return true;
    }

    public String callMethod(String request) throws IOException {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpPost httppost = new HttpPost(url);

            final File file = new File(request);

            final InputStreamEntity reqEntity =
                    new InputStreamEntity(
                            new FileInputStream(file), -1, ContentType.APPLICATION_JSON);
            httppost.setEntity(reqEntity);
            System.out.println("Executing request: " + request);
            try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public void asyncCallMethod(Message message, ResponseCallback callback) throws IOException {
        String response = this.callMethod(Arrays.toString(message.getData()));
        Response resp = new Response();
        resp.setContent(response);
        callback.onResponse(resp);
    }
}
