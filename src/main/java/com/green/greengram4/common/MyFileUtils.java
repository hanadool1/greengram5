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
        // mkdirs 하위폴더까지 만들어 줌
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

    // 랜덤 파일명 만들기 with 확장자 from MultipartFile
    public String getRandomFileNm(MultipartFile mf) {
        String fileNm = mf.getOriginalFilename();

        return getRandomFileNm(fileNm);
    }

    // 메모리에 있는 내용 > 파일로 옮기는 메소드
    public String transferTo(MultipartFile mf, String target) {
        String fileNm = getRandomFileNm(mf);
        String folderPath = makeFolders(target);
        File saveFile = new File(folderPath, fileNm);
        try {
            mf.transferTo(saveFile);
            // 메모리에 있던 내용을 파일로 transfer
            return fileNm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delFiles(String folderPath) {
        // 폴더 아래에 폴더 및 파일 삭제, 보낸 폴더는 삭제 안함
        File folder = new File(folderPath);
        // 파일의 full주소를 File타입으로 folder에 저장
        if (folder.exists()) {
            File[] files = folder.listFiles();
            // 폴더 안의 파일들을 리스트형태로

            for (File file: files) {
                // 반복문 돌면서 체크
                if (file.isDirectory()) {
                    // 디렉토리인지 체크
                    delFiles(file.getAbsolutePath());
                    // 재귀호출
                }
                file.delete();
            }
        }
        // 반복문 돌면서 하위 폴더들과 폴더안의 파일 모두 삭제(스택과 비슷한 push, pop)
    }



}
