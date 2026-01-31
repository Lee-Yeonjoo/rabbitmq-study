/*
package study.hellomessagequeue.step7;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderConsumer {

    private static final int MAX_RETRIES = 3; // 총 시도 제한 수
    private int retryCount = 0; // 재시도 횟수 -> 시도할 때마다 증가

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE, containerFactory = "rabbitListenerContainerFactory")  //config에 선언한 컨테이너 팩토리 메서드명을 적음
    public void processOrder(String message, Channel channel, @Header("amqp_deliveryTag") long tag) {  //ack/nack처리할 때 channel이 필요. 헤더로 메시지 태그를 갖고다녀야함
        try {
            // 강제로 실패 유발
            if ("fail".equalsIgnoreCase(message)) {  //메시지가 fail일 경우 실패 발생
                if (retryCount < MAX_RETRIES) {  //재시도 횟수 < 3
                    System.err.println("#### Fail  & Retry = " + retryCount);  //시스템 에러 메시지
                    retryCount++;  //재시도 횟수 증가
                    throw new RuntimeException(message);
                } else {
                    System.err.println("#### 최대 횟수 초과, DLQ 이동 시킴 ");
                    retryCount = 0;  //dlq로 넘어갔으니까 재시도 횟수 초기화
                    channel.basicNack(tag, false, false);  //태그 넣고, 현재 메시지만 처리, 초과됐으니까 dlq로 보낼거라서 order queue로 리큐하지 않음
                    return;
                }
            }
            // 성공 처리
            System.out.println("# 성공 : " + message);
            channel.basicAck(tag, false);  //태그를 실어서 보내고, false - 이전 메시지를 한꺼번에 처리하지 않고, 하나만 처리
            retryCount = 0;  //성공이니까 0으로 초기화
        } catch (Exception e) {
            System.err.println("# error 발생 : " + e.getMessage());
            try {
                // 실패 시 basicReject 재처리 전송
                channel.basicReject(tag, true);  //태그, 리큐해줌 -> 재처리해야되니까(이 메서드 자체가 실패한거라 재처리)
            } catch (IOException e1) {
                System.err.println("# fail & reject message : " + e1.getMessage());
            }
        }
    }
}
*/
