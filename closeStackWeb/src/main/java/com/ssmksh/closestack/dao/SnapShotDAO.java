package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.SnapShot;

public interface SnapShotDAO {
public void insertSnapShot(SnapShot snapShot);
	
	public List<SnapShot> getSnapShots();
	
	public SnapShot findByName(String name);
	
	public void delete(String name);
	

}
