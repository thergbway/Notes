package webServer;

import webServer.dto.Note;

import java.util.Arrays;

public class MethodInvokeResult {
    private MethodInvokeResult() {
    }

    public static enum ResultCodes {
        SUCCESS("0"),

        INVALID_TOKEN_ERROR("1"),

        TOKEN_EXPIRED_ERROR("2"),

        INVALID_LOGIN_OR_PASSWORD_ERROR("3"),

        INVALID_ID_ERROR("4"),

        INVALID_PARAMS_ERROR("5"),

        USER_EXISTS_ERROR("6");

        public String jsonRpcCode;

        public static ResultCodes createResultCodeFromString(String resultCode) {
            switch (resultCode) {
                case "0":
                    return SUCCESS;
                case "1":
                    return INVALID_TOKEN_ERROR;
                case "2":
                    return TOKEN_EXPIRED_ERROR;
                case "3":
                    return INVALID_LOGIN_OR_PASSWORD_ERROR;
                case "4":
                    return INVALID_ID_ERROR;
                case "5":
                    return INVALID_PARAMS_ERROR;
                case "6":
                    return USER_EXISTS_ERROR;
                default:
                    throw new RuntimeException("No such ResultCode for code = " + resultCode);
            }
        }

        ResultCodes(String jsonRpcCode) {
            this.jsonRpcCode = jsonRpcCode;
        }
    }

    public static class SignInReturnResult {
        private String resultCode;
        private String token;

        public SignInReturnResult(String resultCode, String token) {
            this.resultCode = resultCode;
            this.token = token;
        }

        public SignInReturnResult() {

        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "SignInReturnResult{" +
                    "resultCode='" + resultCode + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    public static class SignUpReturnResult {
        private String resultCode;

        @Override
        public String toString() {
            return "SignUp{" +
                    "resultCode='" + resultCode + '\'' +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public SignUpReturnResult(String resultCode) {

            this.resultCode = resultCode;
        }

        public SignUpReturnResult() {

        }
    }

    public static class GetAllNotesIdsReturnResult {
        private String resultCode;
        Integer[] ids;

        @Override
        public String toString() {
            return "GetAllNotesIdsReturnResult{" +
                    "resultCode='" + resultCode + '\'' +
                    ", ids=" + Arrays.toString(ids) +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public Integer[] getIds() {
            return ids;
        }

        public void setIds(Integer[] ids) {
            this.ids = ids;
        }

        public GetAllNotesIdsReturnResult(String resultCode, Integer[] ids) {

            this.resultCode = resultCode;
            this.ids = ids;
        }

        public GetAllNotesIdsReturnResult() {

        }
    }

    public static class GetNoteByIdReturnResult {
        private String resultCode;
        private Note note;

        @Override
        public String toString() {
            return "GetNoteById{" +
                    "resultCode='" + resultCode + '\'' +
                    ", note=" + note +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public Note getNote() {
            return note;
        }

        public void setNote(Note note) {
            this.note = note;
        }

        public GetNoteByIdReturnResult(String resultCode, Note note) {

            this.resultCode = resultCode;
            this.note = note;
        }

        public GetNoteByIdReturnResult() {

        }
    }

    public static class SaveOrUpdateNoteReturnResult {
        private String resultCode;
        private Integer noteId;

        @Override
        public String toString() {
            return "SaveOrUpdateNote{" +
                    "resultCode='" + resultCode + '\'' +
                    ", noteId=" + noteId +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public Integer getNoteId() {
            return noteId;
        }

        public void setNoteId(Integer noteId) {
            this.noteId = noteId;
        }

        public SaveOrUpdateNoteReturnResult(String resultCode, Integer noteId) {

            this.resultCode = resultCode;
            this.noteId = noteId;
        }

        public SaveOrUpdateNoteReturnResult() {

        }
    }

    public static class GetAllNotesByTagsReturnResult {
        private String resultCode;
        private Integer[] noteIds;

        @Override
        public String toString() {
            return "GetAllNotesByTagsReturnResult{" +
                    "resultCode='" + resultCode + '\'' +
                    ", noteIds=" + Arrays.toString(noteIds) +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public Integer[] getNoteIds() {
            return noteIds;
        }

        public void setNoteIds(Integer[] noteIds) {
            this.noteIds = noteIds;
        }

        public GetAllNotesByTagsReturnResult(String resultCode, Integer[] noteIds) {

            this.resultCode = resultCode;
            this.noteIds = noteIds;
        }

        public GetAllNotesByTagsReturnResult() {

        }
    }

    public static class DeleteNoteByIdReturnResult {
        private String resultCode;

        @Override
        public String toString() {
            return "DeleteNoteByIdReturnResult{" +
                    "resultCode='" + resultCode + '\'' +
                    '}';
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public DeleteNoteByIdReturnResult() {

        }

        public DeleteNoteByIdReturnResult(String resultCode) {
            this.resultCode = resultCode;
        }
    }
}