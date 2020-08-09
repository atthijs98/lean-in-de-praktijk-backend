package lean.core;

import javax.ws.rs.core.Response;

public class Body {
    private Object content;
    private String message;
    private Response.Status status;

    public Body() {
        status = Response.Status.OK;
    }

    /**
     * Method to create a response
     *
     * @param status Response status
     * @param message Response message
     * @param content Response content
     * @return a http Response
     */
    public static Response buildResponse(Response.Status status, String message, Object content) {
        Body body = new Body();

        if (content != null) {
            body.setContent(content);
        }
        body.setStatus(status);
        body.setMessage(message);

        return body.build();
    }

    public Response build(){
        return Response.status(status).entity(this).build();
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
