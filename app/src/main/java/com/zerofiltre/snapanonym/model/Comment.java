package com.zerofiltre.snapanonym.model;

public class Comment extends Data {
    private String commentatorPseudo;
    private String content;

    public String getCommentatorPseudo() {
        return commentatorPseudo;
    }

    public void setCommentatorPseudo(String commentatorPseudo) {
        this.commentatorPseudo = commentatorPseudo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment() {
    }

    public Comment(int id, String commentatorPseudo, String content) {
        super(id);
        this.commentatorPseudo = commentatorPseudo;
        this.content = content;
    }
}
