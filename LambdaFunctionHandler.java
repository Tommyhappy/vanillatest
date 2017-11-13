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

public class LambdaFunctionHandler implements RequestHandler<Input, Object> {

    @Override
    public Object handleRequest(Input input, Context context) {
        // TODO Auto-generated method stub

        context.getLogger().log("token : " + input.getEvents()[0].getReplyToken());
        context.getLogger().log("text : " + input.getEvents()[0].getMessage().getText());

        Output output = new Output();
        output.setReplyToken(input.getEvents()[0].getReplyToken());
        Messages outMessage = new Messages();
        outMessage.setType("text");
        outMessage.setText(input.getEvents()[0].getMessage().getText() + "?");
        output.getMessages().add(outMessage);

        HttpPost httpPost = new HttpPost("https://api.line.me/v2/bot/message/reply");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + D/Pq3PwT2g+59tNO65CQwbiPzhRw0ooDw+7FKXbvisQhDUm3xCCJZyeFHHTwfTYlDEG+XKROeda2HuTQ+R88DVvNwQ0TWicMcNKv6zRTGldeTneGs3UtlIS91gt/THMJg4+geBIEPvluQbr55wG8EwdB04t89/1O/w1cDnyilFU=);

        Gson gson = new Gson();
        context.getLogger().log(gson.toJson(output));
        StringEntity entity = new StringEntity(gson.toJson(output), StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse resp = client.execute(httpPost);
                BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), StandardCharsets.UTF_8)))
        {
            int statusCode = resp.getStatusLine().getStatusCode();
            switch (statusCode) {
            case 200:
                br.readLine();
                break;
            default:
            }
        } catch (final ClientProtocolException e) {
        } catch (final IOException e) {
        }
        return null;
    }

}
