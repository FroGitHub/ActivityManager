package back.activitymanager.service.impl;

import back.activitymanager.exception.DropboxProcessException;
import back.activitymanager.service.DropboxService;
import back.activitymanager.service.UserService;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DropboxServiceImpl implements DropboxService {

    private final DbxClientV2 client;

    @Value("${dropbox.folder:/photos}")
    private String folderPath;

    @Override
    public String uploadPhoto(MultipartFile file) {
        String path = "/photos/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try (InputStream in = file.getInputStream()) {
            client.files().uploadBuilder(path).uploadAndFinish(in);
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload photo", e);
        }
    }

    @Override
    public String getPhotoLink(String path) {

        if (UserService.DEFAULT_PHOTO_PATH.equals(path)) {
            return "";
        }

        try {
            GetTemporaryLinkResult result = client.files().getTemporaryLink(path);
            return result.getLink();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate photo link", e);
        }
    }

    @Override
    public void deletePhoto(String dropboxPath) {
        try {
            client.files().deleteV2(dropboxPath);
        } catch (DbxException e) {
            throw new DropboxProcessException("Can't delete photo: " + dropboxPath, e);
        }
    }
}
