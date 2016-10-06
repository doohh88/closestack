package com.ssmksh.closestack.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SnapShot implements Serializable {
   

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;
   private String name;
   private String vmName;
   private String type;
   private String time;
   private String newName;
   private String ip;
   

}