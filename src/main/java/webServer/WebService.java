package webServer;

import webServer.dto.Note;
import loggingUtils.Loggers;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;

public class WebService {
    private WebServerMethodInvoker methodInvoker;

    public WebService(WebServerMethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    @JsonRpcMethod("sign_in")
    public MethodInvokeResult.SignInReturnResult signIn(
            @JsonRpcParam("login") String login,
            @JsonRpcParam("password") String password) {
        Loggers.WEB_SERVER.info("Method sign_in with login=" + login + " invoked");
        return methodInvoker.signIn(login, password);
    }

    @JsonRpcMethod("sign_up")
    public MethodInvokeResult.SignUpReturnResult signUp(
            @JsonRpcParam("login") String login,
            @JsonRpcParam("password") String password,
            @JsonRpcParam("firstName") String firstName,
            @JsonRpcParam("lastName") String lastName) {
        Loggers.WEB_SERVER.info("Method sign_up with login = " + login + ", firstName = " + firstName + ", lastName = " + lastName + " invoked");
        return methodInvoker.signUp(login, password, firstName, lastName);
    }

    @JsonRpcMethod("get_all_note_ids")
    public MethodInvokeResult.GetAllNotesIdsReturnResult getAllNotesIds(
            @JsonRpcParam("token") String token) {
        Loggers.WEB_SERVER.info("Method get_all_note_ids with token = " + token + " invoked");
        return methodInvoker.getAllNoteIds(token);
    }

    @JsonRpcMethod("get_note_by_id")
    public MethodInvokeResult.GetNoteByIdReturnResult getNoteById(
            @JsonRpcParam("token") String token,
            @JsonRpcParam("id") Integer id) {
        Loggers.WEB_SERVER.info("Method get_note_by_id with token = " + token + ", id = " + id + " invoked");
        return methodInvoker.getNoteById(token, id);
    }

    @JsonRpcMethod("save_or_update_note")
    public MethodInvokeResult.SaveOrUpdateNoteReturnResult saveOrUpdateNote(
            @JsonRpcParam("token") String token,
            @JsonRpcParam("note") Note note) {
        Loggers.WEB_SERVER.info("Method save_or_update_note with token = " + token + ", note = " + note + " invoked");
        return methodInvoker.saveOrUpdateNote(token, note);
    }

    @JsonRpcMethod("get_all_notes_by_tags")
    public MethodInvokeResult.GetAllNotesByTagsReturnResult getAllNotesByTags(
            @JsonRpcParam("token") String token,
            @JsonRpcParam("tags") String tags) {
        Loggers.WEB_SERVER.info("Method get_all_notes_by_tags with token = " + token + ", tags = " + tags + " invoked");
        return methodInvoker.getAllNotesByTags(token, tags);
    }

    @JsonRpcMethod("delete_note_by_id")
    public MethodInvokeResult.DeleteNoteByIdReturnResult deleteNoteById(
            @JsonRpcParam("token") String token,
            @JsonRpcParam("id") Integer id) {
        Loggers.WEB_SERVER.info("Method delete_note_by_id with token = " + token + ", id = " + id + " invoked");
        return methodInvoker.deleteNoteById(token, id);
    }
}