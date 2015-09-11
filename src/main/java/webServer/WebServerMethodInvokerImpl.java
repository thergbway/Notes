package webServer;

import dao.DAO;
import webServer.dto.Note;

public class WebServerMethodInvokerImpl implements WebServerMethodInvoker {
    @Override
    public MethodInvokeResult.SignInReturnResult signIn(String login, String password) {
        String token = DAO.createToken(login, password);
        if (token == null) {
            MethodInvokeResult.ResultCodes resultCode = MethodInvokeResult.ResultCodes.INVALID_LOGIN_OR_PASSWORD_ERROR;
            return new MethodInvokeResult.SignInReturnResult(resultCode.jsonRpcCode, null);
        } else {
            MethodInvokeResult.ResultCodes resultCode = MethodInvokeResult.ResultCodes.SUCCESS;
            return new MethodInvokeResult.SignInReturnResult(resultCode.jsonRpcCode, token);
        }
    }

    @Override
    public MethodInvokeResult.SignUpReturnResult signUp(String login, String password, String firstName, String lastName) {
        return new MethodInvokeResult.SignUpReturnResult(DAO.signUp(login, password, firstName, lastName).jsonRpcCode);
    }

    @Override
    public MethodInvokeResult.GetAllNotesIdsReturnResult getAllNoteIds(String token) {
        MethodInvokeResult.ResultCodes tokenValidationResult = DAO.isValidToken(token);
        if (tokenValidationResult != MethodInvokeResult.ResultCodes.SUCCESS)
            return new MethodInvokeResult.GetAllNotesIdsReturnResult(tokenValidationResult.jsonRpcCode, null);

        Integer[] noteIds = DAO.getAllNoteIds(DAO.getUserIdByToken(token));
        return new MethodInvokeResult.GetAllNotesIdsReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode, noteIds);
    }

    @Override
    public MethodInvokeResult.GetNoteByIdReturnResult getNoteById(String token, Integer id) {
        MethodInvokeResult.ResultCodes tokenValidationResult = DAO.isValidToken(token);
        if (tokenValidationResult != MethodInvokeResult.ResultCodes.SUCCESS)
            return new MethodInvokeResult.GetNoteByIdReturnResult(tokenValidationResult.jsonRpcCode, null);

        Integer userId = DAO.getUserIdByToken(token);
        if (!DAO.isNoteExistingAndBelongsToUser(id, userId)) {
            return new MethodInvokeResult.GetNoteByIdReturnResult(MethodInvokeResult.ResultCodes.INVALID_ID_ERROR.jsonRpcCode, null);
        }

        Note note = DAO.getNoteById(id);
        return new MethodInvokeResult.GetNoteByIdReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode, note);
    }

    @Override
    public MethodInvokeResult.SaveOrUpdateNoteReturnResult saveOrUpdateNote(String token, Note note) {
        MethodInvokeResult.ResultCodes tokenValidationResult = DAO.isValidToken(token);
        if (tokenValidationResult != MethodInvokeResult.ResultCodes.SUCCESS)
            return new MethodInvokeResult.SaveOrUpdateNoteReturnResult(tokenValidationResult.jsonRpcCode, null);

        if (note.getId() == null) {
            Integer userId = DAO.getUserIdByToken(token);
            Integer noteId = DAO.saveNewNote(note, userId);
            return new MethodInvokeResult.SaveOrUpdateNoteReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode, noteId);
        } else {
            DAO.updateNote(note);
            return new MethodInvokeResult.SaveOrUpdateNoteReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode, note.getId());
        }
    }

    @Override
     public MethodInvokeResult.GetAllNotesByTagsReturnResult getAllNotesByTags(String token, String tags) {
        MethodInvokeResult.ResultCodes tokenValidationResult = DAO.isValidToken(token);
        if (tokenValidationResult != MethodInvokeResult.ResultCodes.SUCCESS)
            return new MethodInvokeResult.GetAllNotesByTagsReturnResult(tokenValidationResult.jsonRpcCode, null);

        Integer userId = DAO.getUserIdByToken(token);
        Integer[] noteIds = DAO.getAllNotesByTagAndBelongsToUser(tags.split(" ")[0], userId);

        return new MethodInvokeResult.GetAllNotesByTagsReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode, noteIds);
    }

    @Override
    public MethodInvokeResult.DeleteNoteByIdReturnResult deleteNoteById(String token, Integer id) {
        MethodInvokeResult.ResultCodes tokenValidationResult = DAO.isValidToken(token);
        if (tokenValidationResult != MethodInvokeResult.ResultCodes.SUCCESS)
            return new MethodInvokeResult.DeleteNoteByIdReturnResult(tokenValidationResult.jsonRpcCode);

        Integer userId = DAO.getUserIdByToken(token);
        Integer noteId = id;
        boolean rst = DAO.deleteNoteById(userId, noteId);
        if(!rst)
            return new MethodInvokeResult.DeleteNoteByIdReturnResult(MethodInvokeResult.ResultCodes.INVALID_PARAMS_ERROR.jsonRpcCode);
        else
            return new MethodInvokeResult.DeleteNoteByIdReturnResult(MethodInvokeResult.ResultCodes.SUCCESS.jsonRpcCode);
    }
}