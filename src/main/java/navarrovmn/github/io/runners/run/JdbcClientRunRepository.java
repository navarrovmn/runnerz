package navarrovmn.github.io.runners.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientRunRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientRunRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll() {
        return jdbcClient.sql("select * from run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT id, location, started_on, completed_on FROM Run where id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run) {
        var updated = jdbcClient.sql("INSERT INTO Run(id, location, started_on, completed_on) values (?,?,?,?)")
                .params(List.of(run.id(), run.location(), run.startedOn(), run.completedOn()))
                .update();

        Assert.state(updated == 1, "Failed to create run " + run.location());
    }

    public void update(Run run, Integer id) {
        var updated = jdbcClient.sql("update run set location = ?, started_on = ?, completed_on = ? where id = ?")
                .params(List.of(run.location(), run.startedOn(), run.completedOn(), id))
                .update();

        Assert.state(updated == 1, "Failed to update run " + run.location());
    }

    public void delete(Integer id) {
        var deleted = jdbcClient.sql("delete from run where id = :id")
                .param("id", id)
                .update();

        Assert.state(deleted == 1, "Failed to delete run " + id);
    }

    public int count() {
        return jdbcClient.sql("select * from run").query().listOfRows().size();
    }

    public void saveAll(List<Run> runs) {
        runs.forEach(this::create);
    }

}
