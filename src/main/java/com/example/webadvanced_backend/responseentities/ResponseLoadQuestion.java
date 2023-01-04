package com.example.webadvanced_backend.responseentities;

import com.example.webadvanced_backend.models.Question;

import java.util.List;

public class ResponseLoadQuestion {
    private List<Question> oldQuestionList;
    private boolean isOwner;

    public ResponseLoadQuestion(List<Question> oldQuestionList, boolean isOwner) {
        this.oldQuestionList = oldQuestionList;
        this.isOwner = isOwner;
    }

    public List<Question> getOldQuestionList() {
        return oldQuestionList;
    }

    public void setOldQuestionList(List<Question> oldQuestionList) {
        this.oldQuestionList = oldQuestionList;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
