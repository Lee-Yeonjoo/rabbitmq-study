package study.hellomessagequeue.step7;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //주문 완료 시 동작할 큐와 exchange 이름
    public static final String ORDER_COMPLETED_QUEUE = "order_completed_queue";
    public static final String ORDER_EXCHANGE = "order_completed_exchange";
    //DLQ, DLX 이름
    public static final String DLQ = "deadLetterQueue";
    public static final String DLX = "deadLetterExchange";

    //정상 건들은 얘가 처리
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    //실패 건들은 얘가 처리
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    //메시지가 처리되지 못했을 경우 자동으로 DLQ로 이동
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_COMPLETED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)  //dlx 설정
                .withArgument("x-dead-letter-routing-key", DLQ)
                .ttl(5000)  //ttl 5초
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    @Bean
    public Binding orderCompletedBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with("order.completed.#");  //큐랑 exchange를 바인딩걸고, 라우팅 키 설정
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ);
    }
}
