package com.ssmksh.closestack.dto;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SnapShot implements Serializable {
   
   
   private String id;
   private String name;
   private String vmName;
   private String type;
   private LocalTime time;
   private String newName;
   private String ip;

}	