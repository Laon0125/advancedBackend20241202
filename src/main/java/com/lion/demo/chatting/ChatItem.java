package com.lion.demo.chatting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatItem {
    private int isMine; //1 이면 내가 쓴 글  0이면 상대방이 쓴 글
    private String message;
    private String timeStr;
    private int hasRead;

}
