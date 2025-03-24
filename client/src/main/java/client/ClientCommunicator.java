package client;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {
    private final String url;
    private final String method;

    private String readResponse(InputStream stream) throws IOException {
        if(stream == null){
            return "";
        }
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))){
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        }
    }

    public ClientCommunicator(String url, String method){
        this.url = url;
        this.method = method;
    }

    public String executeRequest(String header, String body) throws URISyntaxException, IOException {
        URL requestURL = new URI(url).toURL();
        HttpURLConnection http = (HttpURLConnection) requestURL.openConnection();
        http.setReadTimeout(5000);
        http.setRequestMethod(method);
        http.setDoOutput(true);
        http.addRequestProperty("authorization", header);
        try(var outputStream = http.getOutputStream()){
            outputStream.write(body.getBytes());
        }

        http.connect();
        var status = http.getResponseCode();
        if(status >= 200 && status < 300) {

            try (InputStream respBody = http.getInputStream()) {
                return readResponse(respBody);
            }
        }
        else{
            try(InputStream err = http.getErrorStream()){
                return readResponse(err);
            }
        }
    }

}
