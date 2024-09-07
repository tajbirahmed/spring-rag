package spring_rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.util.function.Function;
import java.util.logging.Logger;

@SpringBootApplication
public class RagApplication {

	@Value("classpath:/chess-ai.pdf")
	private Resource resource;

	public static void main(String[] args) {
		SpringApplication.run(RagApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
		return chatClientBuilder.build();
	}

	@Bean
	ApplicationRunner applicationRunner(VectorStore vectorStore) {
		return args -> {
			TikaDocumentReader documentReader = new TikaDocumentReader(resource);
			TextSplitter textSplitter = new TokenTextSplitter(
					400, 100, 5, 10000, true
			);
			vectorStore.accept(
					textSplitter.apply(
							documentReader.get()));
		};
	}

}
