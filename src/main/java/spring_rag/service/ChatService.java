package spring_rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import spring_rag.dto.Answer;
import spring_rag.dto.Question;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/generate-subject.st")
    private Resource resource;

    public ChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public Answer ask(Question question) {
        List<Document> similarDocuments =
                vectorStore
                        .similaritySearch(
                                SearchRequest
                                        .query(question.question())
                                        .withTopK(2)
                        );

        List<String> contentList =
                similarDocuments
                        .stream()
                        .map(Document :: getContent)
                        .toList();

        String answer =
                chatClient
                        .prompt()
                        .advisors(new SimpleLoggerAdvisor())
                        .user(sp ->
                                sp
                                        .text(resource)
                                        .param("input", question.question())
                                        .param("documents", String.join("\n", contentList))
                        )
                        .call()
                        .content();

        return new Answer(answer);


    }
}
