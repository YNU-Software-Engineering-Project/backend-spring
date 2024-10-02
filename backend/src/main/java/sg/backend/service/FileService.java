package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.User;
import sg.backend.exception.CustomException;
import sg.backend.repository.UserRepository;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FileService {

    private final UserRepository userRepository;

    @Value("${profile.path}")
    private String profileFilePath;

    public Resource getProfileImage(String fileName) {
        Resource resource;

        try {
            resource = new UrlResource("file:" + profileFilePath + fileName);
        } catch (Exception e) {
            throw new CustomException("NF", "Image not found.");
        }

        return resource;
    }

    @Transactional
    public ResponseEntity<ResponseDto> deleteProfileImage(String fileName, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER));

        user.setProfileImage(null);
        deleteCurrentProfileImage(fileName, profileFilePath);

        return ResponseDto.success();
    }

    public static void deleteCurrentProfileImage(String currentImageUrl, String profileFilePath) {
        int index = currentImageUrl.lastIndexOf("/");
        String fileName = currentImageUrl.substring(index+1);
        String path = profileFilePath + fileName;
        File file = new File(path);

        if (file.exists()) {
            if(!file.delete()) {
                throw new CustomException("FF", "Failed to delete file.");
            }
        }
    }
}
