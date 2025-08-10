package com.circlehub.controller;

import com.circlehub.dao.PostDAO;
import com.circlehub.model.Post;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class DashboardController {
    @FXML private ListView<String> postsListView;

    @FXML
    public void initialize() {
        try {
            PostDAO dao = new PostDAO();
            List<Post> posts = dao.listRecent(20);
            for (Post p : posts) {
                postsListView.getItems().add(p.getTitle() == null ? p.getBody() : p.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
