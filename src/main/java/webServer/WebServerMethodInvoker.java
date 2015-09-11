package webServer;

import webServer.dto.Note;

public interface WebServerMethodInvoker {
    MethodInvokeResult.SignInReturnResult signIn(String login, String password);

    MethodInvokeResult.SignUpReturnResult signUp(String login, String password, String firstName, String lastName);

    MethodInvokeResult.GetAllNotesIdsReturnResult getAllNoteIds(String token);

    MethodInvokeResult.GetNoteByIdReturnResult getNoteById(String token, Integer id);

    MethodInvokeResult.SaveOrUpdateNoteReturnResult saveOrUpdateNote(String token, Note note);

    MethodInvokeResult.GetAllNotesByTagsReturnResult getAllNotesByTags(String token, String tags);

    MethodInvokeResult.DeleteNoteByIdReturnResult deleteNoteById(String token, Integer id);
}