package back.activitymanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "activities")
@Getter
@Setter
@SQLDelete(sql = "UPDATE activities SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String forWho;

    @Column(nullable = false)
    private int numberOfPeople;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String format;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false)
    private int imgId;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(
            name = "users_activities",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    public void addParticipant(User user) {
        if (!participants.contains(user)) {
            participants.add(user);
            user.addActivity(this);
            numberOfPeople++;
        }
    }

    public void removeParticipant(User user) {
        if (participants.contains(user)) {
            participants.remove(user);
            user.removeActivity(this);
            numberOfPeople--;
        }
    }
}
