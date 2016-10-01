package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.Flavor;

public interface FlavorDAO {
	
	public void insertFlavor(Flavor flavor);
	
	public List<Flavor> getFlavors();
	
	public Flavor getFlavor(String name);
	
}
