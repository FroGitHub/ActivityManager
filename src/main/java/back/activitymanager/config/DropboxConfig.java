package back.activitymanager.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class DropboxConfig {

    @Bean
    public DbxClientV2 dropboxClient(@Value("${dropbox.token}") String accessToken) {
        DbxRequestConfig config = DbxRequestConfig
                .newBuilder("spring-dropbox-app")
                .build();
        return new DbxClientV2(config, accessToken);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

