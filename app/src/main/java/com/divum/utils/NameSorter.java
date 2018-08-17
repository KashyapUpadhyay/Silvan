package com.divum.utils;

import java.util.Comparator;

import com.divum.entity.MoodEntity;

public class NameSorter implements Comparator<MoodEntity> {

	@Override
	public int compare(MoodEntity lhs, MoodEntity rhs) {
		
		return lhs.getNameMood().compareTo(rhs.getNameMood());
	}

}
