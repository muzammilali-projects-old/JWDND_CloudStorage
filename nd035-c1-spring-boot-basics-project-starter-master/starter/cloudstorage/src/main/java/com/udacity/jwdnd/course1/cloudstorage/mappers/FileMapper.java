package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileDataBytes})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Select("SELECT * FROM FILES where userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(int fileId);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void delete(int fileId);
}
