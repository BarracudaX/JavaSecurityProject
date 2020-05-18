package dao;

import java.util.List;

public interface Comments {

    List<String> comments();

    void addComment(String comment);
}
