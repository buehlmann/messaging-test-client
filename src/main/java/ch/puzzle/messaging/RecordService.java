package ch.puzzle.messaging;

import org.kohsuke.randname.RandomNameGenerator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;

public class RecordService {
    static RandomNameGenerator rnd = new RandomNameGenerator(new Random().nextInt());

    @PersistenceContext
    EntityManager em;

    public void writeRecord() {
        Record record = new Record(rnd.next());

        em.persist(record);

        System.out.println("Persisted " + record);
    }

    public List<Record> findAll() {
        return em.createQuery("from Record").getResultList();
    }
}
