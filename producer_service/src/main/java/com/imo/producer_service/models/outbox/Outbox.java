package com.imo.producer_service.models.outbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "outbox")
@Data
@AllArgsConstructor
public class Outbox<T> {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  private T payload;

  private OutboxStatus status;

  private OutboxEvent event;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}

