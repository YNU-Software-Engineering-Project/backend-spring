package sg.backend.service;

import org.springframework.core.io.Resource;

public interface FileService {
    Resource getImage(String fileName);
}
