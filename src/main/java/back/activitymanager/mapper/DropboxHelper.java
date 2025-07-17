package back.activitymanager.mapper;

import back.activitymanager.service.DropboxService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DropboxHelper {

    private final DropboxService dropboxService;

    @Autowired
    public DropboxHelper(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
    }

    @Named("resolvePhotoLink")
    public String resolvePhotoLink(String path) {
        if (path == null) {
            return "";
        }

        return dropboxService.getPhotoLink(path);
    }
}
