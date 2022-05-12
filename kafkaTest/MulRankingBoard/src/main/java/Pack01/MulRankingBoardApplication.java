package Pack01;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MulRankingBoardApplication extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
       return builder.sources(MulRankingBoardApplication.class);
    }
	@Bean
	Jackson2JsonMessageConverter jackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	public static void main(String[] args) {
		SpringApplication.run(MulRankingBoardApplication.class, args);
	}

}
