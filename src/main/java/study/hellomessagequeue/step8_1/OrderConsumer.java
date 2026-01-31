package study.hellomessagequeue.step8_1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    public OrderConsumer(RabbitTemplate rabbitTemplate, RetryTemplate retryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.retryTemplate = retryTemplate;
    }

    //주문 큐에서 메시지 수신했을 때의 동작
    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE)
    public void consume(String message) {
        //channel의 reject, ack, nack 설정없이 자동 재시도하는 방법
        retryTemplate.execute(context -> {
            try {
                System.out.println("# 리시브 메시지 : " + message + " # retry : " + context.getRetryCount());  //context에서 재시도 횟수를 조회할 수 있음
                // 강제로 실패 발생
                if ("fail".equalsIgnoreCase(message)) {
                    throw new RuntimeException(message);
                }
                System.out.println("# 메시지 처리 성공 " + message);
            } catch (Exception e) {
                //재시도 로직 구현
                if (context.getRetryCount() >= 2) {  //재시도 3번 다 시도한 경우
                    //DLX로 보내기
                    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_TOPIC_DLX,
                            RabbitMQConfig.DEAD_LETTER_ROUTING_KEY, message);  //DLQ에 해당하는 라우팅 키를 전달
                } else {
                    //리트라이 아닌 경우 -> 한번 더 에러 밥ㄹ생시키기 -> 3번 채우면 dlx로 가도록
                    throw e;
                }
            }
            return null;
        });
    }
}
