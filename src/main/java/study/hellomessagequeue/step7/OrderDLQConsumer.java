/*
package study.hellomessagequeue.step7;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDLQConsumer {

    private final RabbitTemplate rabbitTemplate;

    public OrderDLQConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //dlq의 메시지를 수신해서 동작
    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void process(String message) {
        System.out.println("DLQ Message Received: " + message);

        try {
            //실패 데이터 보정
            String fixMessage = "success";

            //보정한 메시지를 다시 order_exchange로 보낸다.
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    "order.completed.shipping",
                    fixMessage
            );
            System.out.println("DLQ Message Sent: " + fixMessage);
        } catch (Exception e) {
            System.err.println("### [DLQ Consumer Error] " + e.getMessage());
        }
    }
}
*/
