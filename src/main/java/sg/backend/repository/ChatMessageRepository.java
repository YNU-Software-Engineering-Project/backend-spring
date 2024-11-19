package sg.backend.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.backend.entity.ChatMessage;
import sg.backend.entity.ChatRoom;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
    Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
