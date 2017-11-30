package ch.puzzle.messaging;

import org.kohsuke.randname.RandomNameGenerator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;

public class RecordService {
    private static RandomNameGenerator rnd = new RandomNameGenerator(new Random().nextInt());

    @PersistenceContext
    EntityManager em;

    public void persistRecord() {
        persistRecord(rnd.next());
    }

    public Record persistRecord(String payload) {
        Record record = new Record(payload + " [" + rnd.next() + "]");
        em.persist(record);
        return record;
    }

    public List<Record> findAll() {
        return em.createQuery("from Record").getResultList();
    }
}
