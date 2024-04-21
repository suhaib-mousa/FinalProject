package atypon.store.controller;

import atypon.store.database.DocumentManager;
import atypon.store.model.Blog;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BlogController
{
    @GetMapping("/Blogs")
    public String index() {
        return "Blogs/index";
    }

    @GetMapping("/Blogs/{id}")
    public String edit() {
        return "Blogs/Admin/edit";
    }

    @GetMapping("/Blogs/Admin")
    public String admin() {
        return "Blogs/Admin/index";
    }
    @GetMapping("/Blogs/Admin/Create")
    public String create() {
        return "Blogs/Admin/create";
    }
    @PostMapping("/create-blog")
    public String create(@RequestBody BlogRequest blogRequest) {
        DocumentManager document = new DocumentManager();
        Map<String, String> blogMap = new HashMap<>();
        blogMap.put("name", blogRequest.blog.getName());
        blogMap.put("content", blogRequest.blog.getContent());

        try {
            document.addDocument(blogRequest.nodeUrl, blogRequest.token, "Blogs", blogMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "Blogs/Admin/index";
    }
    @PostMapping("/edit-blog")
    public String edit(@RequestBody BlogRequest blogRequest) {
        DocumentManager document = new DocumentManager();
        Map<String, String> blogMap = new HashMap<>();
        blogMap.put("name", blogRequest.blog.getName());
        blogMap.put("content", blogRequest.blog.getContent());
        Map<Integer,Map<String, String>> doc = new HashMap<>();
        doc.put(blogRequest.docId, blogMap);

        try {
            document.editDocument(blogRequest.nodeUrl, blogRequest.token, blogRequest.docId,doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "Blogs/Admin/index";
    }
    @PostMapping("/delete-blog")
    public ResponseEntity<Boolean> delete(@RequestBody BlogRequest blogRequest) {
        DocumentManager document = new DocumentManager();

        try {
            document.deleteDocument(blogRequest.nodeUrl, blogRequest.token, blogRequest.docId);
            return ResponseEntity.ok(true); // Return HTTP 200 OK with true indicating success
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-blogs")
    @ResponseBody
    public String getBlogs(@RequestParam String nodeUrl, String token) throws Exception {
        DocumentManager document = new DocumentManager();
        return document.getCollectionDocs("Blogs", nodeUrl, token);
    }
    @GetMapping("/get-blog")
    @ResponseBody
    public String getBlog(@RequestParam String nodeUrl, String token, int id) throws Exception {
        DocumentManager document = new DocumentManager();

        return document.getDocumentByIDJson(nodeUrl, token, id);
    }
    public static class BlogRequest{
        public Integer docId;
        public String nodeUrl;
        public String token;
        public Blog blog;
    }
}
