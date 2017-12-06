package com.yangle.controller;

import com.yangle.domain.FileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangle on 2017/11/30.
 */
@Controller
public class IndexControllor {
    private static final String ROOT ="D:\\blog-files\\";
    private final ResourceLoader resourceLoader;

    @Autowired
    public IndexControllor(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/to_write")
public String to_write(){
        return "writearticle";
}

@RequestMapping("/to_manage_article")
public String to_manage_article(){
        return "glwz";
}



@ResponseBody
@RequestMapping("/uploadImage")
public Map uploadImage(HttpServletRequest request, String img ){
    Map map=new HashMap();
    map.put("msg","上传成功");
    System.out.println(img);
return map;
}
/**
 * 上传文件
 */
@ResponseBody
@RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
public FileResult handleFileUpload(@RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes, HttpServletRequest request) {
    if (!file.isEmpty()) {
        try {


            Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));

            FileResult fileResult=new FileResult(0,"上传成功",ROOT+file.getOriginalFilename());
            return fileResult;
        }  catch (IOException e) {
            e.printStackTrace();
            FileResult fileResult=new FileResult(1,"上传失败","");
            return fileResult;
        }


}  else {
        FileResult fileResult=new FileResult(1,"上传失败","");
        return fileResult;
    }

}

    /**
     * 显示图片
     * @param filename
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
