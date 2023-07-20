package click.klaassen;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workspace")
public class WorkspaceResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String workspace() {
        return "Workspace Endpoint";
    }
}