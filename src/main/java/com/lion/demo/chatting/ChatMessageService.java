package com.lion.demo.chatting;

import java.util.List;
import java.util.Map;

public interface ChatMessageService {
    List<ChatMessage> getListByUser(String uid, String friendUid);
    ChatMessage getLastChatMessage(String uid, String friendUid);
    Map<String, List<ChatMessage>> getChatMessagesByDate(String uid, String friendUid);
    int getNewCount(String friendUid, String uid);
    ChatMessage insertChatMessage(ChatMessage chatMessage);

}
