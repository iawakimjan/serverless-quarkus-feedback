package nl.p4c.feedback.api.resource;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.p4c.feedback.api.form.InviteForm;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import nl.p4c.feedback.api.model.Invite;
import nl.p4c.feedback.api.service.InviteService;

@Path("/invites")
public class InviteResource {

    @Inject
    InviteService inviteService;

    @Inject
    Template index;

    @Inject
    Template intro;

    @Inject
    Template finish;

    @POST
    @Path("/validate-invite")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response validateInvite(@MultipartForm InviteForm inviteForm) {
        Optional<Invite> invite = inviteService.findInvite(inviteForm.inviteCode);

        try {
            if (invite.isEmpty()) {
                return Response.seeOther(new URI("/no-code"))
                    .build();
            } else if ("CLOSED".equals(invite.get()
                .getStatus())) {
                return Response.seeOther(new URI("/invites/" + inviteForm.inviteCode + "/finish"))
                    .build();
            } else {
                return Response.seeOther(new URI("/invites/" + inviteForm.inviteCode))
                    .build();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error");
        }
    }

    @GET
    @Path("/{inviteCode}")
    public TemplateInstance getInvite(@PathParam("inviteCode") String inviteCode) {
        Optional<Invite> invite = inviteService.findInvite(inviteCode);

        if (invite.isEmpty()) {
            return index.data("error", "Sorry, but your code does not exist");
        } else if ("CLOSED".equals(invite.get()
            .getStatus())) {
            return this.finish(inviteCode);
        } else {
            return intro.data("invite", invite.get());
        }
    }

    @GET
    @Path("/{inviteCode}/finish")
    public TemplateInstance finish(@PathParam("inviteCode") String inviteCode) {
        Optional<Invite> invite = inviteService.findInvite(inviteCode);

        if (invite.isEmpty()) {
            throw new RuntimeException();
        }

        inviteService.closeInvite(inviteCode);

        return finish.data("invite", invite.get());
    }
}
