package com.teacherwl.eblog.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "post",type = "post" ,createIndex = true)
public class PostDocument {
    @Id
    private  Long id;
    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    private  String title;
    @Field(type = FieldType.Keyword)
    private  String authorName;
    @Field(type = FieldType.Long)
    private  Long authorId;
    private  String authorAvatar;
    private  Long categoryId;
    @Field(type = FieldType.Keyword)
    private  String categoryName;
    private Integer level;
    private  Boolean recomment;
    private  Integer commentCount;
    private  Integer viewCount;
    @Field(type = FieldType.Keyword ,format = DateFormat.basic_date_time)
    private Date created;
}
