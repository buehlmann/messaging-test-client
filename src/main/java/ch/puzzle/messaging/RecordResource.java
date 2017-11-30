package ch.puzzle.messaging;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import java.util.List;

@Path("/records")
public class RecordResource {
    @Inject
    RecordService service;

    @GET
    @Produces("text/json")
    public List<Record> findAll() {
        return service.findAll();
    }
}
