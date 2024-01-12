package com.green.greengram4.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({MyFileUtils.class})
@TestPropertySource(properties = {"file.dir = D:/home/download",})
public class MyFileUtilsTest {
    @Autowired
    private MyFileUtils myFileUtils;

    @Test
    public void makeFolderTest() {
        String path = "/gksk";
        File preFolder = new File(myFileUtils.getUploadPrefixPath(), path);
        assertEquals(false, preFolder.exists());


        String newpath = myFileUtils.makeFolders(path);
        File newFolder = new File(newpath);
        assertEquals(preFolder.getAbsolutePath(), newFolder.getAbsolutePath());
        assertEquals(true, newFolder.exists());
    }

    @Test
    public void getRandomFileNmTest() {
        String fileNm = myFileUtils.getRandomFileNm();
        System.out.println("fileNm : " + fileNm);
        assertNotNull(fileNm);
        assertNotEquals("", fileNm);
    }

    @Test
    public void getExtTest() {
        String filemNm = "abc.efg.eee.jpg";

        String ext = myFileUtils.getExt(filemNm);
        assertEquals(".jpg", ext);

        String filemNm2 = "hhh-wefd.pnge";
        String ext2 = myFileUtils.getExt(filemNm2);
        assertEquals(".pnge", ext2);
    }

    @Test
    public void getRandomFileNm2() {
        String fileNm1 = "반가웡.친구양.jpeg";
        String rFileNm1 = myFileUtils.getRandomFileNm(fileNm1);
        System.out.println("rFileNm1: " + rFileNm1);

        String fileNm2 = "반가d.dd양.jng";
        String rFileNm2 = myFileUtils.getRandomFileNm(fileNm2);
        System.out.println("rFileNm2: " + rFileNm2);
    }


}
