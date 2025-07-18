package back.activitymanager.repository;

import back.activitymanager.model.Activity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class ActivitySpecification {

    public static Specification<Activity> hasForWho(String forWho) {
        return (root, query, cb) -> forWho == null ? null :
                cb.equal(root.get("forWho"), forWho);
    }

    public static Specification<Activity> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null :
                cb.equal(root.get("category"), category);
    }

    public static Specification<Activity> hasFormat(String format) {
        return (root, query, cb) -> format == null ? null :
                cb.equal(root.get("format"), format);
    }

    public static Specification<Activity> afterDate(LocalDateTime dateTime) {
        return (root, query, cb) -> dateTime == null ? null :
                cb.greaterThanOrEqualTo(root.get("localDateTime"), dateTime);
    }

}
