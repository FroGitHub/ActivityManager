package back.activitymanager.service;

import org.springframework.web.multipart.MultipartFile;

public interface DropboxService {
    String uploadPhoto(MultipartFile file);

    String getPhotoLink(String path);

    void deletePhoto(String dropboxPath);
}
