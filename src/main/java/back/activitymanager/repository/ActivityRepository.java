package back.activitymanager.repository;

import back.activitymanager.model.Activity;
import back.activitymanager.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    boolean existsByAuthor(User principal);

    @EntityGraph
    @Query("SELECT a FROM Activity a WHERE a.isDeleted = false AND a.localDateTime > current_timestamp")
    Optional<Activity> findById(Long id);

    @Query("SELECT a FROM Activity a WHERE a.isDeleted = false AND a.localDateTime > current_timestamp")
    Page<Activity> findByAuthorEmail(Pageable pageable, String email);

    @Query("SELECT a FROM Activity a WHERE a.isDeleted = false AND a.localDateTime > current_timestamp")
    Page<Activity> findByParticipantsEmail(Pageable pageable, String name);
}
