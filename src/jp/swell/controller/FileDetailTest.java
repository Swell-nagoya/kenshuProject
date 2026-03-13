package jp.swell.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jp.patasys.common.AtareSysException;


class FileDetailTest {

	@Test
	void countTest() throws AtareSysException {
		FileDetail fileDetail = new FileDetail();
		assertEquals(101, fileDetail.count());
	}
}



	
	
	
	
