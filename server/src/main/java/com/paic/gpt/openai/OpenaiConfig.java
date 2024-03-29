package com.paic.gpt.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class OpenaiConfig {

    public static final long timeoutConn = 10;
    public static final long timeout = 60L;

//    @Value("${openai.api-key1}")
    @Value("${openai.gpt35.api-key1}")
    private String apiKey1;

    @Value("${openai.gpt35.endpoint}")
    private String endpointGpt35;

    /*
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
    @Bean
    public OpenAiClient init() {
        //国内访问需要做代理，国外服务器不需要
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .proxy(proxy)//自定义代理
                .addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(timeoutConn, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(timeout, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(timeout, TimeUnit.SECONDS)//自定义超时时间
                .build();
        //构建客户端
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Collections.singletonList(apiKey1))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
//                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();
        return openAiClient;
    }

     */

    @Bean
    public OpenAIClient init() {
        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(endpointGpt35)
                .credential(new AzureKeyCredential(apiKey1))
//                .configuration(com.azure.core.util.Configuration.getGlobalConfiguration())
                .buildClient();
        return client;
    }


}
