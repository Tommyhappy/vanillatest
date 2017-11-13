package jp.linebot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

@WebServlet(name="LineServlet", urlPatterns={"/line"})
public class LineServlet extends HttpServlet {  
    
    private static final String CHANNEL_SECRET = "076f367d196ae98c03adc34cfdb3fb3a";
    private static final String CHANNEL_ACCESS_TOKEN = "D/Pq3PwT2g+59tNO65CQwbiPzhRw0ooDw+7FKXbvisQhDUm3xCCJZyeFHHTwfTYlDEG+XKROeda2HuTQ+R88DVvNwQ0TWicMcNKv6zRTGldeTneGs3UtlIS91gt/THMJg4+geBIEPvluQbr55wG8EwdB04t89/1O/w1cDnyilFU=";
    private static final String HMAC_SHA256 = "HmacSHA256";

      @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 署名検証
        String body = null;
        try (Stream<String> stream = request.getReader().lines()) {
            String signature = request.getHeader("X-Line-Signature");
            body = stream.reduce((s1, s2) -> s1 + "\n" + s2).orElse("");
            SecretKeySpec key = new SecretKeySpec(CHANNEL_SECRET.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(key);
            byte[] source = body.getBytes(StandardCharsets.UTF_8);
            String createdSignature = Base64.encodeBase64String(mac.doFinal(source));
            if (!signature.equals(createdSignature)) {
                // LINEからのリクエストじゃない場合の処理
                // 常に200を返す
                response.setStatus(200);
                return;
            }
        } catch (IOException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {}

        ObjectMapper mapper = new ObjectMapper();
        JsonNode events = mapper.readTree(body).path("events");
        // リプライの種類（今回はtext）
        String type = events.path(0).path("message").path("type").asText(null);
        // テキスト
        String query = events.path(0).path("message").path("text").asText(null);
        // 返信用Token
        String replyToken = events.path(0).path("replyToken").asText(null);
        // リプライを送る
        HttpPost httpPost = new HttpPost("https://api.line.me/v2/bot/message/reply");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + CHANNEL_ACCESS_TOKEN);
        // 返信用のJSON
        // サンプルとして「リプライありがとう！」を返す
        String replyBody = String.format("{\"replyToken\":\"%s\", \"messages\":[{\"type\":\"text\", \"text\":\"リプライありがとう！\"}]}", replyToken);
        StringEntity params = new StringEntity(replyBody, StandardCharsets.UTF_8);
        httpPost.setEntity(params);
        try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse resp = client.execute(httpPost);
                BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), StandardCharsets.UTF_8)))
        {
            int statusCode = resp.getStatusLine().getStatusCode();
            switch (statusCode) {
            case 200:
                // ↓これは空のJSON({})が返るはず
                br.readLine();
                break;
            default:
            }
        } catch (final ClientProtocolException e) {
        } catch (final IOException e) {
        }
        response.setStatus(200);
    }
}
