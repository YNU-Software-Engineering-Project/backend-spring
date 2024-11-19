package sg.backend.service;

import static sg.backend.service.UserService.formatter;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.object.ChatMessageDataDto;
import sg.backend.dto.request.chat.ChatMessageRequest;
import sg.backend.dto.response.chat.ChatMessageResponse;
import sg.backend.dto.response.chat.GetChatMessagesResponse;
import sg.backend.entity.ChatMessage;
import sg.backend.entity.ChatRoom;
import sg.backend.exception.CustomException;
import sg.backend.repository.ChatMessageRepository;
import sg.backend.repository.ChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageResponse processAndSaveMessage(Long roomId, ChatMessageRequest chatMessageRequest) {
        // 채팅방 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("NOT_FOUND", "해당 채팅방을 찾을 수 없습니다."));

        // 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageRequest.getContent())
                .senderId(chatMessageRequest.getSenderId())
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();


        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 저장된 메시지를 DTO로 변환하여 반환
        return toChatMessageResponse(savedMessage);
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .createdAt(formatter.format(chatMessage.getCreatedAt()))
                .build();
    }

    @Transactional
    public ResponseEntity<GetChatMessagesResponse> getChatMessages(Long chatRoomId, Pageable pageable) {
        // 채팅방 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException("NOT_FOUND", "해당 채팅방을 찾을 수 없습니다."));

        // 메시지 조회
        Page<ChatMessage> messages = chatMessageRepository.findByChatRoom(chatRoom, pageable);

        // DTO 변환
        List<ChatMessageDataDto> data = messages.getContent().stream()
                .map(this::toChatMessageDataDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(GetChatMessagesResponse.success(messages, data));
    }

    private ChatMessageDataDto toChatMessageDataDto(ChatMessage message) {
        return ChatMessageDataDto.builder()
                .chatMessageId(message.getChatMessageId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .createdAt(formatter.format(message.getCreatedAt()))
                .build();
    }
}
