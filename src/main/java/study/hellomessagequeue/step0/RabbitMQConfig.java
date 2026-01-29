package study.hellomessagequeue.step0;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "helloqueue";

    @Bean
    public Queue queue() {
        //durable: volatile(휘발성) 여부 false=휘발성, true=영속성(persistent)
        //래빗엠큐 서버가 종료될 때 큐에 메시지가 사라지는 옵션
        return new Queue(QUEUE_NAME, false);
    }

    //통신을 위한 템플릿 JdbcTemplate같은 개념
    //ConnectionFactory 주입 - 래빗엠큐의 연결을 관리하는 객체
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    //수신한 메시지를 특정 클래스의 특정 메서드로 전달하는 애
    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");  //receiverMessage라는 메서드로 전달
    }
}
