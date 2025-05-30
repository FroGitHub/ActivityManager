package back.activitymanager.repository;

import back.activitymanager.model.Activity;
import back.activitymanager.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    boolean existsByAuthor(User principal);

    @EntityGraph
    Optional<Activity> findById(Long id);

    Page<Activity> findByAuthorEmail(Pageable pageable, String email);

    Page<Activity> findByParticipantsEmail(Pageable pageable, String name);
}
