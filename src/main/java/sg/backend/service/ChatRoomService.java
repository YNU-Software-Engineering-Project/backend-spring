package sg.backend.service;

import static sg.backend.service.UserService.findUserByEmail;
import static sg.backend.service.UserService.formatter;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.object.ChatMessageDataDto;
import sg.backend.dto.object.ChatRoomDataDto;
import sg.backend.dto.response.chat.GetChatRoomListDto;
import sg.backend.entity.ChatMessage;
import sg.backend.entity.ChatRoom;
import sg.backend.entity.Role;
import sg.backend.entity.User;
import sg.backend.exception.CustomException;
import sg.backend.repository.ChatMessageRepository;
import sg.backend.repository.ChatRoomRepository;
import sg.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ResponseEntity<GetChatRoomListDto> getChatRoomList(String email, Pageable pageable) {
        User user = findUserByEmail(email, userRepository);
        Page<ChatRoom> chatRooms;
        List<ChatRoomDataDto> data = new ArrayList<>();

        if (user.getRole().equals(Role.ADMIN)) {
            // 관리자와 관련된 채팅방 조회 (대화 내역 존재하는 것만)
            chatRooms = chatRoomRepository.findByAdminIdAndMessagesNotEmpty(user.getUserId(), pageable);

            data = chatRooms.getContent().stream()
                    .map(room -> toChatRoomDataDto(room, user.getUserId()))
                    .toList();
        } else if (user.getRole().equals(Role.USER)) {
            // 사용자의 채팅방 조회 (없으면 생성)
            ChatRoom chatRoom = chatRoomRepository.findByUserId(user.getUserId())
                    .orElseGet(() -> createChatRoomForUser(user));

            chatRooms = createSingleChatRoomPage(chatRoom, pageable);
            data.add(toChatRoomDataDto(chatRoom, user.getUserId()));
        } else {
            throw new IllegalArgumentException("Invalid role for user: " + user.getRole());
        }

        return GetChatRoomListDto.success(chatRooms, data);
    }

    private ChatRoom createChatRoomForUser(User user) {
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        if (admins.isEmpty()) {
            throw new CustomException("NOT_EXISTED_ADMIN", "관리자 계정이 존재하지 않아 채팅방을 생성할 수 없습니다.");
        }

        User randomAdmin = admins.get((int) (Math.random() * admins.size()));

        ChatRoom chatRoom = ChatRoom.builder()
                .userId(user.getUserId())
                .adminId(randomAdmin.getUserId())
                .messages(new ArrayList<>())
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    private ChatRoomDataDto toChatRoomDataDto(ChatRoom chatRoom, Long currentUserId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderByCreatedAtDesc(chatRoom);

        ChatMessage latestMessage = messages.isEmpty() ? null : messages.get(0);
        long counterpartId = chatRoom.getAdminId().equals(currentUserId) ? chatRoom.getUserId() : chatRoom.getAdminId();

        return ChatRoomDataDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .counterpartId(counterpartId)
                .counterpartProfile(userRepository.findByUserId(counterpartId).get().getProfileImage())
                .latestMessage(latestMessage != null ? toChatMessageDataDto(latestMessage) : null)
                .build();
    }

    private ChatMessageDataDto toChatMessageDataDto(ChatMessage message) {
        return ChatMessageDataDto.builder()
                .chatMessageId(message.getChatMessageId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .createdAt(formatter.format(message.getCreatedAt()))
                .build();
    }

    private Page<ChatRoom> createSingleChatRoomPage(ChatRoom chatRoom, Pageable pageable) {
        return new PageImpl<>(List.of(chatRoom), pageable, 1);
    }
}
