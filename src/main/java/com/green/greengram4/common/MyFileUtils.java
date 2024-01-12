package com.green.greengram4.common;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter
public class MyFileUtils {
    private final String uploadPrefixPath;

    public MyFileUtils(@Value("${file.dir}") String uploadPrefixPath) {
        this.uploadPrefixPath = uploadPrefixPath;
    }

    // 폴더 만들기
    public String makeFolders(String path) {
        File folder = new File(uploadPrefixPath,path);
        folder.mkdirs();
        return folder.getAbsolutePath();
        // AbsolutePath : 절대주소
    }

    // 랜덤 파일명 만들기
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();
        // UUID : 범용고유식별자
    }

    // 확장자 얻어오기
    public String getExt(String filemNm) {
       String extension = filemNm.substring(filemNm.lastIndexOf("."));
       System.out.println(extension);
       return extension;
    }

    // 랜덤 파일명 만들기 with 확장자
    public String getRandomFileNm(String originalFileNm) {
        return getRandomFileNm() + getExt(originalFileNm);

    }

    //랜덤 파일명 만들기 with 확장자 from MultipartFile
    public String getRandomFileNm(MultipartFile mf) {
        String fileNm = mf.getOriginalFilename();

        return getRandomFileNm(fileNm);
    }

}
