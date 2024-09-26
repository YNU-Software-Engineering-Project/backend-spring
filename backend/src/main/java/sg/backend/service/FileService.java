package sg.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Value("${profile.path}")
    private String profileFilePath;

    public Resource getProfileImage(String fileName) {
        Resource resource = null;

        try {
            resource = new UrlResource("file:" + profileFilePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return resource;
    }
}
