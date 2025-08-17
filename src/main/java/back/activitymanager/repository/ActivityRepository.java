package back.activitymanager.repository;

import back.activitymanager.model.Activity;
import back.activitymanager.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<Activity, Long>,
        JpaSpecificationExecutor<Activity> {

    boolean existsByAuthor(User principal);

    @EntityGraph(attributePaths = {"participants", "author"})
    Optional<Activity> findById(Long id);

    @Query("SELECT a FROM Activity a JOIN a.author author "
            + "WHERE a.isDeleted = false AND a.localDateTime > CURRENT_TIMESTAMP")
    @EntityGraph(attributePaths = {"participants", "author"})
    Page<Activity> findAll(Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.author author "
            + "WHERE a.isDeleted = false AND a.localDateTime > CURRENT_TIMESTAMP "
            + "AND author.email = :email")
    @EntityGraph(attributePaths = {"participants", "author"})
    Page<Activity> findByAuthorEmail(Pageable pageable, @Param("email") String email);

    @Query("SELECT a FROM Activity a JOIN a.participants p "
            + "WHERE a.isDeleted = false AND a.localDateTime > CURRENT_TIMESTAMP "
            + "AND p.email = :email")
    @EntityGraph(attributePaths = {"participants", "author"})
    Page<Activity> findByParticipantsEmail(Pageable pageable, @Param("email") String email);
}
