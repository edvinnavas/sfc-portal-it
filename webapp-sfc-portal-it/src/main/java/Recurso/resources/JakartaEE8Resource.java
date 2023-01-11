package Recurso.resources;

import java.io.Serializable;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("rest")
public class JakartaEE8Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @GET
    public Response ping() {
        return Response
                .ok("ping")
                .build();
    }

}
