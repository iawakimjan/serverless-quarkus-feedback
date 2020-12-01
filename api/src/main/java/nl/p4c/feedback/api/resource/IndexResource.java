package nl.p4c.feedback.api.resource;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class IndexResource {

    @Inject
    Template index;

    @GET
    public TemplateInstance index() {
        return index.instance();
    }

    @GET
    @Path("no-code")
    public TemplateInstance noCode() {
        return index.data("error", "Sorry, your code does not exist");
    }
}
