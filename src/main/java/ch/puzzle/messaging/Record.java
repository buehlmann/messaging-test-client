package ch.puzzle.messaging;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Record {
    public Record() { }

    public Record(String message) {
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String message;

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", message='" + message +
                '}';
    }
}
