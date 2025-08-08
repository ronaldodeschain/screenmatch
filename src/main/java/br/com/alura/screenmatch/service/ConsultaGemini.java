package br.com.alura.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class ConsultaGemini {

    public static String obterTraducao(String texto) {

            Client client = Client.builder().apiKey(System.getenv("GEMENI_APIKEY"))
                    .build();;

            GenerateContentResponse response =  client.models.
                    generateContent("gemini-2.0-flash",
                            "Traduza para o português o texto: " + texto,
                            null);

            return response.text();
    }
}














